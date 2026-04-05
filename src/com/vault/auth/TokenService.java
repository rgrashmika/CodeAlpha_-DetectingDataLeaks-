package com.vault.auth;

import java.util.UUID;

public class TokenService {
    // Generates a unique 'Capability Code' after login
    public static String generateToken(String username, String role) {
        // In a real cloud app, this would be a JWT. 
        // For now, we create a unique ID that represents their 'Capability'.
        String token = UUID.randomUUID().toString();
        System.out.println("Issued Capability Token for " + username + " (Role: " + role + ")");
        return token;
    }
}
