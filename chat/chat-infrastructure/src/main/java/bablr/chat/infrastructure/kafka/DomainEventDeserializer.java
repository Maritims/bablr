package bablr.chat.infrastructure.kafka;

import bablr.chat.common.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;

public final class DomainEventDeserializer {
    private final ObjectMapper                              objectMapper = new ObjectMapper();
    private final Map<String, Class<? extends DomainEvent>> registry;

    public DomainEventDeserializer(Map<String, Class<? extends DomainEvent>> registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    public DomainEvent deserialize(String json) throws JsonProcessingException {
        var node = objectMapper.readTree(json);
        var type = node.get("type").asText();
        var clazz = registry.get(type);

        if (clazz == null) {
            throw new IllegalArgumentException("Unknown event type:" + type);
        }

        return objectMapper.readValue(json, clazz);
    }
}
