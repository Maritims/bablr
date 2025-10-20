package bablr.chat.application.event;

import bablr.chat.common.DomainEvent;

@FunctionalInterface
public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T event);
}
