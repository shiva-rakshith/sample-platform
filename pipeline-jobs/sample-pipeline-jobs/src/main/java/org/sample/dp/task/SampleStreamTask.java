package org.sample.dp.task;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.sample.dp.connector.FlinkKafkaConnector;
import org.sample.dp.functions.SampleProcessFunction;
import scala.Option;
import scala.Some;

import java.io.File;
import java.util.Map;

public class SampleStreamTask {

    private final SampleJobConfig config;
    private final FlinkKafkaConnector kafkaConnector;

    public SampleStreamTask(SampleJobConfig config, FlinkKafkaConnector kafkaConnector){
        this.config = config;
        this.kafkaConnector = kafkaConnector;
    }

    public static void main(String[] args) {
        Option<String> configFilePath = new Some<String>(ParameterTool.fromArgs(args).get("config.file.path"));
        Config conf = configFilePath.map(path -> ConfigFactory.parseFile(new File(path)).resolve())
                .getOrElse(() -> ConfigFactory.load("resources/sample-pipeline.conf").withFallback(ConfigFactory.systemEnvironment()));
        SampleJobConfig config = new SampleJobConfig(conf,"Sample-Pipeline-Job");
        FlinkKafkaConnector kafkaConnector = new FlinkKafkaConnector(config);
        SampleStreamTask streamTask = new SampleStreamTask(config, kafkaConnector);
        try {
            streamTask.process();
        } catch (Exception e) {
            System.out.println("Error while processing the job, exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void process() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SourceFunction<String> kafkaConsumer =  kafkaConnector.kafkaStringSource(config.kafkaInputTopic);
        env.enableCheckpointing(config.checkpointingInterval);
        env.getCheckpointConfig().setCheckpointTimeout(config.checkpointingTimeout);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(config.checkpointingPauseSeconds);
        SingleOutputStreamOperator<String> eventStream = env.addSource(kafkaConsumer, config.jobConsumer)
                .uid(config.jobConsumer).setParallelism(config.consumerParallelism)
                .rebalance()
                .process(new SampleProcessFunction(config)).setParallelism(config.downstreamOperatorsParallelism);

        /** Sink for output events */
        eventStream.getSideOutput(config.dataOutputTag).addSink(kafkaConnector.kafkaStringSink(config.kafkaOutputTopic))
                .name(config.jobProducer).uid(config.jobProducer).setParallelism(config.downstreamOperatorsParallelism);

        System.out.println(config.jobName + " is processing");
        env.execute(config.jobName);
    }
}
