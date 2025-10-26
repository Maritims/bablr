package bablr.chat.application.port.in.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandHandler {
    private final Map<Class<? extends Command>, Consumer<Command>> handlers = new HashMap<>();

    public CommandHandler register(Class<? extends Command> command, Consumer<Command> handler) {
        handlers.put(command, handler);
        return this;
    }

    public void handle(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var handler = handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for command: " + command);
        }

        handler.accept(command);
    }
}
