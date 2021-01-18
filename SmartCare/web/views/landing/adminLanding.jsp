<%-- 
    Document   : adminLanding
    Created on : 06-Dec-2020, 09:29:32
    Author     : Michael
--%>

<%@page import="smartcare.models.Appointment"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SmartCare - Admin</title>
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
                System.out.println(userName);
            }
        %>
        <div class="top-bar">

            <p>Admin Dashboard</p>

            <div class ="logout-button">
                <form action="Logout.do" method="post" class="logout-form">
                    <input type="submit" value="Log out" /> 
                </form>
            </div>

        </div>


        <div class="outer-container">

            <div class="invoice-container">
                <h1>Invoice Form</h1>
                <form action="AdminServlet.do" name="invoice" method="Post" >
                    Patient Username:<br/> <input type="text" name="patientID" placeholder="Ex. j-doe" required>  <br/><br/>       
                    
                    Type of service<br/> 
                    <select id="services" name="services" required>
                        <option value="" disabled="disabled" selected="selected">Please select</option>
                        <option value="surgery">Surgery</option>
                        <option value="consultation">Consultation</option>
                    </select>
                    <br/><br/>
                    Details: <br/> <textarea type="text" name="detail" rows="4" cols="50" required></textarea><br/><br/>
                    Total amount:<br/> <input type="number" name="amount" required><br/><br/> 
                    Payment type:<br/>             
                    <select id="paymenttype" name="paymenttype" required>
                        <option value="" selected="selected" disabled="disabled">Please select</option>
                        <option value="NHS">NHS</option>
                        <option value="Private">Private</option>
                    </select>
                    <br/><br/> 
                    <input type="submit" value="Issue Invoice" name ="action"><br/>
                </form>
                <p>
                    <%                      //show result of sending invoice
                        String update = (String) session.getAttribute("updateSuccess");
                        if (update != null) {
                            out.println(update);
                        }
                    %>
                </p>

                <div class="appointment-container">
                    <%
                        //check if there are appointments
                        boolean showTable = false;
                        if (!((ArrayList) request.getAttribute("appointments")).isEmpty()) {
                            showTable = true;
                        }
                        if (showTable) {
                            out.print("<h4>All booked appointments:</h4>");
                        } else {
                            out.print("<h4>There aren't any booked appointments</h4>");
                        }
                    %>

                    <table <% if (!showTable) {
                            out.print("hidden='true'");
                        } %> >
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
                                    out.print("<form action='AdminServlet.do' name ='deleteAppointment' method='Post'>");
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
                                    out.print("<input type='submit' value='Remove' name='action'>");
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



            <div class="document-form">
                <h1>Document Management</h1>
                <form action="AdminServlet.do" name="document management" method="Post" >
                    Start date:<br/> <input type="date" name="startDate" required><br/><br/> 
                    End date:<br/> <input type="date" name="endDate" required><br/><br/> 
                    <input type="submit" value="Produce Weekly Documents" name ="action"><br/>
                </form>
                <br/>
                <%
                    //show result of sending invoice
                    String turnover = (String) session.getAttribute("turnover");
                    if (turnover != null) {
                        out.println(turnover);
                    }
                %>
                <br/>
                
                <h1>Find details of a patient</h1>
                <form action="AdminServlet.do" name="invoice" method="Post" >
                Patient Username:<br/> <input type="text" name="patientID" placeholder="Ex. j-doe" required>    
                <input type="submit" value="Get patient details" name="action"><br/><br/>
                    <p>
                        <%
                            //show patient details
                            String patientDetail = (String) session.getAttribute("patientDetail");

                            if (patientDetail != null) {
                                out.println(patientDetail);
                            }

                        %>
                    </p>
                </form>
            </div>

            <div class="register-container">
                <h1>Register New Staff Account</h1>
                <form action="RegisterStaff" method="post">
                    <p>Email: <input type="text" name="new_acc_email" placeholder="johndoe@gmail.com"></p>
                    <p>First name: <input type="text" name="new_acc_firstname" placeholder="John"></p>
                    <p>Last name: <input type="text" name="new_acc_lastname" placeholder="Doe"></p>
                    <p>User type: <input type="radio" name="new_acc_type" value="D">
                        <label for="doctor">Doctor</label>
                        <input type="radio" name="new_acc_type" value="N">
                        <label for="nurse">Nurse</label></p>
                    <p>Date of birth: <input type="date" name="new_acc_dob"></p>
                    <p>Phone number: <input type="text" name="new_acc_phone" placeholder="Maximum 11 numbers"></p>
                    <p>Address: <input type="text" name="new_acc_address" placeholder="Somewhere on Earth..."></p>
                    <input type="submit" value="Submit">
                </form>
                <%
                    String error = (String) request.getAttribute("updateSuccess");
                    if (error != null) {
                        out.println(error);
                    }
                %>

            </div>

        </div>

    </body>
    <style>
        <%@ include file="../css/adminLanding.css" %>
    </style>
</html>
