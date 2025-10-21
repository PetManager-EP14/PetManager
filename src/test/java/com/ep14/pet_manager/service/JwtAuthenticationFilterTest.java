package com.ep14.pet_manager.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    // 1️⃣ Sin encabezado Authorization -> debe continuar el filtro sin autenticación
    @Test
    void doFilterInternal_shouldContinueFilterWhenNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // 2️⃣ Encabezado mal formado (no inicia con Bearer)
    @Test
    void doFilterInternal_shouldContinueFilterWhenHeaderMalformed() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Token 123456");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // 3️⃣ Token inválido o error al extraer username
    @Test
    void doFilterInternal_shouldContinueFilterWhenTokenInvalid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.extractUsername("invalid.token")).thenThrow(new JwtException("Invalid token"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // 4️⃣ Token válido pero el usuario no autenticado aún
    @Test
    void doFilterInternal_shouldAuthenticateUserWhenTokenValid() throws ServletException, IOException {
        String token = "valid.token";
        String username = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Validar autenticación establecida
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);

        verify(filterChain).doFilter(request, response);
    }

    // 5️⃣ Token válido pero el usuario ya está autenticado
    @Test
    void doFilterInternal_shouldSkipWhenAlreadyAuthenticated() throws ServletException, IOException {
        String token = "valid.token";
        String username = "user@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);

        // Simular que ya hay autenticación
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()));

        jwtFilter.doFilterInternal(request, response, filterChain);

        // No debe volver a autenticar ni tocar jwtService.isTokenValid
        verify(jwtService, never()).isTokenValid(anyString(), any());
        verify(filterChain).doFilter(request, response);
    }

    // 6️⃣ Token válido pero isTokenValid devuelve false
    @Test
    void doFilterInternal_shouldNotAuthenticateWhenTokenInvalidForUser() throws ServletException, IOException {
        String token = "expired.token";
        String username = "user@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        // No se debe establecer autenticación
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
