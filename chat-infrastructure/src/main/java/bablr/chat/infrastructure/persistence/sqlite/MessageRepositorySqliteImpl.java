package bablr.chat.infrastructure.persistence.sqlite;

import bablr.chat.common.Discoverable;
import bablr.chat.common.Initializable;
import bablr.chat.domain.repository.MessageRepository;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;
import bablr.chat.domain.value.MessageId;
import bablr.chat.domain.value.ParticipantId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Discoverable
public class MessageRepositorySqliteImpl extends BaseRepositorySqliteImpl implements MessageRepository,
                                                                                     Initializable {
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
                var senderId  = UUID.fromString(resultSet.getString("senderId"));
                var content   = resultSet.getString("content");
                var timestamp = resultSet.getTimestamp("timestamp").toInstant();
                var message   = new Message(new MessageId(messageId), chatId, new ParticipantId(senderId), content, timestamp);
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

        var insertMessageSQL = "INSERT INTO message (message_id, chat_id, sender, content, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (var connection = connection(); var statement = connection.prepareStatement(insertMessageSQL)) {
            statement.setString(1, MessageId.newMessageId().value().toString());
            statement.setString(2, message.chatId().value().toString());
            statement.setString(3, message.senderId().value().toString());
            statement.setString(4, message.content());
            statement.setTimestamp(5, java.sql.Timestamp.from(message.timestamp()));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
