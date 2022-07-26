package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.*;

@Getter @Setter
@JsonTypeName("manufacturer")
public class Manufacturer extends BaseModel {

    private String name;

    public Manufacturer() {
        this.documentType = "manufacturer";
    }

}

