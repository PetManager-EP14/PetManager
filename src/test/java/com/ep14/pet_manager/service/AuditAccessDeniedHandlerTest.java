package com.ep14.pet_manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ep14.pet_manager.entity.AccessLog;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.repository.AccessLogRepository;
import com.ep14.pet_manager.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AuditAccessDeniedHandlerTest {

    @Mock
    private AccessLogRepository repo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private AuditAccessDeniedHandler handler;

    private StringWriter responseWriter;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        handler = new AuditAccessDeniedHandler(repo, userRepository);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        // Default request values
        when(request.getRequestURI()).thenReturn("/api/pets");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getAttribute("requiredPermission")).thenReturn("VIEW_PETS");
    }

    // 1️. Usuario autenticado y encontrado
    @Test
    void handle_shouldSaveLogWithUserId_whenUserAuthenticatedAndFound() throws Exception {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        var auth = new UsernamePasswordAuthenticationToken("test@example.com", "pwd");
        SecurityContextHolder.getContext().setAuthentication(auth);

        handler.handle(request, response, new AccessDeniedException("No access"));

        // Capturar log guardado
        ArgumentCaptor<AccessLog> logCaptor = ArgumentCaptor.forClass(AccessLog.class);
        verify(repo).save(logCaptor.capture());

        AccessLog log = logCaptor.getValue();
        assertThat(log.getUserId()).isEqualTo(user.getUserId());
        assertThat(log.getDecision()).isEqualTo("DENIED");
        assertThat(log.getReason()).contains("user=test@example.com");
        assertThat(log.getPath()).isEqualTo("/api/pets");
        assertThat(log.getRequiredPermission()).isEqualTo("VIEW_PETS");

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        assertThat(responseWriter.toString()).contains("403").contains("Insufficient permission");
    }

    // 2️. Usuario autenticado pero NO encontrado
    @Test
    void handle_shouldSaveLogWithoutUserId_whenUserNotFound() throws Exception {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        var auth = new UsernamePasswordAuthenticationToken("missing@example.com", "pwd");
        SecurityContextHolder.getContext().setAuthentication(auth);

        handler.handle(request, response, new AccessDeniedException("Forbidden"));

        ArgumentCaptor<AccessLog> logCaptor = ArgumentCaptor.forClass(AccessLog.class);
        verify(repo).save(logCaptor.capture());

        AccessLog log = logCaptor.getValue();
        assertThat(log.getUserId()).isNull();
        assertThat(log.getReason()).contains("user=missing@example.com :: Forbidden");
        assertThat(responseWriter.toString()).contains("Insufficient permission");
    }

    // 3️. Sin autenticación
    @Test
    void handle_shouldSaveLogWithoutAuth() throws Exception {
        SecurityContextHolder.clearContext(); // no auth

        handler.handle(request, response, new AccessDeniedException("Access denied"));

        ArgumentCaptor<AccessLog> logCaptor = ArgumentCaptor.forClass(AccessLog.class);
        verify(repo).save(logCaptor.capture());

        AccessLog log = logCaptor.getValue();
        assertThat(log.getUserId()).isNull();
        assertThat(log.getReason()).contains("Access denied");
        assertThat(log.getPath()).isEqualTo("/api/pets");
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    // 4️. Maneja nulos y campos opcionales
    @Test
    void handle_shouldHandleNullAttributesGracefully() throws Exception {
        when(request.getAttribute("requiredPermission")).thenReturn(null);
        when(request.getRequestURI()).thenReturn(null);
        when(request.getMethod()).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(null);

        handler.handle(request, response, new AccessDeniedException("Denied test"));

        verify(repo).save(any(AccessLog.class));
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        assertThat(responseWriter.toString()).contains("Insufficient permission");
    }
}

