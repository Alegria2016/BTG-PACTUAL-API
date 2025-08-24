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
        Instant expiresAt
) {
    public AuthResponse(String token, String email, String firstName, String lastName, List<String> roles) {
        this(token, email, firstName, lastName, roles, Instant.now().plus(24, ChronoUnit.HOURS));
    }
}
