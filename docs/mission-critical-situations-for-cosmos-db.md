---
title: Azure Cosmos DB for mission critical situations
description: Learn about Azure Cosmos DB for mission critical situations, as well as security features, and backup and restore.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 15
---

# Azure Cosmos DB for mission critical situations

*Azure Cosmos DB* is built for mission critical situations. It's a replicable, globally distributed database system and it has other additional features that help in mission critical situations:

- Continuous backup and restore

- Secure access via firewalls, virtual networks, and private connections

- Secure data via customer managed keys and client-side encryption

## Continuous backup and restore

Cosmos DB performs data backups without impacting the performance and availability of your databases. It operates without consuming additional RUs. Also, backups are taken in every region where the account exists. These backups can be used for point-in-time restorations while you're inside of retention periods.

Retention periods are the lowest of these two situations:

- Up to resource creation time

- 30 days in the past

Continuous backups are stored in locally redundant storage blobs by default. However, if regions are set up to be zone redundant, then continuous backups are stored in zone redundant storage blobs.

These settings are configurable in Azure portal, under the **Backup & Restore** setting for your Cosmos DB account.

![Screenshot that shows an Azure Cosmos DB account with Backup and Restore selected.](./media/mission-critical-situations-for-cosmos-db/backup-and-restore-settings.png)

> [!NOTE]
> When you restore data it is always restored to a new account.

Learn more about how to work with continuous backup and restore in this article on [Continuous backup with point in time restore feature in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/continuous-backup-restore-introduction).

## Secure access to your Cosmos DB account

Your Cosmos DB is publicly accessible by default and can be accessed whenever requests have valid authorization tokens. You can configure your Cosmos DB account however to be more secure.

![Screenshot that shows a Cosmos DB account with Firewall and virtual networks selected.](./media/mission-critical-situations-for-cosmos-db/firewall-and-virtual-network-settings.png)

You can view the resource in Azure portal. There are a couple sections under **Settings** that secure your Cosmos DB account:

- Firewall and virtual networks

- Private Endpoint Connections

You can use firewall settings to allow connections from specific IP addresses and CIDR ranges. Note that IP addresses are in IPv4 notation. Learn more about [how to Configure IP firewall in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-firewall).

You can secure Cosmos DB by placing it on a virtual network for access. Virtual network traffic accesses Cosmos DB via a service endpoint. Learn more about [how to Configure access to Azure Cosmos DB from virtual networks](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-vnet-service-endpoint).

Virtual networks that access Cosmos DB work in conjunction with Azure Private Link. It exposes a private endpoint to a subnet within your virtual network. Learn more about [how to Configure Azure Private Link for an Azure Cosmos DB account](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-private-endpoints).

## Secure data via additional encryption

There are multiple ways to manage data access in Cosmos DB. You can use multiple keys, read-only keys, and role-based access control. Data is also secured in other ways. By default, data stored in Cosmos DB is encrypted with service-managed keys. This means you don't need to do anything to configure the first layer of encryption.

If service-managed keys aren't enough, you can use customer-managed keys as a second layer of data encryption. Learn more about [Configure customer-managed keys for your Cosmos DB account](https://docs.microsoft.com/azure/cosmos-db/how-to-setup-cmk).

Another Cosmos DB data encryption feature is *Always Encrypted*. This feature supports client-side encryption. This should be used when you transmit sensitive data, such as credit card numbers or personal identification numbers such as national identification numbers. Always Encrypted allows you to set access controls at the property level for stored objects. Learn more about [how to Use client-side encryption with Always Encrypted for Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/how-to-always-encrypted?tabs=java).

[Next &#124; Migrate to Azure Spring Cloud](migrate-to-azure-spring-cloud.md){: .btn .btn-primary .btn-lg }
