package bablr.chat.application.service.chat;

import bablr.chat.application.port.in.chat.SendMessage;
import bablr.chat.application.command.SendMessageDomainCommand;
import bablr.chat.application.port.out.event.DomainEventDispatcher;
import bablr.chat.application.port.out.persistence.ChatRepository;
import bablr.chat.application.port.out.persistence.MessageRepository;
import bablr.chat.model.chat.Message;
import bablr.chat.model.event.MessageSentEvent;

public class SendMessageService implements SendMessage {
    private final ChatRepository        chatRepository;
    private final MessageRepository     messageRepository;
    private final DomainEventDispatcher domainEventDispatcher;

    public SendMessageService(ChatRepository chatRepository, MessageRepository messageRepository, DomainEventDispatcher domainEventDispatcher) {
        if (chatRepository == null) {
            throw new IllegalArgumentException("chatRepository must not be null");
        }
        if (messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        if (domainEventDispatcher == null) {
            throw new IllegalArgumentException("eventPublisher must not be null");
        }

        this.chatRepository        = chatRepository;
        this.messageRepository     = messageRepository;
        this.domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    public void sendMessage(SendMessageDomainCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var chat    = chatRepository.findById(command.chatId()).orElseThrow(() -> new IllegalArgumentException("Chat not found: " + command.chatId()));
        var message = Message.newMessage(chat.getId(), command.senderId(), command.content());
        messageRepository.save(message);
        domainEventDispatcher.dispatch(new MessageSentEvent(chat.getId(), command.senderId(), command.content(), command.occurredAt()));
    }
}
