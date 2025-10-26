package bablr.chat.application.port.out.event;

import bablr.chat.model.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DomainEventDispatcher {
    private static final Logger                                                     log      = LoggerFactory.getLogger(DomainEventDispatcher.class);
    private final        Map<Class<? extends DomainEvent>, Set<DomainEventHandler>> registry = new HashMap<>();

    public DomainEventDispatcher() {
    }

    public void register(Class<? extends DomainEvent> event, DomainEventHandler handler) {
        this.registry.computeIfAbsent(event, k -> new HashSet<>()).add(handler);
    }

    public void dispatch(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("event must not be null");
        }

        var handlers = registry.get(domainEvent.getClass());
        if (handlers == null) {
            throw new IllegalArgumentException("No handlers registered for event: " + domainEvent);
        }

        log.debug("Dispatching event: {}", domainEvent);

        handlers.forEach(handler -> handler.handle(domainEvent));
    }
}
