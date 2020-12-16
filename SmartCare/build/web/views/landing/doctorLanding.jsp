<%-- 
    Document   : doctorLanding
    Created on : 06-Dec-2020, 09:33:59
    Author     : Michael, Giacomo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Doctor</title>
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
        
        <h1>Doctor landing page</h1>
        <form action="DoctorServlet.do" name="show" method="Post">
            <input type="submit" name="action" value="list daily appoinments">
        </form>
        <% 
            String appointments = (String) request.getAttribute("appointmentsdata");
            if(appointments!=null){
                out.print(appointments);
            }
            
            out.print(request.getAttribute("status"));
        
          %>
          
     <div>
        <h1>Prescription Form</h1>
        <form action="DoctorServlet.do" name="prescription" method="Post" >
            Patient ID:<br/> <input type="text" name="patientID" >         
            <input type="submit" value="Get patient detail" name ="action"><br/><br/>
            <p>
            <% 
                String patientDetail = (String) session.getAttribute("patientDetail");
                
                if(patientDetail!=null)
                {
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
            <% 
                String update = (String) session.getAttribute("updateSuccess");
                if(update!=null)
                {
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
