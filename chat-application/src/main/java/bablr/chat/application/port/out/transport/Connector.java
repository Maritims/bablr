package bablr.chat.application.port.out.transport;

import bablr.chat.application.command.DomainCommand;
import bablr.chat.model.chat.ChatId;
import bablr.chat.model.event.DomainEvent;

import java.util.Set;

public interface Connector<TConnectionIdHolder, TConnectionId> {
    Set<ChatId> getChatIds();

    void listen();

    void shutdown();

    ConnectionId<TConnectionId> parseConnectionId(TConnectionIdHolder id);

    void onClientConnected(TConnectionIdHolder connection);

    void onClientDisconnected(TConnectionIdHolder connection);

    void onCommandReceived(DomainCommand domainCommand);

    void send(TConnectionIdHolder connectionIdHolder, DomainEvent domainEvent);

    void broadcast(DomainEvent domainEvent);
}
