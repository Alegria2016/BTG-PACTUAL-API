package com.btgpactual.btg_investment_api.repository;

import com.btgpactual.btg_investment_api.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FundRepository extends MongoRepository<Fund, String> {
    Optional<Fund> findByName(String name);
    Boolean existsByName(String name);
}
