package com.example.demo.dao;

import com.example.demo.model.Manufacturer;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.azure.cosmos.models.PartitionKey;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

@Repository
public interface ManufacturerRepository extends ReactiveCosmosRepository<Manufacturer, String> {
    Mono<Void> deleteById(String manufacturerId, PartitionKey key);    
    
    Flux<Manufacturer> findAll(PartitionKey key);

    Mono<Manufacturer> findById(String manufactuerId, PartitionKey key);

    Mono<Manufacturer> save(Manufacturer manufacturer);
}
