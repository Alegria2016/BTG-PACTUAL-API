package com.btgpactual.btg_investment_api.model.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Investment {
    private String fundId;
    private BigDecimal investedAmount;
    private LocalDateTime investmentDate;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(BigDecimal investedAmount) {
        this.investedAmount = investedAmount;
    }

    public LocalDateTime getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(LocalDateTime investmentDate) {
        this.investmentDate = investmentDate;
    }
}
