package org.sample.dp.connector;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.sample.dp.serde.MapDeserializationSchema;
import org.sample.dp.serde.MapSerializationSchema;
import org.sample.dp.serde.StringDeserializationSchema;
import org.sample.dp.serde.StringSerializationSchema;
import org.sample.dp.task.SampleJobConfig;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

public class FlinkKafkaConnector {

    private final SampleJobConfig config;

    public FlinkKafkaConnector(SampleJobConfig config) {
        this.config = config;
    }

    public SourceFunction<String> kafkaStringSource(String kafkaTopic) {
        return new FlinkKafkaConsumer<>(kafkaTopic, new StringDeserializationSchema(), getKafkaConsumerProperties());
    }

    public SinkFunction<String> kafkaStringSink(String kafkaTopic) {
        return new FlinkKafkaProducer<>(kafkaTopic, new SimpleStringSchema(), getKafkaProducerProperties());
    }

    public SourceFunction<Map<String,Object>> kafkaMapSource(String kafkaTopic) {
        return new FlinkKafkaConsumer<>(kafkaTopic, new MapDeserializationSchema() , getKafkaConsumerProperties());
    }

    public SinkFunction<Map<String, Object>> kafkaMapSink(String kafkaTopic) {
        return new FlinkKafkaProducer<>(kafkaTopic, new MapSerializationSchema(kafkaTopic, Optional.empty()), getKafkaProducerProperties(), FlinkKafkaProducer.Semantic.AT_LEAST_ONCE);
    }

    public Properties getKafkaConsumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaBrokerServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, config.groupId);
        properties.setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return properties;
    }

    public Properties getKafkaProducerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaBrokerServers);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, new Integer(config.kafkaProducerLingerMs));
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, new Integer(config.kafkaProducerBatchSize));
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, new Integer(config.kafkaProducerMaxRequestSize));
        return properties;
    }
}
