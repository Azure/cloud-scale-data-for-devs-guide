package com.example.demo.service;

import com.azure.cosmos.models.PartitionKey;
import com.example.demo.dao.ManufacturerRepository;
import com.example.demo.model.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ManufacturerService {
    
    @Autowired
    private ManufacturerRepository repository;

    public Flux<Manufacturer> findAll(String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findAll(partitionKey);
    }

    public Mono<Manufacturer> findById(String manufacturerId, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(manufacturerId, partitionKey);
    }

    public Mono<Boolean> deleteById(String customerId, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(customerId, partitionKey)
            .flatMap(customer ->
                this.repository.delete(customer)
                .then(Mono.just(true))
            ).defaultIfEmpty(false);
    }

    public Mono<Manufacturer> save(Manufacturer product) {
        return this.repository.save(product);
    }
}
