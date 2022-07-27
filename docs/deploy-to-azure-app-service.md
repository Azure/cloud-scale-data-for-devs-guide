---
title: Deploy to Azure App Service
description: Learn how to deploy to Azure App Service.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
---

# Deploy to Azure App Service

Azure App Service is one of the ways to host Java applications in Azure. Once you have the sample running locally, use the [Maven Plugin for Azure App Service](https://github.com/microsoft/azure-maven-plugins/blob/develop/azure-webapp-maven-plugin/) to deploy the sample app to Azure App Service. This tool will create the resources if they don't exist.

> [!NOTE]
> You don't need to install anything separately. The **pom.xml** file has a reference in the \<build\> section for the azure-webapp-maven-plugin artifact.

In the **pom.xml** file, update the \<properties\> section for the
following properties:

- azure.webapp.AppName: App name

- azure.webapp.appServicePlanName: App Service Plan Name

- azure.webapp.region: Region code, such as eastus, westus, etc.

- azure.webapp.resourceGroup: Resource group name

Once the configuration information is updated for your values, deploy the application with the following command:

```maven
mvn package azure-webapp:deploy
```

> [!NOTE]
> The plugin section in the pom.xml file has an \<appSettings\> section. This will copy our environment variables and their values to the Azure App Service app's settings.

![Screenshot showing the App Service Configuration page.](./media/deploy-to-azure-app-service/app-service-configuration.png)

Once the project is deployed, you'll see a message "Successfully deployed the artifact to" with a URL for your Azure App Service application. Curl the URL and ensure you get an HTTP status of 200.
