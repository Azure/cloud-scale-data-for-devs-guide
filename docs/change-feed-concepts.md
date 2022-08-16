---
title: Azure Cosmos DB change feed
description: Learn about the Azure Cosmos DB change feed.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 8
---

# Azure Cosmos DB change feed

## What is the change feed?

The [change feed](https://docs.microsoft.com/azure/cosmos-db/change-feed) is an Azure Cosmos DB feature that tracks changes to documents. Changes are tracked in a monitored container in the same order that they're processed.

Some potential uses for the change feed include:

- Audit trails: Track changes as they happen and preserving the order in which they happen.

- Real-time data processing: Examples include working with data from sensors, devices, applications, and other technologies that report data on a large scale. Or, working with the data in materialized views.

- Event sourcing: As an append-only data source, the change feed works well as a source in an [event sourcing pattern](https://docs.microsoft.com/azure/architecture/patterns/event-sourcing).

- Data synchronization: Examples include synchronizing data with a data store, such as a data warehouse or data lake.

## Who uses the change feed?

The following customer stories include examples that demonstrate how to use the change feed:

- [Sandvik Coromant](https://customers.microsoft.com/story/810496-sandvik-coromant-chemicals-power-bi) leverages the change feed to react to events more effectively.

- [SitePro](https://customers.microsoft.com/story/1366128637262632842-sitepro-accelerates-green-expansion-using-azure-cache-for-redis) uses the change feed as a decision audit trail.

- [London-based online fashion retailer ASOS](https://customers.microsoft.com/story/asos-retail-and-consumer-goods-azure) makes the change feed part of their order workflow, working with event-driven microservices.

## Key components for processing the change feed

There are a few key components to understand about change feed processing:

- Monitored container

- Lease container

- Host

- Delegate

The *monitored container* stores monitored changes. In the example, you monitor the pet-supplies collection in the pet-supplies database. The *lease container* tracks the state of feeds for all change feed consumers. You need to add this container to your Azure Cosmos DB database for it to work. You should create the *lease collection* with a partition key of \/id. The *host* listens and reacts to changes from the feed. The *delegate* handles the business logic of the process.

The host needs to acquire a *lease*. This document bookmarks the progress of the host that's processing the feed. Suppose the change feed processor stops while changes are being made to the monitored container. When the change feed processor restarts, it grabs its lease, passes the changes to the delegate, and then updates its lease with a new continuation token. This bookmark marks its location in the feed.

### Create the lease container

To create the lease container, run the following command:

```azurecli
az cosmosdb sql container create -g pet-supplies-demo-rg --account-name
pet-supplies-demo --database-name pet-supplies --name lease
--partition-key-path "/id"
```

### Review the code

The code has the following settings in *application-default.properties* file for the change feed:

```json
azure.cosmos.feed-container=pet-supplies
azure.cosmos.lease-container=lease
```

You also update the returned builder in the `getCosmosClientBuilder()` function, which is in the `CosmosConfiguration` class. Include `contentResponseOnWriteEnabled`, because the change feed processor requires it.

```java
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
```

## Delete strategy with the change feed

Inserts and updates appear in the change feed. However, if you delete an item from a collection, it isn't captured by the change feed. You can capture deletes in two ways:

- Enable Time to Live (TTL) in the collection. Doing so enables you to set expiration times on documents. For more information, see [configure time to live in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/how-to-time-to-live?tabs=javav4).

- Use the TTL property of an item to mark it for deletion.

The TTL property has the following characteristics:

- If the property is null or not present in a document, the document won't expire.

- If the property is present and set to -1 in a document, it won't expire.

- If the TTL property is present in a document, then that document will expire according to the TTL period. The duration is expressed in number of seconds after the last modified time.

## Consume the change feed

There are a few ways to read the change feed. These methods usually involve push and pull models. Push models are the easiest way to get data from the change feed.

> If you need to control change feed consumption at a customized pace, use the pull model. For more information, see [Change feed pull model in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/change-feed-pull-model).

There are two ways to receive push notifications:

- Azure Functions

- Change feed processor

The code sample uses the change feed processor, with reactive programming.

### Azure Functions

Azure Functions is the simplest way to work with the change feed. You need to use the push model in a serverless, event-based flow. Functions connect to the change feed through an Azure Functions trigger for Azure Cosmos DB. For more information, see [Create a function triggered by Azure Cosmos DB](https://docs.microsoft.com/azure/azure-functions/functions-create-cosmos-db-triggered-function).

### Use the change feed processor

To use the change feed processor in your application, create the change feed service, which starts the change feed processor. As the service picks up changes from the change feed, it passes them to the `handleChanges` function.

![Diagram showing how the change feed processor works.](media/change-feed-concepts/change-feed-processor.png)

You use the change feed controller to access output from the change feed via the API. The change feed controller gets changes from the change feed service and the activity occurs in the *ChangeFeedService.java* file.

There's a `ChangeFeedProcessorBuilder` bean in *CosmosConfiguration.java* where you set the database, the monitored container (feedContainer), the lease container (leaseContainer), and any options. The builder bean is set to emit the items through the for loop passed to `handleChanges()`.

In *CosmosConfiguration.java*, you use the `ChangeFeedProcessorBuilder` bean to set the database the monitored container (feedContainer), the lease container (leaseContainer), and any options. The builder bean is set to emit the items through the `for` loop passed to `handleChanges()`.

The change feed service handles the logic for processing change documents. In `init()`, you build a change feed processor by using the builder, which you then pass to an updated change handler. You also set the `changedProducts` public property to subscribe to the `productSink` output. The logic for filtering our documents is handled in the `handleChanges()` function.

## Run the code

The purpose of this code is to monitor product changes. It's a starting point for handling change feed modifications by using your own logic.

1. On the home page of the sample application, open the link to the change feed service in another tab.

1. Run the Update Product query in the Postman collection.

   After the query has finished, an entry appears on the change feed page in your browser.

1. Run the Update Product Price query in the Postman collection, which does a partial document update.

   After the query has finished, partial updates appear on the change feed page in your browser.

## Learn more

- [Change feed design patterns in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/change-feed-design-patterns)

- [Azure Cosmos DB change feed](https://github.com/AzureCosmosDB/labs/blob/master/java/labs/08-change_feed_with_azure_functions.md)

[Next &#124; Azure Cosmos DB Trigger for Azure Functions in Java](change-feed-with-cosmos-db-trigger-function.md){: .btn .btn-primary .btn-lg }
