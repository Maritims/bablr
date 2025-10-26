package bablr.chat.adapters.out.persistence.sqlite;

import bablr.chat.application.port.out.persistence.ChatRepository;
import bablr.chat.common.Initializable;
import bablr.chat.model.chat.Chat;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.chat.Participant;
import bablr.chat.model.chat.ParticipantId;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

public class SqliteChatRepository extends SqliteBaseRepository implements
                                                               ChatRepository,
                                                               Initializable {
    public SqliteChatRepository(String connectionString, Integer queryTimeout) {
        super(connectionString, queryTimeout);
    }

    @Override
    public Initializable initialize() {
        try (var connection = connection(); var statement = connection.createStatement()) {
            statement.execute("""
                                      CREATE TABLE IF NOT EXISTS chat (
                                          id UUID PRIMARY KEY,
                                          created TIMESTAMP NOT NULL,
                                          updated TIMESTAMP NOT NULL
                                      )
                                      """);
            statement.execute("""
                                      CREATE TABLE IF NOT EXISTS participant (
                                          id UUID PRIMARY KEY,
                                          chat_id UUID NOT NULL,
                                          created TIMESTAMP NOT NULL,
                                          updated TIMESTAMP NOT NULL,
                                          FOREIGN KEY (chat_id) REFERENCES chat(id) ON DELETE CASCADE)
                                      """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    @Override
    public Optional<Chat> findById(ChatId id) {
        var sql = """
                SELECT c.id AS chat_id, c.owner_id, c.created, p.id AS participant_id FROM chat c
                INNER JOIN participant p ON p.chat_id = c.id
                WHERE c.id = ?
                """;

        try (var connection = connection(); var statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.value().toString());

            var resultSet    = statement.executeQuery();
            var chatId       = (ChatId) null;
            var ownerId      = (ParticipantId) null;
            var participants = new HashSet<Participant>();
            var created      = (Instant) null;

            while (resultSet.next()) {
                if (chatId == null) {
                    chatId = ChatId.parseChatId(resultSet.getString("chat_id"));
                }
                if (ownerId == null) {
                    ownerId = ParticipantId.parseParticipantId(resultSet.getString("owner_id"));
                }
                if (created == null) {
                    created = resultSet.getTimestamp("created").toInstant();
                }

                var participantId = ParticipantId.parseParticipantId(resultSet.getString("participant_id"));
                if (!resultSet.wasNull()) {
                    var participant = Participant.existingParticipant(participantId);
                    participants.add(participant);
                }
            }

            return Optional.of(Chat.existingChat(chatId, ownerId, participants, created));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("chat must not be null");
        }

        if (chat.getId() == null) {
            try (var connection = connection()) {
                connection.setAutoCommit(false);
                var now = Timestamp.from(Instant.now());

                var insertChatSQL = "INSERT INTO chat (id, created, updated) VALUES(?, ?, ?)";
                var chatStatement = connection.prepareStatement(insertChatSQL);
                var chatId        = ChatId.newChatId();
                chatStatement.setString(1, chatId.value().toString());
                chatStatement.setTimestamp(2, now);
                chatStatement.setTimestamp(3, now);
                chatStatement.executeUpdate();
                chat = Chat.existingChat(chatId, chat.getOwnerId(), chat.getParticipants(), chat.getCreated());

                var insertParticipantsSQL = "INSERT INTO participant (id, chat_id, created, updated) VALUES (?, ?, ?, ?)";
                var participantStatement  = connection.prepareStatement(insertParticipantsSQL);
                for (var participant : chat.getParticipants()) {
                    participantStatement.setString(1, participant.getId().value());
                    participantStatement.setString(2, chat.getId().value().toString());
                    participantStatement.setTimestamp(3, now);
                    participantStatement.setTimestamp(4, now);
                    participantStatement.addBatch();
                }
                participantStatement.executeBatch();

                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
