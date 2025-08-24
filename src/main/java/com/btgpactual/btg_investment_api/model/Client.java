package com.btgpactual.btg_investment_api.model;

import com.btgpactual.btg_investment_api.model.enums.NotificationPreference;
import com.btgpactual.btg_investment_api.model.support.Investment;
import com.btgpactual.btg_investment_api.model.support.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
@Document(collection = "clients")
public class Client {

    @Id
    private String id;
    private String userId;
    private Double balance;
    private String notificationPreference; // "EMAIL" o "SMS"
    private List<FundSubscription> subscriptions;

    // Constructores
    public Client() {
        this.balance = 500000.0; // Saldo inicial COP $500.000
    }

    public Client(String userId, String notificationPreference) {
        this.userId = userId;
        this.balance = 500000.0;
        this.notificationPreference = notificationPreference;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public String getNotificationPreference() { return notificationPreference; }
    public void setNotificationPreference(String notificationPreference) { this.notificationPreference = notificationPreference; }

    public List<FundSubscription> getSubscriptions() { return subscriptions; }
    public void setSubscriptions(List<FundSubscription> subscriptions) { this.subscriptions = subscriptions; }
}
