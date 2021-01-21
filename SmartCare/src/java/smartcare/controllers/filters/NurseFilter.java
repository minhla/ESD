/*
Class: AdminFilter
Description: Controls user session for nurses
Author: Michael Tonkin.
*/
package smartcare.controllers.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Michael
 */
public class NurseFilter implements Filter
{
    private static final String NURSE_FILTER_ERROR = "nurseFilterError";
    private static final String LOGIN_JSP = "views/login.jsp";
    private ServletContext context;
    
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter Initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Get the current user from the session
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession httpSession = httpRequest.getSession(false);
        
        // Check if the user is an admin
        if (httpSession.getAttribute("userType") != "N") {
            request.setAttribute(NURSE_FILTER_ERROR, "Unauthorised attempt to access nurse page.");
            RequestDispatcher view = request.getRequestDispatcher(LOGIN_JSP);
            view.forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
    @Override
    public void destroy() {
        //close resources here
    }
}