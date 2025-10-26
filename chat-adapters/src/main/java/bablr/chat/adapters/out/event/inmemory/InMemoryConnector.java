package bablr.chat.adapters.out.event.inmemory;

import bablr.chat.application.command.Command;
import bablr.chat.application.command.CommandHandler;
import bablr.chat.application.port.out.event.DomainEventHandler;
import bablr.chat.application.port.out.transport.ConnectionId;
import bablr.chat.application.port.out.transport.Connector;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.event.DomainEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryConnector implements Connector<String, String>, DomainEventHandler {
    private static final Map<String, InMemoryConnector> connections = new HashMap<>();

    private final CommandHandler commandHandler;
    private final Set<ChatId>    chats = new HashSet<>();

    public InMemoryConnector(CommandHandler commandHandler) {
        if (commandHandler == null) {
            throw new IllegalArgumentException("commandHandler must not be null");
        }

        this.commandHandler = commandHandler;
    }

    @Override
    public void listen() {
        System.out.println("Started InMemoryListener");
    }

    @Override
    public void shutdown() {
        connections.clear();
        chats.clear();
        System.out.println("Stopped InMemoryListener");
    }

    @Override
    public ConnectionId<String> parseConnectionId(String id) {
        return new ConnectionId<>(id);
    }

    @Override
    public void onClientConnected(String clientId) {
        connections.put(clientId, this);
    }

    @Override
    public void onClientDisconnected(String clientId) {
        connections.remove(clientId);
    }

    @Override
    public void onCommandReceived(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        commandHandler.handle(command);
    }

    @Override
    public void broadcast(DomainEvent domainEvent) {

    }

    @Override
    public void send(String connectionIdHolder, DomainEvent domainEvent) {
        System.out.printf("%s: %s\n%n", connectionIdHolder, domainEvent);
    }

    @Override
    public void handle(DomainEvent domainEvent) {
        for (var connection : connections.entrySet()) {
            if (connection.getValue().chats.contains(domainEvent.chatId())) {
                send(null, domainEvent);
            }
        }
    }
}
