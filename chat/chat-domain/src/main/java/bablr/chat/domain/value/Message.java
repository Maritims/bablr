package bablr.chat.domain.value;

import bablr.chat.domain.entity.ChatParticipant;

import java.time.Instant;
import java.util.Objects;

public record Message(MessageId id, ChatParticipant sender, String content, Instant timestamp) {
    public Message(MessageId id, ChatParticipant sender, String content, Instant timestamp) {
        this.id        = Objects.requireNonNull(id);
        this.sender    = Objects.requireNonNull(sender);
        this.content   = Objects.requireNonNull(content);
        this.timestamp = Objects.requireNonNull(timestamp);
    }
}