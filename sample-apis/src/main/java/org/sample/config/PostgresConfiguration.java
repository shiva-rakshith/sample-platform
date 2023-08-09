package org.sample.config;

import org.sample.clients.PostgreSQLClient;
import org.sample.exception.ServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class PostgresConfiguration {

    @Value("${postgres.url}")
    private String postgresUrl;

    @Value("${postgres.user}")
    private String postgresUser;

    @Value("${postgres.password}")
    private String postgresPassword;

    @Bean
    public PostgreSQLClient postgreSQLClient() throws ServerException {
        return new PostgreSQLClient(postgresUrl, postgresUser, postgresPassword);
    }

}
