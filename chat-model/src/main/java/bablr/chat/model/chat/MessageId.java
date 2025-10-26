package bablr.chat.model.chat;

import java.util.UUID;

public record MessageId(UUID value) {
    public static MessageId newMessageId() {
        return new MessageId(UUID.randomUUID());
    }

    public static MessageId parseMessageId(String messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("messageId must not be null");
        }
        if (messageId.isBlank()) {
            throw new IllegalArgumentException("messageId must not be blank");
        }
        return new MessageId(UUID.fromString(messageId));
    }
}
