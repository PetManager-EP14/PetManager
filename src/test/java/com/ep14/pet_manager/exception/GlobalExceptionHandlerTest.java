package com.ep14.pet_manager.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // 1️. IllegalArgumentException → BAD_REQUEST
    @SuppressWarnings("null")
    @Test
    void handleIllegalArgument_shouldReturnBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Campo inválido");
        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgument(ex);

        assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().get("message")).isEqualTo("Campo inválido");
        assertThat(response.getBody().get("error")).isEqualTo("Bad Request");
    }

    // 2️. MethodArgumentNotValidException → BAD_REQUEST con detalles de campos
    @SuppressWarnings("null")
    @Test
    void handleValidationErrors_shouldReturnFieldDetails() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "dto");
        bindingResult.addError(new FieldError("dto", "nombre", "no puede ser vacío"));
        bindingResult.addError(new FieldError("dto", "edad", "debe ser mayor que 0"));

        MethodParameter parameter = new MethodParameter(this.getClass().getDeclaredMethods()[0], -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().get("error")).isEqualTo("Validation Error");
        @SuppressWarnings({ "unchecked", "null" })
        Map<String, String> details = (Map<String, String>) response.getBody().get("details");

        assertThat(details)
                .isNotNull()
                .containsKeys("nombre", "edad");
    }

    // 3️. DataIntegrityViolationException → CONFLICT con mensaje de "NOT NULL"
    @SuppressWarnings("null")
    @Test
    void handleDataIntegrity_shouldReturnConflict_forNotNullMessage() {
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("error", new Throwable("NOT NULL"));

        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrity(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().get("error")).isEqualTo("Data Integrity Violation");
        assertThat(response.getBody().get("message"))
                .isEqualTo("Faltan campos obligatorios en la solicitud.");
    }

    // 4️. DataIntegrityViolationException → CONFLICT con mensaje de "sale_method_check"
    @SuppressWarnings("null")
    @Test
    void handleDataIntegrity_shouldReturnConflict_forSaleMethodCheck() {
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("error", new Throwable("sale_method_check"));

        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrity(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().get("message"))
                .isEqualTo("El campo 'method' (método de pago) no puede ser nulo o inválido.");
    }

    // 5️. Generic Exception → INTERNAL_SERVER_ERROR
    @SuppressWarnings("null")
    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception ex = new Exception("Fallo interno");
        ResponseEntity<Map<String, Object>> response = handler.handleException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().get("error")).isEqualTo("Internal Server Error");
        assertThat(response.getBody().get("message")).isEqualTo("Fallo interno");
    }

    // 6️. AccessDeniedException → debe relanzarse
    @Test
    void handleException_shouldRethrowAccessDenied() {
        AccessDeniedException ex = new AccessDeniedException("Acceso denegado");
        assertThrows(RuntimeException.class, () -> handler.handleException(ex));
    }

    // 7️. AuthenticationException → debe relanzarse
    @Test
    void handleException_shouldRethrowAuthenticationException() {
        AuthenticationException ex = new AuthenticationException("Error de autenticación") {};
        assertThrows(RuntimeException.class, () -> handler.handleException(ex));
    }
}
