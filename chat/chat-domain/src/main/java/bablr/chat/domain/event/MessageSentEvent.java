package bablr.chat.domain.event;

import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;

import java.time.Instant;
import java.util.Objects;

public record MessageSentEvent(ChatId chatId, Message message, Instant occurredAt, String type) implements DomainEvent {
    public MessageSentEvent {
        Objects.requireNonNull(chatId);
        Objects.requireNonNull(message);
        Objects.requireNonNull(occurredAt);
        Objects.requireNonNull(type);
    }

    public MessageSentEvent(ChatId chatId, Message message, Instant occurredAt) {
        this(chatId, message, occurredAt, "MessageSentEvent");
    }
}
