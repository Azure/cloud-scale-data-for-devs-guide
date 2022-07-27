---
title: What is NoSQL?
description: NoSQL databases can efficiently query and serve substantial amounts of semi-structured and unstructured data.
ms.service: cosmos-db
ms.topic: reference
ms.date: 08/19/2022
author: seesharprun
ms.author: sidandrews
ms.reviewer: mjbrown
sequence: 1
---

# What is NoSQL?

NoSQL is considered a non-relational, distributed database. NoSQL databases can efficiently query and serve substantial amounts of semi-structured and unstructured data. Unlike relational databases, NoSQL databases are schema-agnostic. NoSQL databases don't define a specific columnar structure allowing for greater flexibility, less brittle systems, and support for rapidly changing or unstructured data.

![Diagram depicting a relational database schema with an Order and OrderDetails table, using a foreign key relationship (one-to-many) defined with the OrderID column. Each table contains multiple columns with metadata about each row of data put into the relational database.](media/intro-nosql/relational_tables.svg)

This diagram depicts an example of the contrast between a relational database table structure and a NoSQL document. On the left, relational tables have a defined schema. Specific columns and their types represent each table's structure, and relationships are represented via foreign keys. For example, a single Order row may have many Order Details rows associated with it. To query this information in its entirety, you must join both tables.

In stark contrast, a NoSQL document depicts the same Order/Order Details relationship via an internal object array in a single document.

```json
{
  "orderId": "873sdfs42981",
  "orderDate": 1574161910220,
  "firstName": "Zhenis",
  "lastName": "Omar",
  "address": "123 Broad Street",
  "city": "Seattle",
  "state": "WA",
  "postalCode": "92839",
  "orderDetails": [
    {
      "orderDetailId": "ds23fhjsdk1",
      "productId": "sdf2378",
      "unitPrice": 7.99,
      "quantity": 1
    },
    {
      "orderDetailId": "zct687786es",
      "productId": "d87f98z",
      "unitPrice": 4.59,
      "quantity": 3
    }
  ]
}
```

Retrieving a single Order and its associated Order Details involves the lookup of a single document.

## Benefits of NoSQL

The NoSQL term was introduced in 2009 at an event to talk about open-source distributed, non-relational databases.  These characteristics – open-source, distributed, and non-relational – are commonly seen in NoSQL databases.

These characteristics lend themselves to these benefits for NoSQL:

* Non-relational leads to storing semi-structured and unstructured data
* Schema-less, leading to flexible data models
* Rapidly adapting to changing requirements
* Horizontally scalable due to the distributed nature
* Highly available due to the distributed nature

We'll demonstrate these benefits in our Contoso pet supplies application.

If you're coming from a relational background and still aren’t sure how NoSQL differs, see [Understanding the differences between NoSQL and relational databases](../../relational-nosql.md).

## NoSQL options

There are many options available in the NoSQL space. When deciding on a NoSQL database system, consider what kind of data you're working with:

* Key-value data
* Column-family data
* Document data
* Graph data
* Schema-less JSON

In this guide, we'll introduce you to Azure Cosmos DB, Microsoft's flagship non-relational database that supports each of these data options. We'll take you through an application that uses the Azure Cosmos DB Core (SQL) API to work with schema-less JSON.

## Why NoSQL for our application

Throughout this guide, we'll be looking at a pet store eCommerce scenario. While internally the data may seem relational, there are cases for non-relational stores. You may be getting new product information from vendors in various formats – structured, semi-structured, and unstructured. You may need to scale your operations for large traffic – such as running sales on an eCommerce site.  Reporting on this data may be complicated.  Throughout this guide, we'll use Azure Cosmos DB as our data store because of its versatility and ease of integrating with other Azure services to solve these problems.

[Next &#124; Introduction to Azure Cosmos DB](intro-cosmos.md){: .btn .btn-primary .btn-lg }
