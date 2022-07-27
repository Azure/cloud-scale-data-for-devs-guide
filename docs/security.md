---
title: Security in Azure Cosmos DB
description: You can implement security in Azure Cosmos DB using role-based access control or keys.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 3
---

# Security in Azure Cosmos DB

There are various concepts to keep in mind with security in Azure Cosmos DB. Let's look at some of the options.

## Accessing data with keys

When you create an Azure Cosmos DB account, it contains a set of keys for Read-Write access and a set of Read Only keys. If you're developing with keys, you need to make sure to secure those credentials so that they don't accidentally make it into source control. Throughout this guide, we advocate for these approaches rather than storing key values in ``application.properties``. If you must work with keys:

* Store the keys in environment variables. Environment variables make it easy to store the values in a place without checking them in to source control. When it comes to deploying the solution into Azure, Azure App Service, Azure Functions, and Azure Spring Cloud support environment variables as well.

* Another option for storing secure values is in Azure Key Vault. For more information on working with Java and Azure Key Vault, check out our [Hello World code sample with Java and Azure Key Vault](https://github.com/solliancenet/cosmos-db-java-quickstart/blob/main/03_SecurityConcepts/03_Hello_World_with_Java_Key_Vault.md).

## Role-based access control

One of the ways you can secure access to your Azure Cosmos DB account is with role-based access control (RBAC) with Azure Active Directory. With Azure RBAC, you can assign users, service principals, managed identities, and groups as members within a role. RBAC for Azure resources is managed in the section labeled **Access Control (IAM)** when working in the Azure portal. IAM is an acronym for Identity and Access Management.

Azure RBAC uses Azure Active Directory for the authentication portion. It uses role assignments for authorization determination.

By assigning roles at the resource level, you have fine-grained control over your resource access.

### Azure Cosmos DB-specific RBAC roles

There are two roles specific for Azure Cosmos DB. They're **Cosmos DB Built-in Data Reader** and **Cosmos DB Built-in Data Contributor**. The Cosmos DB Built-in Data Reader role includes actions for reading metadata on database accounts, reading items in a container, executing queries, and reading the change feed. The Cosmos DB Built-in Data Contributor role includes actions for reading metadata on database accounts, reading and writing with containers, and reading and writing with items.

There are no built-in Azure Cosmos DB-specific roles regarding:

* Creating, replacing, and deleting databases and containers
* Replacing container throughput
* Creating, replacing, deleting, and reading stored procedures, triggers, and user defined functions

### Cosmos DB-specific Guidance for Custom RBAC Roles

The Azure Cosmos DB actions support wildcard notation at the containers and items levels when creating custom role definitions for RBAC. Their actions can be referred to as:

* ``Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/*``
* ``Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/items/*``

There are three scopes for role assignments:

* account level (``/``)
* database level (``/dbs/<database-name>``)
* container level (``/dbs/<database-name>/colls/<container-name>``)

## Using solely RBAC

You can configure Azure Cosmos DB to use only RBAC and not allow the use of the primary and secondary keys. Using solely RBAC can be done as part of creating or updating your Azure Cosmos DB account by using Azure Resource Manager templates, setting the ``disableLocalAuth`` property to true.

## Managed identities

Azure resources can have a managed identity for authentication purposes between Azure resources. Services that support managed identities for Azure resources include Azure App Service, Azure Spring Cloud, Azure Logic Apps, and Azure Functions. For a comprehensive list of services that support managed identities, see [what Azure services support managed identity](../../../active-directory/managed-identities-azure-resources/overview.md#what-azure-services-support-the-feature). The mentioned services are offerings we use throughout this guide. Rather than having to rely on keys, we can use these managed identities to grant roles via RBAC. When you create a managed identity, it lives with the resource. When the resource is deleted, the managed identity is also deleted.

Suppose we have a microservice running in Azure Spring Cloud. The microservice wants to write to Azure Cosmos DB and needs the key. You can grant the managed identity access to read the keys via the Cosmos DB Account Reader role. Managed identities are seen on the screen for RBAC.

## Storing credentials in Azure Key Vault

When working in applications, you may be tempted to store key information in application.properties. However, if this file gets checked into source control, you now have given access to individuals who have access to your source code. Rather than running the risk of exposing credentials, we recommend you store them securely in Azure Key Vault. When storing values in Azure Key Vault, you can use either their vault access policies or secure things via RBAC. You can grant permissions to users, service principals, managed identities, and groups.

## Networking security

Azure Cosmos DB can be secured through networking as well. Options include:

* [IP firewall](../../how-to-configure-firewall.md)
* [Virtual network support](../../how-to-configure-vnet-service-endpoint.md)
* [Private endpoints using Azure Private Link](../../how-to-configure-private-endpoints.md)

## Encrypting data

Data stored in Azure Cosmos DB is automatically encrypted with service-managed keys. Service-managed keys are keys maintained by Microsoft. If you want to add another layer of encryption, you can bring your own customer-managed keys. These keys must be stored in Azure Key Vault. The customer-managed keys must be configured during account creation. There are no extra charges for enabling the feature for your own customer-managed key; however, there may be an increase in extra processing for your encryption and decryption. This cost will be seen in the RU (Request Units) cost. For more information about setting up customer-managed keys, see [configure customer-managed keys](../../how-to-setup-cmk.md).

Azure Cosmos DB takes backups of your data. If you need to restore this data with a customer-managed key, the encryption key must be available in Azure Key Vault.

[Next &#124; Getting started with Java and Azure Key Vault](get-started-with-java-and-key-vault.md){: .btn .btn-primary .btn-lg }
