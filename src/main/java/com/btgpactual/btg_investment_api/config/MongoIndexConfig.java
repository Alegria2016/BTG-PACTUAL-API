package com.btgpactual.btg_investment_api.config;

import com.btgpactual.btg_investment_api.model.Client;
import com.btgpactual.btg_investment_api.model.Transaction;
import com.btgpactual.btg_investment_api.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import jakarta.annotation.PostConstruct;

@Configuration
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;
    private final MongoMappingContext mongoMappingContext;

    public MongoIndexConfig(MongoTemplate mongoTemplate, MongoMappingContext mongoMappingContext) {
        this.mongoTemplate = mongoTemplate;
        this.mongoMappingContext = mongoMappingContext;
    }

    @PostConstruct
    public void initIndexes() {
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

        // Índices para User usando ensureIndex reemplazado por createIndex
        resolver.resolveIndexFor(User.class).forEach(index ->
                mongoTemplate.indexOps(User.class).createIndex(index));

        // Índices para Client
        resolver.resolveIndexFor(Client.class).forEach(index ->
                mongoTemplate.indexOps(Client.class).createIndex(index));

        // Índices para Transaction
        resolver.resolveIndexFor(Transaction.class).forEach(index ->
                mongoTemplate.indexOps(Transaction.class).createIndex(index));

        // Índices personalizados
        createCustomIndexes();
    }

    private void createCustomIndexes() {
        // Índice único para email de usuario
        Index emailIndex = new Index().on("email", Sort.Direction.ASC).unique();
        mongoTemplate.indexOps(User.class).createIndex(emailIndex);

        // Índice para búsqueda por userId en Client
        Index userIdIndex = new Index().on("userId", Sort.Direction.ASC);
        mongoTemplate.indexOps(Client.class).createIndex(userIdIndex);

        // Índice compuesto para transacciones
        Index transactionIndex = new Index()
                .on("clientId", Sort.Direction.ASC)
                .on("date", Sort.Direction.DESC);
        mongoTemplate.indexOps(Transaction.class).createIndex(transactionIndex);

        // Índice para búsqueda por tipo de transacción
        Index typeIndex = new Index().on("type", Sort.Direction.ASC);
        mongoTemplate.indexOps(Transaction.class).createIndex(typeIndex);

        // Índice para búsqueda por fondos activos en clientes
        Index activeSubscriptionsIndex = new Index()
                .on("subscriptions.active", Sort.Direction.ASC)
                .on("subscriptions.fundId", Sort.Direction.ASC);
        mongoTemplate.indexOps(Client.class).createIndex(activeSubscriptionsIndex);

        // Índice TTL para transacciones (opcional: eliminar después de 2 años)
        Index ttlIndex = new Index().on("date", Sort.Direction.ASC)
                .expire(63072000); // 2 años en segundos
        mongoTemplate.indexOps(Transaction.class).createIndex(ttlIndex);
    }
}