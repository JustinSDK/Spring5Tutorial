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
    urlPatterns={"/new_message"}, 
    initParams={
        @WebInitParam(name = "MEMBER_PATH", value = "member")
    }
)
@ServletSecurity(
    @HttpConstraint(rolesAllowed = {"member"})
)
public class NewMessage extends HttpServlet {

    protected void doPost(
            HttpServletRequest request, HttpServletResponse response) 
                            throws ServletException, IOException {
            
        request.setCharacterEncoding("UTF-8");
        String blabla = request.getParameter("blabla");
        
        if(blabla == null || blabla.length() == 0) {
            response.sendRedirect(getInitParameter("MEMBER_PATH"));
            return;
        }        
       
        if(blabla.length() <= 140) {
            UserService userService = (UserService) getServletContext().getAttribute("userService");
            userService.addMessage(getUsername(request), blabla);
            response.sendRedirect(getInitParameter("MEMBER_PATH"));
        }
        else {
            request.getRequestDispatcher(getInitParameter("MEMBER_PATH")).forward(request, response);
        }
    }
    
    private String getUsername(HttpServletRequest request) {
        return  (String) request.getSession().getAttribute("login");
    }
    
}
