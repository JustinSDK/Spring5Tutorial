package cc.openhome.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.openhome.model.Account;
import cc.openhome.model.EmailService;
import cc.openhome.model.UserService;

@WebServlet(
    urlPatterns={"/register"}, 
    initParams={
        @WebInitParam(name = "SUCCESS_PATH", value = "/WEB-INF/jsp/register_success.jsp"),
        @WebInitParam(name = "FORM_PATH", value = "/WEB-INF/jsp/register.jsp")
    }
)
public class Register extends HttpServlet {
    
    private final Pattern emailRegex = Pattern.compile(
        "^[_a-z0-9-]+([.][_a-z0-9-]+)*@[a-z0-9-]+([.][a-z0-9-]+)*$");

    private final Pattern passwdRegex = Pattern.compile("^\\w{8,16}$");
    
    private final Pattern usernameRegex = Pattern.compile("^\\w{1,16}$");
    
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {
        request.getRequestDispatcher(getInitParameter("FORM_PATH")).forward(request, response);
    }

    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");

        List<String> errors = new ArrayList<>(); 
        if (!validateEmail(email)) {
            errors.add("未填寫郵件或格式不正確");
        }
        if(!validateUsername(username)) {
            errors.add("未填寫使用者名稱或格式不正確");
        }
        if (!validatePassword(password, password2)) {
            errors.add("請確認密碼符合格式並再度確認密碼");
        }
        
        String path;
        if(errors.isEmpty()) {
            path = getInitParameter("SUCCESS_PATH");
            
            UserService userService = (UserService) getServletContext().getAttribute("userService");
            EmailService emailService = (EmailService) getServletContext().getAttribute("emailService");
            Optional<Account> optionalAcct = userService.tryCreateUser(email, username, password);
            if(optionalAcct.isPresent()) {
                emailService.validationLink(optionalAcct.get());
            } else {
                emailService.failedRegistration(username, email);
            }
        } else {
            path = getInitParameter("FORM_PATH");
            request.setAttribute("errors", errors);
        }

        request.getRequestDispatcher(path).forward(request, response);
    }

    private boolean validateEmail(String email) {
        return email != null && emailRegex.matcher(email).find();
    }
    
    private boolean validateUsername(String username) {
        return username != null && usernameRegex.matcher(username).find();
    }

    
    private boolean validatePassword(String password, String password2) {
        return password != null && 
               passwdRegex.matcher(password).find() && 
               password.equals(password2);
    }
}
