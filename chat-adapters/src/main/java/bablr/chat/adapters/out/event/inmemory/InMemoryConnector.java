package bablr.chat.adapters.out.event.inmemory;

import bablr.chat.application.command.DomainCommand;
import bablr.chat.application.command.DomainCommandHandler;
import bablr.chat.application.port.out.event.DomainEventHandler;
import bablr.chat.application.port.out.transport.ConnectionId;
import bablr.chat.application.port.out.transport.Connector;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryConnector implements Connector<String, String>,
                                          DomainEventHandler {
    private static final Logger                         log        = LoggerFactory.getLogger(InMemoryConnector.class);
    private static final Map<String, InMemoryConnector> connectors = new HashMap<>();

    private final DomainCommandHandler domainCommandHandler;
    private final Set<ChatId>          chats = new HashSet<>();

    private String clientId;

    public InMemoryConnector(DomainCommandHandler domainCommandHandler) {
        if (domainCommandHandler == null) {
            throw new IllegalArgumentException("commandHandler must not be null");
        }

        this.domainCommandHandler = domainCommandHandler;
    }

    @Override
    public void listen() {
        System.out.println("Started InMemoryListener");
    }

    @Override
    public void shutdown() {
        connectors.clear();
        chats.clear();
        System.out.println("Stopped InMemoryListener");
    }

    @Override
    public ConnectionId<String> parseConnectionId(String id) {
        return new ConnectionId<>(id);
    }

    @Override
    public void onClientConnected(String clientId) {
        this.clientId = clientId;

        connectors.put(clientId, this);
    }

    @Override
    public void onClientDisconnected(String clientId) {
        connectors.remove(clientId);
    }

    @Override
    public void onCommandReceived(DomainCommand domainCommand) {
        if (domainCommand == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        domainCommandHandler.handle(domainCommand);
    }

    @Override
    public void broadcast(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        connectors.values().forEach(c -> c.send(domainEvent.chatId().toString(), domainEvent));
    }

    @Override
    public void send(String connectionIdHolder, DomainEvent domainEvent) {
        if (connectionIdHolder == null) {
            throw new IllegalArgumentException("connectionIdHolder must not be null");
        }
        if (connectionIdHolder.isBlank()) {
            throw new IllegalArgumentException("connectionIdHolder must not be blank");
        }
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        System.out.printf("%s: %s\n%n", connectionIdHolder, domainEvent);
    }

    @Override
    public void handle(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("domainEvent must not be null");
        }

        log.info("Handling event: {}", domainEvent);

        for (var connectors : connectors.entrySet()) {
            if (connectors.getValue().chats.contains(domainEvent.chatId())) {
                send(connectors.getKey(), domainEvent);
            }
        }
    }
}
