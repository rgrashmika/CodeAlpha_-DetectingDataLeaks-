package com.vault.dao;

import com.vault.database.DbConnection;
import java.sql.*;

public class UserDAO {

    // --- STEP 5: THE SECURITY PROTOCOL LAYER ---
    public boolean isInputSafe(String input) {
        // Red flags that hackers use in SQL Injection
        String[] redFlags = {";", "--", "DROP", "UNION", "SELECT", "UPDATE", "DELETE", "'"};
        
        if (input == null) return false;

        for (String flag : redFlags) {
            if (input.toUpperCase().contains(flag)) {
                System.out.println("ALERT: Malicious character '" + flag + "' detected!");
                return false; // STOP: Layer 1 of the Protocol triggered
            }
        }
        return true; // SAFE: Proceed to Layer 2
    }

    // --- LAYER 2: THE PREPARED STATEMENT SHIELD ---
    public void registerUser(String name, String scrambledPass) {
        // Double-check with our Step 5 Protocol first
        if (!isInputSafe(name)) {
            System.out.println("Registration blocked: Input is not secure.");
            return;
        }

        String sql = "INSERT INTO secure_vault_users (username, password) VALUES (?, ?)";        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, scrambledPass);
            pstmt.executeUpdate();
            System.out.println("User registered safely in the Cloud Vault!");
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}