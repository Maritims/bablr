package bablr.chat.application.event;

import bablr.chat.common.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
