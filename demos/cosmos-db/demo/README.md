# Java Setup

There are a few things you need to set up before you can run this code.

1. Clone this Repository
2. Set up Environment Variables.
3. Create the instance of Azure Cosmos DB Core (SQL) API.
4. Add secrets to Azure Key Vault.
5. Run the code.

## Set up Azure Identity Environment Variables

When going through the walk through for Hello World with Java Key Vault, you create a service principal called `java-keyvault-demo-sp`.  You will need the following information:

* AppId
* Password
* TenantId
* Azure Key Vault URL
  
Then, set up the following environment variables with these values:

* AZURE_CLIENT_ID - AppId
* AZURE_TENANT_ID - TenantId
* AZURE_CLIENT_SECRET - Password
* AZURE_KEYVAULT_URL - Azure Key Vault URL

## Create an Azure Cosmos DB Core (SQL) API instance

To create an Azure Cosmos DB Core (SQL) API Free Tier account, use the following command:

```azurecli
az cosmosdb create -n "pet-supplies-demo" -g "java-keyvault-demo-rg" --enable-free-tier true --default-consistency-level "Session"
```

## Add Secrets to Azure Key Vault

Once the Azure Cosmos DB Core (SQL) API instance is created, then add these secrets to Azure Key Vault. **application.properties** is configured to look for the following secrets:

* azure-documentdb-uri
* azure-documentdb-key
* azure-documentdb-database

### Document DB URI

Use the following command to store the URI in the secret named **azure-documentdb-uri**:

```azurecli
az keyvault secret set --vault-name java-keyvault-demo-kv --name "azure-documentdb-uri" --value (az cosmosdb show --name pet-supplies-demo -g java-keyvault-demo-rg --query "readLocations[0].documentEndpoint" -o tsv)
```

## Document DB Key

This is for the read and write key.  Use the following command to store the URI in the secret named **azure-documentdb-uri**:

```azurecli
az keyvault secret set --vault-name java-keyvault-demo-kv --name "azure-documentdb-key" --value (az cosmosdb keys list --name pet-supplies-demo -g java-keyvault-demo-rg --query "primaryMasterKey")
```

## DocumentDB Database

Use the following command to store the URI in the secret named **azure-documentdb-database**:

```azurecli
az keyvault secret set --vault-name java-keyvault-demo-kv --name "azure-documentdb-database" --value "pet-supplies-demo"
```

## Running the Code

Run `mvn install` to install dependencies.

Run `mvn spring-boot:run` to run locally and access the api at http://localhost:8080.

## Deploying to an Azure App Service

To deploy this to an Azure App Service, use the [Maven Plugin for Azure App Service](https://github.com/microsoft/azure-maven-plugins/blob/develop/azure-webapp-maven-plugin/). This tool will create the resources if they do not exist.

In the **pom.xml** file, update the `<properties>` section for the following properties:

* azure.webapp.AppName - App name
* azure.webapp.appServicePlanName - App Service Plan Name
* azure.webapp.region - region code - such as `eastus`, `westus`, etc.
* azure.webapp.resourceGroup - resource group name

Once the configuration information is updated for your values, deploy the application with the following command:

```bash
mvn package azure-webapp:deploy
```

## Additional Notes

* This project was created using [Spring Initializr](https://start.spring.io).

* If using Visual Studio Code, [try the VS Code Spring initializr extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr).

* If using Visual Studio Code, [install the Lombok extension](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok) for the getters and setters.

* The Spring packages to work with Azure Cosmos DB and Azure have dependencies that require Spring Boot >= 2.2.11.RELEASE and < 2.7.0-M1.
  
## Tear Down

* Remove the resource group created for this code.
* Delete the service principal created for this exercise.