package com.example.demo.service;

import java.util.function.Consumer;
import com.example.demo.model.Product;
import reactor.core.publisher.FluxSink;

public class ChangedProductSink implements Consumer<FluxSink<Product>> {
    private FluxSink<Product> sink;

    @Override
    public void accept(FluxSink<Product> productFluxSink) {
        this.sink = productFluxSink;   
    }
    
    public void next(Product product) {
        this.sink.next(product);
    }
}
