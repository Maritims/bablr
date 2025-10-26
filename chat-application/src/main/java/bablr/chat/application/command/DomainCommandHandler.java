package bablr.chat.application.command;

import bablr.chat.common.tuples.Tuple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DomainCommandHandler {
    private final Map<Class<? extends DomainCommand>, Consumer<DomainCommand>> handlers = new HashMap<>();

    @SafeVarargs
    public DomainCommandHandler(Tuple<Class<? extends DomainCommand>, Consumer<DomainCommand>>... handlers) {
        Arrays.stream(handlers).forEach(handler -> this.handlers.put(handler.first(), handler.second()));
    }

    public void handle(DomainCommand domainCommand) {
        if (domainCommand == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var handler = handlers.get(domainCommand.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for command: " + domainCommand);
        }

        handler.accept(domainCommand);
    }
}
