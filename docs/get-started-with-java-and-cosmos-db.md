---
title: Get started with Java and Azure Cosmos DB for the developers guide
description: Get started using Java and Azure Cosmos DB for the developers guide. 
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
---

# Get started with Java and Azure Cosmos DB for the developers guide

In this guide, we work with a Spring Boot application that uses Azure Cosmos DB for its data store. We'll use Azure Cosmos DB's Core (SQL) API. This application takes an opinionated approach to development using Java 11, Spring Data, and reactive development.

## Prerequisites

- An Azure subscription or free Azure Cosmos DB trial account
  - If you don't have an [Azure subscription](https://docs.microsoft.com/azure/guides/developer/azure-developer-guide#understanding-accounts-subscriptions-and-billing), create an [Azure free account](https://azure.microsoft.com/free/?ref=microsoft.com&utm_source=microsoft.com&utm_medium=docs&utm_campaign=visualstudio) before you begin.

  - We'll create an [Azure Cosmos DB free tier account](https://docs.microsoft.com/azure/cosmos-db/optimize-dev-test#azure-cosmos-db-free-tier), with the first 400 RU/s and 5 GB of storage for free.

- Azure CLI
  - The Azure CLI can be used with Windows, macOS, Linux, Docker, and in Azure Cloud Shell. For installation guidance, refer to this [How to Install the Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) article.

  - We'll use Azure CLI extensions to work with Azure Cosmos DB and Azure Spring Cloud.

- Java 11 or higher

- Java development environment, such as Visual Studio Code, IntelliJ, NetBeans, or Eclipse

- Maven

  - We can use the following Maven plugins to assist with Azure deployments:
    - [Maven Plugin for Azure App Service](https://github.com/Microsoft/azure-maven-plugins/tree/develop/azure-webapp-maven-plugin)
    - [Maven Plugin for Azure Spring Cloud](https://github.com/microsoft/azure-maven-plugins/wiki/Azure-Spring-Cloud)
    - [Maven Plugin for Azure Functions](https://github.com/microsoft/azure-maven-plugins/wiki/Azure-Functions)

## Create a resource group for the pet supplies demo

We need a resource group to hold our Azure resources for this demo. Before creating your resource group, determine a location to use. If you're uncertain of the locations available, run the following command:

```azurecli
az account list-locations -o table
```

You'll need a value from the **Name** column.

Create a resource group with the following command:

```azurecli
az group create --location eastus --resource-group
pet-supplies-demo-rg
```

## Create an Azure Cosmos DB Core (SQL) API pet supplies demo instance

In order to set up your Azure Cosmos DB account to work with the demo, you'll need to follow the steps to:

1. Create an Azure Cosmos DB account.

1. Create an Azure Cosmos DB Core (SQL) API database.

1. Create the pet-supplies container.

These can all be created using the Azure CLI and the Cosmos DB extension.

### Create an Azure Cosmos DB account

To create an Azure Cosmos DB account, run the following command:

```azurelcli
az cosmosdb create -n pet-supplies-demo -g pet-supplies-demo-rg --enable-free-tier true --default-consistency-level Session
```

This command may take a few minutes to create your Azure Cosmos DB account.

### Create an Azure Cosmos DB database with the Core (SQL) API

To create the pet-supplies database with the Core (SQL) API, run the following command:

```azurecli
az cosmosdb sql database create -g pet-supplies-demo-rg --account-name
pet-supplies-demo -n pet-supplies
```

### Create a container for Azure Cosmos DB Core (SQL) API

Before creating a container for your Azure Cosmos DB database, you need to be sure of your data model and establish a partition key. A partition key is an immutable property on a document that is used to logically group documents by their partition key value.

> [!NOTE]
> Once a partition key is set for a container, it can't change. The partition key is a design time decision.

These are some things you need to keep in mind when choosing a partition key:

- The partition key should have a wide range of values.

- The partition key should have an even spread of data across the values.

- Cross-partition queries are expensive and should be avoided.

We're using the documentType as our partition key. Now that we have a partition key, we can create the container. To create the pet-supplies container for the pet-supplies database, run the following command:

```azurecli
az cosmosdb sql container create -g pet-supplies-demo-rg --account-name
pet-supplies-demo --database-name pet-supplies --name pet-supplies
--partition-key-path "/documentType"
```

## Running the Code

There are a few things you need to set up before you can run this code.

1. Clone this repository

1. Set up environment variables.

1. Create the instance of Azure Cosmos DB Core (SQL) API.

1. Run the code.

### Clone the repository

Our sample code is available here. The repository contains the following:

- Demos folder with the following demos:

  - Hello World Key Vault: An introduction to Java + Azure Key Vault for storing your application secrets securely.

  - Cosmos-db: The main sample we'll refer to throughout the rest of the guide

Once the repository is cloned, you can continue following this guide.

### Set the environment variables

Once the Azure Cosmos DB Core (SQL) API instance is created, then create environment variables with the URI and the key. **application-default.properties** is configured to look for the following environment variables:

- AZURE_COSMOS_URI

- AZURE_COSMOS_KEY

#### Get the value for AZURE_COSMOS_URI

Use the following command to store the URI in the environment variable named **AZURE_COSMOS_URI**:

```azurecli
AZURE_COSMOS_URI=$(az cosmosdb show --name pet-supplies-demo -g
pet-supplies-demo-rg --query "readLocations[0].documentEndpoint" -o
tsv)
```

#### Get the value for AZURE_COSMOS_KEY*

This is for the read and write key. Use the following command to store the key in the environment variable named AZURE_COSMOS_KEY:

```azurecli
AZURE_COSMOS_KEY=$(az cosmosdb keys list --name pet-supplies-demo -g
pet-supplies-demo-rg --query "primaryMasterKey" -o tsv)
```

### Run the code

Install dependencies:

```maven
mvn install to install dependencies.
```

Run locally and access the API at `http://localhost:8080:`

```maven
mvn spring-boot:run
```

## Additional notes

- This project was created using [Spring Initializr](https://start.spring.io/).

- If using Visual Studio Code, [try the VS Code Spring initializr extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr).

- If using Visual Studio Code, [install the Lombok extension](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok) for the getters and setters.

- The Spring packages to work with Azure Cosmos DB and Azure have dependencies that require Spring Boot \>= 2.2.11.RELEASE and \<2.7.0-M1.

## Learn More

- [Data modeling Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/modeling-data)

- [How to model and partition data on Azure Cosmos DB using a real-world example](https://docs.microsoft.com/azure/cosmos-db/sql/how-to-model-partition-example)

- [Partition Strategy - Azure Cosmos DB Essentials, Season 2](https://www.youtube.com/watch?v=QLgK8yhKd5U)
