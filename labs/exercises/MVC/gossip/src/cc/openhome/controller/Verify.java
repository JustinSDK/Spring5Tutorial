package cc.openhome.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.openhome.model.Account;
import cc.openhome.model.UserService;

@WebServlet("/verify")
public class Verify extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String token = request.getParameter("token");
	    UserService userService = (UserService) getServletContext().getAttribute("userService");
	    request.setAttribute("acct", userService.verify(email, token));
	    request.getRequestDispatcher("/WEB-INF/jsp/verify.jsp").forward(request, response);
	}
}
