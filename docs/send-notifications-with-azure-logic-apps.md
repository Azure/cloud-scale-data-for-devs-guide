---
title: Send notifications with Azure Logic Apps
description: Learn how to send notifications with Azure Logic Apps.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
---

# Send notifications with Azure Logic Apps

In our sample, we want to notify someone when products are updated. We
will use Azure Logic Apps to automate this process.

Azure Logic apps are used to handle automations that integrate systems,
services, apps, and data. They work well in moving data around, sending
notifications, monitoring data sources, and connecting with other
services through connectors.

Examples of logic apps include:

-   Sending email when an event happens

-   Monitoring business reviews, analyzing the review contents, and
    sending notifications based on outcomes

```{=html}
<!-- -->
```
-   Data cleansing pipeline and data migration

-   Integrating APIs and external systems

We are building this example from the chain we started with our Azure
Cosmos DB triggered Azure Function. The Azure Function is sending data
to Azure Event Hub. Our Logic App will monitor the Azure Event Hub
notifications and email us when products are updated.

## Create a logic app

In the Azure portal, select **Create a resource.**

![](/media/image.png){width="5.0in" height="0.7708333333333334in"}

Search for logic app, then select **Logic App.**

![](/media/image2.png){width="5.0in" height="4.010416666666667in"}

Select **Create**.

On the Create Logic App screen, enter the following settings:

-   Resource Group: pet-supplies-demo-rg

-   Logic App name: pet-supplies-wishlist-notice

-   Publish: Workflow

-   Region: choose your region

-   Plan type: Consumption

Select **Review + Create**, then select **Create**.

## Building the Logic App

Once the logic app is created, navigate to the resource. This will load
the Logic App Designer. Select **Blank Logic App**.

![](/media/image3.png){width="5.0in" height="1.5625in"}

For the first task, search for event hub, then select the trigger **When
events are available in Event Hub**.

![](/media/image4.png){width="4.46875in" height="5.0in"}

Give your connection a name. For Connection String, set it to the value
you used in AZURE_EVENT_HUB_CONNECTION. Then, select **Create**.

Once the step connects to the event hub, enter the following settings:

-   Event Hub name: pet-supplies-events

-   Content type: application/json

-   Consumer group name: \$Default

Add a new step.

Search for **Send an Email (V2) Outlook**.

Sign in with your Office 365 credentials.

Put your email address in the To list.

For the subject, use **Contoso Product Update**.

For the body, use:

The following product is updated:

Then add **Content** from the **Dynamic content** dialog.

Select **Save**.

Make some changes in the Pet Supplies products. These should add events
to the event hub.

Select **Run Trigger,** then **Run**.

## Learn More

-   [Overview for Azure Logic Apps - Azure Logic Apps \| Microsoft
    Docs](https://docs.microsoft.com/en-us/azure/logic-apps/logic-apps-overview)

-   [Integration and automation platform options in Azure \| Microsoft
    Docs](https://docs.microsoft.com/en-us/azure/azure-functions/functions-compare-logic-apps-ms-flow-webjobs)
