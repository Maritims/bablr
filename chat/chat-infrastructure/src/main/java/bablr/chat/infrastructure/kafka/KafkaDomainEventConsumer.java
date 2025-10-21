package bablr.chat.infrastructure.kafka;

import bablr.chat.application.event.DomainEventDispatcher;
import bablr.chat.domain.event.DomainEvent;
import bablr.chat.domain.event.MessageReadEvent;
import bablr.chat.domain.event.MessageSentEvent;
import bablr.chat.infrastructure.DomainEventDeserializer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.*;

public final class KafkaDomainEventConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final DomainEventDeserializer       deserializer;
    private final DomainEventDispatcher         domainEventDispatcher;

    public KafkaDomainEventConsumer(String bootstrapServers, String topic, String groupId, DomainEventDispatcher domainEventDispatcher) {
        this.domainEventDispatcher = Objects.requireNonNull(domainEventDispatcher);

        var registry = new HashMap<String, Class<? extends DomainEvent>>(Map.of(
                MessageReadEvent.class.getSimpleName(), MessageReadEvent.class,
                MessageSentEvent.class.getSimpleName(), MessageSentEvent.class
        ));

        this.deserializer = new DomainEventDeserializer(registry);

        var props = new Properties();
        props.put("bootstrap.servers", Objects.requireNonNull(bootstrapServers));
        props.put("group.value", Objects.requireNonNull(groupId));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(Objects.requireNonNull(topic)));
    }

    public void pollAndDispatch() {
        var records = consumer.poll(Duration.ofMillis(1000));
        records.forEach(record -> {
            try {
                var json = record.value();
                var event = deserializer.deserialize(json);
                domainEventDispatcher.dispatch(event);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize event", e);
            }
        });
    }
}
