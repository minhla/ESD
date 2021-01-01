<%-- 
    Document   : adminLanding
    Created on : 06-Dec-2020, 09:29:32
    Author     : Michael
--%>

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
                    Patient ID:<br/> <input type="number" name="patientID" placeholder="Ex. 1246092" >         
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
                    Type of service<br/> 
                    <select id="services" name="services">
                        <option value="surgery" selected="selected">Surgery</option>
                        <option value="consultation">Consultation</option>
                    </select>
                    <br/><br/>
                    Details: <br/> <textarea type="text" name="detail" rows="4" cols="50"></textarea><br/><br/>
                    Total amount:<br/> <input type="number" name="amount" ><br/><br/> 
                    Payment type:<br/>             
                    <select id="paymenttype" name="paymenttype">
                        <option value="NHS" selected="selected">NHS</option>
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
            </div>



            <div class="document-form">
                <h1>Document Management</h1>
                <form action="AdminServlet.do" name="document management" method="Post" >
                    Start date:<br/> <input type="date" name="startDate" ><br/><br/> 
                    End date:<br/> <input type="date" name="endDate" ><br/><br/> 
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
            </div>

            <div class="register-container">
                <h1>Register New Account</h1>
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
                    <p>Password: <input type="password" name="new_acc_password" placeholder="Make it as secure as possible"></p>
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
