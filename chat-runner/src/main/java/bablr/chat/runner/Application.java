package bablr.chat.runner;

import bablr.chat.adapters.out.event.inmemory.InMemoryConnector;
import bablr.chat.adapters.out.persistence.inmemory.InMemoryChatRepository;
import bablr.chat.adapters.out.persistence.inmemory.InMemoryMessageRepository;
import bablr.chat.adapters.out.persistence.sqlite.SqliteChatRepository;
import bablr.chat.adapters.out.persistence.sqlite.SqliteMessageRepository;
import bablr.chat.application.command.CommandHandler;
import bablr.chat.application.command.CreateChatCommand;
import bablr.chat.application.command.SendMessageCommand;
import bablr.chat.application.port.out.event.DomainEventDispatcher;
import bablr.chat.application.port.out.persistence.ChatRepository;
import bablr.chat.application.port.out.persistence.MessageRepository;
import bablr.chat.application.service.chat.CreateChatService;
import bablr.chat.application.service.chat.SendMessageService;
import bablr.chat.common.tuples.Tuple;
import bablr.chat.model.chat.ParticipantId;

import java.time.Instant;

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
        var commandHandler = new CommandHandler(
                Tuple.of(CreateChatCommand.class, command -> createChat.process((CreateChatCommand) command)),
                Tuple.of(SendMessageCommand.class, command -> sendMessage.sendMessage((SendMessageCommand) command))
        );

        //var connector = new WebsocketServer().commandHandler(commandHandler);
        var connectorAndEventHandler = new InMemoryConnector(commandHandler);

        var participantId = ParticipantId.newParticipantId();
        connectorAndEventHandler.onClientConnected(participantId.value());

        var chat = createChat.process(new CreateChatCommand("Foo Bar", participantId, Instant.now()));
        sendMessage.sendMessage(new SendMessageCommand(chat.getId(), participantId, "Hello, World!", Instant.now()));
    }
}
