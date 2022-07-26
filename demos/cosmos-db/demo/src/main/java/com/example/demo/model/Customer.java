package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.*;

@Getter @Setter
@JsonTypeName("customer")
public class Customer extends BaseModel {

    private String firstName;
    private String lastName;
    private String email;
    
    public Customer() {
        this.documentType = "customer";
    }

}
