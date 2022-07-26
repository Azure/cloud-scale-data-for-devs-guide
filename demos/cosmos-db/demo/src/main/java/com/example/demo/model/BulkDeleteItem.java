package com.example.demo.model;

import org.apache.commons.lang3.tuple.Pair;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class BulkDeleteItem {
    private String partitionKey;
    private String id;

    public Pair<String, String> toPair(){
        return Pair.of(partitionKey, id);
    }
}
