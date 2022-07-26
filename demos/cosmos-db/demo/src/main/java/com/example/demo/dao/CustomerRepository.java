package com.example.demo.dao;

import com.example.demo.model.Customer;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.azure.cosmos.models.PartitionKey;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

@Repository
public interface CustomerRepository extends ReactiveCosmosRepository<Customer, String> {
    Mono<Void> deleteById(String customertId, PartitionKey key); 

    Flux<Customer> findAll(PartitionKey key);

    Mono<Customer> findById(String customerId, PartitionKey key);

    Mono<Customer> save(Customer customer);
}
