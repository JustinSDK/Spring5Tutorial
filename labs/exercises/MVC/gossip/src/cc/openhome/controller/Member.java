package cc.openhome.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.openhome.model.Message;
import cc.openhome.model.UserService;

@WebServlet(
    urlPatterns={"/member"}, 
    initParams={
        @WebInitParam(name = "MEMBER_PATH", value = "/WEB-INF/jsp/member.jsp")
    }
)
@ServletSecurity(
    @HttpConstraint(rolesAllowed = {"member"})
)
public class Member extends HttpServlet {
    
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException {
        processRequest(request, response);
    }
   
    protected void processRequest(
                HttpServletRequest request, HttpServletResponse response) 
                        throws ServletException, IOException {

        UserService userService = (UserService) getServletContext().getAttribute("userService");
        List<Message> messages = userService.messages(getUsername(request));
        
        request.setAttribute("messages", messages);
        request.getRequestDispatcher(getInitParameter("MEMBER_PATH")).forward(request, response);
    }

    private String getUsername(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("login");
    }
}
