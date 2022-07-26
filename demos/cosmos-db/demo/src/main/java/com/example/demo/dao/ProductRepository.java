package com.example.demo.dao;

import com.example.demo.model.Product;
import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.azure.cosmos.models.PartitionKey;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

@Repository
public interface ProductRepository extends ReactiveCosmosRepository<Product, String> {
    Mono<Void> deleteById(String productId, PartitionKey key);    
    
    Flux<Product> findAll(PartitionKey key);

    Mono<Product> findById(String productId, PartitionKey key);

    Mono<Product> save(Product product);
    
    // Note: UPDATE isn't supported. This is here to quiet the compiler.
    @Query("UPDATE c SET c.msrp = @msrp WHERE c.Id=@id AND c.documentType=@documentType")
    void saveMsrp(Product product);
}
