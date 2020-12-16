/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
<<<<<<< HEAD:SmartCare/src/java/smartcare/controllers/DoctorServlet.java
package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.RequestDispatcher;
=======
package smartcare.controllers.landings;

import java.io.IOException;
import java.io.PrintWriter;
>>>>>>> Filters:SmartCare/src/java/smartcare/controllers/landings/AdminServlet.java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD:SmartCare/src/java/smartcare/controllers/DoctorServlet.java
/**
 *
 * @author jitojar
 */
public class DoctorServlet extends HttpServlet {
=======
@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminLanding.do"})
public class AdminServlet extends HttpServlet {
>>>>>>> Filters:SmartCare/src/java/smartcare/controllers/landings/AdminServlet.java

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
<<<<<<< HEAD:SmartCare/src/java/smartcare/controllers/DoctorServlet.java
    
    private HttpServletRequest showAppointments(HttpServletRequest request){
        
        String appointments = "";
        Jdbc jdbc = new Jdbc();
        
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
        request.setAttribute("status", "it works mate!!!!");
        
        return request;
        
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String viewPath = "/views/landing/doctorLanding.jsp";
        
        //show appointment
        request = showAppointments(request);
        
        RequestDispatcher view = request.getRequestDispatcher(viewPath);
        view.forward(request,response);
=======
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
>>>>>>> Filters:SmartCare/src/java/smartcare/controllers/landings/AdminServlet.java
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
