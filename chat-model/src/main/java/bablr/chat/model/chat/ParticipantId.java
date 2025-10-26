package bablr.chat.model.chat;

public record ParticipantId(String value) {
    public static ParticipantId newParticipantId() {
        return new ParticipantId(java.util.UUID.randomUUID().toString());
    }

    public static ParticipantId parseParticipantId(String participantId) {
        if (participantId == null) {
            throw new IllegalArgumentException("participantId must not be null");
        }
        if (participantId.isBlank()) {
            throw new IllegalArgumentException("participantId must not be blank");
        }

        return new ParticipantId(participantId);
    }
}
