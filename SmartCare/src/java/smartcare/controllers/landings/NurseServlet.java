/*
Class: NurseServlet
Description: the servlet for handing nurse interactions
Created: 14/12/2020
Updated: 16/12/2020
Author/s: Asia Benyadilok
*/
package smartcare.controllers.landings;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Appointment;
import smartcare.models.Invoice;
import smartcare.models.users.Nurse;
import smartcare.models.users.User;
import smartcare.models.database.Jdbc;
import smartcare.models.users.Admin;
import smartcare.models.users.Fees;

/**
 *
 * @author asia
 */
public class NurseServlet extends HttpServlet {

    final String JSP = "/views/landing/nurseLanding.jsp";
    private Jdbc jdbc = Jdbc.getJdbc();
    private Admin admin = new Admin();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    /**
    * Retrieves appointments for current Nurse (scheduled for today).
    * Finds the appointment for this patient and alerts the nurseLanding.
    * Links the Nurse.getAppointments with nurseLanding
    *
    * @param request The servlet request variable.
    * @param patient The patient object to find the appointments of.
    */
    private void showAppointments(HttpServletRequest request, Nurse nurse){
        ArrayList<Appointment> appointments;
        appointments = nurse.getAppointments();
        request.setAttribute("appointments", appointments);
    }
    
    
    /*
    Method: createInvoice
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
     */
    private HttpServletRequest createInvoice(HttpServletRequest request) {

        HttpSession session = request.getSession();

        //get parameters from the form
        String appointmentID = request.getParameter("appointmentID");

        String service = request.getParameter("services");
        String detail = request.getParameter("detail");
        String amount = request.getParameter("amount");
        String paymenttype = request.getParameter("paymenttype");

        //sanitize the comment input
        detail = detail.replace("'", "''");
        
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
        String validation = jdbc.getResultSet("firstname, lastname, dob", "(username ='" + patientID + "' AND usertype = 'P')", "users", 3);

        if (!validation.equals("")) {

            int success = invoice.createInvoicedeleteAppointment(appointmentID);

            //check if the database is successfully updated or not
            if (success != 0) {
                session.setAttribute("updateInvoice", "The invoice has been added!");
            } else {
                session.setAttribute("updateInvoice", "There has been a problem.");
            }
        } else {
            session.setAttribute("updateInvoice", "Patient not found!");
        }

        return request;

    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        //Make a new nurse instance from session variable
        Nurse nurse;
        nurse = (Nurse)(User)session.getAttribute("user");
        
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("Issue Invoice")) {
                request = createInvoice(request);
            }
        }
        //Show appointments
        showAppointments(request, nurse);
        
        //send response
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
