---
title: Explore the Bulk executor feature of Azure Cosmos DB
description: Learn about the bulk executor, which is a Azure Cosmos DB feature that allows bulk inserts, updates, and deletes.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 13
---

# Bulk executor

 Bulk executor is an Azure Cosmos DB feature that allows bulk inserts, updates, and deletes. When inserts and updates occur, they appear in the change feed.

Let's see how we can incorporate bulk executor into our code sample.

## Add a dependency

Add a new dependency in the *pom.xml* file:

```xml
<dependency>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>documentdb-bulkexecutor</artifactId>
   <version>2.12.5</version>
</dependency>
```

As this moves away from the Spring Data setup, we create a `DocumentDbConfiguration` class specifically for use with bulk executor.

## Add application properties

We add details to **application-default.properties**:

```properties
# values for bulk executor
azure.cosmos.collection=pet-supplies
azure.cosmos.partitionKey=documentType
azure.cosmos.collectionThroughput = 100000;
```

## Update code

Most of the bulk operations occur in `BulkExecutorService`. Each operation is in its own class file:

- `BulkUpdateOperation`

- `BulkDeleteItem`

- `BulkUpdateItem`

- `BulkExecutorService`

We update our models to inherit from a `BaseModel` class.

We add a controller to call the bulk operations, with the following endpoints:

- `/api/bulk POST`: Bulk import

- `/api/bulk PUT`: Bulk update

- `/api/bulk DELETE`: Bulk delete

We've included a section in the Postman collection for you to try each of these bulk operations against the sample code.

## Learn more

- [Azure Cosmos DB bulk executor library overview](https://docs.microsoft.com/azure/cosmos-db/bulk-executor-overview)

[Next &#124; Schema design considerations](schema-considerations.md){: .btn .btn-primary .btn-lg }
