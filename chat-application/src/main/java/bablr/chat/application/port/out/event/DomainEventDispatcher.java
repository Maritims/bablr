package bablr.chat.application.port.out.event;

import bablr.chat.model.event.DomainEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class DomainEventDispatcher {
    private final Map<Class<? extends DomainEvent>, Set<DomainEventHandler>> registry = new HashMap<>();

    public DomainEventDispatcher() {}

    @SafeVarargs
    public DomainEventDispatcher(BiConsumer<Class<? extends DomainEvent>, Set<DomainEventHandler>>... registryConsumers) {
        Arrays.stream(registryConsumers).forEach(consumer -> consumer.accept(DomainEvent.class, registry.get(DomainEvent.class)));
    }

    public void register(Class<? extends DomainEvent> event, Set<DomainEventHandler> handlers) {
        this.registry.put(event, handlers);
    }

    public void dispatch(DomainEvent domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("event must not be null");
        }

        var handlers = registry.get(domainEvent.getClass());
        if (handlers == null) {
            throw new IllegalArgumentException("No handlers registered for event: " + domainEvent);
        }

        handlers.forEach(handler -> handler.handle(domainEvent));
    }
}
