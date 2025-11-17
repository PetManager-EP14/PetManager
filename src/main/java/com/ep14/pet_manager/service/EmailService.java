package com.ep14.pet_manager.service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;

/**
 * Servicio para envío de correos electrónicos
 * Nota: Esta implementación es un placeholder. En producción se debe integrar
 * con un servicio real de email (JavaMail, SendGrid, AWS SES, etc.)
 */
@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Value("${notification.email.recipients:compras@petmanager.com}")
    private String notificationRecipients;

    /**
     * Envía una notificación de venta de alto volumen
     * @param sale La venta que supera el umbral
     */
    public void sendHighVolumeSaleNotification(Sale sale) {
        String subject = "🔔 Notificación: Venta de Alto Volumen - ID: " + sale.getSaleId();
        String body = buildHighVolumeSaleEmailBody(sale);
        
        // Por ahora solo registramos en log
        logger.info("=== EMAIL NOTIFICATION ===");
        logger.info("TO: " + notificationRecipients);
        logger.info("SUBJECT: " + subject);
        logger.info("BODY:\n" + body);
        logger.info("========================");
        
        // En producción, aquí iría la llamada real al servicio de email:
        // mailSender.send(mimeMessage);
    }

    /**
     * Construye el cuerpo del email con los detalles de la venta
     */
    private String buildHighVolumeSaleEmailBody(Sale sale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        StringBuilder body = new StringBuilder();
        body.append("Estimado/a Responsable de Compras,\n\n");
        body.append("Se ha registrado una venta de alto volumen en el sistema.\n\n");
        body.append("DETALLES DE LA VENTA:\n");
        body.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        body.append(String.format("ID Venta: %d\n", sale.getSaleId()));
        body.append(String.format("Fecha: %s\n", sale.getDate().format(formatter)));
        body.append(String.format("Cliente: %s\n", sale.getUser().getName()));
        body.append(String.format("Email: %s\n", sale.getUser().getEmail()));
        body.append(String.format("Total: $%.2f\n", sale.getTotal()));
        body.append(String.format("Método de Pago: %s\n", sale.getMethod()));
        body.append("\nPRODUCTOS VENDIDOS:\n");
        body.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        for (SaleDetails detail : sale.getSaleDetails()) {
            BigDecimal subtotal = detail.getProduct().getPriceSale()
                .multiply(detail.getAmount());
            body.append(String.format("• %s\n", detail.getProduct().getName()));
            body.append(String.format("  Cantidad: %s | Precio Unit: $%.2f | Subtotal: $%.2f\n",
                detail.getAmount(),
                detail.getProduct().getPriceSale(),
                subtotal));
        }
        
        body.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        body.append("\nEsta notificación se genera automáticamente cuando una venta supera ");
        body.append("el umbral establecido para tomar decisiones sobre inventario y reabastecimiento.\n\n");
        body.append("Atentamente,\n");
        body.append("Sistema PetManager\n");
        
        return body.toString();
    }

    /**
     * Envía notificación por SMS (placeholder)
     */
    public void sendSmsNotification(String phoneNumber, String message) {
        logger.info("SMS to " + phoneNumber + ": " + message);
    }
}
