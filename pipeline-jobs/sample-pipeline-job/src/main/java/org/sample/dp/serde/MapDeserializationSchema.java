package org.sample.dp.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.reflect.TypeToken;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.MapTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

import org.sample.dp.util.JSONUtil;

public class MapDeserializationSchema implements KafkaDeserializationSchema<Map<String,Object>> {

    private static final long serialVersionUID = -3224825136576915426L;

    @Override
    public boolean isEndOfStream(Map nextElement) {
        return false;
    }

    @Override
    public Map<String,Object> deserialize(ConsumerRecord<byte[], byte[]> record) {
        byte[] valueBytes = record.value();
        return JSONUtil.deserializeToMap(valueBytes);
    }

    @Override
    public TypeInformation<Map<String,Object>> getProducedType() {
        return TypeInformation.of(new TypeHint<Map<String, Object>>() {});
    }

}



