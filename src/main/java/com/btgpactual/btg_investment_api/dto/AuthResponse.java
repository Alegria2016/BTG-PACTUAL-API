package com.btgpactual.btg_investment_api.dto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public record AuthResponse(
        String token,
        String email,
        String firstName,
        String lastName,
        List<String> roles,
        Double balance,
        String notificationPreference,
        Instant expiresAt
) {
    public AuthResponse(String token, String email, String firstName, String lastName,
                        List<String> roles, Double balance, String notificationPreference) {
        this(token, email, firstName, lastName, roles, balance, notificationPreference,
                Instant.now().plus(24, ChronoUnit.HOURS));
    }

    // Constructor para compatibilidad hacia atrás
    public AuthResponse(String token, String email, String firstName, String lastName, List<String> roles) {
        this(token, email, firstName, lastName, roles, 500000.0, "EMAIL");
    }
}