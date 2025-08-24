package com.btgpactual.btg_investment_api.repository;

import com.btgpactual.btg_investment_api.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
public interface TransactionCustomRepository {
    List<Transaction> findTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findTransactionsByClientAndDateRange(String clientId, LocalDateTime startDate, LocalDateTime endDate);
    Double getTotalInvestedByClient(String clientId);
}
