package bablr.chat.application.event;

import bablr.chat.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
