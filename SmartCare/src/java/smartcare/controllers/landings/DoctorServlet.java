/*
Class: DoctorServlet
Description: the servlet for handing doctor interactions
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
import smartcare.models.users.Doctor;
import smartcare.models.users.User;
import smartcare.models.Prescription;
import smartcare.models.database.Jdbc;

/**
 *
 * @author jitojar
 */
public class DoctorServlet extends HttpServlet {

    final String JSP = "/views/landing/doctorLanding.jsp";
    Jdbc jdbc = Jdbc.getJdbc();
    
    
    
    /*
    Method: createPrescription
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
    private void createPrescription(HttpServletRequest request,Doctor doctor){
        
        
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String weight = request.getParameter("weight");
       String allergies = request.getParameter("allergies");
       String med = request.getParameter("med");
       
       //create prescription 
       Prescription prescription = new Prescription(patientID,weight,allergies,med);
       
       String result = doctor.createPrescription(prescription);
       request.setAttribute("updateSuccess", result);
       

        
    }
    
    /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private void getPatientDetail(HttpServletRequest request,Doctor doctor){
        
         //get patient details deom database
       String patientID = request.getParameter("patientID");
       String result = doctor.getPatientDetails(patientID);
       request.setAttribute("updateSuccess",result);
     
        
    }
    
    /**
    * Retrieves appointments for current doctor (scheduled for today).
    * Finds the appointment for this patient and alerts the doctor Landing.
    * Links the Doctor.getAppointments with doctorLanding
    *
    * @param request The servlet request variable.
    * @param patient The patient object to find the appointments of.
    */
    private void showAppointments(HttpServletRequest request, Doctor doctor){
        ArrayList<Appointment> appointments;
        appointments = doctor.getAppointments();
        request.setAttribute("appointments", appointments);
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");    
        
        HttpSession session = request.getSession();
        
        //Make a new doctor instance from session variable
        Doctor doctor;
        doctor = (Doctor)(User)session.getAttribute("user");
        
        showAppointments(request, doctor);
        
        //get action type from the doctor landing
        String action = request.getParameter("action");
        if(action != null)
        {
            if (action.equals("Get patient detail"))
            {
                getPatientDetail(request,doctor);
            }
            else if (action.equals("Create Prescription"))
            {
                createPrescription(request,doctor);
            }
         
        //show appointment
        showAppointments(request, doctor);
        }
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
