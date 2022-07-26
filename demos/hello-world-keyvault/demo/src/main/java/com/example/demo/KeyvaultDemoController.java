package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  

import com.azure.identity.DefaultAzureCredentialBuilder;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

@RestController  
public class KeyvaultDemoController   
{  
    @RequestMapping("/")  
    public String getKeyvaultValue()   
    {  
        return getStoredValue("keyName");
    }  

    private String getStoredValue(String keyName){
        String keyVaultName = System.getenv("KEY_VAULT_NAME");
        String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
        SecretClient secretClient = new SecretClientBuilder()
            .vaultUrl(keyVaultUri)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();
        KeyVaultSecret storedSecret = secretClient.getSecret("keyName");
        return storedSecret.getValue();
    }
}  