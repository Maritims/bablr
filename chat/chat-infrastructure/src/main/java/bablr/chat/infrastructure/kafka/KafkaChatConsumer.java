package bablr.chat.infrastructure.kafka;

import bablr.chat.domain.event.MessageSentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class KafkaChatConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper                  objectMapper = new ObjectMapper();

    public KafkaChatConsumer(String bootstrapServers, String topic, String groupId) {
        var props = new Properties();
        props.put("bootstrap.servers", Objects.requireNonNull(bootstrapServers));
        props.put("group.id", Objects.requireNonNull(groupId));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(Objects.requireNonNull(topic)));
    }

    public void pollAndProcess() {
        var records = consumer.poll(Duration.ofMillis(1000));
        records.forEach(record -> {
            try {
                var event = objectMapper.readValue(record.value(), MessageSentEvent.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize event", e);
            }
        });
    }
}
