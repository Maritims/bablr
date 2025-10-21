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
}
