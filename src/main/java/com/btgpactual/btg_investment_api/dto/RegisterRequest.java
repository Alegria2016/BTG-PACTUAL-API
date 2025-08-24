package com.btgpactual.btg_investment_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

        @NotNull(message = "Balance es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "Balance debe ser mayor a 0")
        Double balance,

        @NotBlank(message = "Preferencia de notificación es obligatoria")
        @Pattern(regexp = "EMAIL|SMS", message = "Preferencia de notificación debe ser EMAIL o SMS")
        String notificationPreference,

        List<String> roles
) {
        // Constructor adicional para compatibilidad hacia atrás
        public RegisterRequest {
                // Valores por defecto si no se proporcionan
                if (balance == null) {
                        balance = 500000.0; // Valor por defecto
                }
                if (notificationPreference == null) {
                        notificationPreference = "EMAIL"; // Valor por defecto
                } else {
                        notificationPreference = notificationPreference.toUpperCase(); // Normalizar a mayúsculas
                }
                if (roles == null) {
                        roles = List.of("CLIENT"); // Valor por defecto
                }
        }

        // Constructor sobrecargado para compatibilidad
        public RegisterRequest(String email, String password, String firstName, String lastName, List<String> roles) {
                this(email, password, firstName, lastName, 500000.0, "EMAIL", roles);
        }

        // Método de validación adicional
        public boolean isValid() {
                return balance != null && balance > 0 &&
                        notificationPreference != null &&
                        (notificationPreference.equals("EMAIL") || notificationPreference.equals("SMS"));
        }
}