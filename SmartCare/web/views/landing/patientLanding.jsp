 <%@page import="smartcare.models.Appointment"%>
<%@page import="java.util.ArrayList"%>
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

        <h1>Patient landing page</h1>
        <h2> Welcome <%out.println((String)session.getAttribute("userEmail"));%></h2>
        <div name="left">
            <h4>Fill in the form to book the appointment</h4>
            <form action="PatientServlet.do" name ="bookAppointment" method="Post">
                Start Time: <input type="time" name="starttime"><br/>
                Date: <input type="date" name="date"><br/> <!-- there will be a dropdown here-->
                Reason: <input type="text" name="comment"><br/>
                Doctor:<br/>
                <input type="submit" value="Book Appointment" name="action">
            </form>
            <p>
                <%
                    if(request.getAttribute("updateSuccess")!=null){
                        out.println(request.getAttribute("updateSuccess"));
                    }
                %>
            </p>
        </div>
        <div>
            <h4> Your booked appointments: </h4>
            <%
                if(request.getAttribute("Appointments")!=null){
                    out.println("there are some appointments");
                    ArrayList<Appointment> a = new ArrayList<Appointment>();
                    a = (ArrayList)request.getAttribute("Appointments");

                    out.println(a.get(0).getComment());
                }
            %>
        </div>

        <form action="Logout.do" method="post">
            <input type="submit" value="Logout" >
        </form>

    </body>
</html>
