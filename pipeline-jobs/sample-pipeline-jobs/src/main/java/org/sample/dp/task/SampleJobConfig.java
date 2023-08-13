package org.sample.dp.task;

import com.typesafe.config.Config;
import org.apache.flink.util.OutputTag;

import java.io.Serializable;

public class SampleJobConfig implements Serializable {

    private final Config config;

    // kafka
    public String kafkaInputTopic;

    public String kafkaOutputTopic;
    public String kafkaBrokerServers;
    public String groupId;
    public int kafkaProducerLingerMs;
    public int kafkaProducerBatchSize;
    public int kafkaProducerMaxRequestSize;

    // Consumers
    public String jobConsumer = "sample-job-consumer";
    public String jobProducer = "sample-job-producer";
    public int consumerParallelism;
    public int downstreamOperatorsParallelism;

    public  int checkpointingInterval;

    public Long checkpointingTimeout;

    public int checkpointingPauseSeconds;

    public String jobName;

    public OutputTag<String> dataOutputTag = new OutputTag<String>("output-events"){};

    public SampleJobConfig(Config config, String jobName) {
        this.config = config;
        this.jobName = jobName;
        initValues();
    }

    public void initValues() {
        kafkaInputTopic = config.getString("kafka.input.topic");
        kafkaOutputTopic = config.getString("kafka.output.topic");
        kafkaBrokerServers = config.getString("kafka.broker-servers");
        groupId = config.getString("kafka.groupId");
        kafkaProducerLingerMs = config.getInt("kafka.producer.linger.ms");
        kafkaProducerBatchSize = config.getInt("kafka.producer.batch.size");
        kafkaProducerMaxRequestSize = config.getInt("kafka.producer.max-request-size");
        consumerParallelism = config.getInt("task.consumer.parallelism");
        downstreamOperatorsParallelism = config.getInt("task.downstream.operators.parallelism");
        checkpointingInterval = config.getInt("task.checkpointing.interval");
        checkpointingTimeout = config.getLong("task.checkpointing.timeout");
        checkpointingPauseSeconds = config.getInt("task.checkpointing.pause.between.seconds");
    }
}
