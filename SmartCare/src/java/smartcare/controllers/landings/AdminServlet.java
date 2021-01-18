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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Document;
import smartcare.models.Invoice;
import smartcare.models.users.Admin;
import smartcare.models.Appointment;
import smartcare.models.users.User;
import smartcare.models.database.Jdbc;

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
     private void getPatientDetail(HttpServletRequest request, Admin admin)
     { 
        
       //get patient details deom database
       String patientID = request.getParameter("patientID");
       String result = admin.getPatientDetails(patientID);
       request.setAttribute("updateSuccess",result);
     
        
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
    private void createInvoice(HttpServletRequest request,Admin admin){
        
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String service = request.getParameter("services");
       String detail = request.getParameter("detail");
       String amount = request.getParameter("amount");
       String paymenttype = request.getParameter("paymenttype");

       //create invoice object
       Invoice invoice = new Invoice(patientID,service,detail,amount,paymenttype);
       
       String result = admin.createInvoice(invoice);
       request.setAttribute("updateInvoice", result);
        
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
        appointments = admin.getAppointments();
        request.setAttribute("appointments", appointments);
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
                getPatientDetail(request,admin);
            }
            else if(action.equals("Issue Invoice"))
            {
                createInvoice(request,admin);
            }
            else if(action.equals("Produce Weekly Documents"))
            {
                getWeeklyDocument(request,admin);
            }else if(action.equals("Cancel"))
            {
                deleteAppointment(request, admin);
            }
        
        showAppointments(request, admin);
        
        
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
