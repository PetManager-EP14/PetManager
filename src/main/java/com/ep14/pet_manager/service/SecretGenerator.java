package com.ep14.pet_manager.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

public class SecretGenerator {

    private static final Logger logger = Logger.getLogger(SecretGenerator.class.getName());

    public static void main(String[] args) {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        // Solo loguear si el nivel INFO está habilitado
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(encodedKey);
        }
    }
    
}
