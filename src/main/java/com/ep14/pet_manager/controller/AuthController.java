package com.ep14.pet_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.Repository.UserRepository;
import com.ep14.pet_manager.Service.JwtService;
import com.ep14.pet_manager.entity.user;
// Add this import if LoginResponse exists in the dto package
import com.ep14.pet_manager.DTO.LoginResponse;
import com.ep14.pet_manager.DTO.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        user u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        LoginResponse resp = new LoginResponse(token, u.getRole().getCode(), u.getName());
        return ResponseEntity.ok(resp);
    
    
}}
