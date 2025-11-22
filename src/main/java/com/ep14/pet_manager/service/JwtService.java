package com.ep14.pet_manager.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secreBase64;

    @Value("${jwt.expirationMs}")
    private Long expirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secreBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Extraer TODAS las autoridades/permisos**
        List<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        // **1. Inyectar la lista de permisos bajo el CLAIM 'authorities'**
        extraClaims.put("authorities", authorities);

        // 2. Inyectar el rol principal (mantener 'role' por si acaso)
        userDetails.getAuthorities().stream()
            .filter(a -> a.getAuthority().startsWith("ROLE_"))
            .findFirst()
            .ifPresent(authority -> extraClaims.put("role", authority.getAuthority()));

        // Construir el token 
        long now = System.currentTimeMillis();
        
        return Jwts.builder()
            .setClaims(extraClaims) // Usa el mapa que ahora contiene 'authorities'
            .setSubject(userDetails.getUsername()) // [11]
            .setIssuedAt(new Date(now)) // [11]
            .setExpiration(new Date(now + expirationMs)) // [11]
            .signWith(key, SignatureAlgorithm.HS256) // [12]
            .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !getClaims(token).getExpiration().before(new Date());
    }

}
