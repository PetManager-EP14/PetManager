package com.ep14.pet_manager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Constantes para evitar duplicación de literales (resuelve los avisos de SonarCloud)
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";

    /**
     * Maneja excepciones genéricas, excepto las relacionadas con seguridad.
     * Las de seguridad deben ser manejadas por Spring Security o el
     * AuditAccessDeniedHandler.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        // si es una excepción de seguridad, la dejamos pasar
        if (ex instanceof AccessDeniedException || ex instanceof AuthenticationException) {
            throw (RuntimeException) ex;
        }

        Map<String, Object> error = new HashMap<>();
        error.put(KEY_TIMESTAMP, LocalDateTime.now());
        error.put(KEY_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put(KEY_ERROR, "Internal Server Error");
        error.put(KEY_MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Manejo de errores de argumentos inválidos.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put(KEY_TIMESTAMP, LocalDateTime.now());
        error.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        error.put(KEY_ERROR, "Bad Request");
        error.put(KEY_MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja errores de validación de datos en DTOs (campos faltantes o inválidos).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put(KEY_TIMESTAMP, LocalDateTime.now());
        error.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        error.put(KEY_ERROR, "Validation Error");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        error.put(KEY_MESSAGE, "Datos inválidos o incompletos");
        error.put("details", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja errores de integridad de datos (por ejemplo, null en columnas NOT NULL, claves foráneas, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put(KEY_TIMESTAMP, LocalDateTime.now());
        error.put(KEY_STATUS, HttpStatus.CONFLICT.value());
        error.put(KEY_ERROR, "Data Integrity Violation");

        String message = ex.getMostSpecificCause().getMessage();
        if (message.contains("sale_method_check")) {
            message = "El campo 'method' (método de pago) no puede ser nulo o inválido.";
        } else if (message.contains("NOT NULL")) {
            message = "Faltan campos obligatorios en la solicitud.";
        }

        error.put(KEY_MESSAGE, message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}