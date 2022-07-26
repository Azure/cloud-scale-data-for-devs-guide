package com.example.demo.model;

import lombok.*;
import java.util.Set;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Getter @Setter 
@JsonTypeName("wishlist")
public class Wishlist extends BaseModel {
    
    @NotBlank
    private String customerId;
    private String customerEmail;
    private Set<String> productIds;
    private Set<Product> products; 

    public Wishlist() {
        this.documentType = "wishlist";
    }

    public void addProduct(Product product) {
        this.productIds.add(product.getId());
        this.products.removeIf(p -> p.getId() == product.getId());
        this.products.add(product);
    }

    public void removeProduct(String productId) {
        this.productIds.remove(productId);
        this.products.removeIf(p -> p.getId() == productId);
    }

}
