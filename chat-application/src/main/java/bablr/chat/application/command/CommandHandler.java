package bablr.chat.application.command;

import bablr.chat.common.tuples.Tuple;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandHandler {
    private final Map<Class<? extends Command>, Consumer<Command>> handlers = new HashMap<>();

    @SafeVarargs
    public CommandHandler(Tuple<Class<? extends Command>, Consumer<Command>>... handlers) {
        Arrays.stream(handlers).forEach(this::register);
    }

    public void register(Tuple<Class<? extends Command>, Consumer<Command>> handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler must not be null");
        }

        handlers.put(handler.first(), handler.second());
    }

    public void scan() {
        var reflections = new Reflections();
        reflections.getTypesAnnotatedWith(CommandProcessor.class).forEach(clazz -> {
            var annotation = clazz.getAnnotation(CommandProcessor.class);
            if (annotation == null) {
                throw new IllegalArgumentException("CommandProcessor annotation not found on class: " + clazz);
            }

            if (!Command.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("CommandProcessor annotation found on class that does not implement Command: " + clazz);
            }

            var processor = clazz.getConstructors();
        });
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
