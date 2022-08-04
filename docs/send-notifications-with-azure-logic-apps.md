---
title: Explore use of Send notifications with Azure Logic Apps
description: Learn about how to send notifications with Azure Logic Apps to notify users when products are updated.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 11
---

# Send notifications with Azure Logic Apps

In the following sample app, we notify users when products are updated. We use *Azure Logic Apps* to automate this process.

Azure Logic apps handles automations that integrate systems, services, apps, and data. They move data around, send notifications, monitor data sources, and connect with other services through connectors.

Logic app capabilities include:

- Sending email when an event occurs

- Monitoring business reviews, analyzing review content, and sending notifications based on outcomes

- Data cleansing pipelines and data migration

- Integrating APIs and external systems

The following example is built from the chain that we started with our Azure Cosmos DB triggered *Azure Function*. The Azure Function sends data to *Azure Event Hub*. Our Logic App monitors Azure Event Hub notifications and emails us when products are updated.

## Create a logic app

1. In Azure portal, select **Create a resource.**

   ![Screenshot that shows the Azure portal Home page with Create a resource group highlighted.](./media/send-notifications-with-azure-logic-apps/select-create-resource.png)

1. Search for the phrase, *logic app*, then select the **Logic App** icon.

   ![Screenshot that shows logic app search results on the Marketplace page.](./media/send-notifications-with-azure-logic-apps/search-for-logic-app.png)

1. Select **Create**.

1. On the **Create Logic App** screen, enter the following settings:

   - Resource Group: pet-supplies-demo-rg

   - Logic App name: pet-supplies-wishlist-notice

   - Publish: Workflow

   - Region: choose your region

   - Plan type: Consumption

1. Select **Review + Create**, then select **Create**.

## Build the logic app

1. Once the logic app is created, navigate to the resource. This will load
the *Logic App Designer*. Select **Blank Logic App**.

   ![Screenshot that shows the Templates page with Blank Logic App selected.](./media/send-notifications-with-azure-logic-apps/select-blank-logic-app.png)

1. For the first task, search for the phrase, *event hub* and then select the trigger **When events are available in Event Hub**.

   ![Screenshot that shows event hub search.](./media/send-notifications-with-azure-logic-apps/search-for-event-hub.png)

1. Give your connection a name. For **Connection String**, set it to the value
you used in `AZURE_EVENT_HUB_CONNECTION`. Then, select **Create**.

1. Once the step connects to the event hub, enter the following settings:

   - Event Hub name: pet-supplies-events

   - Content type: application/json

   - Consumer group name: $Default

1. Add a new step.

1. Search for **Send an Email (V2) Outlook**.

1. Sign in with your Office 365 credentials.

1. Put your email address in the **To** list.

1. For the subject, use *Contoso Product Update*.

1. For the body, use: *The following product is updated:*.

1. Add **Content** from the **Dynamic content** dialog.

1. Select **Save**.

1. As a test, make changes to Pet Supplies products. This should add events to the event hub.

1. Select **Run Trigger**, then **Run**.

## Learn more

- [Overview for Azure Logic Apps - Azure Logic Apps](https://docs.microsoft.com/azure/logic-apps/logic-apps-overview)

- [Integration and automation platform options in Azure](https://docs.microsoft.com/azure/azure-functions/functions-compare-logic-apps-ms-flow-webjobs)

[Next &#124; Scaling in Azure Cosmos DB](scaling-in-cosmos-db.md){: .btn .btn-primary .btn-lg }
