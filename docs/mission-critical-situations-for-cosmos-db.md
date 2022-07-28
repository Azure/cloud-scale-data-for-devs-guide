---
title: Azure Cosmos DB for mission critical situations
description: Learn about Azure Cosmos DB for mission critical situations.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 15
---

# Azure Cosmos DB for mission critical situations

Azure Cosmos DB is built for mission critical situations. In addition to being globally replicated, there are other features that assist in these situations:

- Continuous backup and restore

- Secure access via firewalls, virtual networks, and private connections

- Secure data via customer managed keys and client-side encryption

## Continuous backup and restore

Azure Cosmos DB performs data backups without impacting the performance and availability of your databases. It operates without consuming additional RUs. These backups are taken in every region where the account exists. These backups can be used for a point-in-time restoration within the retention period.

Retention periods are the lowest of these two situations:

- Up to resource creation time

- 30 days in the past

The continuous backups are stored in locally redundant storage blobs by default. However, if the regions are set up as zone redundant, then the continuous backups are stored in zone redundant storage blobs.

These settings are configurable in the Azure portal, under the **Backup & Restore** setting for your Azure Cosmos DB account.

![](/media/image.png)

> [!NOTE]
> Restoring always restores into a new account.

Learn more about how to work with continuous backup and restoring in this article on [Continuous backup with point in time restore feature in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/continuous-backup-restore-introduction).

## Secure access to your Azure Cosmos DB account

Your Azure Cosmos DB is publicly accessible by default if the request comes in with a valid authorization token. However, you can configure your Azure Cosmos DB account to be more secure.

![](/media/image2.png)

Looking at the resource in the Azure portal, there are a couple sections under Settings to know about for securing your Azure Cosmos DB account:

- Firewall and virtual networks

- Private Endpoint Connections

You can use the firewall portion to allow connections from specific IP addresses and CIDR ranges. Note that the IP addressing is in IPv4 notation. Learn more about how to [Configure IP firewall in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-firewall).

You can secure Azure Cosmos DB by putting it on a virtual network for access. Virtual network traffic accesses Azure Cosmos DB via a service endpoint. Learn more about how to [Configure access to Azure Cosmos DB from virtual networks](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-vnet-service-endpoint).

Virtual networks accessing Azure Cosmos DB work in conjunction with Azure Private Link, by exposing a private endpoint to a subnet within your virtual network. Learn more about how to [Configure Azure Private Link for an Azure Cosmos DB account](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-private-endpoints).

## Secure data via additional encryption

There are multiple ways to manage data access in Azure Cosmos DB. With multiple keys, read-only keys, and role-based access control, you can manage the access of the data. The data itself is also secured in other ways. By default, the data stored in Azure Cosmos DB is encrypted with service-managed keys. This means there's nothing you need to do for this first layer of encryption.

However, if service-managed keys aren't enough, you can use customer-managed keys as a second layer of data encryption. Learn more about [Configure customer-managed keys for your Azure Cosmos DB account](https://docs.microsoft.com/azure/cosmos-db/how-to-setup-cmk).

Another feature for data encryption in Azure Cosmos DB is known as Always Encrypted. This feature supports client-side encryption, which should be used when transmitting sensitive data such as credit card numbers or personal identification numbers such as national identification numbers. Always Encrypted allows you to set access controls at the property level for stored objects. Learn more on how to [Use client-side encryption with Always Encrypted for Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/how-to-always-encrypted?tabs=java).

[Next &#124; Migrate to Azure Spring Cloud](migrate-to-azure-spring-cloud.md){: .btn .btn-primary .btn-lg }
