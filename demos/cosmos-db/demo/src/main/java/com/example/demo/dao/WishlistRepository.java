package com.example.demo.dao;

import com.example.demo.model.Wishlist;
import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.azure.cosmos.models.PartitionKey;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

@Repository
public interface WishlistRepository extends ReactiveCosmosRepository<Wishlist, String> {
    Mono<Void> deleteById(String wishlistId, PartitionKey key);
    
    Flux<Wishlist> findAll(PartitionKey key);

    @Query("SELECT * FROM c WHERE c.customerId = @customerId AND c.documentType = 'wishlist'")
    Flux<Wishlist> findByCustomerId(@Param("customerId") String customerId);

    @Query("SELECT * FROM c WHERE ARRAY_CONTAINS(c.productIds, @productId)")
    Flux<Wishlist> findByProductId(@Param("productId") String productId);

    Mono<Wishlist> findById(String wishlistId, PartitionKey key);

    Mono<Wishlist> save(Wishlist wishlist);
}
