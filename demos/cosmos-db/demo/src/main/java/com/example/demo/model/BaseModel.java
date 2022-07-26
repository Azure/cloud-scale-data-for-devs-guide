package com.example.demo.model;

import java.util.UUID;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Container(containerName = "pet-supplies")
@Getter @Setter @NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXISTING_PROPERTY, property = "documentType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Customer.class, name="customer"),
    @JsonSubTypes.Type(value = Manufacturer.class, name="manufacturer"),
    @JsonSubTypes.Type(value = Product.class, name="product"),
    @JsonSubTypes.Type(value = Wishlist.class, name="wishlist")
})
public class BaseModel {
    @Id
    protected String id = UUID.randomUUID().toString();
    @PartitionKey
    protected String documentType = "unknown";
}
