package com.btgpactual.btg_investment_api.service;
import com.btgpactual.btg_investment_api.model.*;
import com.btgpactual.btg_investment_api.repository.ClientRepository;
import com.btgpactual.btg_investment_api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FundService fundService;

    @Autowired
    private NotificationService notificationService;

    public Client getClientByUserId(String userId) {
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public Client subscribeToFund(String userId, String fundId) {
        Client client = getClientByUserId(userId);
        Fund fund = fundService.getFundById(fundId)
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

        if (client.getBalance() < fund.getMinimumAmount()) {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fund.getName());
        }

        // Verificar si ya está suscrito
        if (client.getSubscriptions() != null) {
            Optional<FundSubscription> existingSubscription = client.getSubscriptions().stream()
                    .filter(s -> s.getFundId().equals(fundId) && s.getActive())
                    .findFirst();

            if (existingSubscription.isPresent()) {
                throw new RuntimeException("Ya está suscrito a este fondo");
            }
        }

        // Crear suscripción
        FundSubscription subscription = new FundSubscription(fundId, fund.getName(), fund.getMinimumAmount());

        // Actualizar saldo
        client.setBalance(client.getBalance() - fund.getMinimumAmount());

        // Agregar suscripción
        if (client.getSubscriptions() == null) {
            client.setSubscriptions(new ArrayList<>());
        }
        client.getSubscriptions().add(subscription);

        // Guardar cliente
        Client updatedClient = clientRepository.save(client);

        // Crear transacción
        Transaction transaction = new Transaction(
                client.getId(),
                "SUBSCRIPTION",
                fundId,
                fund.getName(),
                fund.getMinimumAmount()
        );
        transaction.setId(UUID.randomUUID().toString());
        transactionRepository.save(transaction);

        // Enviar notificación
        notificationService.sendNotification(client, "Suscripción exitosa",
                "Te has suscrito al fondo " + fund.getName() + " con un monto de $" + fund.getMinimumAmount());

        return updatedClient;
    }

    public Client cancelSubscription(String userId, String fundId) {
        Client client = getClientByUserId(userId);

        // Buscar suscripción activa
        FundSubscription subscription = client.getSubscriptions().stream()
                .filter(s -> s.getFundId().equals(fundId) && s.getActive())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No tiene una suscripción activa a este fondo"));

        // Cancelar suscripción
        subscription.setActive(false);
        subscription.setCancellationDate(LocalDateTime.now());

        // Devolver fondos
        Fund fund = fundService.getFundById(fundId)
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));
        client.setBalance(client.getBalance() + subscription.getAmount());

        // Guardar cliente
        Client updatedClient = clientRepository.save(client);

        // Crear transacción
        Transaction transaction = new Transaction(
                client.getId(),
                "CANCELLATION",
                fundId,
                fund.getName(),
                subscription.getAmount()
        );
        transaction.setId(UUID.randomUUID().toString());
        transactionRepository.save(transaction);

        // Enviar notificación
        notificationService.sendNotification(client, "Cancelación exitosa",
                "Has cancelado tu suscripción al fondo " + fund.getName() + ". Se ha devuelto $" + subscription.getAmount() + " a tu cuenta.");

        return updatedClient;
    }

    public List<Transaction> getTransactionHistory(String userId) {
        Client client = getClientByUserId(userId);
        return transactionRepository.findByClientId(client.getId());
    }
}
