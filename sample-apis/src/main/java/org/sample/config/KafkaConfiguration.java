package org.sample.config;

import org.sample.clients.KafkaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Value("${bootstrap-servers}")
    private String kafkaServerUrl;

    @Bean
    public KafkaClient kafkaClient() {
        return new KafkaClient(kafkaServerUrl);
    }

}
