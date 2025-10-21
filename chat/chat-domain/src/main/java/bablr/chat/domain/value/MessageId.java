package bablr.chat.domain.value;

import java.util.UUID;

public record MessageId(UUID value) {
    public MessageId {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
    }

    public static MessageId newMessageId() {
        return new MessageId(UUID.randomUUID());
    }
}
