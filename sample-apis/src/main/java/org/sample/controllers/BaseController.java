package org.sample.controllers;

import org.sample.clients.AuditIndexer;
import org.sample.clients.KafkaClient;
import org.sample.clients.PostgreSQLClient;
import org.sample.clients.RedisClient;
import org.sample.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.UUID;

public class BaseController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PostgreSQLClient postgreSQLClient;

    @Autowired
    private KafkaClient kafkaClient;

    @Autowired
    private AuditIndexer auditIndexer;

    @Value("${kafka.topic.input}")
    private String kafkaTopic;

    @Value("${postgres.tablename}")
    private String postgresTable;

    public void processRequest(Map<String,Object> requestBody) throws Exception {
        String mid = UUID.randomUUID().toString();
        requestBody.put("ets", System.currentTimeMillis());
        requestBody.put("mid", mid);
        String requestStr = JSONUtils.serialize(requestBody);

        System.out.println("Processsing request, Mid: " + mid);

        //push request data to redis
        redisClient.set(mid, requestStr, 36000);
        System.out.println("Data pushed to redis");

        //push request data to postgres
        String query = String.format("INSERT INTO %s (mid,data) VALUES ('%s','%s')", postgresTable, mid, requestStr);
        System.out.println(query);
        postgreSQLClient.execute(query);
        System.out.println("Data pushed to Postgres");

        //push request data to kafka
        kafkaClient.send(kafkaTopic, mid, requestStr);
        System.out.println("Data pushed to Kafka");

        //push request data to elastic search
        auditIndexer.createDocument(requestBody);
        System.out.println("Data pushed to Elastic Search");
    }
}

