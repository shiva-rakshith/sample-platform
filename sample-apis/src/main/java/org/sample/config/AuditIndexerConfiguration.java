package org.sample.config;

import org.sample.clients.AuditIndexer;
import org.sample.clients.ElasticSearchClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditIndexerConfiguration {

    @Value("${es.host:localhost}")
    public String esHost;

    @Value("${es.port:9200}")
    public int esPort;

    @Bean
    public AuditIndexer auditIndexer() throws Exception {
        return new AuditIndexer(esHost, esPort, "request_audit", "request_audit");
    }

    @Bean
    public ElasticSearchClient elasticSearchClient() throws Exception {
        return new ElasticSearchClient(esHost, esPort);
    }
}
