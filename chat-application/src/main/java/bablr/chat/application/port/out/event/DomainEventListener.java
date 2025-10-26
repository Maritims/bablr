package bablr.chat.application.port.out.event;

import bablr.chat.application.port.in.chat.CommandHandler;
import bablr.chat.model.event.DomainEvent;

public interface DomainEventListener {
    void handle(DomainEvent domainEvent);

    DomainEventListener commandHandler(CommandHandler commandHandler);

    void start();

    void stop();
}
