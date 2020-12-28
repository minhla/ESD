/*
Class: Logout
Description: clears browser session upon the user logging out.
Created: 06/12/2020
Updated: 06/12/2020
Author/s: Michael Tonkin
*/
package smartcare.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Logout", urlPatterns = {"/Logout"})
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	response.setContentType("text/html");
    	Cookie[] cookies = request.getCookies();
        
    	if(cookies != null){
    	for(Cookie cookie : cookies){
    		if(cookie.getName().equals("JSESSIONID")){
    			System.out.println("JSESSIONID="+cookie.getValue());
    			break;
    		}
    	}
    	}
    	//invalidate the session if one exists
    	HttpSession session = request.getSession(false);
    	System.out.println("User="+session.getAttribute("user"));
    	if(session != null){
    		session.invalidate();
    	}
    	response.sendRedirect("views/login.jsp");
    }

}
