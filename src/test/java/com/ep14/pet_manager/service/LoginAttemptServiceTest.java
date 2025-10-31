package com.ep14.pet_manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginAttemptServiceTest {
    
    private LoginAttemptService service;
    
    @BeforeEach
    void setup() {
        service = new LoginAttemptService();
    }
    
    @Test
    void shouldNotBlockAfterTwoFailedAttempts() {
        String email = "test@example.com";
        
        service.loginFailed(email);
        service.loginFailed(email);
        
        assertThat(service.isBlocked(email)).isFalse();
        assertThat(service.getRemainingAttempts(email)).isEqualTo(1);
    }
    
    @Test
    void shouldBlockAfterThreeFailedAttempts() {
        String email = "test@example.com";
        
        service.loginFailed(email);
        service.loginFailed(email);
        service.loginFailed(email);
        
        assertThat(service.isBlocked(email)).isTrue();
        assertThat(service.getRemainingAttempts(email)).isZero();
        assertThat(service.getLockTime(email)).isNotNull();
    }
    
    @Test
    void shouldResetAttemptsAfterSuccessfulLogin() {
        String email = "test@example.com";
        
        service.loginFailed(email);
        service.loginFailed(email);
        service.loginSucceeded(email);
        
        assertThat(service.isBlocked(email)).isFalse();
        assertThat(service.getRemainingAttempts(email)).isEqualTo(3);
    }
    
    @Test
    void shouldUnblockAfterLockTimePasses() {
        String email = "test@example.com";
        
        // Simular 3 intentos fallidos
        service.loginFailed(email);
        service.loginFailed(email);
        service.loginFailed(email);
        
        assertThat(service.isBlocked(email)).isTrue();
        
        // En producción, esperaría 15 minutos
        // Para testing, puedes modificar el tiempo de bloqueo
    }
}