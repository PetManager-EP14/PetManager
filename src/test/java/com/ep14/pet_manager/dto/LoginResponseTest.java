package com.ep14.pet_manager.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        LoginResponse response = new LoginResponse();

        response.setToken("abc123");
        response.setRole("ADMIN");
        response.setName("Brayan");

        assertThat(response.getToken()).isEqualTo("abc123");
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertThat(response.getName()).isEqualTo("Brayan");
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        LoginResponse response = new LoginResponse("jwtToken123", "USER", "Geraldine");

        assertThat(response.getToken()).isEqualTo("jwtToken123");
        assertThat(response.getRole()).isEqualTo("USER");
        assertThat(response.getName()).isEqualTo("Geraldine");
    }
}
