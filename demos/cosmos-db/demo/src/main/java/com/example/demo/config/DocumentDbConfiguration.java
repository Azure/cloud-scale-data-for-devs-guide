package com.example.demo.config;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.ResourceResponse;
import com.microsoft.azure.documentdb.bulkexecutor.DocumentBulkExecutor;
import com.microsoft.azure.documentdb.bulkexecutor.DocumentBulkExecutor.Builder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DocumentDbConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosmosConfiguration.class);

    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String dbName;

    @Value("${azure.cosmos.collection}")
    private String collectionName;

    @Value("${azure.cosmos.partitionKey}")
    private String partitionKey;


    @Bean 
    public DocumentClient getDocumentClient() {
        ConnectionPolicy cPolicy = new ConnectionPolicy();
        cPolicy.setMaxPoolSize(1000);
        return new DocumentClient(uri, key, cPolicy, ConsistencyLevel.Session);
    }

    public static DocumentCollection getDocumentCollection(DocumentClient client, String databaseId, String collectionId) {
        String collectionLink = String.format("/dbs/%s/colls/%s", databaseId, collectionId);
        DocumentCollection collection = null;
        try {
            ResourceResponse<DocumentCollection> collectionResponse = client.readCollection(collectionLink, null);
            collection = collectionResponse.getResource();
        } catch (DocumentClientException dce) {
            LOGGER.error("Error getting collection", dce);
        }
        return collection;
    }

    @Bean
    public Builder getBulkExecutorBuilder() {
        DocumentClient client = getDocumentClient();
         
        client.getConnectionPolicy().getRetryOptions().setMaxRetryWaitTimeInSeconds(30);
        client.getConnectionPolicy().getRetryOptions().setMaxRetryAttemptsOnThrottledRequests(9);
        DocumentCollection collection = getDocumentCollection(client, dbName, collectionName);
        
        return DocumentBulkExecutor.builder()
        .from(
            client,
            dbName,
            collection.getId(),
            collection.getPartitionKey(),
            1000
        );
    }
}