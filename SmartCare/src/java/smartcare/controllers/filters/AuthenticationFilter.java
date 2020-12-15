/*
Class: AuthenticationFilter
Description: 
*/

package smartcare.controllers.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter
{
    
    private ServletContext context;

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter Initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        this.context.log("Requested Resouces::" + uri);
        
        HttpSession session = req.getSession(false);
        
        if(session == null & !(uri.endsWith("html") || uri.endsWith("Login.do")))
        {
            this.context.log("Unauthorized access request");
            res.sendRedirect("login.jsp");
        }
        else
        {
            //pass the request along to the filter chain
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        //close resources here
    }
    
    
    
}
