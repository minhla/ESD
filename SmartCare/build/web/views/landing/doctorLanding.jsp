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
        <h1>Doctor landing page</h1>
        <form action="ShowAppointments.do" name="show" method="Post">
            <input type="submit" value="list daily appoinments">
        </form>
        <% 
            String appointments = (String) request.getAttribute("appointmentsdata");
            if(appointments!=null){
                out.print(appointments);
            }
        
          %>
    </body>
</html>
