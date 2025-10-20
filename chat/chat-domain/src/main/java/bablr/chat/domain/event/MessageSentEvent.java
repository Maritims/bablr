package bablr.chat.domain.event;

import bablr.chat.common.DomainEvent;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.Message;

import java.time.Instant;
import java.util.Objects;

public record MessageSentEvent(DirectChatId directChatId, Message message, Instant occurredAt, String type) implements DomainEvent {
    public MessageSentEvent {
        Objects.requireNonNull(directChatId);
        Objects.requireNonNull(message);
        Objects.requireNonNull(occurredAt);
        Objects.requireNonNull(type);
    }

    public MessageSentEvent(DirectChatId directChatId, Message message, Instant occurredAt) {
        this(directChatId, message, occurredAt, "MessageSentEvent");
    }
}
