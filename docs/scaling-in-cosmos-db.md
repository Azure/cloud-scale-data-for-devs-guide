---
title: Scaling in Azure Cosmos DB
description: Learn about scaling in Azure Cosmos DB and the manual and Autoscale options that can be set depending on your workload.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 12
---

# Scaling in Azure Cosmos DB

Azure Cosmos DB works with scaling throughput in two ways: provisioned throughput, which is used in the demo, and serverless.

At the time that you create your database, you must decide whether to use provisioned throughput or serverless scaling.

![Screenshot that shows the New Database page with Provision throughput selected.](media/scaling-in-cosmos-db/provision-throughput-selected.png)

Serverless scalability works well with intermittent workloads, and is coupled with Azure Functions and other [Azure serverless](https://azure.microsoft.com/solutions/serverless/) offerings. With serverless scalability, you pay on a per-hour basis rather than on RU/s. Serverless accounts aren't geo-redundant, as they're limited to one region.

With provisioned throughput, scale settings are configured at the container level. There are two options for scaling container throughput:

- Manual
- Autoscale

The Manual setting for scaling your containers gives you control to set the throughput of the container. This setting works best when for a consistent, steady workload. When you configure a container for manual throughput, you need to set the desired throughput. In our example, we set scaling to 400 RU/s and it consistently stays at 400 RU/s. If the workload gets heavier, we need to adjust the setting manually.

![Screenshot that shows the manual scale settings.](media/scaling-in-cosmos-db/manual-scale-settings.png)

The Autoscale setting works best in cases when traffic ebbs and flows. If you choose Autoscale for your container, you need to specify a maximum number of RU/s for the resource. Your Azure Cosmos DB container can scale from 10% of maximum RU/s to the maximum RU/s.

## Learn more

- [How to choose between provisioned throughput and serverless on Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/throughput-serverless)

- [How to choose between manual and autoscale on Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/how-to-choose-offer)

[Next &#124; Bulk executor](bulk-executor.md){: .btn .btn-primary .btn-lg }
