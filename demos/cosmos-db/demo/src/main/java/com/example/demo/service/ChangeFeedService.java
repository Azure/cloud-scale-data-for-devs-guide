package com.example.demo.service;

import java.util.List;

import javax.annotation.PostConstruct;
import com.azure.cosmos.ChangeFeedProcessor;
import com.azure.cosmos.ChangeFeedProcessorBuilder;
import com.example.demo.model.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ChangeFeedService {
    
    @Autowired
    private ChangeFeedProcessorBuilder cfProcessorBuilder;
    private ChangeFeedProcessor cFeedProcessor;
    public ChangedProductSink productSink = new ChangedProductSink();
    //Subscribe to this to receive changed products
    public Flux<Product> changedProducts; 

    @PostConstruct
    public void init() {
        cFeedProcessor = cfProcessorBuilder
            .handleChanges(
                (List<JsonNode> changes) -> { handleChanges(changes); })
            .buildChangeFeedProcessor();
        cFeedProcessor.start().subscribe(System.out::println);
        changedProducts = Flux.create(productSink);
        changedProducts.subscribe(System.out::println);
    }

    public void handleChanges(List<JsonNode> changes) {
        ObjectMapper objectMapper = new ObjectMapper();
        changes.forEach(node -> {
            try {
                // Consumer's accept doesn't fire until a client starts listening
                if (productSink == null) {
                    return;
                }
                // Change feed documents come in as json nodes from creates/updates to any document in the container
                if (node.get("documentType").asText().equals("product")) {
                    productSink.next(objectMapper.readValue(node.toString(), Product.class));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });

    }
}
