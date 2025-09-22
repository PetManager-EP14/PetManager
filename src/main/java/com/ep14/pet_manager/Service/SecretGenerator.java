package com.ep14.pet_manager.Service;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretGenerator {

    public static void main(String[] args) {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        System.err.println(Base64.getEncoder().encodeToString(key));
    }
    
}
