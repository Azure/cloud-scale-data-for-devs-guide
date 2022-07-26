---
title: What is Azure Cosmos DB?
description: Azure Cosmos DB is Microsoft's fully managed, distributed NoSQL database offering on Azure.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
---

# What is Azure Cosmos DB?

Azure Cosmos DB is Microsoft's fully managed, distributed NoSQL database offering on Azure. It's a globally distributed, massively scalable, multi-model database service. Unlike many other non-relational databases, Azure Cosmos DB offers five consistency levels for data durability. Let's talk about what it has to offer.

## Non-relational database

While Azure SQL DB is Microsoft Azure’s flagship product for relational data, Azure Cosmos DB is its flagship offering for non-relational data. There's support for document data such as product information on an eCommerce site, blog posts on a website, and user profiles. Other non-relational data could include column-family data such as content management, sensor logs, and time series data. Graph data is used for applications such as recommendation engines, fraud detection algorithms, networks, and other relationship trends. Azure Cosmos DB supports these non-relational data types and schema-less JSON.

## Globally distributed

Global distribution is one of the key pillars of Azure Cosmos DB. Azure Cosmos DB is built for supporting globally accessible data as it's designed with low latency at the 99th percentile, elastic scalable throughput, and high availability. Your data in Azure Cosmos DB is automatically synchronized across replicas in multiple regions. As it's a foundational piece of Azure, Cosmos DB is distributed in all regions of Azure in the five key Azure cloud environments:

* Azure public cloud
* Azure Government
* Azure Government for Department of Defense
* Azure China 21Vianet

Data can be configured to be readable and writeable across multiple regions. There are service-level agreements (SLAs) guaranteed for Azure Cosmos DB resources based on different conditions. For more information about the SLAs, see [SLA for Azure Cosmos DB](https://azure.microsoft.com/support/legal/sla/cosmos-db/).

## Massively scalable

Azure Cosmos DB is horizontally scalable - adding capacity as needed to increase the storage and throughput. It can adapt to needs, scaling from 10 s to 100 s of millions of requests per second over multiple regions. Whether it's consistent traffic or bursts of traffic, Azure Cosmos DB can be configured to scale according to your needs. There's manual provisioned throughput  for when you have predictable request traffic. It can autoscale to set a maximum throughput, and Azure Cosmos DB between the max throughput and 10% of that amount. With the autoscaling, you can trust that Azure Cosmos DB will scale out. when that burst of traffic in sales and views hits, Azure Cosmos DB will scale in when it needs to.

While the system is massively scalable, there are quotas around resources such as the maximum storage across all items per partition. For more information on service quotas, see [Azure Cosmos DB service quotas](../../concepts-limits.md).

## Maintenance

Azure Cosmos DB's infrastructure is maintained by Microsoft. Other than configuring and tuning your application, you aren't responsible for the hardware, operating system, or other infrastructure maintenance.

## Throughput and costs

The cost of database operations in Azure Cosmos DB is expressed in a rate-based currency called Request Units (RUs). RUs are calculated based on usage of CPU, IOPS, and memory resources. A point read - getting an item by its partition key and ID - for a 1-KB item is one RU. Throughput can be provisioned at the database level, and the container level.

Traditional Azure Cosmos DB pricing for the provisioned throughput capacity mode is broken into two components - provisioned throughput and consumed storage. The serverless capacity mode is handled on usage. For more information on choosing the capacity mode for your Azure Cosmos DB account, see [how to choose between provisioned throughput and serverless](../../throughput-serverless.md).

For more information on managing costs, see [plan and manage costs for Azure Cosmos DB](../../plan-manage-costs.md).

## Multi-model with multiple APIs

Azure Cosmos DB is considered multi-model because your data can be stored in diverse ways. Azure Cosmos DB has APIs that can be used with each of these models. The models and their supported APIs in Azure Cosmos DB include:

* Key-value data using the Azure Cosmos DB Table API
* Column-family data using the Azure Cosmos DB API for Cassandra
* Document data using the Azure Cosmos DB API for MongoDB
* Graph data using the Azure Cosmos DB API for Gremlin

The Core (SQL) API is the main API for interacting with Azure Cosmos DB, used for querying JSON objects. When features are introduced, it's the first API to see the updates. The SQL API is the API that we're using in this guide.

> While the SQL API may suggest relational data could work in Azure Cosmos DB, know that this database platform is built with NoSQL in mind. The flavor of SQL with this API is designed for querying JSON data and structures.

## Consistency models

Most non-relational databases support two consistency models - eventual and strong. Azure Cosmos DB supports a sliding scale of consistency over five consistency models.

![Diagram of consistency scale in this order: Eventual, Consistent Prefix, Session, Bounded Staleness, and Strong. An arrow illustrates the spectrum of data persistence and replication. The spectrum starts with performance and availability with the starting consistency (Eventual) and ends with Data integrity and consistency with the ending consistency (Strong).](media/intro-cosmos/consistency_scale.svg)

These models from weakest to strongest are:

* Eventual
* Consistent Prefix
* Session
* Bounded Staleness
* Strong

### Eventual consistency

Eventual consistency is the weakest of consistencies offered by Azure Cosmos DB. Order isn't guaranteed for reading with the eventual consistency. Replicas eventually synchronize up in a break from writes. With the eventual consistency, it's possible to read older data rather than the most updated commit.

### Consistent Prefix consistency

With consistent prefix consistency, reads won't see out-of-order writes. Not all writes may come through on a read, but their order is persisted. So, let's say that three writes happen and then a read. The read may pick up the commit from write 1 or the commits from write 1 and write 2. However, it wouldn't pick up the writes out of order - such as write 1 and write 3, missing write 2.

### Session consistency

Session consistency is the mid-grade consistency offered in Azure Cosmos DB. It builds on top of consistent prefix consistency and offers monotonic reads and writes, read-your-writes, and write-follow-reads. Session is the default consistency level. This consistency level guarantees that if the user writes something, they're guaranteed to read their updated item.

### Bounded Staleness consistency

Bounded staleness consistency is the second strongest of consistency models offered in Azure Cosmos DB. It also is built on top of consistent prefix consistency. With bounded staleness consistency, reads will have some lag behind the writes. The lag can be configured based on two different criteria - the number of K versions of an item or a time interval T. The minimum lag configuration is:

| Number of Regions | Minimum Write Operations (K) | Minimum Time Interval (T) |
| ---: | :--- | :--- |
| **Single** | 10 | 5 seconds |
| **Multiple** | 100,000 | 300 seconds (about 5 minutes) |

### Strong consistency

The strongest consistency model offered by Azure Cosmos DB is Strong. This consistency means that the reads are guaranteed to return the most recent committed data. There are no dirty reads - no reading uncommitted data. There are no nonrepeatable nor phantom reads. Regardless of where the write happens, all regions will serve up the same response to a query.

## Data durability

If there's a region-wide outage, the recovery point objective (RPO) - the amount of time you're able to lose - is tied to the consistency level. The RPO is dependent on the number of regions, the replication mode, and the consistency level. Consider the table below to show how they matter.

For more information on consistency levels, see [consistency levels in Azure Cosmos DB](../../consistency-levels.md).

[Next &#124; Security in Azure Cosmos DB](security.md){: .btn .btn-primary .btn-lg }
