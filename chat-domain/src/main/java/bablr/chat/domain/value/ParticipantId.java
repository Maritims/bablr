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

    public static ParticipantId parseParticipantId(String rawParticipantId) {
        if(rawParticipantId == null) {
            throw new IllegalArgumentException("rawParticipantId must not be null");
        }
        if (rawParticipantId.isBlank()) {
            throw new IllegalArgumentException("rawParticipantId must not be blank");
        }

        var uuid = UUID.fromString(rawParticipantId);
        return new ParticipantId(uuid);
    }
}
