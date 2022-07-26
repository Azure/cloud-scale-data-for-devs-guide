package com.example.demo.controller;

import com.example.demo.service.ChangeFeedService;
import com.example.demo.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/changefeed")
public class ChangeFeedController {

    @Autowired 
    ChangeFeedService changeFeedService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE) 
    public Flux<Product> read() {
        return changeFeedService.changedProducts;
    }
}