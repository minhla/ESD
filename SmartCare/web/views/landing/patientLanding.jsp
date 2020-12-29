<%@page import="smartcare.models.User"%>
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
        <title>SmartCare - Patient</title>
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
            <p>Patient landing page</p>

            <form action="Logout.do" method="post">
                <input type="submit" value="Logout" >
            </form>
        </div>

        <div class="outer-container">
            <div class="book-appointment-container">
                <h2> Welcome, <%out.println(((User)session.getAttribute("user")).getName());%></h2>
                <h1>Book an appointment</h1>
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
                            if (request.getAttribute("updateSuccess") != null) {
                                out.println(request.getAttribute("updateSuccess"));
                            }
                        %>
                    </p>
                </div>
                <div>
                    <%
                        //check if to show doctors appointments
                        boolean showTable = false;
                        if(!((ArrayList)request.getAttribute("appointments")).isEmpty()){
                            showTable = true;
                        }
                    %>
                    
                    <%
                        if(showTable){
                            out.print("<h4>Your appointments:</h4>");
                        }else{
                            out.print("<h4>You haven't got any booked appointments</h4>");
                        }
                    %>
                    
                    <table <% if(!showTable){out.print("hidden='true'");} %> >
                        <tr>
                            <th>Appointment ID</th>
                            <th>Date</th>
                            <th>Start time</th>
                            <th>Comment</th>
                            <th>Action</th>
                        </tr>
                        <%
                            if (showTable) {
                                ArrayList<Appointment> a;
                                a = (ArrayList) request.getAttribute("appointments");
                                
                                //loop through all of the appointments in the array list
                                for (Appointment appointment : a) {
                                    out.print("<form action='PatientServlet.do' name ='deleteAppointment' method='Post'>");
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
                                    out.print("<td>");
                                    out.print("<input type='submit' value='Cancel' name='action'>");
                                    out.print("</td>");
                                    out.print("</tr>");
                                    out.print("</form>");
                                }
                            }
                        %>
                    </table>
                    <%
                        if (request.getAttribute("deleteSuccess") != null) {
                            out.print("<p>" + request.getAttribute("deleteSuccess") + "</p>");
                        }
                    %>
                </div>
            </div>

            <div class="re-issue-container">
                <h1>Request for re-issue prescription: </h1>
                <form action="PatientServlet.do" name="re-issue prescription" method="Post" >
                    Patient ID:<br/> <input type="text" name="patientID" ><br/><br/> 
                    Date of prescription issued:<br/> <input type="date" name="issuedate" ><br/><br/> 
                    reason:<br/> <textarea  type="text" name="reason" rows="4" cols="50"></textarea><br/><br/>
                    <input type="submit" value="request for re-issue" name ="action"><br/>
                </form>
                <p>
                    <%
                        String prescription = (String) session.getAttribute("prescriptionDetail");
                        if (prescription != null) {
                            out.println(prescription);
    
                        }
                    %>
                </p>
            </div>
        </div>

    </body>
    <style>
        <%@ include file="../css/patientLanding.css" %>
    </style>
</html>
