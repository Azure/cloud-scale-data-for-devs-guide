package com.example.demo.service;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosPatchItemRequestOptions;
import com.azure.cosmos.models.CosmosPatchOperations;
import com.azure.cosmos.models.PartitionKey;
import com.example.demo.config.CosmosConfiguration;
import com.example.demo.dao.ProductRepository;
import com.example.demo.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProductService {
    
    @Autowired
    private ProductRepository repository;

    @Autowired
    private WishlistService wishlistService;

    private CosmosConfiguration cosmosConfig;

    @Value("${azure.cosmos.database}")
    private String dbName;

    @Value("${azure.cosmos.feed-container}")
    private String containerName;

    @PostConstruct
    public void init() {
        wishlistService.setProductService(this);
    }

    public Flux<Product> findAll(String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findAll(partitionKey);
    }

    public Mono<Product> findById(String productId, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(productId, partitionKey);
    }

    public Mono<Boolean> deleteById(String productId, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(productId, partitionKey)
            .flatMap(product -> this.repository.delete(product))
            .thenMany(this.wishlistService.findByProductId(productId))
            .flatMap(wishlist -> {
                log.info("removing product {}", productId);
                log.info("products in wishlist {}", wishlist.getProducts());
                wishlist.removeProduct(productId);
                return this.wishlistService.save(wishlist);
            })
            .then(Mono.just(true));
    }

    public Mono<Product> save(Product product) {
        return this.wishlistService.findByProductId(product.getId())
            .flatMap(wishlist -> {
                log.info("Updating wishlist with product {}", product.getManufacturerId());
                wishlist.addProduct(product);
                return this.wishlistService.save(wishlist);
            })
            .then(repository.save(product));
            
    }

    // Used to show partial document updates
    // This is using the Azure Cosmos DB Java SDK v4    
    public void saveMsrp(Product product){        
        CosmosClient client = cosmosConfig.getCosmosClientBuilder().buildClient();
        CosmosDatabase database = client.getDatabase(dbName);
        CosmosContainer container = database.getContainer(containerName);
        CosmosPatchOperations cosmosPatchOperations = CosmosPatchOperations.create();
        cosmosPatchOperations.replace("/msrp", product.getMsrp());
        CosmosPatchItemRequestOptions options = new CosmosPatchItemRequestOptions();
        try {
            container.patchItem(product.getId(), new PartitionKey(product.getDocumentType()), cosmosPatchOperations, options, Product.class);
            log.info("Updating the price for product {}", product.getName());
        } catch (Exception e){
            log.error("Failed the partial update for product {}", product.getName());
        }
    }
}
