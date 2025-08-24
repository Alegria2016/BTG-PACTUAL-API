package com.btgpactual.btg_investment_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterRequest(
        @NotBlank(message = "Email es obligatorio")
        @Email(message = "Email debe tener formato válido")
        String email,

        @NotBlank(message = "Password es obligatorio")
        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        String password,

        @NotBlank(message = "Nombre es obligatorio")
        String firstName,

        @NotBlank(message = "Apellido es obligatorio")
        String lastName,

        List<String> roles
) {}