package com.example.demo.controller;

import com.example.demo.service.CustomerService;
import com.example.demo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired 
    CustomerService customerService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Mono<Customer> create(@RequestBody Customer customer) {
        return this.customerService.save(customer);
    }

    @GetMapping
    @PostMapping(produces = "application/json") 
    public Flux<Customer> read(
        @RequestParam(name = "key", defaultValue = "customer") String key) {
        return customerService.findAll(key);
    }

    @GetMapping(path = "/{customerId}", produces = "application/json")
    public Mono<Customer> findById(
        @PathVariable(name = "customerId") String customerId,
        @RequestParam(name = "key", defaultValue = "customer") String key) {
        return this.customerService.findById(customerId, key);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public Mono<Customer> update(@RequestBody Customer customer) {
        return this.customerService.save(customer);
    }

    @DeleteMapping(path = "/{customerId}", produces = "application/json")
    public Mono<Boolean> delete(
        @PathVariable(name = "customerId") String customerId,
        @RequestParam(name = "key", defaultValue = "customer") String key) {
        return this.customerService.deleteById(customerId, key);
    }
    
}   
