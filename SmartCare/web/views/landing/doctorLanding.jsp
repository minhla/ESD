<%-- 
    Document   : doctorLanding
    Created on : 06-Dec-2020, 09:33:59
    Author     : Michael, Giacomo
--%>

<%@page import="smartcare.models.users.User"%>
<%@page import="smartcare.models.Appointment"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Doctor</title>
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

        <h1>Doctor landing page</h1>
        <h2>Welcome Dr. <%out.println(((User)session.getAttribute("user")).getName());%></h2>
        <%
            //check if to show doctors appointments
            boolean showTable = false;
            if(!((ArrayList)request.getAttribute("appointments")).isEmpty()){
                showTable = true;
            }
        %>
        
        <%
            if(showTable){
                out.print("<h4>Your daily booked appointments:</h4>");
            }else{
                out.print("<h4>You have no appointments scheduled for today</h4>");
            }
        %>
        
        <table <% if(!showTable){out.print("hidden='true'");} %> >
            <tr>
                <th>Appointment ID</th>
                <th>Start time</th>
                <th>Comment</th>
                <th>Date</th>
            </tr>
            <%
                if (showTable) {
                    ArrayList<Appointment> a;
                    a = (ArrayList) request.getAttribute("appointments");
                    
                    //loop through all of the appointments in the array list
                    for (Appointment appointment : a) {
                        out.print("<input type='hidden' name='appointmentId' value='" + appointment.getID() + "'");
                        out.print("<tr>");
                        out.print("<td>");
                        out.print(appointment.getID());
                        out.print("</td>");
                        out.print("<td>");
                        out.print(appointment.getDate());
                        out.print("</td>");
                        out.print("<td>");
                        out.print(appointment.getStarttime());
                        out.print("</td>");
                        out.print("<td>");
                        out.print(appointment.getComment());
                        out.print("</td>");
                        out.print("</tr>");
                    }
                }
            %>
        </table>
        

        <div>
            <h1>Prescription Form</h1>
            <form action="DoctorServlet.do" name="prescription" method="Post" >
                Patient ID:<br/> <input type="text" name="patientID" >         
                <input type="submit" value="Get patient detail" name ="action"><br/><br/>
                <p>
                    <%                String patientDetail = (String) session.getAttribute("patientDetail");

                        if (patientDetail != null) {
                            out.println(patientDetail);
                        }

                    %>
                </p>
                Weight:<br/> <input type="text" name="weight"><br/><br/>
                Allergies:<br/> <textarea type="text" name="allergies" rows="4" cols="50"></textarea><br/><br/>
                Medicine:<br/> <textarea  type="text" name="med" rows="4" cols="50"></textarea><br/><br/>
                <input type="submit" value="Create Prescription" name ="action"><br/>
            </form>
            <p>
                <%                String update = (String) session.getAttribute("updateSuccess");
                    if (update != null) {
                        out.println(update);
                    }
                %>
            </p>
        </div>

        <form action="Logout.do" method="post">
            <input type="submit" value="Logout" >
        </form>
    </body>
</html>
