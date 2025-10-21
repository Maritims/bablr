package bablr.chat.infrastructure.persistence.sqlite;

import bablr.chat.common.Initializable;
import bablr.chat.domain.aggregate.Chat;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.repository.ChatRepository;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.value.ParticipantId;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class ChatRepositorySqliteImpl extends BaseRepositorySqliteImpl implements Initializable,
                                                                                  ChatRepository {
    public ChatRepositorySqliteImpl(String connectionString, int queryTimeout) {
        super(connectionString, queryTimeout);
    }

    @Override
    public void initialize() {
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
    }

    @Override
    public Optional<Chat> findById(ChatId id) {
        var sql = """
                SELECT c.id AS chat_id, p.id AS participant_id FROM chat c
                INNER JOIN participant p ON p.chat_id = c.id
                WHERE c.id = ?
                """;

        try (var connection = connection(); var statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.value().toString());

            var resultSet    = statement.executeQuery();
            var chatId       = (ChatId) null;
            var participants = new HashSet<Participant>();

            while (resultSet.next()) {
                if (chatId == null) {
                    chatId = new ChatId(UUID.fromString(resultSet.getString("chat_id")));
                }

                var participantId = UUID.fromString(resultSet.getString("participant_id"));
                if (!resultSet.wasNull()) {
                    var participant = new Participant(new ParticipantId(participantId));
                    participants.add(participant);
                }
            }

            return Optional.of(Chat.existingChat(chatId, participants));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Chat save(Chat chat) {
        if (chat.isNew()) {
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
                chat = Chat.existingChat(chatId, chat.getParticipants());

                var insertParticipantsSQL = "INSERT INTO participant (id, chat_id, created, updated) VALUES (?, ?, ?, ?)";
                var participantStatement  = connection.prepareStatement(insertParticipantsSQL);
                for (var participant : chat.getParticipants()) {
                    participantStatement.setString(1, participant.id().value().toString());
                    participantStatement.setString(2, chat.getId().value().toString());
                    participantStatement.setTimestamp(3, now);
                    participantStatement.setTimestamp(4, now);
                    participantStatement.addBatch();
                }
                participantStatement.executeBatch();

                connection.commit();
                return chat;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
