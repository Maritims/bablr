package bablr.chat.domain.value;

import java.util.Objects;
import java.util.UUID;

public record ChatId(UUID value) {
    public ChatId {
        Objects.requireNonNull(value);
    }

    public static ChatId newChatId() {
        return new ChatId(UUID.randomUUID());
    }

    public static ChatId parseChatId(String rawChatId) {
        if(rawChatId == null) {
            throw new IllegalArgumentException("rawChatId must not be null");
        }
        if (rawChatId.isBlank()) {
            throw new IllegalArgumentException("rawChatId must not be blank");
        }

        var uuid = UUID.fromString(rawChatId);
        return new ChatId(uuid);
    }
}
