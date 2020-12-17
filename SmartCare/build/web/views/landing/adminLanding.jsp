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
        <title>JSP Page</title>
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
        
        <h1>Admin landing page</h1>

            <div>
              <h1>Invoice Form</h1>
              <form action="AdminServlet.do" name="invoice" method="Post" >
                  Patient ID:<br/> <input type="text" name="patientID" >         
                  <input type="submit" value="Get patient detail" name ="action"><br/><br/>
                  <p>
                  <% 
                      //show patient details
                      String patientDetail = (String) session.getAttribute("patientDetail");

                      if(patientDetail!=null)
                      {
                           out.println(patientDetail);              
                      }

                  %>
                  </p>
                  Type of service<br/> 
                  <input list="services" name ="services">
                  <datalist id="services">
                    <option value="surgery">
                    <option value="consultation">
                  </datalist>
                  <br/><br/>
                  Details: <br/> <textarea type="text" name="detail" rows="4" cols="50"></textarea><br/><br/>
                  Total amount:<br/> <input type="text" name="amount" ><br/><br/> 
                  Payment type:<br/>             
                  <input list="paymenttype" name ="paymenttype">
                  <datalist id="paymenttype">
                    <option value="NHS">
                    <option value="Private">
                  </datalist>
                  <br/><br/> 
                  <input type="submit" value="issue invoice" name ="action"><br/>
              </form>
              <p>
                  <% 
                      //show result of sending invoice
                      String update = (String) session.getAttribute("updateSuccess");
                      if(update!=null)
                      {
                          out.println(update);
                      }
                  %>
              </p>
          </div>



          <div>
              <h1>Document management Form</h1>
             <form action="AdminServlet.do" name="document management" method="Post" >
              <input type="submit" value="produce weekly documents" name ="action"><br/>
             </form>
              <br/>
               <% 
                      //show result of sending invoice
                      String turnover = (String) session.getAttribute("turnover");
                      if(turnover!=null)
                      {
                          out.println(turnover);
                      }
                %>
               <br/>
          </div>
        
         <div>
             <h1>Register New Account</h1>
             <form action="RegisterStaff" method="post">
                 <p>Email: <input type="text" name="new_acc_email"></p>
                 <p>First name: <input type="text" name="new_acc_firstname"></p>
                 <p>Last name: <input type="text" name="new_acc_lastname"></p>
                 <p>User type: <input type="radio" name="new_acc_type" value="D">
                 <label for="doctor">Doctor</label>
                 <input type="radio" name="new_acc_type" value="N">
                 <label for="nurse">Nurse</label></p>
                 <p>Date of birth: <input type="date" name="new_acc_dob"></p>
                 <p>Phone number: <input type="text" name="new_acc_phone"></p>
                 <p>Address: <input type="text" name="new_acc_address"></p>
                 <p>Password: <input type="password" name="new_acc_password"></p>
                 <input type="submit" value="Submit">
             </form>
            <%
                String error = (String)request.getAttribute("updateSuccess");
                if (error != null) out.println(error);
            %>
             
         </div>
        
        <form action="Logout.do" method="post">
            <input type="submit" value="Logout" >
        </form>
    </body>
</html>
