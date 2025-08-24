package com.btgpactual.btg_investment_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "funds")
public class Fund {
    @Id
    private String id;
    private String name;
    private Double minimumAmount;
    private String category;

    // Constructores
    public Fund() {}

    public Fund(String name, Double minimumAmount, String category) {
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.category = category;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(Double minimumAmount) { this.minimumAmount = minimumAmount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
