package com.ep14.pet_manager.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;
import com.ep14.pet_manager.entity.User;

/**
 * Pruebas unitarias para EmailService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmailService Unit Tests")
class EmailServiceTest {

    private EmailService emailService;
    private Sale testSale;
    private User testUser;
    private Product testProduct1;
    private Product testProduct2;
    
    private TestLogHandler logHandler;
    private Logger logger;

    /**
     * Handler personalizado para capturar logs durante las pruebas
     */
    private static class TestLogHandler extends Handler {
        private final List<LogRecord> logRecords = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            logRecords.add(record);
        }

        @Override
        public void flush() {
            // No necesita implementación
        }

        @Override
        public void close() throws SecurityException {
            // No necesita implementación
        }

        public List<LogRecord> getLogRecords() {
            return logRecords;
        }

        public void clear() {
            logRecords.clear();
        }
    }

    @BeforeEach
    void setUp() {
        emailService = new EmailService();
        
        // Configurar destinatarios de prueba
        ReflectionTestUtils.setField(emailService, "notificationRecipients", 
            "compras@petmanager.com,admin@petmanager.com");

        // Configurar captura de logs
        logger = Logger.getLogger(EmailService.class.getName());
        logHandler = new TestLogHandler();
        logger.addHandler(logHandler);
        logger.setLevel(Level.INFO);

        // Configurar usuario de prueba
        Role role = new Role();
        role.setRoleId(1L);
        role.setCode("CUSTOMER");

        testUser = new User();
        testUser.setUserId(UUID.randomUUID());
        testUser.setName("María López");
        testUser.setEmail("maria.lopez@example.com");
        testUser.setPhone("555-1234");
        testUser.setAddress("Calle Principal 789");
        testUser.setRole(role);

        // Configurar productos de prueba
        testProduct1 = new Product();
        testProduct1.setProductId(1L);
        testProduct1.setName("Alimento Premium para Perros 20kg");
        testProduct1.setPriceSale(new BigDecimal("150.00"));
        testProduct1.setStock(new BigDecimal("50"));

        testProduct2 = new Product();
        testProduct2.setProductId(2L);
        testProduct2.setName("Cama para Mascotas Grande");
        testProduct2.setPriceSale(new BigDecimal("75.50"));
        testProduct2.setStock(new BigDecimal("30"));

        // Configurar detalles de venta
        SaleDetails detail1 = new SaleDetails();
        detail1.setSaleDetailId(1L);
        detail1.setProduct(testProduct1);
        detail1.setAmount(new BigDecimal("10"));

        SaleDetails detail2 = new SaleDetails();
        detail2.setSaleDetailId(2L);
        detail2.setProduct(testProduct2);
        detail2.setAmount(new BigDecimal("5"));

        // Configurar venta de prueba
        testSale = new Sale();
        testSale.setSaleId(200L);
        testSale.setUser(testUser);
        testSale.setDate(OffsetDateTime.now());
        testSale.setTotal(new BigDecimal("1877.50")); // (150*10) + (75.50*5)
        testSale.setMethod(Sale.paymentMethod.CARD);
        
        List<SaleDetails> saleDetailsList = new ArrayList<>();
        saleDetailsList.add(detail1);
        saleDetailsList.add(detail2);
        testSale.setSaleDetails(saleDetailsList);
        
        detail1.setSale(testSale);
        detail2.setSale(testSale);
    }

    @AfterEach
    void tearDown() {
        logger.removeHandler(logHandler);
        logHandler.clear();
    }

    @Test
    @DisplayName("Debe enviar notificación de alto volumen con todos los datos")
    void testSendHighVolumeSaleNotification_Complete() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        assertThat(logs).isNotEmpty();

        // Verificar que se registraron los logs esperados
        boolean foundEmailHeader = logs.stream()
            .anyMatch(log -> log.getMessage().contains("EMAIL NOTIFICATION"));
        assertThat(foundEmailHeader).isTrue();

        boolean foundRecipients = logs.stream()
            .anyMatch(log -> log.getMessage().contains("compras@petmanager.com"));
        assertThat(foundRecipients).isTrue();

        boolean foundSubject = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Venta de Alto Volumen") 
                && log.getMessage().contains("ID: 200"));
        assertThat(foundSubject).isTrue();
    }

    @Test
    @DisplayName("Debe incluir información del cliente en el email")
    void testSendHighVolumeSaleNotification_CustomerInfo() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        // Verificar que el cuerpo del email incluye información del cliente
        boolean foundCustomerName = logs.stream()
            .anyMatch(log -> log.getMessage().contains("María López"));
        assertThat(foundCustomerName).isTrue();

        boolean foundCustomerEmail = logs.stream()
            .anyMatch(log -> log.getMessage().contains("maria.lopez@example.com"));
        assertThat(foundCustomerEmail).isTrue();
    }

    @Test
    @DisplayName("Debe incluir detalles de productos en el email")
    void testSendHighVolumeSaleNotification_ProductDetails() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        // Verificar que incluye los productos
        boolean foundProduct1 = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Alimento Premium para Perros 20kg"));
        assertThat(foundProduct1).isTrue();

        boolean foundProduct2 = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Cama para Mascotas Grande"));
        assertThat(foundProduct2).isTrue();
    }

    @Test
    @DisplayName("Debe incluir el total de la venta en el email")
    void testSendHighVolumeSaleNotification_Total() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundTotal = logs.stream()
            .anyMatch(log -> log.getMessage().contains("1877.50"));
        assertThat(foundTotal).isTrue();
    }

    @Test
    @DisplayName("Debe formatear la fecha correctamente")
    void testSendHighVolumeSaleNotification_DateFormatting() {
        // Arrange
        OffsetDateTime specificDate = OffsetDateTime.parse("2024-03-15T14:30:00Z");
        testSale.setDate(specificDate);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String expectedDate = specificDate.format(formatter);

        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundFormattedDate = logs.stream()
            .anyMatch(log -> log.getMessage().contains(expectedDate));
        assertThat(foundFormattedDate).isTrue();
    }

    @Test
    @DisplayName("Debe incluir método de pago en el email")
    void testSendHighVolumeSaleNotification_PaymentMethod() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundPaymentMethod = logs.stream()
            .anyMatch(log -> log.getMessage().contains("CARD"));
        assertThat(foundPaymentMethod).isTrue();
    }

    @Test
    @DisplayName("Debe calcular subtotales correctamente")
    void testSendHighVolumeSaleNotification_Subtotals() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        // Verificar subtotal del producto 1: 150.00 * 10 = 1500.00
        boolean foundSubtotal1 = logs.stream()
            .anyMatch(log -> log.getMessage().contains("1500.00"));
        assertThat(foundSubtotal1).isTrue();

        // Verificar subtotal del producto 2: 75.50 * 5 = 377.50
        boolean foundSubtotal2 = logs.stream()
            .anyMatch(log -> log.getMessage().contains("377.50"));
        assertThat(foundSubtotal2).isTrue();
    }

    @Test
    @DisplayName("Debe manejar venta con un solo producto")
    void testSendHighVolumeSaleNotification_SingleProduct() {
        // Arrange
        Sale singleProductSale = new Sale();
        singleProductSale.setSaleId(201L);
        singleProductSale.setUser(testUser);
        singleProductSale.setDate(OffsetDateTime.now());
        singleProductSale.setTotal(new BigDecimal("1500.00"));
        singleProductSale.setMethod(Sale.paymentMethod.CASH);

        SaleDetails detail = new SaleDetails();
        detail.setSaleDetailId(1L);
        detail.setProduct(testProduct1);
        detail.setAmount(new BigDecimal("10"));
        detail.setSale(singleProductSale);

        List<SaleDetails> details = new ArrayList<>();
        details.add(detail);
        singleProductSale.setSaleDetails(details);

        // Act
        emailService.sendHighVolumeSaleNotification(singleProductSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundProduct = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Alimento Premium para Perros 20kg"));
        assertThat(foundProduct).isTrue();

        boolean foundID = logs.stream()
            .anyMatch(log -> log.getMessage().contains("ID: 201"));
        assertThat(foundID).isTrue();
    }

    @Test
    @DisplayName("Debe incluir todos los separadores visuales en el email")
    void testSendHighVolumeSaleNotification_VisualSeparators() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundSeparators = logs.stream()
            .anyMatch(log -> log.getMessage().contains("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        assertThat(foundSeparators).isTrue();
    }

    @Test
    @DisplayName("Debe enviar SMS placeholder")
    void testSendSmsNotification() {
        // Arrange
        String phoneNumber = "555-9999";
        String message = "Su pedido ha sido procesado";

        // Act
        emailService.sendSmsNotification(phoneNumber, message);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundSmsLog = logs.stream()
            .anyMatch(log -> log.getMessage().contains("SMS to " + phoneNumber) 
                && log.getMessage().contains(message));
        assertThat(foundSmsLog).isTrue();
    }

    @Test
    @DisplayName("Debe usar los destinatarios configurados")
    void testSendHighVolumeSaleNotification_ConfiguredRecipients() {
        // Arrange
        String customRecipients = "ventas@test.com,gerencia@test.com";
        ReflectionTestUtils.setField(emailService, "notificationRecipients", customRecipients);

        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundCustomRecipients = logs.stream()
            .anyMatch(log -> log.getMessage().contains(customRecipients));
        assertThat(foundCustomRecipients).isTrue();
    }

    @Test
    @DisplayName("Debe incluir encabezado y pie de email corporativo")
    void testSendHighVolumeSaleNotification_CorporateFormat() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundGreeting = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Estimado/a Responsable de Compras"));
        assertThat(foundGreeting).isTrue();

        boolean foundSignature = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Sistema PetManager"));
        assertThat(foundSignature).isTrue();

        boolean foundClosing = logs.stream()
            .anyMatch(log -> log.getMessage().contains("Atentamente"));
        assertThat(foundClosing).isTrue();
    }

    @Test
    @DisplayName("Debe incluir explicación del propósito de la notificación")
    void testSendHighVolumeSaleNotification_Purpose() {
        // Act
        emailService.sendHighVolumeSaleNotification(testSale);

        // Assert
        List<LogRecord> logs = logHandler.getLogRecords();
        
        boolean foundPurpose = logs.stream()
            .anyMatch(log -> log.getMessage().contains("umbral establecido") 
                && log.getMessage().contains("inventario y reabastecimiento"));
        assertThat(foundPurpose).isTrue();
    }
}
