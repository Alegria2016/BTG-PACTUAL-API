package com.btgpactual.btg_investment_api.repository;

import com.btgpactual.btg_investment_api.model.Transaction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
@Repository
public class TransactionCustomRepositoryImpl implements TransactionCustomRepository {

    private final MongoTemplate mongoTemplate;

    public TransactionCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Transaction> findTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = Criteria.where("date").gte(startDate).lte(endDate);
        Aggregation aggregation = newAggregation(match(criteria));
        return mongoTemplate.aggregate(aggregation, Transaction.class, Transaction.class).getMappedResults();
    }

    @Override
    public List<Transaction> findTransactionsByClientAndDateRange(String clientId, LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = Criteria.where("clientId").is(clientId)
                .and("date").gte(startDate).lte(endDate);
        Aggregation aggregation = newAggregation(match(criteria));
        return mongoTemplate.aggregate(aggregation, Transaction.class, Transaction.class).getMappedResults();
    }

    @Override
    public Double getTotalInvestedByClient(String clientId) {
        Criteria criteria = Criteria.where("clientId").is(clientId)
                .and("type").is("SUBSCRIPTION");

        MatchOperation matchStage = match(criteria);
        GroupOperation groupStage = group().sum("amount").as("totalInvested");

        Aggregation aggregation = newAggregation(matchStage, groupStage);
        AggregationResults<TotalInvestment> results = mongoTemplate.aggregate(
                aggregation, Transaction.class, TotalInvestment.class);

        return results.getUniqueMappedResult() != null ?
                results.getUniqueMappedResult().getTotalInvested() : 0.0;
    }

    // Clase interna para el resultado de la agregación
    private static class TotalInvestment {
        private Double totalInvested;

        public Double getTotalInvested() { return totalInvested; }
        public void setTotalInvested(Double totalInvested) { this.totalInvested = totalInvested; }
    }
}
