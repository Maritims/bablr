package bablr.chat.domain.value;

import java.util.Objects;
import java.util.UUID;

public record DirectChatId(UUID id) {
    public DirectChatId {
        Objects.requireNonNull(id);
    }
}
