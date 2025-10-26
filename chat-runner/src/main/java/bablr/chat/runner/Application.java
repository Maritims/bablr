package bablr.chat.runner;

import bablr.chat.adapters.out.event.inmemory.InMemoryConnector;
import bablr.chat.adapters.out.event.websocket.WebsocketConnector;
import bablr.chat.adapters.out.persistence.inmemory.InMemoryChatRepository;
import bablr.chat.adapters.out.persistence.inmemory.InMemoryMessageRepository;
import bablr.chat.adapters.out.persistence.sqlite.SqliteChatRepository;
import bablr.chat.adapters.out.persistence.sqlite.SqliteMessageRepository;
import bablr.chat.application.command.DomainCommandHandler;
import bablr.chat.application.command.CreateChatDomainCommand;
import bablr.chat.application.command.SendMessageDomainCommand;
import bablr.chat.application.port.out.event.DomainEventDispatcher;
import bablr.chat.application.port.out.persistence.ChatRepository;
import bablr.chat.application.port.out.persistence.MessageRepository;
import bablr.chat.application.service.chat.CreateChatService;
import bablr.chat.application.service.chat.SendMessageService;
import bablr.chat.common.tuples.Tuple;
import bablr.chat.model.chat.ParticipantId;
import bablr.chat.model.event.ChatCreatedEvent;
import bablr.chat.model.event.MessageSentEvent;

import java.time.Instant;

import static bablr.chat.common.tuples.Tuple.tuple;

public class Application {
    static String  connectionString = "jdbc:sqlite:chat.db";
    static Integer queryTimeout     = 30;

    static ChatRepository sqliteChatRepository() {
        return (ChatRepository) new SqliteChatRepository(connectionString, queryTimeout).initialize();
    }

    static MessageRepository sqliteMessageRepository() {
        return (MessageRepository) new SqliteMessageRepository(connectionString, queryTimeout).initialize();
    }

    static ChatRepository inMemoryChatRepository() {
        return new InMemoryChatRepository();
    }

    static MessageRepository inMemoryMessageRepository() {
        return new InMemoryMessageRepository();
    }

    public static void main(String[] args) {
        var domainEventDispatcher = new DomainEventDispatcher();
        var chatRepository        = inMemoryChatRepository();
        var messageRepository     = inMemoryMessageRepository();
        var createChat            = new CreateChatService(chatRepository, domainEventDispatcher);
        var sendMessage           = new SendMessageService(chatRepository, messageRepository, domainEventDispatcher);
        var domainCommandHandler = new DomainCommandHandler(
                tuple(CreateChatDomainCommand.class, domainCommand -> createChat.createChat((CreateChatDomainCommand) domainCommand)),
                tuple(SendMessageDomainCommand.class, domainCommand -> sendMessage.sendMessage((SendMessageDomainCommand) domainCommand))
        );

        //var connectorAndEventHandler = new WebsocketConnector(domainCommandHandler);
        var connectorAndEventHandler = new InMemoryConnector(domainCommandHandler);

        domainEventDispatcher.register(ChatCreatedEvent.class, connectorAndEventHandler);
        domainEventDispatcher.register(MessageSentEvent.class, connectorAndEventHandler);

        var participantId = ParticipantId.newParticipantId();
        connectorAndEventHandler.onClientConnected(participantId.value());

        var chat = createChat.createChat(new CreateChatDomainCommand("Foo Bar", participantId, Instant.now()));
        sendMessage.sendMessage(new SendMessageDomainCommand(chat.getId(), participantId, "Hello, World!", Instant.now()));
    }
}
