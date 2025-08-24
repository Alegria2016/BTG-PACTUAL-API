package com.btgpactual.btg_investment_api.dto;

import java.time.Instant;
import java.util.List;

public record UserResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        List<String> roles,
        boolean enabled
) {}