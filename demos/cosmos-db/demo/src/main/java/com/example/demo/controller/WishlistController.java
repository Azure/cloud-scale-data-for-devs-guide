package com.example.demo.controller;

import com.example.demo.service.WishlistService;
import com.example.demo.model.Wishlist;
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
@RequestMapping("/api/wishlists")
public class WishlistController {
    
    @Autowired
    private WishlistService wishlistService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Mono<Wishlist> create(@RequestBody Wishlist wishlist) {
        return this.wishlistService.save(wishlist);
    }

    @GetMapping(produces = "application/json")
    public Flux<Wishlist> read(@RequestParam(name = "key", defaultValue = "wishlist") String key) {
        return wishlistService.findAll(key);
    }

    @GetMapping(path = "/products/{productId}", produces = "application/json")
    public Flux<Wishlist> findByProductId(@PathVariable("productId") String productId) {
        return wishlistService.findByProductId(productId);
    }

    @GetMapping(path = "/{wishlistId}", produces = "application/json")
    public Mono<Wishlist> findById(
        @PathVariable("wishlistId") String wishlistId,
        @RequestParam(name = "key", defaultValue = "wishlist") String key) {
        return this.wishlistService.findById(wishlistId, key);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public Mono<Wishlist> update(@RequestBody Wishlist wishlist) {
        return this.wishlistService.save(wishlist);
    }

    @DeleteMapping(path = "/{wishlistId}", produces = "application/json")
    public Mono<Boolean> delete(
        @PathVariable("wishlistId") String wishlistId,
        @RequestParam(name = "key", defaultValue = "wishlist") String key) {
        return this.wishlistService.deleteById(wishlistId, key);
    }
    
}   
