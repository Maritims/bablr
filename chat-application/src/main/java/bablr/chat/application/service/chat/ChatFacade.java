package bablr.chat.application.service.chat;

import bablr.chat.application.port.out.event.DomainEventDispatcher;
import bablr.chat.application.port.out.event.DomainEventListener;
import bablr.chat.model.event.ChatCreated;
import bablr.chat.model.event.MessageSent;

import java.util.Set;

public class ChatFacade {
    private final Set<DomainEventListener> domainEventListeners;

    public ChatFacade(DomainEventListener... domainEventListeners) {
        this.domainEventListeners = Set.of(domainEventListeners);
    }

    public ChatFacade registerListeners(DomainEventDispatcher domainEventDispatcher) {
        domainEventDispatcher.registerListeners(ChatCreated.class, domainEventListeners);
        domainEventDispatcher.registerListeners(MessageSent.class, domainEventListeners);
        return this;
    }

    public ChatFacade start() {
        domainEventListeners.forEach(DomainEventListener::start);
        return this;
    }

    public void stop() {
        domainEventListeners.forEach(DomainEventListener::stop);
    }
}
