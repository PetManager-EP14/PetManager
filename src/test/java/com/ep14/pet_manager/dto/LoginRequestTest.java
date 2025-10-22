package com.ep14.pet_manager.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        LoginRequest request = new LoginRequest("user@example.com", "securePass123");

        assertThat(request.getEmail()).isEqualTo("user@example.com");
        assertThat(request.getPassword()).isEqualTo("securePass123");
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LoginRequest request = new LoginRequest();
        request.setEmail("brayan@udea.edu.co");
        request.setPassword("12345abc");

        assertThat(request.getEmail()).isEqualTo("brayan@udea.edu.co");
        assertThat(request.getPassword()).isEqualTo("12345abc");
    }
}