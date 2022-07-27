---
title: Bulk Executor
description: Learn about The Bulk Executor, which is a feature in Azure Cosmos DB.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
---

# Bulk Executor

The Bulk Executor is a feature in Azure Cosmos DB that allows bulk
inserts, updates, and deletes. Inserts and updates appear in the change
feed.

Let's see how we incorporate it into our current code sample.

## Add a dependency

Add a new dependency in the **pom.xml** file:

```xml
<dependency>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>documentdb-bulkexecutor</artifactId>
   <version>2.12.5</version>
</dependency>
```

As this gets away from the Spring Data setup, we created a
DocumentDbConfiguration class specifically for use with the bulk executor.

## Add application properties

We added details to **application-default.properties**:

```json
# values for bulk executor
azure.cosmos.collection=pet-supplies
azure.cosmos.partitionKey=documentType
azure.cosmos.collectionThroughput = 100000;
```

## Update code

Most of the bulk operations are happening in the BulkExecutorService.
Each operation is in their own class file:

- BulkUpdateOperation

- BulkDeleteItem

- BulkUpdateItem

- BulkExecutorService

We updated our models to inherit from a BaseModel class.

We added a controller for calling the bulk operations, with the
following endpoints:

- /api/bulk POST -- bulk import

- /api/bulk PUT -- bulk update

- /api/bulk DELETE --bulk delete

We've included a section in the Postman collection for you to try each
of these bulk operations against the sample code.

## Learn more

- [Azure Cosmos DB bulk executor library overview](https://docs.microsoft.com/azure/cosmos-db/bulk-executor-overview)
