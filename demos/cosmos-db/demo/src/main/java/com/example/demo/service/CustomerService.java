package com.example.demo.service;

import com.azure.cosmos.models.PartitionKey;
import com.example.demo.dao.CustomerRepository;
import com.example.demo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerService {
    
    @Autowired
    private CustomerRepository repository;
    
    @Autowired
    private WishlistService wishlistService;
    
    @PostConstruct
    public void init() {
        wishlistService.setCustomerService(this);
    }

    public Flux<Customer> findAll(String partitionKey) {
        PartitionKey key = new PartitionKey(partitionKey);
        return this.repository.findAll(key);
    }

    public Mono<Customer> findById(String customer, String key) {
        PartitionKey partitionKey = new PartitionKey(key);
        return this.repository.findById(customer, partitionKey);
    }

    public Mono<Boolean> deleteById(String customerId, String key) {
        return this.repository.findById(customerId)
            .flatMap(customer -> 
                this.repository.delete(customer)
                    .then(this.wishlistService.deleteByCustomerId(customerId))
                    .thenReturn(true))
            .defaultIfEmpty(false);
    }

    public Mono<Customer> save(Customer customer) {
        return this.repository.save(customer);
    }

}
