---
title: Migrate to Azure Spring Cloud
description: Learn how to migrate to Azure Spring Cloud.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 16
---

## Migrate to Azure Spring Cloud

When it comes to microservices and Java in Azure, consider Azure Spring Cloud. It allows you to easily migrate Sprint Boot microservices to Azure with no code changes. It also supports scaling microservices both vertically and horizontally, which makes it a smart choice for a Spring Boot Cloud native application host.

The components used for Azure Spring Cloud include:

- Azure Spring Cloud configuration server

- Spring Boot microservices

- Spring Cloud registry for discovering microservices

This is the way our application is broken up for deployment to Azure Spring Cloud:

- Pet supplies services

- Discovery service

- Configuration server linked to a Git repository

![](/media/image7.png)

## Create an Azure Spring Cloud instance

We need to create an Azure Spring Cloud instance for our resources to live.

> [!NOTE]
> To work with Azure Spring Cloud from Azure CLI, you may need to install the **spring-cloud** extension. This can be installed by running:

```azurecli
az extension add --name spring-cloud
```

To create the Azure Spring Cloud instance, you need a name for the resource and a resource group. When naming your Azure Spring Cloud instance, keep the following in mind:

- The name can contain only lowercase letters, numbers and hyphens. The first character must be a letter. The last character must be a letter or number. The value must be between 4 and 32 characters long.

- The name of an Azure Spring Cloud needs to be unique across all of Azure. You may need to use identifying prefixes or suffixes to help achieve this uniqueness.

Set the following environment variables:

- AZURE_RESOURCE_GROUP=ms-cosmos-db-java-guide

- AZURE_SPRING_CLOUD_NAME=ms-cosmos-db-java-guide-spring-SUFFIX

We are using the Basic SKU since this isn't a production application.

For our example app, run:

```azurecli
az spring-cloud create -n $AZURE_SPRING_CLOUD_NAME -g $AZURE_RESOURCE_GROUP --sku Basic
```

This will also create an Application Insights component for gathering analytics and telemetry.

We will also set defaults for Azure CLI so that we do not need to specify group and spring-cloud in all our commands. Run the following commands to set the default resource group and spring-cloud for our Azure CLI session:

```azurecli
az configure --defaults group=$AZURE_RESOURCE_GROUP
az configure --defaults spring-cloud=$AZURE_SPRING_CLOUD_NAME
```

### Azure Spring Cloud configuration server

The Azure Spring Cloud configuration server needs a Git repository for storing the Spring Boot configuration files. This allows you to achieve a few things:

- Your configuration is stored in a central location, making it easier to maintain.

- You can secure your Git repository so that only the people who need access to those configuration details -- such as application passwords - can access them.

- With the configuration stored in a Git server, it is easy to push changes or roll them back as needed.

- Your secrets do not need to be stored with the application itself.

The Git repository can be public, secured by SSH, or secured using HTTP basic authentication.

The configuration files can be stored in YAML in **application.yml** or in name-value pairs in **application.properties.**

To get an Azure Spring Cloud Config Server running:

1. Create a Git repository with an application.yml or application.properties file to store the configuration needed for the services. This is our application.properties file for the Azure Spring Cloud Config Server:

   ```properties
   # azure.cosmos properties are needed for the CosmosConfiguration bean
   # azure.cosmosdb values are coming from Service bindings on Azure Spring Cloud apps
   azure.cosmos.uri=${azure.cosmosdb.uri}
   azure.cosmos.key=${azure.cosmosdb.key}
   azure.cosmos.database=${azure.cosmosdb.database}
   azure.cosmos.populateQueryMetrics=false
   # Disabling web security for the spring project
   spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
   ```

1. Create a Git personal access token (PAT) with repo access to be used by the Azure Spring Cloud Config Server.

1. In the [Azure portal](https://portal.azure.com), navigate to the Azure Spring Cloud instance.

1. On the left menu, select **Config Server**.

   ![](/media/image8.png)

1. Add a default repository with the following details:

    - **URI**: Git repository URI, including the .git

    - **Label**: branch or tag to use; defaults to master. Since our
        repo defaults to main, we used main.

    - **Search Path**: \<blank>

    - **Authentication:** Select **Public** so that it brings up the
        authentication dropdown.

       - **Authentication Type**: Select **HTTP Basic**

       - **Username:** Use your Git username.

       - **Password:** Use your password or personal access token.

1. Select **Validate**. Once the settings are validated, select
    **Apply**.

   ![](/media/image9.png)

Your service settings can now live in the **application.properties** or **application.yml** file in the Config Server repo.

## Deploy a Maven application to Azure Spring Cloud

Let's deploy our service complete with CRUD to Azure Spring Cloud.

### Create the Azure Spring Cloud app

Once the Azure Spring Cloud instance is created, you need to create an app in it. Each service will have its own app.

To create a new app, you need:

- Azure resource group

- Name for the Azure Spring Cloud instance

- Name for the app

- Runtime version

You can also specify whether to assign an endpoint and assign a managed identity. Since we will want this to be public, we will assign an endpoint. As we are not explicitly granting access between Azure resources, we are not creating a managed identity on this application.

This is the code we are using to create our Azure Spring Cloud pet-supplies-app app:

```azurecli
az spring-cloud app create -n pet-supplies-app --runtime-version Java_11 --assign-endpoint true
```

### Linking the Azure Spring Cloud app to Azure Cosmos DB

Once the app is created, we can add a service binding for the app to talk directly with Azure Cosmos DB.

1. In the Azure portal, navigate to the Azure Spring Cloud resource.

1. Select the **pet-supplies-app** App**.**

1. From the left menu, select **Service bindings.** Then, select **Create service binding.**

   ![](/media/imagea.png)

1. In the **Create service binding dialog**, set the following
settings:

   - **Name:** This is the name for the service binding. We are using pet-supplies-api.

   - **Subscription**: Choose your subscription.

   - **Binding type**: Select **Azure Cosmos DB**.

   - **Resource name:** Select your Azure Cosmos DB instance.

   - **API type:** Select **sql**.

   - **Database name**: Select **pet-supplies.**

   - **Key:** Select the **Primary master key**.

   ![](/media/imageb.png)

1. Once all settings are set, select **Create**.

1. Once the service binding is created, select the **pet-supplies-api** from the Binding Name column. 

   This opens the **View service binding** dialog. Notice that the **Property** section has the settings needed for connecting to Azure Cosmos DB. We will use this rather than storing the connection information in Azure Key Vault.

   ![](/media/imaged.png)

### Set up logging (optional)

Logging makes it easier for debugging issues with deployment. We will set up a Log workspace and have our logs go to it. This is how to create it via the Azure CLI.

```azurecli
az monitor log-analytics workspace create --workspace-name ms-cosmos-db-java-guide-logs
```

Once the log workspace is created, then we can set up diagnostic
logging.

1. In the Azure portal, navigate to the Azure Spring Cloud instance.

1. From the left menu, select **Diagnostic settings**. Then, select **Add diagnostic setting.**

   ![](/media/image.png)

1. Set the following settings:

   - Set the **Diagnostic setting name** to write-all-logs.

   - Check all Categories under **Logs**.

   - Check **AllMetrics** under **Metrics**.

   - In **Destination details**, check **Send to Log Analytics workspace** and specify the Log workspace we just created.

1. Select **Save**.

This will help us if we run into issues in deploying our solution to Azure Spring Cloud, as we can then query the logs to see any information.

## Deploy the code to Azure Spring Cloud app

Once the Azure Spring Cloud instance and app are created, you can deploy the code to the Azure Spring Cloud app. To deploy to an Azure Spring Cloud app, you need:

- App name

- Azure Spring Cloud instance name (service)

- Azure Resource Group

- Artifact path

This is the code we are using:

```azurecli
az spring-cloud app deploy -n pet-supplies-app --artifact-path target/demo-0.0.1-SNAPSHOT.jar

This takes a few minutes to perform. Once completed, you should get a JSON response object indicating that the deployment was successful.

Once this is successful, you can use the URL of the app to test access. To curl the URL, use the following command:

```curl
curl $(az spring-cloud app list --query "[?name=='pet-supplies-app'].[properties.url]" -o tsv)
```

## Tear down

Once you have gotten your application in Azure Spring Cloud for this demo, please be sure to tear this down. **This service is not included in our goal for an application running for $20/month, as even the Basic SKU is hundreds of dollars per month.**

## Learn more

To learn more about Azure Spring Cloud, consider the following resources:

- [Azure Spring Cloud documentation](https://docs.microsoft.com/azure/spring-cloud/)

- [Azure Spring Cloud training](https://github.com/microsoft/azure-spring-cloud-training)

- [Azure Spring Cloud pricing](https://azure.microsoft.com/pricing/details/spring-cloud/)
