---
title: Get started with Java and Azure Cosmos DB
description: Learn how to create a Java demo application that uses Azure Cosmos DB for its data store.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 5
---

# Get started with Java and Azure Cosmos DB

In this guide, you work with a Spring Boot application that uses Azure Cosmos DB for its data store and Azure Cosmos DB for NoSQL. This application takes a prescribed approach to development using Java 11, Spring Data, and reactive development. This project was created using [Spring Initializr](https://start.spring.io/).

## Prerequisites

- An Azure subscription or free Azure Cosmos DB trial account:
  - If you don't have an [Azure subscription](https://docs.microsoft.com/azure/guides/developer/azure-developer-guide#understanding-accounts-subscriptions-and-billing), create an [Azure free account](https://azure.microsoft.com/free/?ref=microsoft.com&utm_source=microsoft.com&utm_medium=docs&utm_campaign=visualstudio) before you begin.

  - For this example, you create an [Azure Cosmos DB free tier account](https://docs.microsoft.com/azure/cosmos-db/optimize-dev-test#azure-cosmos-db-free-tier). This account comes with 25 GB of free storage and also includes the first 1,000 RU/s for free.

- Azure CLI:
  - You can use Azure CLI with Windows, macOS, Linux, Docker, and Azure Cloud Shell.
  
  - If you run Azure CLI commands in the [Azure Cloud Shell](https://docs.microsoft.com/azure/cloud-shell/quickstart), use the Bash environment.

  - If you prefer to run Azure CLI commands locally, [install](/cli/azure/install-azure-cli) the Azure CLI. After you install it, sign in to the Azure CLI by using the [az login](/cli/azure/reference-index#az-login) command. For other sign-in options, see [Sign in with the Azure CLI](https://docs.microsoft.com/cli/azure/authenticate-azure-cli).

  - When you use Azure Spring Apps with Azure CLI for the first time, you're prompted to install the *spring* [Azure CLI extension](https://docs.microsoft.com/cli/azure/azure-cli-extensions-overview).

  - Certain Azure Cosmos DB commands might prompt you to install the *cosmosdb-preview* Azure CLI extension.

- Java:
  - Use Java 11 or higher.
  - Use a Java development environment, such as Visual Studio Code, IntelliJ, NetBeans, or Eclipse.

- Use the following Maven plugins to assist with Azure deployments:
  - [Maven Plugin for Azure App Service](https://github.com/microsoft/azure-maven-plugins/tree/develop/azure-webapp-maven-plugin)
  - [Maven Plugin for Azure Spring Apps](https://github.com/microsoft/azure-maven-plugins/tree/develop/azure-spring-apps-maven-plugin)
  - [Maven Plugin for Azure Functions](https://github.com/microsoft/azure-maven-plugins/tree/develop/azure-functions-maven-plugin)

- For the Spring packages to work with Azure Cosmos DB and Azure CLI, you must use a version of Spring Boot \>= 2.2.11.RELEASE and \< 2.7.0-M1.

- If you use Visual Studio Code, consider installing the [Spring initializr extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr) for Spring Boot support. Install the [Lombok extension](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok) if you want to automatically generate getters and setters.

## Create a resource group

To store Azure resources for this demo, you need an Azure resource group:

1. Before you create your resource group, determine the location to use. If you're not sure which locations are available, run the following Azure CLI command and use the value from the `Name` column of the table:

   ```azurecli
   az account list-locations -o table
   ```

1. Use the following command to create a resource group:

   ```azurecli
   az group create --location eastus --resource-group
   pet-supplies-demo-rg
   ```

## Create an Azure Cosmos DB account and database

To configure your Azure Cosmos DB account to work with the demo, do the following tasks:

1. Create an Azure Cosmos DB account.

1. Create an Azure Cosmos DB for NoSQL database.

1. Create the pet-supplies container.globaly

### Create an Azure Cosmos DB account

To create an Azure Cosmos DB account, run the following command.  Note that Cosmos DB account names are globally unique.  Choose a name that is memorable to you, such as *yourname-pet-supplies-demo*.:

```azurelcli
az cosmosdb create -n <your-cosmosdb-name> -g pet-supplies-demo-rg --enable-free-tier true --default-consistency-level Session
```

After you run this command, it may take a few minutes to create your Azure Cosmos DB account.

### Create a database with Azure Cosmos DB for NoSQL 

To create the pet-supplies database with Azure Cosmos DB for NoSQL, run the following command:

```azurecli
az cosmosdb sql database create -g pet-supplies-demo-rg --account-name
<your-cosmosdb-name> -n pet-supplies
```

### Create a container with Azure Cosmos DB for NoSQL

Before you create a container for your Azure Cosmos DB database, you need to be sure of your data model and establish a partition key. A partition key is an immutable property on a document that is used to group documents logically.

> After a partition key is set for a container, it can't change. The partition key is a design-time decision.

Keep the following information in mind when you choose your partition keys:

- Partition keys should have a wide range of values.

- Partition keys should have an even spread of data across their values.

- Avoid cross-partition queries because they're expensive.

After you've created a partition key, you can create the container. In this example, use \/documentType for your partition key.

To create the pet-supplies container for the pet-supplies database, run the following command:

```azurecli
az cosmosdb sql container create -g pet-supplies-demo-rg --account-name
<your-cosmosdb-name> --database-name pet-supplies --name pet-supplies
--partition-key-path "/documentType"
```

## Clone the repository and set environment variables

There are a few more things you need to set up before you can run your code:

1. Clone the repository.

1. Navigate to the demo directory

1. Set the environment variables.

1. Create an instance of Azure Cosmos DB for NoSQL.

### Clone the repository

Clone the repository that contains the sample code, which is available in the [Cloud-Scale Data for Spring Developers repository](https://github.com/Azure/cloud-scale-data-for-devs-guide/tree/main/demos). The repository contains a folder named *demos*, with the following contents:

- hello-world-keyvault: An introduction to Java and Azure Key Vault, which is used to store your application secrets securely.

- cosmos-db: The main sample referred to throughout the rest of the guide.

### Navigate to the demo directory

The files you will need for this demo are in demos\cosmos-db\demo.  Open the repo in a terminal then navigate to the demo directory:

```azurecli
cd demos\cosmos-db\demo
```

### Set the environment variables

After you create your Azure Cosmos DB for NoSQL instance, create environment variables by using the URI and the key. *application-default.properties* is configured to look for the following environment variables:

- `AZURE_COSMOS_URI`

- `AZURE_COSMOS_KEY`

#### Get the value for AZURE_COSMOS_URI

Use the following command to store the URI in the environment variable named `AZURE_COSMOS_URI`:

```azurecli
AZURE_COSMOS_URI=$(az cosmosdb show --name <your-cosmosdb-name> -g
pet-supplies-demo-rg --query "readLocations[0].documentEndpoint" -o
tsv)
```

#### Get the value for AZURE_COSMOS_KEY

 Use the following command to store the read and write key in the environment variable named AZURE_COSMOS_KEY:

```azurecli
AZURE_COSMOS_KEY=$(az cosmosdb keys list --name <your-cosmosdb-name> -g
pet-supplies-demo-rg --query "primaryMasterKey" -o tsv)
```

## Run the code

To run the code, follow these steps:

1. Install your dependencies:

```bash
mvn install
```

1. Run the following code locally, and then access the API at `http://localhost:8080`:

```bash
mvn spring-boot:run
```

You should see a home page for Contoso Pet Supplies at `http://localhost:8080`.  Most of the links will return errors for now, we will be setting up the services that support the rest of the site in future exercises. 

## Learn more

- [Data modeling in Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/sql/modeling-data)

- [How to model and partition data on Azure Cosmos DB using a real-world example](https://docs.microsoft.com/azure/cosmos-db/sql/how-to-model-partition-example)

- [Partition Strategy - Azure Cosmos DB Essentials, Season 2](https://www.youtube.com/watch?v=QLgK8yhKd5U)

[Next &#124; Deploy to Azure App Service](deploy-to-azure-app-service.md){: .btn .btn-primary .btn-lg }
