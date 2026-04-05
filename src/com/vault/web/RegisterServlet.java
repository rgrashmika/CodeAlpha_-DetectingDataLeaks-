package com.vault.web;

import com.vault.security.EncryptionUtil;
import com.vault.dao.UserDAO;
import com.vault.auth.TokenService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// This tells the browser: "If someone sends data to /register, send it here!"
@WebServlet("/RegisterServlet") 
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 1. Grab the data from the HTML text boxes
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        UserDAO dao = new UserDAO();

        // 2. LAYER 1: Check for SQL Injection (Step 5 Protocol)
        if (dao.isInputSafe(user)) {
            
            // 3. LAYER 2: Scramble the password (AES-256)
            String encryptedPass = EncryptionUtil.encrypt(pass);
            
            // 4. LAYER 3: Save to Oracle using Prepared Statements
            dao.registerUser(user, encryptedPass);
            
            // 5. LAYER 4: Issue the Capability Token
            String token = TokenService.generateToken(user, "STUDENT_USER");

            out.println("<html><body>");
            out.println("<h2 style='color:green;'>Registration Successful!</h2>");
            out.println("<p>Your data is encrypted and saved in the Cloud Vault.</p>");
            out.println("<p><b>Your Capability Token:</b> " + token + "</p>");
            out.println("</body></html>");
            
        } else {
            // If the security protocol detects a threat
            out.println("<html><body>");
            out.println("<h2 style='color:red;'>SECURITY ALERT: Access Denied!</h2>");
            out.println("<p>Malicious characters detected. Connection terminated.</p>");
            out.println("</body></html>");
        }
    }
}
