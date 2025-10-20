package bablr.chat.domain.event;

import bablr.chat.common.DomainEvent;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.MessageId;

import java.time.Instant;
import java.util.Objects;

public record MessageReadEvent(DirectChatId directChatId, MessageId messageId, Instant occurredAt) implements DomainEvent {
    public MessageReadEvent(DirectChatId directChatId, MessageId messageId, Instant occurredAt) {
        this.directChatId = Objects.requireNonNull(directChatId);
        this.messageId    = Objects.requireNonNull(messageId);
        this.occurredAt   = Objects.requireNonNull(occurredAt);
    }
}
