package org.sample.dp.serde;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class StringSerializationSchema implements KafkaSerializationSchema<String> {

    private static final long serialVersionUID = -4284080856874185929L;
    private final String topic;
    private final Optional<String> key;

    public StringSerializationSchema(String topic) {
        this(topic, Optional.empty());
    }

    public StringSerializationSchema(String topic, Optional<String> key) {
        this.topic = topic;
        this.key = key;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String element, Long timestamp) {
        byte[] keyBytes = key.map(k -> k.getBytes(StandardCharsets.UTF_8)).orElse(null);
        byte[] valueBytes = element.getBytes(StandardCharsets.UTF_8);

        return new ProducerRecord<>(topic, keyBytes, valueBytes);
    }
}
