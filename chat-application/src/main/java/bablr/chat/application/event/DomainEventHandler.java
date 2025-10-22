package bablr.chat.application.event;

import bablr.chat.domain.event.DomainEvent;

@FunctionalInterface
public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T event);
}
