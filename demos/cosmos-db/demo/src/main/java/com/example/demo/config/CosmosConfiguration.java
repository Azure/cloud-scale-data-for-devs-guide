package com.example.demo.config;

import com.azure.cosmos.ChangeFeedProcessorBuilder;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.cosmos.models.ChangeFeedProcessorOptions;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

@Configuration
@EnableCosmosRepositories
public class CosmosConfiguration extends AbstractCosmosConfiguration {

    // These azure.cosmos variables are coming from the Config server if in Azure Spring Apps.
    // Otherwise, for development purposes, these values are stored in application-default.properties.
    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String dbName;

    @Value("${azure.cosmos.feed-container}")
    private String feedContainer;

    @Value("${azure.cosmos.lease-container}")
    private String leaseContainer;

    public CosmosConfiguration() {}

    @Bean
    public CosmosClientBuilder getCosmosClientBuilder() {
        DirectConnectionConfig directConnectionConfig = new DirectConnectionConfig();
        GatewayConnectionConfig gatewayConnectionConfig = new GatewayConnectionConfig();
        return new CosmosClientBuilder()
            .endpoint(uri)
            .key(key)
            .contentResponseOnWriteEnabled(true)
            .directMode(directConnectionConfig, gatewayConnectionConfig);
    }

    @Bean
    public ChangeFeedProcessorBuilder getChangeFeedProcessor() {
        CosmosAsyncClient client = getCosmosClientBuilder().buildAsyncClient();
        CosmosAsyncDatabase database = client.getDatabase(this.dbName);
        CosmosAsyncContainer feedContainer = database.getContainer(this.feedContainer);
        CosmosAsyncContainer leaseContainer = database.getContainer(this.leaseContainer);
        ChangeFeedProcessorOptions cfOptions = new ChangeFeedProcessorOptions();
        return new ChangeFeedProcessorBuilder()
            .options(cfOptions)
            .hostName(this.uri)
            .feedContainer(feedContainer)
            .leaseContainer(leaseContainer)
            .handleChanges(docs -> {
                for (JsonNode item : docs) {
                    System.out.println(item);
                }
            });
    }

    @Override
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                           .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                           .build();
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            // LOGGER.info("Response Diagnostics {}", responseDiagnostics);
        }
    }

}