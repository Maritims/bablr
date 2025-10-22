package bablr.chat.domain.entity;

import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.value.MessageId;
import bablr.chat.domain.value.ParticipantId;

import java.time.Instant;

public record Message(MessageId id, ChatId chatId, ParticipantId senderId, String content, Instant timestamp) {
    public Message {
        if (id == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        if (chatId == null) {
            throw new IllegalArgumentException("chatId must not be null");
        }
        if (senderId == null) {
            throw new IllegalArgumentException("senderId must not be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }
    }
}