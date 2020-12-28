<%-- 
    Document   : nurseLanding
    Created on : 06-Dec-2020, 09:34:16
    Author     : Michael
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SmartCare - Nurse</title>
        <link rel="icon" 
              type="image/png" 
              href="https://i.imgur.com/Vwma7mV.png">
    </head>
    <body>

        <%
            //allow access only if session exists
            String user = (String) session.getAttribute("userEmail");
            String userName = null;
            String sessionID = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) {
                        userName = cookie.getValue();
                    }
                    if (cookie.getName().equals("JSESSIONID")) {
                        sessionID = cookie.getValue();
                    }
                }
            }
        %>
        <div class="top-bar">
            <p>Nurse Dashboard</p>

            <div class="logout-button">
                <form action="Logout.do" method="post" class="logout-form">
                    <input type="submit" value="Logout" >
                </form>  
            </div>
        </div>

        <div class="outer-container">
            <div class="appointment-container">
                <h1>See appointments today</h1>
                <form action="NurseServlet.do" name="show" method="Post">
                    <input type="submit" name="action" value="List daily appoinments">
                </form>
                <div class="appointment-list">
                    <%
                    String appointments = (String) request.getAttribute("appointmentsdata");
                    if (appointments != null) {
                        out.print(appointments);
                    }
        
                    out.print(request.getAttribute("status"));
        
                    %>
                </div>
            </div>
        </div>
            
    </body>
    <style>
        <%@ include file="../css/nurseLanding.css" %>
    </style>
</html>
