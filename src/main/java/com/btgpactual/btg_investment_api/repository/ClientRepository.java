package com.btgpactual.btg_investment_api.repository;

import com.btgpactual.btg_investment_api.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByUserId(String userId);
    Boolean existsByUserId(String userId);
}