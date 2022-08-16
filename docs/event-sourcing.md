---
title: Update the Azure Cosmos DB trigger code for event sourcing
description: Learn how to update the Azure Cosmos DB trigger code you previously created for event sourcing and apply it to Azure Cosmos DB change feed. 
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 10
---

# Update the Azure Cosmos DB trigger code for event sourcing

The *event sourcing* pattern is a design pattern that captures events in the same order as they occur. You use this pattern for tasks such as audit trails and data migration. You can also use it as the source of truth in nonrelational data stores when you work with event consistency. The Azure Cosmos DB change feed is a good source to use for event sourcing because it's an append-only feed that preserves the order of changes. It also captures data add and update events.

The following code sample builds on the Azure Cosmos DB trigger code for Azure Functions. It sends events to Azure Event Hubs to trigger notifications for product updates. The goal is to use the Azure Cosmos DB change feed to trigger your function, which will then send its output to Azure Event Hubs.

![Diagram showing the Azure Cosmos DB trigger.](media/event-sourcing/cosmos-db-trigger.png)

## Create Azure Event Hubs resources

1. To work with [Azure Event Hubs](https://docs.microsoft.com/azure/event-hubs/), you first need to create an Azure Event Hubs namespace:

   ```azurecli
   az eventhubs namespace create --name pet-supplies-events -g pet-supplies-demo-rg -l eastus
   ```

1. After you create the namespace, you can then create the event hub:

   ```azurecli
   az eventhubs eventhub create --name pet-supplies-events -g pet-supplies-demo-rg --namespace-name pet-supplies-events
   ```

1. After you create the namespace and event hub, you're ready to create an authorization rule for accessing the event hub. In this case, you want to publish to the event hub and then view what's in it. To do so, use the following command:

   ```azurecli
   az eventhubs eventhub authorization-rule create --resource-group pet-supplies-demo-rg --name pet-supplies-events-auth --eventhub-name pet-supplies-events --namespace-name pet-supplies-events --rights Listen Send
   ```

1. Store the event hub connection string in an environment variable:

   ```azurecli
   AZURE_EVENT_HUB_CONNECTION=$(az eventhubs eventhub authorization-rule keys list --eventhub-name pet-supplies-events -g pet-supplies-demo-rg --namespace-name pet-supplies-events --name pet-supplies-events-auth --query primaryConnectionString)
   ```

## Update the code

You need to update your function to handle the `@EventHubOutput` annotation. You also need to update the return type and return the array of objects returned by the change feed. The updated class in *Function.java* should look like this:

```java
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
)  {
   context.getLogger().info("Azure Cosmos DB Java trigger function executed.");
   context.getLogger().info("Documents count: " + items.length);
   return items;
   }
}
```

## Update configurations

1. Update the *local.settings.json* file to include information from the event hub connection:

   ```json
   {
   "IsEncrypted": false,
   "Values": {
      "AzureWebJobsStorage": "%AZURE_WEBJOBS_STORAGE%",
      "AzureCosmosDBConnectionString": "%AZURE_COSMOS_CONNECTION_STRING%",
      "AzureEventHubConnection": "%AZURE_EVENT_HUB_CONNECTION%",
      "FUNCTIONS_WORKER_RUNTIME": "java"
      }
   }
   ```

1. Update the *pom.xml* file to include your new environment variable:

   ```xml
   <property>
      <name>\<AzureEventHubConnection></name>
      <value>${AZURE_EVENT_HUB_CONNECTION}</value>
   </property>
   ```

   The `@EventHubOutput` annotation's connection property looks for the \<AzureEventHubConnection> application setting.

## Deploy to Azure App Service

1. Deploy the previous changes to Azure App Service by using the following commands:

   ```cmd
   mvn clean package
   mvn azure-functions:deploy
   ```

   After your changes deploy successfully, you should see a new environment variable:

   ![Screenshot showing the Function App Configuration page.](media/event-sourcing/function-app-configuration.png)

1. To test the deployment, change your Contoso Pet Supplies data to trigger the change feed.

1. Wait about 10 minutes, then check Azure Event Hubs for events in the log that are ready to be consumed by its subscribers.

   ![Screenshot showing the Azure Event Hubs Instance Overview page.](media/event-sourcing/event-hubs-instance-overview.png)

[Next &#124; Sending Notifications with Azure Logic Apps](send-notifications-with-azure-logic-apps.md){: .btn .btn-primary .btn-lg }
