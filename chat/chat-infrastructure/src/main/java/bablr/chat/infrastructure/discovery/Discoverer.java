package bablr.chat.infrastructure.discovery;

import bablr.chat.common.Discoverable;
import bablr.chat.common.Initializable;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Discoverer {
    private static void processInitializable(Class<?> discoverable) {
        try {
            var constructor = discoverable.getConstructor();
            var instance    = (Initializable) constructor.newInstance();
            instance.initialize();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No public no-args constructor found for " + discoverable.getName());
        }
    }

    private static final Map<Class<?>, Consumer<Class<?>>> registry = new HashMap<>(Map.of(Initializable.class, Discoverer::processInitializable));

    public static void discover(String packageName) {
        new Reflections(packageName)
                .getTypesAnnotatedWith(Discoverable.class)
                .forEach(discoverable -> registry.forEach((type, processor) -> {
                    if(type.isAssignableFrom(discoverable)) {
                        processor.accept(discoverable);
                    }
                }));
    }
}
