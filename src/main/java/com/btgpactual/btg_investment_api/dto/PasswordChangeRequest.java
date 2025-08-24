package com.btgpactual.btg_investment_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
        @NotBlank(message = "Password actual es obligatorio")
        String currentPassword,

        @NotBlank(message = "Nuevo password es obligatorio")
        @Size(min = 6, message = "Nuevo password debe tener al menos 6 caracteres")
        String newPassword
) {}
