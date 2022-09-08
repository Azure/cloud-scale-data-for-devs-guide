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

Add to the Spring Data setup by creating a `DocumentDbConfiguration` class specifically for use with the bulk executor.

## Add application properties

Add bulk executer details to the *application-default.properties* file:

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

Update your code as follows:

1. Update your models to inherit from a `BaseModel` class.

1. Add a controller to call the bulk operations, with the following endpoints:

   - `/api/bulk POST`: Bulk import
   - `/api/bulk PUT`: Bulk update
   - `/api/bulk DELETE`: Bulk delete

In the Postman collection, we've included a section  for you to try each of these bulk operations against the sample code.

## Learn more

[Azure Cosmos DB bulk executor library overview](https://docs.microsoft.com/azure/cosmos-db/bulk-executor-overview)

[Next &#124; Schema design considerations](schema-considerations.md){: .btn .btn-primary .btn-lg }
