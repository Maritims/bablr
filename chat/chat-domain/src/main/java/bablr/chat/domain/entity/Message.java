package bablr.chat.domain.entity;

import bablr.chat.domain.value.MessageId;

import java.time.Instant;

public record Message(MessageId id, Participant sender, String content, Instant timestamp) {
    public Message {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (sender == null) {
            throw new IllegalArgumentException("sender must not be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }
    }
}