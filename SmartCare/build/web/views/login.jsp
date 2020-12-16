<%--
    Document   : login
    Created on : 03-Dec-2020, 09:18:55
    Author     : Michael
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Care: Login</title>
    </head>

    <body>
        <div class="login-container">
            <a href="/SmartCare"><h1>SmartCare</h1></a>
            <form method="post" action="<%=request.getContextPath() %>/Login.do" class="login-form">
                <p>Email</p>
                <input type="text" name="email">
                <p>Password</p>
                <input type="password" name="password"> <br />
                <button type="Submit" value="Login">Login</button>
            </form>
            <div class="error-message">
                <%
                    String error = (String)request.getAttribute("errorMsg");
                    if (error != null) out.println(error);
                %>
            </div>
        </div>

    </body>
    <style>
        <%@ include file="./css/styles.css" %>
    </style>
</html>
