---
title: Scaling in Azure Cosmos DB
description: Learn about scaling in Azure Cosmos DB.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 12
---

# Scaling in Azure Cosmos DB

There are a couple ways that Azure Cosmos DB works with scaling throughput. There is provisioned throughput, which we use in our demo, and there is serverless. Provisioned throughput versus serverless is determined when you create your database.

![Screenshot showing the New Database page with Provision throughput selected.](media/scaling-in-cosmos-db/provision-throughput-selected.png)

Serverless scalability works well when working with intermittent workloads and is coupled with Azure Functions and other [Azure serverless](https://azure.microsoft.com/solutions/serverless/) offerings. With serverless, you pay on a per-hour basis rather than on RU/s. Serverless accounts are not geo-redundant, as they are limited to one region.

With provisioned throughput, scale settings are configured at the container level. There are two options for scaling container throughput:

- Manual

- Autoscale

The Manual setting for scaling your containers gives you the control to set your container's throughput if you have a consistent, steady workload. When configuring a container for manual throughput, you set the desired throughput. In our example, our scaling is set to 400 RU/s. It will stay at 400 RU/s. If we have a heavier workload, we need to come in here to adjust the setting if we leave it at Manual throughput.

![Screenshot showing the manual scale settings.](media/scaling-in-cosmos-db/manual-scale-settings.png)

Autoscale works better in cases where your traffic ebbs and flows. If you choose to configure your container to use the Autoscale setting, you need to specify the maximum number of RU/s required by the resource. Your Azure Cosmos DB container will scale from 10% of max RU/s to the max RU/s.

## Learn more

- [How to choose between provisioned throughput and serverless on Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/throughput-serverless)

- [How to choose between manual and autoscale on Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/how-to-choose-offer)

[Next &#124; Bulk executor](bulk-executor.md){: .btn .btn-primary .btn-lg }
