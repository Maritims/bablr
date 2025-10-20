package bablr.chat.common;

import java.util.UUID;

public record UserId(UUID id) {
    public UserId {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
    }
}
