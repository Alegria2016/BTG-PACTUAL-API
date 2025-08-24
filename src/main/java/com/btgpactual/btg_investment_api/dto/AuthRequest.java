package com.btgpactual.btg_investment_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Email es obligatorio")
        @Email(message = "Email debe tener formato válido")
        String email,

        @NotBlank(message = "Password es obligatorio")
        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        String password
) {}