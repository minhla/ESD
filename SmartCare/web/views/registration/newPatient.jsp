<%-- 
    Document   : newPatient
    Created on : 15-Dec-2020, 22:01:25
    Author     : David
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SmartCare - Create a new account</title>
        <link rel="icon" 
              type="image/png" 
              href="https://i.imgur.com/Vwma7mV.png">
    </head>
    <body>
        <div class="register-container">
            <h1>Patient registration</h1>
            <h3>Fill in your information</h3>
            <form action="<%=request.getContextPath()%>/AddPatient.do" name="addpatient" method="Post">
                <input type="text" pattern="\S+" title="Only alphabet letters" name="firstname" placeholder="First name" required><br/>
                <input type="text" pattern="\S+" title="Only alphabet letters" name="lastname" placeholder="Last name" required><br/>
                <input type="date" name="dob" placeholder="Date of birth" required><br/>
                <input type="email" name="email" placeholder="Email" required><br/>
                <input type="text" pattern="\d*" title="Only numbers" name="phone" maxlength="11" placeholder="Phone number" required><br/>
                <input type="text" name="address" placeholder="Address" required><br/>

                <button type="Submit" value="Submit">Register</button>
            </form>
            <div class="login-container">
                <%
                String error = (String) request.getAttribute("updateSuccess");
                if (error != null) {
                    out.println(error);
                }
                else {
                    out.println("Already have an account? <a href='/SmartCare/'>Login</a> here.");
                }
            %>
            </div>
        </div>
    </body>
    <style>
        <%@ include file="../css/patientRegister.css" %>
    </style>
</html>
