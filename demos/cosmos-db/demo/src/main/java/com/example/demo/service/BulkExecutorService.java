package com.example.demo.service;

import java.util.List;

import com.example.demo.model.BaseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.models.*;

@Component
public class BulkExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkExecutorService.class);

    CosmosAsyncClient client;
    CosmosAsyncDatabase database;
    CosmosAsyncContainer container;
    ObjectMapper mapper;

    //favouring constructor dependency injection
    public BulkExecutorService(CosmosAsyncClient client, CosmosAsyncDatabase database, CosmosAsyncContainer container, ObjectMapper mapper){
        this.client = client;
        this.database = database;
        this.container = container;
        this.mapper = mapper;
    }

    public CosmosBulkItemResponse bulkDelete(List<BaseModel> deleteItems) {
        Flux<BaseModel> docs = Flux.fromIterable(deleteItems);
        Flux<CosmosItemOperation> cosmosItemOperations = docs.map(
            document -> CosmosBulkOperations
                .getDeleteItemOperation(document.getId(), new PartitionKey(document.getDocumentType())));
        return container.executeBulkOperations(cosmosItemOperations).blockLast().getResponse();
    }    

    private String serialize(BaseModel model)  {
        String rawJson = null;
        try {
            rawJson = mapper.writeValueAsString(model);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error serializing", ex);
        }
        LOGGER.info(rawJson);
        return rawJson;
    } 

    public CosmosBulkItemResponse bulkImport(List<BaseModel> documents) {
        Flux<BaseModel> docs = Flux.fromIterable(documents);
        Flux<CosmosItemOperation> cosmosItemOperations = docs.map(
            document -> CosmosBulkOperations.getUpsertItemOperation(document, new PartitionKey(document.getDocumentType())));
        return container.executeBulkOperations(cosmosItemOperations).blockLast().getResponse();
    }

    public CosmosBulkItemResponse bulkUpdate(List<BaseModel> updateItems) {
        Flux<BaseModel> docs = Flux.fromIterable(updateItems);
        Flux<CosmosItemOperation> cosmosItemOperations = docs.map(
            document -> CosmosBulkOperations.getUpsertItemOperation(document, new PartitionKey(document.getDocumentType())));
        return container.executeBulkOperations(cosmosItemOperations).blockLast().getResponse();
    }
}