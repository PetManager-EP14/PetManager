package com.ep14.pet_manager.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.dto.ErrorResponse;
import com.ep14.pet_manager.dto.LoginRequest;
import com.ep14.pet_manager.dto.LoginResponse;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.repository.UserRepository;
import com.ep14.pet_manager.service.JwtService;
import com.ep14.pet_manager.service.LoginAttemptService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService, 
            UserRepository userRepository,
            LoginAttemptService loginAttemptService
        ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        
        // Verificar si la cuenta está bloqueada
        if (loginAttemptService.isBlocked(email)) {
            LocalDateTime lockTime = loginAttemptService.getLockTime(email);
            String message = String.format(
                "Cuenta bloqueada por múltiples intentos fallidos. Intente nuevamente después de: %s",
                lockTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(new ErrorResponse(message, "ACCOUNT_LOCKED"));
        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            
            // Login exitoso - resetear intentos
            loginAttemptService.loginSucceeded(email);
            
            LoginResponse resp = new LoginResponse(token, u.getRole().getCode(), u.getName());
            return ResponseEntity.ok(resp);
            
        } catch (BadCredentialsException e) {
            // Login fallido - incrementar intentos
            loginAttemptService.loginFailed(email);
            
            int remainingAttempts = loginAttemptService.getRemainingAttempts(email);
            
            if (remainingAttempts > 0) {
                String message = String.format(
                    "Credenciales inválidas. Le quedan %d intentos antes del bloqueo.",
                    remainingAttempts
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(message, "INVALID_CREDENTIALS"));
            } else {
                LocalDateTime lockTime = loginAttemptService.getLockTime(email);
                String message = String.format(
                    "Cuenta bloqueada por múltiples intentos fallidos. Intente nuevamente después de: %s",
                    lockTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                );
                return ResponseEntity.status(HttpStatus.LOCKED)
                        .body(new ErrorResponse(message, "ACCOUNT_LOCKED"));
            }
        }
    }
}