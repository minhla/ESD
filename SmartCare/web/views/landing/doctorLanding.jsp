<%-- 
    Document   : doctorLanding
    Created on : 06-Dec-2020, 09:33:59
    Author     : Michael, Giacomo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Doctor</title>
    </head>
    <body>
        
        <%
            //allow access only if session exists
            String user = (String) session.getAttribute("userEmail");
            String userName = null;
            String sessionID = null;
            Cookie[] cookies = request.getCookies();
            if(cookies !=null)
            {
                for(Cookie cookie : cookies)
                {
                        if(cookie.getName().equals("user")) userName = cookie.getValue();
                        if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
                }
            }
        %>
        
        <h1>Doctor landing page</h1>
        <form action="DoctorServlet.do" name="show" method="Post">
            <input type="submit" name="action" value="list daily appoinments">
        </form>
        <% 
            String appointments = (String) request.getAttribute("appointmentsdata");
            if(appointments!=null){
                out.print(appointments);
            }
            
            out.print(request.getAttribute("status"));
        
          %>
          
        <form action="Logout.do" method="post">
            <input type="submit" value="Logout" >
        </form>
    </body>
</html>
