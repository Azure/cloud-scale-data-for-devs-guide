---
title: Schema design considerations
description: Learn about schema design considerations.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 14
---

# Schema design considerations

Azure Cosmos DB stores data as schema-less JSON objects. Any schema
intended would have to be managed or enforced by outside applications.

Now that said, there are decisions in your database design that need to
be made before you create your Azure Cosmos DB database. These are the
things that you need to think about when it comes to your JSON objects:

-   Data Modeling

-   Partition Key

## Data Modeling

Before storing the data, make sure you have objects stored in a way that
makes sense.

In a nonrelational database, data is stored denormalized, optimized for
querying and writes.

Consider the Contoso Pet Supplies models in a relational entity
relationship diagram:

![](/media/image2.png){width="5.0in" height="2.5625in"}

Notice that tables are broken out by entities, and bridge tables are
present to establish relationships between entities.

When breaking this into documents for Azure Cosmos DB, we need to
consider some things:

-   How is this data being accessed?

-   Are columns being called together frequently in queries?

-   Are columns being called together frequently in queries via JOINs?
    Keep in mind that the relational concept of JOINs does not translate
    to the JOIN in Azure Cosmos DB.

-   Are columns being updated together?

Sometimes, entities translate 1-to-1 in relational to nonrelational.
Sometimes, you'll find that it makes more sense to embed entities --
such as embedding the manufacturer within a product, especially if we
are not using the manufacturer by itself and only use it when we're
working with products. In cases where you need to use JOINs in
relational queries, you may need to denormalize the data, such as
passing along a customer email for wishlist notifications as updates to
the wishlist need to go to the customer.

This article gives guidance on [Modeling data in Azure Cosmos
DB](https://docs.microsoft.com/en-us/azure/cosmos-db/sql/modeling-data).

## Partition Key Decisions

The partition key is used to store objects of the same value of the
partition key property together. For example, if we wanted to categorize
our products, we could store them in a separate container and partition
them by category. This would store products of the same category
together in a logical partition. When determining what to use for the
partition key, keep this guidance in mind:

-   For read-heavy containers, use a field that is optimized for
    frequently run queries.

```{=html}
<!-- -->
```
-   For write-heavy containers, use a field with diverse data that
    balances the load.

When looking at the models in the Contoso Pet Supplies project, we
created a property called documentType to use as our partition key. We
got to that decision based on a couple exercises:

-   We first looked at our data to see what details were being stored in
    our objects. If they all had to live in one container, what would
    the queries look like? There would be a lot of queries filtered by
    the object's type.

-   We then looked at what our data looked like for being loaded in the
    database. The object\'s type would be diverse enough to balance the
    load across partitions where each value will have a similar load and
    no single value would take on a heavy load.

By setting \"/documentType\" as our partition key, we have a key that
could work well for our environment.

![](/media/image.png){width="2.6268657042869643in"
height="1.8333333333333333in"}

## Additional References

-   [Azure Cosmos DB Essentials Series, Season 2 -- Partition Strategy
    (YouTube)](https://www.youtube.com/watch?v=QLgK8yhKd5U)

-   [Azure Cosmos DB Essentials Series, Season 2 - Schema Design
    Strategy (YouTube)](https://www.youtube.com/watch?v=bKDaL-GRSAM)

[Next &#124; Azure Cosmos DB for mission critical situations](mission-critical-situations-for-cosmos-db.md){: .btn .btn-primary .btn-lg }
