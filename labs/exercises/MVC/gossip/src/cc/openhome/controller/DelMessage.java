package cc.openhome.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.openhome.model.UserService;

@WebServlet(
    urlPatterns={"/del_message"}, 
    initParams={
        @WebInitParam(name = "MEMBER_PATH", value = "member")
    }
)
@ServletSecurity(
    @HttpConstraint(rolesAllowed = {"member"})
)
public class DelMessage extends HttpServlet {    
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String millis = request.getParameter("millis");
        
        if(millis != null) {
            UserService userService = (UserService) getServletContext().getAttribute("userService");
            userService.deleteMessage(getUsername(request), millis);
        }
        
        response.sendRedirect(getInitParameter("MEMBER_PATH"));
    }
    
    private String getUsername(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("login");
    }
}
