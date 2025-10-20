package bablr.chat.domain.entity;

import bablr.chat.common.UserId;

public record ChatParticipant(UserId userId) {
    public ChatParticipant {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }
}
