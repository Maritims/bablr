package bablr.chat.adapters.out.persistence.sqlite;

import bablr.chat.application.port.out.persistence.MessageRepository;
import bablr.chat.common.Initializable;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.chat.Message;
import bablr.chat.model.chat.MessageId;
import bablr.chat.model.chat.ParticipantId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteMessageRepository extends SqliteBaseRepository implements MessageRepository,
                                                                             Initializable {
    public SqliteMessageRepository(String connectionString, Integer queryTimeout) {
        super(connectionString, queryTimeout);
    }

    @Override
    public Initializable initialize() {
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

        return this;
    }

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
                var messageId = resultSet.getString("message_id");
                var senderId  = resultSet.getString("inviterId");
                var content   = resultSet.getString("content");
                var timestamp = resultSet.getTimestamp("timestamp").toInstant();
                var message   = Message.existingMessage(MessageId.parseMessageId(messageId), chatId, new ParticipantId(senderId), content, timestamp);
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
            statement.setString(2, message.getChatId().value().toString());
            statement.setString(3, message.getSenderId().value());
            statement.setString(4, message.getContent());
            statement.setTimestamp(5, java.sql.Timestamp.from(message.getCreated()));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
