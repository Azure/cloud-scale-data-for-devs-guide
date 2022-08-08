---
title: Deploy to Azure App Service
description: Learn how to deploy to Azure App Service.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 6
---

# Deploy to Azure App Service

*Azure App Service* is one of the ways to host Java applications in Azure. Once your sample app is running locally, use the [Maven Plugin for Azure App Service](https://github.com/microsoft/azure-maven-plugins/blob/develop/azure-webapp-maven-plugin/) to deploy the sample app to Azure App Service. This tool creates the resources if they don't exist.

## Modify the POM

> You don't need to install anything separately. The **pom.xml** file has a reference in the \<build> section for the azure-webapp-maven-plugin artifact.

In the **pom.xml** file, update the following properties in the \<properties> section:

- azure.webapp.AppName: \<App name>

- azure.webapp.appServicePlanName: \<App service plan name>

- azure.webapp.region: \<Region code, such as eastus, westus>

- azure.webapp.resourceGroup: \<Resource group name>

## Deploy the application

Once you update your configuration information with your values, deploy the application by using the following command:

```cmd
mvn package azure-webapp:deploy
```

> The plugin section in the pom.xml file has an \<appSettings> section. This will copy our environment variables and their values to the Azure App Service app's settings.

![Screenshot showing the App Service Configuration page.](media/deploy-to-azure-app-service/app-service-configuration.png)

Once the project is deployed, you'll see a message "Successfully deployed the artifact to" with a URL for your Azure App Service application. Curl the URL and ensure you get an HTTP status of 200.

[Next &#124; CRUD Operations with Azure Cosmos DB](crud-operations-with-cosmos-db.md){: .btn .btn-primary .btn-lg }
