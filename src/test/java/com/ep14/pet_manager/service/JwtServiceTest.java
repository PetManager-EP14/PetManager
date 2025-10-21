package com.ep14.pet_manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Encoders;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    // Helpers para escribir/leer campos privados
    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        String secret = "MySuperSecretKeyForJwtTesting123456789012345678901234567890";
        String encoded = Encoders.BASE64.encode(secret.getBytes());

        // Inyección vía reflection
        setPrivateField(jwtService, "secreBase64", encoded);
        setPrivateField(jwtService, "expirationMs", 60_000L); // 1 minuto
        jwtService.init();

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@test.com");
        GrantedAuthority authority = () -> "ROLE_USER";
        when(userDetails.getAuthorities()).thenAnswer(invocation -> List.of(authority));


    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldExtractUsernameCorrectly() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("user@test.com", username);
    }

    @Test
    void shouldReturnFalseForInvalidUsername() {
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("another@test.com");

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        setPrivateField(jwtService, "expirationMs", -10_000L);
        String expiredToken = jwtService.generateToken(userDetails);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class,
                () -> jwtService.isTokenValid(expiredToken, userDetails));
    }

    @Test
    void shouldGetClaimsSuccessfully() {
        String token = jwtService.generateToken(userDetails);
        Claims claims = jwtService.getClaims(token);

        assertEquals("user@test.com", claims.getSubject());
        assertTrue(claims.containsKey("role"));
    }
}
