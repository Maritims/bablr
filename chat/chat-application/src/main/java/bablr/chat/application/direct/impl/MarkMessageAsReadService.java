package bablr.chat.application.direct.impl;

import bablr.chat.application.direct.MarkMessageAsReadUseCase;
import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.domain.entity.ChatParticipant;
import bablr.chat.domain.event.MessageReadEvent;
import bablr.chat.domain.repository.DirectChatRepository;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.MessageId;

import java.time.Instant;
import java.util.Objects;

public final class MarkMessageAsReadService implements MarkMessageAsReadUseCase {
    private final DirectChatRepository repository;
    private final DomainEventPublisher publisher;

    public MarkMessageAsReadService(DirectChatRepository repository, DomainEventPublisher publisher) {
        this.repository = Objects.requireNonNull(repository);
        this.publisher  = Objects.requireNonNull(publisher);
    }

    @Override
    public void markMessageAsRead(DirectChatId directChatId, MessageId messageId, ChatParticipant participant) {
        if (directChatId == null) {
            throw new IllegalArgumentException("directChatId must not be null");
        }
        if (messageId == null) {
            throw new IllegalArgumentException("messageId must not be null");
        }
        if (participant == null) {
            throw new IllegalArgumentException("participant must not be null");
        }

        var chat = repository.findById(directChatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        chat.markMessageAsRead(participant, messageId);

        publisher.publish(new MessageReadEvent(directChatId, messageId, Instant.now()));
    }
}
