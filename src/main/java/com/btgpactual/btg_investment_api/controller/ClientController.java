package com.btgpactual.btg_investment_api.controller;
import com.btgpactual.btg_investment_api.model.Client;
import com.btgpactual.btg_investment_api.model.Transaction;
import com.btgpactual.btg_investment_api.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/me")
    @Operation(summary = "Obtener fondos suscritos")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Client> getMyProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        try {
            Client client = clientService.getClientByUserId(user.getUsername());
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/subscribe/{fundId}")
    @Operation(summary = "Suscribirse a un nuevo fondo (apertura).")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<?> subscribeToFund(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                                             @PathVariable String fundId) {
        try {
            Client client = clientService.subscribeToFund(user.getUsername(), fundId);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel/{fundId}")
    @Operation(summary = "Cancelar la suscripción a un fondo actual.")
    public ResponseEntity<?> cancelSubscription(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                                                @PathVariable String fundId) {
        try {
            Client client = clientService.cancelSubscription(user.getUsername(), fundId);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/transactions")
    @Operation(summary = "Ver historial de transacciones (aperturas y cancelaciones).")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        try {
            List<Transaction> transactions = clientService.getTransactionHistory(user.getUsername());
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
