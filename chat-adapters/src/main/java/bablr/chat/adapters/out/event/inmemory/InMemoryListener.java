package bablr.chat.adapters.out.event.inmemory;

import bablr.chat.application.port.in.chat.CommandHandler;
import bablr.chat.application.port.out.event.DomainEventListener;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.event.DomainEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryListener implements DomainEventListener {
    private static final Map<String, InMemoryListener> connections = new HashMap<>();
    private final        Set<ChatId>                   chats       = new HashSet<>();
    private              CommandHandler                commandHandler;

    public void acceptConnection(String clientId) {
        connections.put(clientId, this);
    }

    public void closeConnection(String clientId) {
        connections.remove(clientId);
    }

    public void send(ChatId chatId, DomainEvent domainEvent) {
        System.out.printf("%s: %s\n%n", chatId, domainEvent);
    }

    @Override
    public void handle(DomainEvent domainEvent) {
        for(var connection : connections.entrySet()) {
            if(connection.getValue().chats.contains(domainEvent.chatId())) {
                send(domainEvent.chatId(), domainEvent);
            }
        }
    }

    @Override
    public DomainEventListener commandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        return this;
    }

    @Override
    public void start() {
        System.out.println("Started InMemoryListener");
    }

    @Override
    public void stop() {
        connections.clear();
        chats.clear();
        System.out.println("Stopped InMemoryListener");
    }
}
