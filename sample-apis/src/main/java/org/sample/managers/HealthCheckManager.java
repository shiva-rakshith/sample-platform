package org.sample.managers;

import org.sample.clients.ElasticSearchClient;
import org.sample.clients.KafkaClient;
import org.sample.clients.PostgreSQLClient;
import org.sample.clients.RedisClient;
import org.sample.dto.Response;
import org.sample.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class HealthCheckManager {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckManager.class);

    @Autowired
    private KafkaClient kafkaClient;
    @Autowired
    private PostgreSQLClient postgreSQLClient;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private ElasticSearchClient elasticSearchUtil;
    public static boolean allSystemHealthResult = true;

    @PostConstruct
    public void init() {
        checkAllSystemHealth();
    }

    public Response checkAllSystemHealth() {
        List<Map<String,Object>> allChecks = new ArrayList<>();
        allChecks.add(generateCheck(Constants.KAFKA, kafkaClient.isHealthy()));
        allChecks.add(generateCheck(Constants.POSTGRESQL, postgreSQLClient.isHealthy()));
        allChecks.add(generateCheck(Constants.REDIS, redisClient.isHealthy()));
        allChecks.add(generateCheck(Constants.ELASTICSEARCH,elasticSearchUtil.isHealthy()));
        for(Map<String,Object> check:allChecks) {
            if((boolean)check.get(Constants.HEALTHY)) {
                allSystemHealthResult = true;
            } else {
                allSystemHealthResult = false;
                break;
            }
        }
        Response response = new Response();
        response.putResult(Collections.singletonMap(Constants.OVERALL_HEALTH, allSystemHealthResult));
        response.putResult(Collections.singletonMap(Constants.CHECKS, allChecks));
        logger.info("All System Health :: Overall Status: {} :: Checks: {}", allSystemHealthResult, allChecks);
        return response;
    }

    private Map<String,Object> generateCheck(String serviceName, boolean health){
        return new HashMap<>() {{
            put(Constants.NAME, serviceName);
            put(Constants.HEALTHY, health);
        }};
    }
}