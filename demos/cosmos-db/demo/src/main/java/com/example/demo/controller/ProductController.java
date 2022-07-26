package com.example.demo.controller;

import com.example.demo.service.ProductService;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Mono<Product> create(@RequestBody Product product) {
        return this.productService.save(product);
    }

    @GetMapping(produces = "application/json")
    public Flux<Product> read(@RequestParam(name = "key", defaultValue = "product") String key) {
        return productService.findAll(key);
    }

    @GetMapping(path = "/{productId}", produces = "application/json")
    public Mono<Product> findById(
        @PathVariable("productId") String productId,
        @RequestParam(name = "key", defaultValue = "product") String key) {
        return this.productService.findById(productId, key);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public Mono<Product> update(@RequestBody Product product) {
        return this.productService.save(product);
    }

    @PatchMapping(produces = "application/json", consumes = "application/json")
    public void saveMsrp(@RequestBody Product product) {
        this.productService.saveMsrp(product);
    }

    @DeleteMapping(path = "/{productId}", produces = "application/json")
    public Mono<Boolean> delete(
        @PathVariable("productId") String productId,
        @RequestParam(name = "key", defaultValue = "product") String key) {
        return this.productService.deleteById(productId, key);
    }
}   
