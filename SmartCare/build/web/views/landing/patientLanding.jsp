 <%-- 
    Document   : patientLanding
    Created on : 06-Dec-2020, 09:33:42
    Author     : Michael, Giacomo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Patient</title>
    </head>
    <body>
        <h1>Patient landing page</h1>
        <h2> Welcome <%out.println((String)session.getAttribute("userEmail"));%></h2>
        <h4>Fill in the form to book the appointment</h4>
        <form action="BookAppointment.do" name ="book" method="Post">
            Start Time: <input type="time" name="starttime"><br/>
            Date: <input type="date" name="date"><br/> <!-- there will be a dropdown here-->
            Reason: <input type="text" name="comment"><br/>
            Doctor:<br/>
            <input type="submit" value="Submit">
        </form>
        <p>
            <% 
                if(request.getAttribute("updateSuccess")!=null){
                    out.println(request.getAttribute("updateSuccess"));
                }
            %>
        </p>
        
    </body>
</html>
