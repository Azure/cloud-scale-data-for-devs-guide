---
title: Security in Azure Cosmos DB
description: Learn how you can implement security in Azure Cosmos DB using role-based access control or keys.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 3
---

# Security in Azure Cosmos DB

Security is a critical concern for any system. Azure Cosmos DB offers various options for keeping your data safe and for controlling who can access it.

## Access data with keys

Your Azure Cosmos DB account contains a set of keys for *read-write* access and a set of *read only* keys. If you're developing with keys, you need to secure those credentials so that they don't accidentally make it into source control. This guide outlines options for controlling keys rather than storing key values in `application.properties`.

If you must work with keys:

* Store the keys in environment variables. Environment variables make it easy to store the values in a place without checking them in to source control. Azure App Service, Azure Functions, and Azure Spring Apps support environment variables as well.

* Another option for storing secure values is in Azure Key Vault. For more information on working with Java and Azure Key Vault, check out our [Hello World code sample with Java and Azure Key Vault](get-started-with-java-and-key-vault.md).

## Role-based access control

One of the ways you can secure access to your Azure Cosmos DB account is with role-based access control (RBAC). With Azure RBAC, you can assign users, service principals, managed identities, and groups as members within a role. Azure RBAC can be managed in the section labeled **Access Control (IAM)** when working in the Azure portal. IAM is an acronym for Identity and Access Management.

Azure RBAC uses [Azure Active Directory (Azure AD)](https://azure.microsoft.com/services/active-directory/) for the authentication portion. It uses role assignments for authorization determination. By assigning roles at the resource level, you have fine-grained control over your resource access.

### Azure Cosmos DB-specific RBAC roles

Azure Cosmos DB uses two built-in roles. The **Cosmos DB Built-in Data Reader** role includes actions for reading metadata on database accounts, reading items in a container, executing queries, and reading the change feed. The **Cosmos DB Built-in Data Contributor** role includes actions for reading metadata on database accounts, reading and writing with containers, and reading and writing with items.

There are no built-in Azure Cosmos DB-specific roles for:

* Creating, replacing, and deleting databases and containers.
* Replacing container throughput.
* Creating, replacing, deleting, and reading stored procedures, triggers, and user defined functions.

### Custom RBAC roles

Azure Cosmos DB actions support wildcard notation at the container and item levels when creating custom role definitions for RBAC. Their actions can be referred to as:

* `Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/*`
* `Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/items/*`

There are three scopes for role assignments:

* Account level (`/`)
* Database level (`/dbs/<database-name>`)
* Container level (`/dbs/<database-name>/colls/<container-name>`)

## Using RBAC alone

You can configure Azure Cosmos DB to use only RBAC and not allow the use of primary and secondary keys. Using RBAC alone can be done as part of creating or updating your Azure Cosmos DB account by using Azure Resource Manager templates, then setting the `disableLocalAuth` property to *true*.

## Managed identities

You can use a managed identity for authentication purposes between Azure resources. Services that support managed identities for Azure resources include Azure App Service, Azure Spring Apps, Azure Logic Apps, and Azure Functions. For a comprehensive list of services that support managed identities, see [what Azure services support managed identity](https://docs.microsoft.com/azure/active-directory/managed-identities-azure-resources/overview#what-azure-services-support-the-feature). These services are used throughout this guide. Rather than having to rely on keys, you can use managed identities to grant roles via RBAC. When you create a managed identity, it lives with the resource. When the resource is deleted, the managed identity is also deleted.

Suppose a microservice runs in Azure Spring Apps. The microservice wants to write to Azure Cosmos DB and needs the key. You can grant the managed identity access to read the keys via the Cosmos DB Account Reader role. Managed identities are seen on the screen for RBAC.

## Azure Key Vault

When working in applications, you may be tempted to store key information in `application.properties`. However, if this file gets checked into source control, anyone who has access to your source code can access your key. Rather than running the risk of exposing credentials, you should store them securely in Azure Key Vault. When storing values in Azure Key Vault, you can use either vault access policies or secure things via RBAC. You can grant permissions to users, service principals, managed identities, and groups.

## Networking security

Another approach to securing Azure Cosmos DB is through networking. Options include:

* [IP firewall](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-firewall)
* [Virtual network support](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-vnet-service-endpoint)
* [Private endpoints using Azure Private Link](https://docs.microsoft.com/azure/cosmos-db/how-to-configure-private-endpoints)

## Encrypting data

Data stored in Azure Cosmos DB is automatically encrypted with service-managed keys. Service-managed keys are keys maintained by Microsoft. If you want to add another layer of encryption, you can bring your own customer-managed keys. These keys must be stored in Azure Key Vault. Customer-managed keys must be configured during account creation. There are no extra charges for enabling the feature for your own customer-managed key; however, there may be an increase in processor usage for encryption and decryption. This cost will be seen in the RU (Request Units) cost. 

For more information about setting up customer-managed keys, see [configure customer-managed keys](https://docs.microsoft.com/azure/cosmos-db/how-to-setup-cmk).

Azure Cosmos DB backs up your data. If you need to restore this data with a customer-managed key, the encryption key must be available in Azure Key Vault.

## Next steps

[Get started with Java and Azure Key Vault](get-started-with-java-and-key-vault.md)
