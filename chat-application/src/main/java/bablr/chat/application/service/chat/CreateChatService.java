package bablr.chat.application.service.chat;

import bablr.chat.application.command.CommandProcessor;
import bablr.chat.application.port.in.chat.CreateChat;
import bablr.chat.application.command.CreateChatCommand;
import bablr.chat.application.port.out.event.DomainEventDispatcher;
import bablr.chat.application.port.out.persistence.ChatRepository;
import bablr.chat.model.chat.Chat;
import bablr.chat.model.event.ChatCreatedEvent;

@CommandProcessor
public class CreateChatService implements CreateChat {
    private final ChatRepository        chatRepository;
    private final DomainEventDispatcher domainEventDispatcher;

    public CreateChatService(ChatRepository chatRepository, DomainEventDispatcher domainEventDispatcher) {
        if (chatRepository == null) {
            throw new IllegalArgumentException("chatRepository must not be null");
        }
        if (domainEventDispatcher == null) {
            throw new IllegalArgumentException("eventPublisher must not be null");
        }

        this.chatRepository        = chatRepository;
        this.domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    public Chat process(CreateChatCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var chat = Chat.newChat(command.ownerId());
        chatRepository.save(chat);
        domainEventDispatcher.dispatch(new ChatCreatedEvent(chat.getId(), chat.getOwnerId(), command.occurredAt()));

        return chat;
    }
}
