package com.btgpactual.btg_investment_api.repository;
import com.btgpactual.btg_investment_api.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByClientId(String clientId);
    List<Transaction> findByClientIdOrderByDateDesc(String clientId);
    List<Transaction> findByFundId(String fundId);
    List<Transaction> findByType(String type);
}
