package bablr.chat.domain.event;

import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.value.MessageId;

import java.time.Instant;
import java.util.Objects;

public record MessageReadEvent(ChatId chatId, MessageId messageId, Instant occurredAt, String type) implements DomainEvent {
    public MessageReadEvent {
        Objects.requireNonNull(chatId);
        Objects.requireNonNull(messageId);
        Objects.requireNonNull(occurredAt);
    }

    public MessageReadEvent(ChatId chatId, MessageId messageId, Instant occurredAt) {
        this(chatId, messageId, occurredAt, "MessageReadEvent");
    }
}
