package org.sample.dp.serde;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.common.serialization.Serializer;
import com.google.gson.Gson;

public class MapSerializationSchema implements KafkaSerializationSchema<Map<String, Object>> {

    private static final long serialVersionUID = -4284080856874185929L;
    private final String topic;
    private final Optional<String> key;

    public MapSerializationSchema(String topic, Optional<String> key) {
        this.topic = topic;
        this.key = key;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Map<String, Object> element, Long timestamp) {
        String out = new Gson().toJson(element);
        byte[] valueBytes = out.getBytes(StandardCharsets.UTF_8);

        if (key.isPresent()) {
            byte[] keyBytes = key.get().getBytes(StandardCharsets.UTF_8);
            return new ProducerRecord<>(topic, keyBytes, valueBytes);
        } else {
            return new ProducerRecord<>(topic, valueBytes);
        }
    }
}

