package cc.openhome.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.openhome.model.Account;
import cc.openhome.model.EmailService;
import cc.openhome.model.UserService;

@WebServlet("/forgot")
public class Forgot extends HttpServlet {
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        UserService userService =
                (UserService) getServletContext().getAttribute("userService");
        Optional<Account> optionalAcct = userService.accountByNameEmail(name, email);
        
        if(optionalAcct.isPresent()) {
            EmailService emailService = 
                    (EmailService) getServletContext().getAttribute("emailService");
            emailService.passwordResetLink(optionalAcct.get());
        }
        
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/jsp/forgot.jsp")
               .forward(request, response);
    }

}
