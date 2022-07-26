package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.*;

@Getter @Setter
@JsonTypeName("product")
public class Product extends BaseModel {

    private String type;
    private String name;
    private String description;
    private String manufacturerId;
    private Double msrp;

    public Product() {
        this.documentType = "product";
    }

}
