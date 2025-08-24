package com.btgpactual.btg_investment_api.repository;

import com.btgpactual.btg_investment_api.model.Client;
import java.util.List;
public interface ClientCustomRepository {
    List<Client> findClientsWithActiveSubscriptions();
    List<Client> findClientsSubscribedToFund(String fundId);
    void updateClientBalance(String clientId, Double newBalance);
}
