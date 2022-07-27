---
title: CRUD Operations with Azure Cosmos DB
description: Learn about CRUD Operations with Azure Cosmos DB.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
---

# CRUD Operations with Azure Cosmos DB

Creating, reading, updating, and deleting records in Azure Cosmos DB require little code. The Spring Data for Azure Cosmos DB SQL API artifact supports Spring Data's CrudRepository and ReactiveCrudRepository interfaces. It also includes CosmosRepository and ReactiveCosmosRepository.

For this application, we're using the ReactiveCosmosRepository.

The code is structured with:

- Controllers serving as API endpoints

- Repositories as interfaces

- Services implementing repositories

## Repositories overview

We have repositories for:

- Customers

- Manufacturers

- Products

- Wish lists

Each of the reactive repositories follows this pattern:

```java
@Repository
public interface ThingRepository extends
ReactiveCosmosRepository<Thing, String> {
   Mono<Void> deleteById(String thingId, PartitionKey key);
   Flux<Product> findAll(PartitionKey key);
   Mono<Product> findById(String thingId, PartitionKey key);
   Mono<Product> save(Thing thing);
}
```

The repositories in the Spring Data for Azure Cosmos DB SQL API artifact also support the following operations that we didn't implement in the sample code:

- Delete entity

- Delete all

## Implementing CRUD operations

Most of the interface functions are using default implementations. However, our wish list object is complex, so we've added a few more functions to help us out:

- findByCustomerId

- findByProductId

The findByCustomerId is annotated with a query to filter on the customer ID. The findByProductId will search for all wish lists that have a particular product ID in their array of product IDs. These are annotated with the \@Query annotation to use a custom Azure Cosmos DB SQL query. As a wish list is tied to one customer, the query for findByCustomerId makes use of the = operator. Since a wishlist can have many products, the query for findByProductId makes use of the ARRAY_CONTAINS() function to check the array of product IDs tied to a wishlist.

If you need to customize queries or add additional queries, make use of the \@Query annotation and use Azure Cosmos DB's SQL syntax for the query.

## Partial document updates

If you're making a small change to an object, you don't necessarily have to do a full document update. Azure Cosmos DB supports a partial document update using a PATCH update. We've implemented an endpoint in our Products API to update a product's price as a partial update.

Some things to note for us to make this happen:

- We need to suppress the issue of trying to bind a query to the method, so we've assigned a Query annotation over the method in the repository.

- The partial document updates are in the Azure Cosmos DB Java SDK v4, not the Spring Data for Azure Cosmos DB SQL API artifact.

## Learn more

- [Azure Cosmos DB PDF query cheat sheets](https://docs.microsoft.com/azure/cosmos-db/sql/query-cheat-sheet)

- [Getting started with SQL queries](https://docs.microsoft.com/azure/cosmos-db/sql/sql-query-getting-started)

- [Working with arrays and objects in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/sql-query-object-array)

- [Partial document update in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/partial-document-update)

- [Frequently asked questions on partial document update in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/partial-document-update-faq)
