package com.example.demo.service;

import java.util.stream.Collectors;

import com.azure.cosmos.implementation.NotFoundException;
import com.azure.cosmos.models.PartitionKey;
import com.example.demo.dao.WishlistRepository;
import com.example.demo.model.Customer;
import com.example.demo.model.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WishlistService {
    
    @Autowired
    private WishlistRepository repository;
    
    private CustomerService customerService;
    
    private ProductService productService;
    
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    public Flux<Wishlist> findAll(String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findAll(partitionKey);
    }

    public Mono<Wishlist> findById(String wishlistId, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(wishlistId, partitionKey);
    }

    public Flux<Wishlist> findByProductId(String productId) {
        return this.repository.findByProductId(productId);
    }

    public Mono<Boolean> deleteById(String wishlistId, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(wishlistId, partitionKey)
            .flatMap(wishlist ->
                this.repository.delete(wishlist)
                .then(Mono.just(true))
            ).defaultIfEmpty(false);
    }

    public Mono<Boolean> deleteByCustomerId(String customerId) {
        return this.repository.findByCustomerId(customerId)
            .flatMap(
                wishlist ->
                    this.deleteById(wishlist.getId(), "wishlist")
                )
            .all(deleted -> deleted == true);         
    }

    public Mono<Wishlist> save(Wishlist wishlist) {
        Flux<String> productIds = Flux.fromIterable(wishlist.getProductIds());
        Mono<Customer> findCustomer = this.customerService.findById(wishlist.getCustomerId(), "customer");
        return findCustomer
            .switchIfEmpty(
                Mono.error(
                    new NotFoundException(
                        String.format("Customer id %s not found", wishlist.getCustomerId())
                    )
                )
            )
           .thenMany(productIds)
           .flatMap(productId -> {
                return this.productService.findById(productId, "product")
                    .switchIfEmpty(
                        Mono.error(
                            new NotFoundException(
                                String.format("Product id %s not found", productId)
                            )
                        )
                    );
            })
           .collect(Collectors.toSet())
           .flatMap(products -> {
                wishlist.setProducts(products);
               return this.repository.save(wishlist);
           });        
        }
}
