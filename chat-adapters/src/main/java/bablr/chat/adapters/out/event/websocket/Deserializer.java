package bablr.chat.adapters.out.event.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;

public final class Deserializer<T> {
    private final ObjectMapper                    objectMapper = new ObjectMapper();
    private final Map<String, Class<? extends T>> registry;

    public Deserializer(Map<String, Class<? extends T>> registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    public T deserialize(String json) throws JsonProcessingException {
        var node  = objectMapper.readTree(json);
        var type  = node.get("type").asText();
        var clazz = registry.get(type);

        if (clazz == null) {
            throw new IllegalArgumentException("Unknown event type:" + type);
        }

        return objectMapper.readValue(json, clazz);
    }
}
