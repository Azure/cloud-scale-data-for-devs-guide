---
title: Add the bulk executor feature
description: Add the bulk executor to your code sample, which is an Azure Cosmos DB feature that allows bulk inserts, updates, and deletes.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 13
---

# Bulk Execution

 Bulk Execution is an Azure Cosmos DB feature that allows bulk inserts, updates, and deletes. When inserts and updates occur, they appear in the change feed. Bulk Execution is part of the Java V4 SDK for Azure Cosmos DB.


## Add application properties

Add details of the collection where bulk execution will be used to the *application-default.properties* file:

```properties
# values for bulk executor
azure.cosmos.collection=pet-supplies
azure.cosmos.partitionKey=documentType
azure.cosmos.collectionThroughput = 100000;
```

## Update code

All bulk operations occur in `BulkExecutorService`.

A controller calls the bulk operations, with the following endpoints:

- `/api/bulk POST`: Bulk import
- `/api/bulk PUT`: Bulk update
- `/api/bulk DELETE`: Bulk delete

In the Postman collection, we've included a section for you to try each of these bulk operations against the sample code.

## Learn more

[Azure Cosmos DB bulk executor library overview](https://docs.microsoft.com/azure/cosmos-db/bulk-executor-overview)

[Next &#124; Schema design considerations](schema-considerations.md){: .btn .btn-primary .btn-lg }
