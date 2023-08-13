package org.sample.dp.functions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.sample.dp.task.SampleJobConfig;
import org.sample.dp.util.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SampleProcessFunction extends ProcessFunction<String, String> {

    private SampleJobConfig config;

    public SampleProcessFunction(SampleJobConfig config){
        this.config = config;
    }

    @Override
    public void processElement(String event, ProcessFunction<String, String>.Context context, Collector<String> collector) {
        try {
            System.out.println("Input Event: " + event);
            HashMap<String, Object> resultMap = JSONUtil.deserialize(event, HashMap.class);
            resultMap.put("status", "successfully process in pipeline job");
            System.out.println("Processed Event: " + resultMap);
            context.output(config.dataOutputTag, JSONUtil.serialize(resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while processing event " + e.getMessage());
        }
    }
}
