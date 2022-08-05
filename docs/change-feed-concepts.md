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

The change feed is an Azure Cosmos DB feature that tracks changes to documents. Changes are tracked in a monitored container in the same order that they are processed.

Some potential uses for the change feed include:

- Audit trails: tracking changes as they happen and preserving the order in which they happen.

- Real time data processing: such as working with data from sensors, devices, applications, and other technologies that report data on a large scale. Or working with the data in materialized views.

- Event sourcing: as an append-only data source, the change feed works well as a source in an [event sourcing pattern](https://docs.microsoft.com/azure/architecture/patterns/event-sourcing).

- Data synchronization: such as synchronizing data with data stores, such as data warehouses or data lakes.

## Who uses the change feed?

The following are a few customer stories that expand on the change feed:

- [Sandvik Coromant](https://customers.microsoft.com/story/810496-sandvik-coromant-chemicals-power-bi) leverages the change feed to react to events more effectively.

- [SitePro](https://customers.microsoft.com/story/1366128637262632842-sitepro-accelerates-green-expansion-using-azure-cache-for-redis) uses the change feed as a decision audit trail.

- [ASOS](https://customers.microsoft.com/story/asos-retail-and-consumer-goods-azure)  makes the change feed part of their order workflow, working with event-driven microservices.

## Key components for processing the change feed

There are a few key components you need to understand about change feed processing:

- Monitored container

- Lease container

- Host

- Delegate

The *monitored container* stores monitored changes. In our example, we monitor the pet-supplies collection in the pet-supplies database. The *lease container*  tracks the state of feeds for all change feed consumers. This is another container that needs to be added to your Cosmos DB database for it to work. The *lease collection* should be created with a partition key of /id. *Host* listens and reacts to changes from the feed. *Delegate* handles the business logic of the process.

The host needs to acquire a *lease*. This a document that bookmarks the progress of the host that is processing the feed. Suppose the change feed processor stops while changes are being made to the monitored container. When the change feed processor restarts, it grabs its lease, passes the changes to the delegate, and then updates its lease with a new continuation token. This is a bookmark of its location in the feed.

### Create the lease container

To create the lease container, run the following command:

```azurecli
az cosmosdb sql container create -g pet-supplies-demo-rg --account-name
pet-supplies-demo --database-name pet-supplies --name lease
--partition-key-path "/id"
```

### Review the code

In our code, we have the following settings in `application-default.properties` for the change feed:

```json
azure.cosmos.feed-container=pet-supplies
azure.cosmos.lease-container=lease
```

We also updated the returned builder in the `getCosmosClientBuilder()` function, which is in the `CosmosConfiguration` class. We want to include `contentResponseOnWriteEnabled`, because the change feed processor requires it.

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

Inserts and updates appear in the change feed. However, if you delete an item from a collection, it isn't captured by the change feed. The way to capture deletes is two-fold:

- Enable Time to Live (ttl) in the collection. This enables you to set expiration times on documents. View [configure time to live in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/how-to-time-to-live?tabs=javav4) for more information.

- Use the ttl property of an item to mark it for deletion.

Let's explore the ttl property:

- If the property is null or not present in a document, the document won't expire.

- If the property is present and set to -1 in a document, it won't expire.

- If the ttl property is present in a document, then that document will expire according to the ttl period. The duration is expressed in number of seconds after the last modified time.

## Consuming the change feed

There are a few ways to read the change feed. These usually involve push and pull models. Push models are the easiest way to get data from the change feed.

> If you need to control change feed consumption at a customized pace, you'll need to use the pull model. If that is the case, refer to this article on the [Change feed pull model in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/change-feed-pull-model).

There are two ways to receive push notifications:

- Azure Functions

- The Change Feed Processor

Our code sample uses the change feed processor, with reactive programming.

### Azure Functions

Azure Functions are the simplest way to work with the change feed. You need to use the push model in a serverless, event-based flow. Functions connect to the change feed through an Azure Functions trigger for Cosmos DB. Learn more in the article, [Create a function triggered by Azure Cosmos DB](https://docs.microsoft.com/azure/azure-functions/functions-create-cosmos-db-triggered-function).

### Use the Change Feed Processor

To use the Change Feed Processor in our application, we create `ChangeFeedService`. This starts the change feed processor. As the service picks up changes from the change feed, it passes them to the `handleChanges` function.

![Diagram showing how the Change Feed Processor works.](media/change-feed-concepts/change-feed-processor.png)

We have a Change Feed controller so that we can access output from the change feed via the API. The Change Feed controller gets changes from the Change Feed service. This activity happens in the
`ChangeFeedService.java` file.

We have a `ChangeFeedProcessorBuilder` bean in `CosmosConfiguration.java`. This is where we set the database, the monitored container (feedContainer), the lease container (leaseContainer), and any options. The builder bean is set to emit the items through the for loop passed to `handleChanges()`.

The Change Feed service handles the logic for processing change documents. In `init()`, we build a change feed processor using the builder. We then pass it an updated change handler. We also set the `changedProducts` public property to subscribe to `productSink` output. The logic for filtering our documents is handled in the `handleChanges()` function.

## Run the code and check it out

The purpose of our code is to specifically monitor product changes. Still, you can see this as a start for handling change feed changes using your own logic. 

There's a link on the home page of the sample application to the Change Feed service. Open that link in another tab, and then run Update Product query in the Postman collection. Once it has run, you should see an entry on the Change Feed page in your browser. Try running the Update Product Price query in the Postman collection. This does a partial document update. Notice that partial updates do appear in the change feed.

## Learn more

- [Change feed design patterns in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/change-feed-design-patterns)

- [Azure Cosmos DB Workshop - Azure Cosmos DB Change Feed](https://github.com/AzureCosmosDB/labs/blob/master/java/labs/08-change_feed_with_azure_functions.md)

[Next &#124; Azure Cosmos DB Trigger for Azure Functions in Java](change-feed-with-cosmos-db-trigger-function.md){: .btn .btn-primary .btn-lg }
