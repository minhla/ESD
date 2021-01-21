<%-- 
    Document   : adminLanding
    Created on : 06-Dec-2020, 09:29:32
    Author     : Michael
--%>

<%@page import="smartcare.models.users.Fees"%>
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
                    Appointment ID: <br/> <input type="text" pattern="\d*" name="appointmentID" placeholder="123" required>  <br/><br/>       
                    
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
                        String update = (String) request.getAttribute("updateSuccess");
                        if (update != null) {
                            out.println(update);
                        }
                    %>
                </p>
                
                <h1>Booked Appointments</h1>
                <div class="appointment-container">
                    <form action="AdminServlet.do" name="p" method="Post" >
                        Private/NHS: <select id="typeOfAppointment" name="typeOfAppointment" onchange="this.form.submit()" required >
                            <option value="" disabled="disabled">Please select</option>
                            <option value="NHS"<%
                                System.out.println("debug");
                                if (((String) session.getAttribute("previousChoice"))!= null) {
                                    System.out.println("debug2");
                                    if (((String) session.getAttribute("previousChoice")).equals("NHS")) {
                                        System.out.println("debug3");
                                        out.print("selected=\"selected\"");
                                    }
                                } %>>NHS</option>
                            <option value="Private"<%
                                if (((String) session.getAttribute("previousChoice"))!= null) {
                                    if (((String) session.getAttribute("previousChoice")).equals("Private")) {
                                        out.print("selected=\"selected\"");
                                    }
                                } %>>Private</option>
                        </select>
                    </form>
                    <%
                        //check if there are appointments
                        boolean showTable = false;
                        if (!((ArrayList) request.getAttribute("appointments")).isEmpty()) {
                            showTable = true;
                        }
                    %>

                    <table <% if (!showTable) {
                            out.print("hidden='true'");
                        } %> style="width:100%;margin-bottom:20px" >
                        <tr>
                            <th>Appointment ID</th>
                            <th>Date</th>
                            <th>Start time</th>
                            <th>Comment</th>
                            <th>Action</th>
                        </tr>
                        <%
                            try
                            {
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
                            }
                            catch(NullPointerException e)
                            {
                            }
                        %>
                    </table>
                    <%
                        try
                        {
                            if (request.getAttribute("deleteSuccess") != null) {
                                out.print("<p>" + request.getAttribute("deleteSuccess") + "</p>");
                                
                        }
                        }
                        catch(NullPointerException e)
                        {
                            System.out.println("deleteSuccess parameter not populated");
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
                    String turnover = (String) request.getAttribute("turnover");
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
                    String patientDetail = (String) request.getAttribute("PatientDetail");
                    String patientID = (String) session.getAttribute("patientID");
                  
                    if (patientDetail != null) {
                        out.println(patientDetail);
                    }
                %>
              
                     
                        
                    </p>
                </form>
                    <%
                        //show option to delete patient
                        if (patientDetail != null) {

                            if(!((String)patientDetail).equals("Patient not found!")){
                                out.print("<form action='AdminServlet.do' name ='deleteUser' method='Post'>");
                                out.print("<input type='submit' value='DeletePatient' name='action'>");
                                out.print("</form>"); 
                            }
                        }

                        try{
                            if (request.getAttribute("deletePatientSuccess") != null) {
                                out.print("<p>" + request.getAttribute("deletePatientSuccess") + "</p>");
                            }
                        }catch(NullPointerException e){

                        }
                    %>
                    <br/>
                    
                <h1>Fees Management</h1>
                
                <%
                        //check if there are appointments
                        boolean showPrice = false;
                        if (!((ArrayList) request.getAttribute("fees")).isEmpty()) {
                            showPrice = true;
                        }
                        if (!showPrice) {
                      
                            out.print("<h4>There aren't any prices fixed</h4>");
                        }
                        
                        ArrayList<Fees> feesList = (ArrayList) request.getAttribute("fees");
                        Fees currentFee = feesList.get(0);
            
                    %>
                    
                <form action="AdminServlet.do" name="updateSuccess" method="Post">
                     Price: <br> <input type="number" name="price" required="" value="<%=currentFee.getPrice() %>"> £<br><br> 
                    Period: <br> <input type="number" name="period" required="" value="<%=currentFee.getPeriod() %>"> Mn<br><br>  
                    <input type="submit" value="Change Appointment Price" name="action"><br>
                </form>
                    
                  
                <br>
                
            </div>
                    
           

            <div class="register-container">
                <h1>Register New Staff Account</h1>
                <form action="AdminServlet.do" method="post">
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
                <input type="submit" value="Register" name="action">
                </form>
                <%
                    String error = (String) request.getAttribute("registerSuccess");
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
