package bablr.chat.domain.event;

import bablr.chat.common.DomainEvent;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.MessageId;

import java.time.Instant;
import java.util.Objects;

public record MessageReadEvent(DirectChatId directChatId, MessageId messageId, Instant occurredAt, String type) implements DomainEvent {
    public MessageReadEvent {
        Objects.requireNonNull(directChatId);
        Objects.requireNonNull(messageId);
        Objects.requireNonNull(occurredAt);
    }

    public MessageReadEvent(DirectChatId directChatId, MessageId messageId, Instant occurredAt) {
        this(directChatId, messageId, occurredAt, "MessageReadEvent");
    }
}
