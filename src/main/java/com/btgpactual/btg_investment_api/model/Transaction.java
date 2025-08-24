package com.btgpactual.btg_investment_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String clientId;
    private String type; // "SUBSCRIPTION" o "CANCELLATION"
    private String fundId;
    private String fundName;
    private Double amount;
    private LocalDateTime date;

    // Constructores
    public Transaction() {}

    public Transaction(String clientId, String type, String fundId, String fundName, Double amount) {
        this.clientId = clientId;
        this.type = type;
        this.fundId = fundId;
        this.fundName = fundName;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFundId() { return fundId; }
    public void setFundId(String fundId) { this.fundId = fundId; }

    public String getFundName() { return fundName; }
    public void setFundName(String fundName) { this.fundName = fundName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
