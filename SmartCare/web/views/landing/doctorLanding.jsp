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

        <div class="top-bar">
            <p>Doctor landing page</p>
            <form action="Logout.do" method="post">
                <input type="submit" value="Logout" >
            </form>
        </div>

        <div class="outer-container">

            <div class="appointment-container">
                <h2>Welcome Dr. <%out.println(((User)session.getAttribute("user")).getName());%></h2>
                <%
                    //check if to show doctors appointments
                    boolean showTable = false;
                    if (!((ArrayList) request.getAttribute("appointments")).isEmpty()) {
                        showTable = true;
                    }
                %>

                <%
                    if (showTable) {
                        out.print("<h4>Your daily booked appointments:</h4>");
                    } else {
                        out.print("<h4>You have no appointments scheduled for today</h4>");
                    }
                %>

                <table <% if (!showTable) {
                        out.print("hidden='true'");
                    } %> >
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
            </div>


            <div class="prescription-container">
                <h1>Find patient details</h1>
                <form action="DoctorServlet.do" name="prescription" method="Post" >
                    Patient username:<br/> <input type="text" name="patientID" >         
                    <input type="submit" value="Get patient detail" name ="action"><br/><br/>
                    <p>
                        <%                String patientDetail = (String) session.getAttribute("patientDetail");

                            if (patientDetail != null) {
                                out.println(patientDetail);
                            }

                        %>
                    </p>
                </form>
                <h1>Prescription Form</h1>
                <form action="DoctorServlet.do" name="prescription" method="Post" >
                    Patient username:<br/> <input type="text" name="patientID" >  <br/><br/>      
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

            <div class="invoice-container">
                <h1>Invoice Form</h1>
                <form action="DoctorServlet.do" name="invoice" method="Post">
                    Appointment ID: <input type="text" name="appointmentID" pattern="\d*" maxlength="4" placeholder="123"> <br/><br/>
                    Type of service: <select id="services" name="services" required>
                        <option value="" disabled="disabled" selected="selected">Please select</option>
                        <option value="surgery">Surgery</option>
                        <option value="consultation">Consultation</option>
                    </select>
                    <br/><br/>
                    Details: <textarea type="text" name="detail" rows="4" cols="50" required></textarea><br/><br/>
                    Total amount: <input type="number" name="amount" required><br/><br/> 
                    Payment type:            
                    <select id="paymenttype" name="paymenttype" required>
                        <option value="" selected="selected" disabled="disabled">Please select</option>
                        <option value="NHS">NHS</option>
                        <option value="Private">Private</option>
                    </select>
                    <br/><br/> 
                    <input type="submit" value="Issue Invoice" name ="action"><br/>
                </form>
                <p>
                    <%                String updateInvoice = (String) session.getAttribute("updateInvoice");
                        if (updateInvoice != null) {
                            out.println(updateInvoice);
                        }
                    %>
                </p>
            </div>

            

        </div>
    </body>
    <style>
        <%@ include file="../css/doctorLanding.css" %>
    </style>
</html>
