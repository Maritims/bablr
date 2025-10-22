package bablr.chat.domain.entity;

import bablr.chat.domain.value.ParticipantId;

public record Participant(ParticipantId id) {
    public Participant {
        if (id == null) {
            throw new IllegalArgumentException("value must not be null");
        }
    }
}
