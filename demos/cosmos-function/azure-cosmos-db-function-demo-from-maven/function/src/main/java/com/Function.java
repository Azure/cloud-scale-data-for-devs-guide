package com;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Cosmos DB trigger.
 */
public class Function {
    /**
     * This function will be invoked when there are inserts or updates in the specified database and collection.
     */
    @FunctionName("CosmosDBChangeFeedMonitor")
    @EventHubOutput(name = "event", eventHubName = "pet-supplies-events", connection = "AzureEventHubConnection")
    public Object[] pushToEventGrid(
        @CosmosDBTrigger(
            name = "items",
            databaseName = "pet-supplies",
            collectionName = "pet-supplies",
            leaseCollectionName="lease",
            connectionStringSetting = "AzureCosmosDBConnectionString",
            createLeaseCollectionIfNotExists = true
        )
        Object[] items,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Cosmos DB trigger function executed.");
        context.getLogger().info("Documents count: " + items.length);
        return items;
    }
}
