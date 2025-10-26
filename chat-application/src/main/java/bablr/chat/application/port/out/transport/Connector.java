package bablr.chat.application.port.out.transport;

import bablr.chat.application.command.Command;
import bablr.chat.model.event.DomainEvent;

public interface Connector<TConnectionIdHolder, TConnectionId> {
    void listen();

    void shutdown();

    ConnectionId<TConnectionId> parseConnectionId(TConnectionIdHolder id);

    void onClientConnected(TConnectionIdHolder connection);

    void onClientDisconnected(TConnectionIdHolder connection);

    void onCommandReceived(Command command);

    void send(TConnectionIdHolder connectionIdHolder, DomainEvent domainEvent);

    void broadcast(DomainEvent domainEvent);
}
