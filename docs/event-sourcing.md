---
title: Event sourcing
description: Learn about the event sourcing design pattern. 
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 10
---

# Event sourcing

The event sourcing pattern is a design pattern used to capture events in the order they happen. This pattern is used for things such as audit trails and migrating data to other systems. This data source of events can also be used in nonrelational data stores as the source of truth when working with eventual consistency. The Azure Cosmos DB change feed is a good source to use for event sourcing, since it's an append-only feed that preserves the order of the changes. It captures the events of adding and updating data.

For our code demo, we're building on top of the Cosmos DB Trigger for Azure Functions. We'll be sending our events to Azure Event Hubs, to eventually be used to trigger notifications for products that are updated.

Our end goal for this part is to have our function triggered by the Azure Cosmos DB change feed. It will send its outputs to Azure Event Hubs.

![Diagram showing the Azure Cosmos DB trigger.](./media/event-sourcing/cosmos-db-trigger.png)

## Create Azure Event Hubs resources

In order to work with the Azure Event Hubs, you need to first create an Azure Event Hubs namespace:

```azurecli
az eventhubs namespace create --name pet-supplies-events -g pet-supplies-demo-rg -l eastus
```

Once the namespace is created, then the event hub can be created:

```azurecli
az eventhubs eventhub create --name pet-supplies-events -g pet-supplies-demo-rg --namespace-name pet-supplies-events
```

Once the namespace and event hub are created, then you need to create an authorization rule for accessing the event hub. In our case, we want to be able to publish to the event hub in addition to seeing what's in the event hub. Use the following command:

```azurecli
az eventhubs eventhub authorization-rule create --resource-group pet-supplies-demo-rg --name pet-supplies-events-auth --eventhub-name pet-supplies-events --namespace-name pet-supplies-events --rights Listen Send
```

Store the event hub connection string in an environment variable:

```azurecli
AZURE_EVENT_HUB_CONNECTION=$(az eventhubs eventhub authorization-rule keys list --eventhub-name pet-supplies-events -g pet-supplies-demo-rg --namespace-name pet-supplies-events --name pet-supplies-events-auth --query primaryConnectionString)
```

## Update the code

We need to update our function to take on the \@EventHubOutput annotation. We've also updated the return type and are returning the array of objects coming from the change feed. The updated class in **Function.java** should look like this:

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
   context.getLogger().info("Java Cosmos DB trigger function executed.");
   context.getLogger().info("Documents count: " + items.length);
   return items;
   }
}
```

## Update configurations

Update **local.settings.json** to include the event hub connection information:

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

Update the **pom.xml** file to include this new environment variable:

```xml
<property>
   <name>AzureEventHubConnection</name>
   <value>${AZURE_EVENT_HUB_CONNECTION}</value>
</property>
```

> [!NOTE]
> The \@EventHubOutput annotation's connection property will look for the AzureEventHubConnection application setting.

## Deploy the Azure Function

Deploy these changes to the Azure App Service with the following commands:

```cmd
mvn clean package
mvn azure-functions:deploy
```

When this is deployed, you should see a new environment variable:

![Screenshot showing the Function App Configuration page.](./media/event-sourcing/function-app-configuration.png)

Make some changes to your Contoso Pet Supplies data to trigger the change feed.

Wait about 10 minutes, then check the Azure Event Hubs for events in the log, ready to be consumed by its subscribers.

![Screenshot showing the Event Hubs Instance Overview page.](./media/event-sourcing/event-hubs-instance-overview.png)

[Next &#124; Sending Notifications with Azure Logic Apps](send-notifications-with-azure-logic-apps.md){: .btn .btn-primary .btn-lg }
