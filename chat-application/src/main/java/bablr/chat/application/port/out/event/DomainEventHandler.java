package bablr.chat.application.port.out.event;

import bablr.chat.model.event.DomainEvent;

public interface DomainEventHandler {
    void handle(DomainEvent domainEvent);
}
