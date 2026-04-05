package com.vault.auth;

import com.vault.dao.UserDAO;
import com.vault.security.EncryptionUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// This annotation tells the browser to find this code at /register
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get the data from your HTML form
        String name = request.getParameter("username");
        String pass = request.getParameter("password");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        UserDAO dao = new UserDAO();

        // 2. LAYER 1: The Protocol Test (Check for hackers)
        if (!dao.isInputSafe(name) || !dao.isInputSafe(pass)) {
            out.println("<h2 style='color:red;'>SECURITY ALERT: Malicious Input Detected!</h2>");
            out.println("<a href='index.html'>Go Back</a>");
            return;
        }

        // 3. LAYER 2: Scramble the password using AES-256
        String securePass = EncryptionUtil.encrypt(pass);

        // 4. LAYER 3: Save to Oracle Database
        dao.registerUser(name, securePass);

        // 5. LAYER 4: Generate a Capability Token using your existing TokenService
        String token = TokenService.generateToken(name, "USER");

        // 6. SUCCESS: Show the user their "Receipt"
        out.println("<h2>Registration Successful!</h2>");
        out.println("<p>Welcome, " + name + "</p>");
        out.println("<p>Your Secure Session Token: <b>" + token + "</b></p>");
        out.println("<a href='index.html'>Logout</a>");
    }
}