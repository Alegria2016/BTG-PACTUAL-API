package com.btgpactual.btg_investment_api.model;

import java.time.LocalDateTime;

public class FundSubscription {
    private String fundId;
    private String fundName;
    private Double amount;
    private LocalDateTime subscriptionDate;
    private LocalDateTime cancellationDate;
    private Boolean active;
    public FundSubscription() {}

    public FundSubscription(String fundId, String fundName, Double amount) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.amount = amount;
        this.subscriptionDate = LocalDateTime.now();
        this.active = true;
    }

    public String getFundId() { return fundId; }
    public void setFundId(String fundId) { this.fundId = fundId; }

    public String getFundName() { return fundName; }
    public void setFundName(String fundName) { this.fundName = fundName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getSubscriptionDate() { return subscriptionDate; }
    public void setSubscriptionDate(LocalDateTime subscriptionDate) { this.subscriptionDate = subscriptionDate; }

    public LocalDateTime getCancellationDate() { return cancellationDate; }
    public void setCancellationDate(LocalDateTime cancellationDate) { this.cancellationDate = cancellationDate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
