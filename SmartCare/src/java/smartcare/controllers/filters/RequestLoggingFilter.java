/*
Class: RequestLoggingFilter
Description: Logs requests parameters to context log.
Created: 15/12/2020
Author/s: Michael Tonkin.
*/
package smartcare.controllers.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestLoggingFilter implements Filter{

    private ServletContext context;
    
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("RequestLoggingFilter Initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {        
        HttpServletRequest req = (HttpServletRequest) request; //get the http request
        Enumeration<String> params = req.getParameterNames();
        
        while(params.hasMoreElements()) //check each parameter in the request
        {
            String name = params.nextElement();
            String value = request.getParameter(name);
            this.context.log(req.getRemoteAddr() + "::Request Params::{" + name + "=" + value+"}"); //log each parameter in the servlet context
        }
        Cookie[] cookies = req.getCookies();
        if(cookies != null)
        {
            for(Cookie cookie : cookies)
            {
                this.context.log(req.getRemoteAddr() + "::Cookie::{" + cookie.getName() + "," + cookie.getValue() + "}");
            }
            //pass the request algon the filter chain
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        //close resources here
    }
    
}
