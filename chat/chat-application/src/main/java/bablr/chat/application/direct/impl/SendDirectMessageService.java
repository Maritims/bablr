package bablr.chat.application.direct.impl;

import bablr.chat.application.direct.SendDirectMessageUseCase;
import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.domain.entity.ChatParticipant;
import bablr.chat.domain.repository.DirectChatRepository;
import bablr.chat.domain.value.DirectChatId;
import bablr.chat.domain.value.Message;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class SendDirectMessageService implements SendDirectMessageUseCase {
    private final DirectChatRepository repository;
    private final DomainEventPublisher publisher;

    public SendDirectMessageService(DirectChatRepository repository, DomainEventPublisher publisher) {
        this.repository = Objects.requireNonNull(repository);
        this.publisher  = Objects.requireNonNull(publisher);
    }

    @Override
    public Message sendMessage(DirectChatId directChatId, ChatParticipant participant, String content) {
        if (directChatId == null) {
            throw new IllegalArgumentException("directChatId must not be null");
        }
        if (participant == null) {
            throw new IllegalArgumentException("participant must not be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }

        var chat    = repository.findById(directChatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        var message = chat.sendMessage(participant, content);
        repository.save(chat);

        chat.getDomainEvents().forEach(publisher::publish);
        chat.clearDomainEvents();

        return message;
    }
}
