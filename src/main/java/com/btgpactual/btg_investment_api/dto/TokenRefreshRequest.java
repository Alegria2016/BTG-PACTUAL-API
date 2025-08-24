package com.btgpactual.btg_investment_api.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank(message = "Token de refresh es obligatorio")
        String refreshToken
) {}
