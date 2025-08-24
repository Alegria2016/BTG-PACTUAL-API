package com.btgpactual.btg_investment_api.service;

import com.btgpactual.btg_investment_api.model.Client;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(Client client, String subject, String message) {
        String preference = client.getNotificationPreference();

        if ("EMAIL".equalsIgnoreCase(preference)) {
            sendEmail(client, subject, message);
        } else if ("SMS".equalsIgnoreCase(preference)) {
            sendSMS(client, subject, message);
        }
    }

    private void sendEmail(Client client, String subject, String message) {
        // Implementar lógica de envío de email
        System.out.println("Enviando email a: " + client.getUserId());
        System.out.println("Asunto: " + subject);
        System.out.println("Mensaje: " + message);
    }

    private void sendSMS(Client client, String subject, String message) {
        // Implementar lógica de envío de SMS
        System.out.println("Enviando SMS a: " + client.getUserId());
        System.out.println("Asunto: " + subject);
        System.out.println("Mensaje: " + message);
    }
}
