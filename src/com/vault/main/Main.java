package com.vault.main;

import com.vault.security.EncryptionUtil;
import com.vault.dao.UserDAO;
import com.vault.auth.TokenService;

public class Main {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        
        System.out.println("--- STARTING SECURITY PROTOCOL TEST ---");

        // SCENARIO 1: A Valid User (Safe)
        String safeUser = "RASHMIKA";
        String rawPass = "123456";
        
        System.out.println("\n[1] Attempting to register safe user...");
        // Layer 2: Scramble the password before sending
        String securePass = EncryptionUtil.encrypt(rawPass);
        dao.registerUser(safeUser, securePass);
        
        // Layer 4: Issue Capability Token
        TokenService.generateToken(safeUser, "USER");


        // SCENARIO 2: A Hacker (SQL Injection Attempt)
        String hackerInput = "Admin' OR 1=1 --"; 
        
        System.out.println("\n[2] Attempting to register with malicious input...");
        // This will trigger Step 5 (The Sanity Check) inside UserDAO
        dao.registerUser(hackerInput, "fake_pass");

        System.out.println("\n--- TEST COMPLETE: SYSTEM SECURED ---");
    }
}
