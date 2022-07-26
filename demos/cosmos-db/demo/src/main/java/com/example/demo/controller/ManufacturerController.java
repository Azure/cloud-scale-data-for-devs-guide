package com.example.demo.controller;

import com.example.demo.service.ManufacturerService;
import com.example.demo.model.Manufacturer;
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
@RequestMapping("/api/manufacturers")
public class ManufacturerController {
    
    @Autowired
    private ManufacturerService manufacturerService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Mono<Manufacturer> create(@RequestBody Manufacturer manufacturer) {
        return this.manufacturerService.save(manufacturer);
    }

    @GetMapping
    public Flux<Manufacturer> read(
        @RequestParam(name = "key", defaultValue = "manufacturer") String key) {
        return manufacturerService.findAll(key);
    }

    @GetMapping(path = "/{manufacturerId}", produces = "application/json")
    public Mono<Manufacturer> findById(
        @PathVariable(name = "manufacturerId") String manufacturerId,
        @RequestParam(name = "key", defaultValue = "manufacturer") String key) {
        return this.manufacturerService.findById(manufacturerId, key);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public Mono<Manufacturer> update(@RequestBody Manufacturer manufacturer) {
        return this.manufacturerService.save(manufacturer);
    }

    @DeleteMapping(path = "/{manufacturerId}", produces = "application/json")
    public Mono<Boolean> delete(
        @PathVariable("manufacturerId") String manufacturerId,
        @RequestParam(name = "key", defaultValue = "manufacturer") String key) {
        return this.manufacturerService.deleteById(manufacturerId, key);
    }
    
}   
