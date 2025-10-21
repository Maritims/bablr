package bablr.chat.infrastructure.kafka;

import bablr.chat.application.event.DomainEventPublisher;
import bablr.chat.domain.event.DomainEvent;
import bablr.chat.domain.event.MessageSentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Objects;
import java.util.Properties;

public final class KafkaDomainEventPublisher implements DomainEventPublisher {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper                  objectMapper = new ObjectMapper();
    private final String                        topic;

    public KafkaDomainEventPublisher(String bootstrapServers, String topic) {
        if (bootstrapServers == null) {
            throw new IllegalArgumentException("bootstrapServers must not be null");
        }
        if (bootstrapServers.isBlank()) {
            throw new IllegalArgumentException("bootstrapServers must not be blank");
        }
        if (topic == null) {
            throw new IllegalArgumentException("topic must not be null");
        }
        if (topic.isBlank()) {
            throw new IllegalArgumentException("topic must not be blank");
        }

        this.topic = topic;

        var props = new Properties();
        props.put("bootstrap.servers", Objects.requireNonNull(bootstrapServers));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void publish(DomainEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("event must not be null");
        }

        try {
            String key = null;
            if(event instanceof MessageSentEvent mse) {
                key = mse.chatId().toString();
            }
            var value = objectMapper.writeValueAsString(event);
            producer.send(new ProducerRecord<>(topic, key, value));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish event to Kafka", e);
        }
    }
}
