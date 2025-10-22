package bablr.chat.application.impl;

import bablr.chat.application.MarkAsReadUseCase;
import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.event.MessageReadEvent;
import bablr.chat.domain.repository.ChatRepository;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.value.MessageId;

import java.time.Instant;
import java.util.Objects;

public final class MarkAsReadService implements MarkAsReadUseCase {
    private final ChatRepository       chatRepository;
    private final DomainEventPublisher publisher;

    public MarkAsReadService(ChatRepository chatRepository, DomainEventPublisher publisher) {
        this.chatRepository = Objects.requireNonNull(chatRepository);
        this.publisher      = Objects.requireNonNull(publisher);
    }

    @Override
    public void markAsRead(ChatId chatId, MessageId messageId, Participant participant) {
        if (chatId == null) {
            throw new IllegalArgumentException("directChatId must not be null");
        }
        if (messageId == null) {
            throw new IllegalArgumentException("messageId must not be null");
        }
        if (participant == null) {
            throw new IllegalArgumentException("participant must not be null");
        }

        var chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        chat.markAsRead(participant, Instant.now());
        chatRepository.save(chat);
        publisher.publish(new MessageReadEvent(chatId, messageId, Instant.now()));
    }
}
