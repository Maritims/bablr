package bablr.chat.application.impl;

import bablr.chat.application.SendMessageUseCase;
import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.domain.entity.Participant;
import bablr.chat.domain.repository.ChatRepository;
import bablr.chat.domain.repository.MessageRepository;
import bablr.chat.domain.value.ChatId;
import bablr.chat.domain.entity.Message;

@SuppressWarnings("ClassCanBeRecord")
public final class SendMessageService implements SendMessageUseCase {
    private final ChatRepository       chatRepository;
    private final MessageRepository    messageRepository;
    private final DomainEventPublisher publisher;

    public SendMessageService(ChatRepository chatRepository, MessageRepository messageRepository, DomainEventPublisher publisher) {
        if (chatRepository == null) {
            throw new IllegalArgumentException("chatRepository must not be null");
        }
        if (messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        if (publisher == null) {
            throw new IllegalArgumentException("publisher must not be null");
        }

        this.chatRepository    = chatRepository;
        this.messageRepository = messageRepository;
        this.publisher         = publisher;
    }

    @Override
    public Message sendMessage(ChatId chatId, Participant participant, String content) {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId must not be null");
        }
        if (participant == null) {
            throw new IllegalArgumentException("participant must not be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }

        var chat    = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        var message = chat.sendMessage(participant.id(), content);
        messageRepository.save(message);

        chat.getEvents().forEach(publisher::publish);
        chat.clearEvents();

        return message;
    }
}
