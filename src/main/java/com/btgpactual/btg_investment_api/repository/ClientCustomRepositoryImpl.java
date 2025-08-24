package com.btgpactual.btg_investment_api.repository;

import com.btgpactual.btg_investment_api.model.Client;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.List;

public class ClientCustomRepositoryImpl  implements ClientCustomRepository {
    private final MongoTemplate mongoTemplate;

    public ClientCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Client> findClientsWithActiveSubscriptions() {
        Query query = new Query();
        query.addCriteria(Criteria.where("subscriptions.active").is(true));
        return mongoTemplate.find(query, Client.class);
    }

    @Override
    public List<Client> findClientsSubscribedToFund(String fundId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("subscriptions.fundId").is(fundId)
                .and("subscriptions.active").is(true));
        return mongoTemplate.find(query, Client.class);
    }

    @Override
    public void updateClientBalance(String clientId, Double newBalance) {
        Query query = new Query(Criteria.where("id").is(clientId));
        Update update = new Update().set("balance", newBalance);
        mongoTemplate.updateFirst(query, update, Client.class);
    }
}
