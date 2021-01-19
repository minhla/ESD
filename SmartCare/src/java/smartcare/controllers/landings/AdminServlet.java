/*
Class: AdminServlet
Description: the servlet for handing admin interactions
Created: 14/12/2020
Updated: 22/12/2020
Author/s: Asia Benyadilok
*/
package smartcare.controllers.landings;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Account;
import smartcare.models.Document;
import smartcare.models.Invoice;
import smartcare.models.users.Admin;
import smartcare.models.Appointment;
import smartcare.models.users.User;
import smartcare.models.database.Jdbc;
import smartcare.models.util.RegistrationUtils;
import smartcare.models.users.Fees;

/**
 *
 * @author asia
 */
public class AdminServlet extends HttpServlet {

    final String JSP = "/views/landing/adminLanding.jsp";
    
    Jdbc jdbc = Jdbc.getJdbc();
    
    
       /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private HttpServletRequest getPatientDetail(HttpServletRequest request){
        
        //create get session
        HttpSession session = request.getSession();
        
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       System.out.println("patientID => "+patientID);
       String patientDetail = null;
       
       try 
       {
           //get patient detail from database
           patientDetail = jdbc.getResultSet("firstname, lastname, dob", "(username = '"+patientID+"' AND usertype = 'P')", "users",3);
           if(patientDetail.equals(""))
           {
               session.setAttribute("patientDetail","Patient not found!");
           }
           else
           {
               String detailList [] = patientDetail.split(" ");

               session.setAttribute("patientDetail","Patient Name: "+detailList[0]+"<br/>"+
                                                    "Patient Surname: "+detailList[1]+"<br/>"+
                                                    "Date of Birth: "+detailList[2]+"<br/>");         
           }
       }
       
       catch(Exception e)
       {
           session.setAttribute("patientDetail","Patient not found!");
       }
     
        
       return request;
        
    }
     
           /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private HttpServletRequest getWeeklyDocument(HttpServletRequest request){
        
        HttpSession session = request.getSession();

       //get parameters from document form
       String startDate = request.getParameter("startDate");
       String endDate = request.getParameter("endDate");
       
       //create document
       Document document = new Document();
       
       //produce turnover document
       ArrayList<Integer> result = document.calTurnover(startDate, endDate);
       
        if(result.get(0) == 0)
        {
            session.setAttribute("turnover","turnover: 0 <br/> private payment: 0 <br/> pay through NHS: 0");
        }
        else
        {
            session.setAttribute("turnover","this week turn over: "+result.get(0)+"<br/>"+
                                                                "private payment: "+result.get(1)+"<br/>"+ 
                                                                "NHS payment: "+result.get(2)+"<br/>" );
            /*
            String detailList [] = patientDetail.split(" ");

            session.setAttribute("patientDetail","Patient Name: "+detailList[0]+"<br/>"+
                                                 "Patient Surname: "+detailList[1]+"<br/>"+
                                                 "Date of Birth: "+detailList[2]+"<br/>");
            */
        }
 
       return request;
        
    }
     
      /*
    Method: createInvoice
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
    private HttpServletRequest createInvoice(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        
       //get parameters from prescription form
       String appointmentID = request.getParameter("appointmentID");
       String service = request.getParameter("services");
       String detail = request.getParameter("detail");
       String amount = request.getParameter("amount");
       String paymenttype = request.getParameter("paymenttype");

       //Get patientID based on appointmentID
        int numOfColumns = 1;
        String column = "patient_username";
        String table = "Appointments";
        String condition = "appointmentid = " + appointmentID + "";

        //JDBC execute search statement
        ArrayList<String> res = this.jdbc.getResultList(column, condition, table, numOfColumns);
        System.out.println(res);
        if (res.isEmpty()) {
            session.setAttribute("updateInvoice", "There has been a problem.");
            return request;
        }
        String patientID = res.get(0);

       //create invoice object
       Invoice invoice = new Invoice(patientID,service,detail,amount,paymenttype);
       
       //validate the patient id
       String validation = jdbc.getResultSet("firstname, lastname, dob", "(username ='"+patientID+"' AND usertype = 'P')", "users",3);
       
       if (!validation.equals(""))
       {

         int success = invoice.createInvoicedeleteAppointment(appointmentID);
         
         //check if the database is successfully updated or not
         if(success != 0)
         {
             session.setAttribute("updateSuccess", "The invoice has been added!");
         }
         else
         {
             session.setAttribute("updateSuccess", "There has been a problem.");
         }
       }
       else
       {
           session.setAttribute("updateSuccess", "Patient not found!");
       }
        

        
        return request;
        
    }
    
    /**
    * Retrieves all appointments.
    * retrieves all appointments in the database.
    *
    * @param request The servlet request variable.
    * @param admin The Admin object.
    */
    private void showAppointments(HttpServletRequest request, Admin admin){
        ArrayList<Appointment> appointments;
        HttpSession session = request.getSession();
        String type = request.getParameter("typeOfAppointment");
        
        //if the user hasn't chosen the value get it from previous choices
        if(type == null){
            //if it's not the first time getting the values
            if(session.getAttribute("previousChoice") != null){
                type = (String)session.getAttribute("previousChoice");
            }else{
                type = "NHS";
                session.setAttribute("previousChoice", type);
            }
        }else{
            session.setAttribute("previousChoice", type);
        }
        
        
        appointments = admin.getAppointments(type);
        request.setAttribute("appointments", appointments);
    }
    
    
    private void registerStaff(HttpServletRequest request, Admin admin)
    {
        RegistrationUtils ru = new RegistrationUtils();        
        Account ac = new Account();
                  
        //get parameters from form
        String firstname = request.getParameter("new_acc_firstname");
        String lastname = request.getParameter("new_acc_lastname");
        String username = firstname.charAt(0) + "-" + lastname;
        username = username.toLowerCase();
        String dob = request.getParameter("new_acc_dob");
        String phone = request.getParameter("new_acc_phone");
        String email = request.getParameter("new_acc_email");
        String address = request.getParameter("new_acc_address");
        String password = ru.dateToPassword(dob);
        String userType = request.getParameter("new_acc_type");

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime date = LocalDateTime.now();
        String regdate = date.format(dtFormatter);
        
        if(admin.userExists(username)) //increment the number at the end of username if the username already exists
        {
            System.out.println("user by the name of " + username + " already exists. Attempting username incrementation...");
            
            username = admin.usernameWithNum(username, 1); //find the version of this username with the highest number at the end
            username = admin.incrementUsername(username);
            
            System.out.println("New username is: " + username);
        }
        
        //Add to database
        String table = "users (username, firstname, lastname, usertype, dob, phone, email, address, password, regdate)";
        String values = "('" + username  + "', '"+  firstname + "','" + lastname + "', '"+ userType
                              + "', '" + dob + "', '" + phone +"', '"
                              + email + "', '" + address + "', '" + ac.generatePasswordHash(password)  + "', '" + regdate +"')";

 
        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "The account has been added!");
            System.out.println("Account added");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
            System.out.println("Account could not be added");
        }
    }
    
    
        /**
    * Retrieves fee.
    *
    * @param request The servlet request variable.
    * @param admin The Admin object.
    */
    private void showFees(HttpServletRequest request, Admin admin){
        ArrayList<Fees> fees;
        fees = admin.getFees();
        request.setAttribute("fees", fees);
    }
    
    
    private HttpServletRequest updateFees(HttpServletRequest request, Admin admin) {
        
        HttpSession session = request.getSession();
        
       //get parameters from prescription form
       String price = request.getParameter("price");
       String period = request.getParameter("period");
       
       //create fee object
       
         int success = admin.updateFees(Integer.parseInt(price),Integer.parseInt(period));
         
         //check if the database is successfully updated or not
         if(success != 0)
         {
             session.setAttribute("updateSuccess", "The price has been updated!");
         }
         else
         {
             session.setAttribute("updateSuccess", "There has been a problem.");
         }
        
        return request;
    }
    
    /**
    * Deletes an appointment from the database.
    * Deletes appointment for this patient and alerts the patientLanding.
    * Links the Patient.deleteAppointment with the patientLanding.
    *
    * @param request The servlet request variable.
    * @param patient The patient object who's appointment we have to delete
    */
    private void deleteAppointment(HttpServletRequest request, Admin admin){
        String appointmentId = request.getParameter("appointmentId");
        String deleteSuccess = admin.deleteAppointment(appointmentId);
        request.setAttribute("deleteSuccess", deleteSuccess);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        //Make a new patient instance
        Admin admin;
        admin = (Admin)(User)session.getAttribute("user");
                
         //get action type from the admin landing
        String action = request.getParameter("action");
        if (action != null)
            if (action.equals("Get patient details"))
            {
                request = getPatientDetail(request);
            }
            else if(action.equals("Issue Invoice"))
            {
                request = createInvoice(request);
            }
            else if(action.equals("Produce Weekly Documents"))
            {
                request = getWeeklyDocument(request);
                
            } else if(action.equals("Change Appointment Price"))
            {
                request = updateFees(request, admin);
            }
            else if(action.equals("Register"))
            {
                registerStaff(request, admin);
            }
            else if(action.equals("Remove"))
            {
                deleteAppointment(request, admin);
                
            } 
        
        showAppointments(request, admin);
        showFees(request, admin);
        
        
        
        RequestDispatcher view = request.getRequestDispatcher(JSP);
        view.forward(request,response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
