package com.ep14.pet_manager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.ep14.pet_manager.dto.LoginRequest;
import com.ep14.pet_manager.dto.LoginResponse;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.repository.UserRepository;
import com.ep14.pet_manager.service.JwtService;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, jwtService, userRepository);
    }

    // 1. Login exitoso: autentica, genera token y devuelve LoginResponse
    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("1234");

        // Configurar mocks de autenticación
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Simular token generado
        when(jwtService.generateToken(userDetails)).thenReturn("fake-jwt-token");

        // Simular usuario en base de datos
        Role role = new Role();
        role.setCode("ADMIN");
        User user = new User();
        user.setName("John Doe");
        user.setRole(role);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Ejecutar
        ResponseEntity<LoginResponse> response = authController.login(request);

        // Verificar respuesta
        assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getBody().getRole()).isEqualTo("ADMIN");
        assertThat(response.getBody().getName()).isEqualTo("John Doe");

        // Capturar autenticación usada
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        UsernamePasswordAuthenticationToken authToken = captor.getValue();
        assertThat(authToken.getPrincipal()).isEqualTo("test@example.com");
        assertThat(authToken.getCredentials()).isEqualTo("1234");
    }

    // 2. Usuario no encontrado: el repositorio lanza excepción
    @Test
    void login_shouldThrowException_whenUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("missing@example.com");
        request.setPassword("pwd");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("missing@example.com");

        when(jwtService.generateToken(userDetails)).thenReturn("fake-token");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authController.login(request))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    // 3. Verifica que se llamen los métodos correctos en el flujo de login
    @Test
    void login_shouldInvokeExpectedMethods() {
        LoginRequest request = new LoginRequest();
        request.setEmail("invocations@test.com");
        request.setPassword("secret");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("invocations@test.com");
        when(jwtService.generateToken(userDetails)).thenReturn("token");

        Role role = new Role();
        role.setCode("USER");

        User user = new User();
        user.setName("Any Name");
        user.setRole(role);

        when(userRepository.findByEmail("invocations@test.com"))
                .thenReturn(Optional.of(user));

        authController.login(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
        verify(userRepository).findByEmail("invocations@test.com");
    }
}
