package bablr.chat.domain.value;

import java.util.UUID;

public record ParticipantId(UUID value) {
    public ParticipantId {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
    }

    public static ParticipantId newParticipantId() {
        return new ParticipantId(UUID.randomUUID());
    }
}
