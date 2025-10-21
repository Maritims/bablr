package bablr.chat.infrastructure.persistence.sqlite;

import bablr.chat.common.Initializable;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.repository.MessageRepository;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;
import bablr.chat.domain.value.MessageId;
import bablr.chat.domain.value.ParticipantId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRepositorySqliteImpl extends BaseRepositorySqliteImpl implements MessageRepository,
                                                                                     Initializable {
    public MessageRepositorySqliteImpl(String connectionString, int queryTimeout) {
        super(connectionString, queryTimeout);
    }

    @Override
    public void initialize() {
        try (var connection = connection(); var statement = connection.createStatement()) {
            statement.execute("""
                                       CREATE TABLE IF NOT EXISTS message (
                                           message_id UUID PRIMARY KEY,
                                           chat_id UUID NOT NULL,
                                           sender UUID NOT NULL,
                                           content VARCHAR(255) NOT NULL,
                                           timestamp TIMESTAMP NOT NULL,
                                           FOREIGN KEY (chat_id) REFERENCES chat(id) ON DELETE CASCADE,
                                           FOREIGN KEY (sender) REFERENCES participant(id) ON DELETE CASCADE
                                      )
                                      """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findByChatId(ChatId chatId, int pageNumber, int pageSize) {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId must not be null");
        }
        if (pageNumber < 0) {
            throw new IllegalArgumentException("pageNumber must not be negative");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("pageSize must be greater than 0");
        }

        try (var connection = connection(); var statement = connection.prepareStatement("SELECT * FROM message WHERE chat_id = ? LIMIT ? OFFSET ?")) {
            var resultSet = statement.executeQuery();
            var messages  = new ArrayList<Message>();
            while (resultSet.next()) {
                var messageId = UUID.fromString(resultSet.getString("message_id"));
                var sender    = UUID.fromString(resultSet.getString("sender"));
                var content   = resultSet.getString("content");
                var timestamp = resultSet.getTimestamp("timestamp").toInstant();
                var message   = new Message(new MessageId(messageId), new Participant(new ParticipantId(sender)), content, timestamp);
                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }
    }
}
