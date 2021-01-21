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
import smartcare.models.users.Nurse;
import smartcare.models.users.User;
import smartcare.models.database.Jdbc;

/**
 *
 * @author asia
 */
public class NurseServlet extends HttpServlet {

    final String JSP = "/views/landing/nurseLanding.jsp";
    Jdbc jdbc = Jdbc.getJdbc();
    
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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        //Make a new doctor instance from session variable
        Nurse nurse;
        nurse = (Nurse)(User)session.getAttribute("user");
        

        
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
