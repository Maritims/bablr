package bablr.chat.application.port.out.event;

import bablr.chat.model.event.DomainEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DomainEventDispatcher {
    private final Map<Class<? extends DomainEvent>, Set<DomainEventListener>> allListeners = new HashMap<>();

    public void registerListeners(Class<? extends DomainEvent> event, Set<DomainEventListener> listeners) {
        this.allListeners.put(event, listeners);
    }

    public void dispatch(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("event must not be null");
        }

        var listeners = allListeners.get(domainEvent.getClass());
        if (listeners == null) {
            throw new IllegalArgumentException("No listeners registered for event: " + domainEvent);
        }

        listeners.forEach(listener -> listener.handle(domainEvent));
    }
}
