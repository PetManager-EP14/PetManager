package com.ep14.pet_manager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.ep14.pet_manager.dto.ErrorResponse;
import com.ep14.pet_manager.dto.LoginRequest;
import com.ep14.pet_manager.dto.LoginResponse;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.repository.UserRepository;
import com.ep14.pet_manager.service.JwtService;
import com.ep14.pet_manager.service.LoginAttemptService;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest validLoginRequest;
    private User testUser;
    private Role userRole;

    @BeforeEach
    void setup() {
        validLoginRequest = new LoginRequest("test@example.com", "password123");
        
        userRole = new Role();
        userRole.setCode("USER");
        
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEmail("test@example.com");
        testUser.setRole(userRole);
    }

    @SuppressWarnings("null")
    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid() {
        // Given
        when(loginAttemptService.isBlocked(validLoginRequest.getEmail())).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(validLoginRequest.getEmail());
        when(jwtService.generateToken(userDetails)).thenReturn("fake-jwt-token");
        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.of(testUser));

        // When
        ResponseEntity<?> response = authController.login(validLoginRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(LoginResponse.class);
        
        LoginResponse loginResponse = (LoginResponse) response.getBody();
        assertThat(loginResponse.getToken()).isEqualTo("fake-jwt-token");
        assertThat(loginResponse.getRole()).isEqualTo("USER");
        assertThat(loginResponse.getName()).isEqualTo("John Doe");

        verify(loginAttemptService).loginSucceeded(validLoginRequest.getEmail());
        
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor
                .forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        
        UsernamePasswordAuthenticationToken authToken = captor.getValue();
        assertThat(authToken.getPrincipal()).isEqualTo(validLoginRequest.getEmail());
        assertThat(authToken.getCredentials()).isEqualTo(validLoginRequest.getPassword());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        // Given
        when(loginAttemptService.isBlocked(validLoginRequest.getEmail())).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(validLoginRequest.getEmail());
        when(jwtService.generateToken(userDetails)).thenReturn("fake-token");
        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authController.login(validLoginRequest))
                .isInstanceOf(RuntimeException.class);
        
        verify(loginAttemptService, never()).loginSucceeded(any());
    }

    @Test
    void login_shouldInvokeExpectedMethods_whenLoginIsSuccessful() {
        // Given
        when(loginAttemptService.isBlocked(validLoginRequest.getEmail())).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(validLoginRequest.getEmail());
        when(jwtService.generateToken(userDetails)).thenReturn("token");
        when(userRepository.findByEmail(validLoginRequest.getEmail()))
                .thenReturn(Optional.of(testUser));

        // When
        authController.login(validLoginRequest);

        // Then
        verify(loginAttemptService).isBlocked(validLoginRequest.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
        verify(userRepository).findByEmail(validLoginRequest.getEmail());
        verify(loginAttemptService).loginSucceeded(validLoginRequest.getEmail());
    }

    @SuppressWarnings("null")
    @Test
    void login_shouldReturnLockedStatus_whenAccountIsBlocked() {
        // Given
        String email = "blocked@example.com";
        LoginRequest request = new LoginRequest(email, "password");
        LocalDateTime lockTime = LocalDateTime.now().plusMinutes(15);

        when(loginAttemptService.isBlocked(email)).thenReturn(true);
        when(loginAttemptService.getLockTime(email)).thenReturn(lockTime);

        // When
        ResponseEntity<?> response = authController.login(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);

        ErrorResponse error = (ErrorResponse) response.getBody();
        assertThat(error.getErrorCode()).isEqualTo("ACCOUNT_LOCKED");
        assertThat(error.getMessage()).contains("Account locked");

        verify(authenticationManager, never()).authenticate(any());
        verify(loginAttemptService, never()).loginFailed(any());
        verify(loginAttemptService, never()).loginSucceeded(any());
    }

    @SuppressWarnings("null")
    @Test
    void login_shouldIncrementAttempts_whenCredentialsAreInvalid() {
        // Given
        String email = "user@example.com";
        LoginRequest request = new LoginRequest(email, "wrongpassword");

        when(loginAttemptService.isBlocked(email)).thenReturn(false);
        when(loginAttemptService.getRemainingAttempts(email)).thenReturn(2);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When
        ResponseEntity<?> response = authController.login(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
        
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertThat(error.getErrorCode()).isEqualTo("INVALID_CREDENTIALS");
        
        verify(loginAttemptService).loginFailed(email);
        verify(loginAttemptService, never()).loginSucceeded(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_shouldResetAttempts_whenLoginIsSuccessful() {
        // Given
        when(loginAttemptService.isBlocked(validLoginRequest.getEmail())).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(validLoginRequest.getEmail());
        when(jwtService.generateToken(userDetails)).thenReturn("fake-jwt-token");
        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.of(testUser));

        // When
        authController.login(validLoginRequest);

        // Then
        verify(loginAttemptService).loginSucceeded(validLoginRequest.getEmail());
        verify(loginAttemptService, never()).loginFailed(any());
    }

    @Test
    void login_shouldReturnErrorResponse_whenAuthenticationFails() {
        // Given
        when(loginAttemptService.isBlocked(validLoginRequest.getEmail())).thenReturn(false);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When
        ResponseEntity<?> response = authController.login(validLoginRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
        verify(loginAttemptService).loginFailed(validLoginRequest.getEmail());
    }
}