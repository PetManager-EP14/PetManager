package com.ep14.pet_manager.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.lang.NonNull;

import io.jsonwebtoken.JwtException;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private UserDetailsService usersDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.usersDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = req.getHeader("Authorization");
        final String token;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        token = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(token);
        } catch (JwtException ex) {
            chain.doFilter(req, res);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usersDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken auth_token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth_token);
            }
        }
        chain.doFilter(req, res);

    }

}
