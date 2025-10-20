package bablr.chat.domain.value;

import java.util.Objects;
import java.util.UUID;

public record MessageId(UUID id) {
    public MessageId {
        Objects.requireNonNull(id);
    }
}
