package bablr.chat.application.event;

import bablr.chat.domain.event.DomainEvent;

import java.util.HashMap;
import java.util.Map;

public final class DomainEventDispatcher {
    private final Map<Class<? extends DomainEvent>, DomainEventHandler<? extends DomainEvent>> handlers = new HashMap<>();

    public <T extends DomainEvent> void registerHandler(Class<T> type, DomainEventHandler<T> handler) {
        handlers.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void dispatch(T event) {
        var handler = (DomainEventHandler<T>) handlers.get(event.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for event type:" + event.getClass().getSimpleName());
        }
        handler.handle(event);
    }
}
