package bablr.chat.domain.event;

import bablr.chat.common.DomainEvent;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.Message;

import java.time.Instant;
import java.util.Objects;

public record MessageSentEvent(DirectChatId directChatId, Message message, Instant occurredAt) implements DomainEvent {
    public MessageSentEvent(DirectChatId directChatId, Message message, Instant occurredAt) {
        this.directChatId = Objects.requireNonNull(directChatId);
        this.message      = Objects.requireNonNull(message);
        this.occurredAt   = Objects.requireNonNull(occurredAt);
    }
}
