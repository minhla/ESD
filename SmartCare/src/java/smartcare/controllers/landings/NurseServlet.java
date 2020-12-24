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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    
      private HttpServletRequest showAppointments(HttpServletRequest request){
        
        String appointments = "";
        
        //get current date
        LocalDate currentDate = java.time.LocalDate.now();
        
        
        String column = "appointmentid, comment, starttime, endtime, appointmentdate";
        int numOfColumns = 5;
        String table = "Appointments";
        String condition = "appointmentdate = '" + currentDate.toString()+"'";
        
        //Get all of the appointments for the doctor
        appointments = jdbc.getResultSet(column, condition, table, numOfColumns);
        System.out.println("Hello, appointments = " + appointments);
        
        request.setAttribute("appointmentsdata", appointments);
        request.setAttribute("status", "button pressed");
        
        return request;
        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        //show appointment
        request = showAppointments(request);
        
        
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
