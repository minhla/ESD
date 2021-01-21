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
import smartcare.models.Registration;
import smartcare.models.users.User;
import smartcare.models.database.Jdbc;
import smartcare.models.users.Fees;

/**
 *
 * @author asia
 */
public class AdminServlet extends HttpServlet {

    final String JSP = "/views/landing/adminLanding.jsp";

    Jdbc jdbc = Jdbc.getJdbc();
    Registration reg = new Registration();

       /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private void getPatientDetail(HttpServletRequest request, Admin admin)
     { 
       HttpSession session = request.getSession();
        
       //get patient details deom database
       String patientID = request.getParameter("patientID");
       String result = admin.getPatientDetails(patientID);
       request.setAttribute("PatientDetail",result); 
       //set patient id to the session
       session.setAttribute("patientID",patientID);
        
    }
     
           /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private void getWeeklyDocument(HttpServletRequest request,Admin admin){

      //get parameters from document form
      String startDate = request.getParameter("startDate");
      String endDate = request.getParameter("endDate");
       
      //calculate turnover
      String result = admin.calTurnover(startDate, endDate);
      request.setAttribute("turnover",result);

        
    }

      /*
    Method: createInvoice
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
    private HttpServletRequest createInvoice(HttpServletRequest request, Admin admin){

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
        }
        String patientID = res.get(0);
        
        // Calculate amount
        ArrayList<Fees> fees = admin.getFees();
        Fees surgeryFee = fees.get(0);
        Fees consultationFee = fees.get(1);
        
        double totalAmount = 0.00;
        
        if (service.equals("surgery")) {
            
            totalAmount = surgeryFee.getPrice();
            
        } else if (service.equals("consultation")) {
            
            totalAmount = consultationFee.getPrice();
            
        } else {
            totalAmount = 20.00;
        }

       //create invoice object
       Invoice invoice = new Invoice(patientID,service,detail,String.valueOf(totalAmount),paymenttype);

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


    private void registerStaff(HttpServletRequest request)
    {
        Account ac = new Account();

        //get parameters from form
        String title = request.getParameter("titles");
        String firstname = request.getParameter("new_acc_firstname");
        String lastname = request.getParameter("new_acc_lastname");
        String username = firstname.charAt(0) + "-" + lastname;
        username = username.toLowerCase();
        String dob = request.getParameter("new_acc_dob");
        String phone = request.getParameter("new_acc_phone");
        String email = request.getParameter("new_acc_email");
        String address = request.getParameter("new_acc_address");
        String password = reg.dateToPassword(dob);
        String userType = request.getParameter("new_acc_type");

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime date = LocalDateTime.now();
        String regdate = date.format(dtFormatter);

        if(reg.userExists(username)) //increment the number at the end of username if the username already exists
        {
            System.out.println("user by the name of " + username + " already exists. Attempting username incrementation...");

            username = reg.usernameWithNum(username, 1); //find the version of this username with the highest number at the end
            username = reg.incrementUsername(username);

            System.out.println("New username is: " + username);
        }

        //Add to database
        String table = "users (title, username, firstname, lastname, usertype, dob, phone, email, address, password, regdate)";
        String values = "('" + title  + "','" + username  + "', '"+  firstname + "','" + lastname + "', '"+ userType
                              + "', '" + dob + "', '" + phone +"', '"
                              + email + "', '" + address + "', '" + ac.generatePasswordHash(password)  + "', '" + regdate +"')";


        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("registerSuccess", "The account has been added!");
            System.out.println("Account added");
        }else{
            request.setAttribute("registerSuccess", "There has been a problem.");
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
       String priceSurgery = request.getParameter("priceSurgery");
       String periodSurgery = request.getParameter("periodSurgery");
       
       String priceCons = request.getParameter("priceCons");
       String periodCons = request.getParameter("periodCons");

       //create fee object

         int successS = admin.updateFees(Integer.parseInt(priceSurgery),Integer.parseInt(periodSurgery), "Surgery");
         int successC = admin.updateFees(Integer.parseInt(priceCons),Integer.parseInt(periodCons), "Consultation");

         //check if the database is successfully updated or not
         if(successS != 0 && successC != 0)
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
    
    private void deleteUser(HttpServletRequest request, Admin admin){
        HttpSession session = request.getSession();
        String patientId = (String) session.getAttribute("patientID");
        String deleteSuccess = admin.deleteUser(patientId);
        request.setAttribute("deletePatientSuccess", deleteSuccess);
        session.setAttribute("patientDetail", null);
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
                 getPatientDetail(request,admin);
            }
            else if(action.equals("Issue Invoice"))
            {
                request = createInvoice(request, admin);
            }
            else if(action.equals("Produce Weekly Documents"))
            {
                 getWeeklyDocument(request,admin);

            } else if(action.equals("Change Appointment Prices"))
            {
                request = updateFees(request, admin);
            }
            else if(action.equals("Register"))
            {
                registerStaff(request);
            }
            else if(action.equals("Remove"))
            {
                deleteAppointment(request, admin);
            }
            else if(action.equals("DeletePatient")){
                deleteUser(request, admin);
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
