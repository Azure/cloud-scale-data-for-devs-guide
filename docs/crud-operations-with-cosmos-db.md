---
title: CRUD Operations with Azure Cosmos DB
description: Learn about CRUD Operations at an introductory level in Azure Cosmos database and how to implement them.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 7
---

# CRUD Operations with Azure Cosmos DB

To create, read, update, and delete records in Azure *Cosmos DB* a small amount of code is required. *Spring Data* for the Cosmos DB Core (SQL) API artifact supports the `CrudRepository` and `ReactiveCrudRepository` interfaces. It also includes `CosmosRepository` and `ReactiveCosmosRepository`.

For this application, we use `ReactiveCosmosRepository`.

The code is structured using the following:

- Controllers serving as API endpoints

- Repositories as interfaces

- Services implementing repositories

## Repositories overview

We have repositories for:

- Customers

- Manufacturers

- Products

- Wish lists

Each of the reactive repositories follow this pattern:

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

Repositories in Spring Data for Cosmos DB Core (SQL) API artifact support more operations than we use in the sample code:

- Delete entity

- Delete all

## Implementing CRUD operations

Most Spring Data interface functions use default implementations. However, our wish list object is complex, so we add a few more functions:

- `findByCustomerId`

- `findByProductId`

The `findByCustomerId` is annotated with a query to filter on the customer ID. The `findByProductId` will search for all wish lists that have a particular product ID in their array of product IDs. These are annotated with the `@Query` annotation to use a custom Azure Cosmos DB SQL query. As a wish list is tied to one customer, the query for `findByCustomerId` makes use of the = operator. Since a wishlist can have many products, the query for `findByProductId` makes use of the `ARRAY_CONTAINS()` function to check the array of product IDs tied to a wishlist.

If you need to customize queries or add additional queries, make use of the `@Query` annotation and use Azure Cosmos DB's SQL syntax for the query.

## Partial document updates

To make a small change to an object, you don't necessarily have to do a full document update. Cosmos DB supports partial document updates, by using PATCH updates. We implemented an endpoint in our Products API to update product prices as partial updates.

There are things to note about partial document updates:

- We need to suppress attempts to bind queries to the method. So we assign a Query annotation over the method in the repository.

- Partial document updates are in Cosmos DB Java SDK v4, not Spring Data for the Cosmos DB Core (SQL) API artifact.

## Learn more

- [Azure Cosmos DB PDF query cheat sheets](https://docs.microsoft.com/azure/cosmos-db/sql/query-cheat-sheet)

- [Getting started with SQL queries](https://docs.microsoft.com/azure/cosmos-db/sql/sql-query-getting-started)

- [Working with arrays and objects in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/sql-query-object-array)

- [Partial document update in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/partial-document-update)

- [Frequently asked questions on partial document update in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/partial-document-update-faq)

[Next &#124; Azure Cosmos DB Change Feed](change-feed-concepts.md){: .btn .btn-primary .btn-lg }
