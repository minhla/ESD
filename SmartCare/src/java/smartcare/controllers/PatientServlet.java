/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Appointment;
import smartcare.models.database.Jdbc;

/**
 *
 * @author jitojar
 */
public class PatientServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private HttpServletRequest bookAppointment(HttpServletRequest request){
        
        ArrayList<Appointment> Appointments = new ArrayList<Appointment>();
        
        String viewPath = "views/landing/patientLanding.jsp";
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
        
        //if we have to add a new appointment
        //get parameters from form
        String starttime = request.getParameter("starttime");
        //add ten minutes to start time
        String endtime = request.getParameter("starttime");
        String date = request.getParameter("date");
        String comment = request.getParameter("comment");
        
        //get the right user ID from the database
        String userEmail = (String)session.getAttribute("userEmail");
        String userID = jdbc.getValueStmt("uuid", "email = '"+ userEmail +"'", "Users");
        System.out.println("userID = " + userID);
        
        //check if that time slot is free
        
        //Add to database
        String table = "appointments (appointmentdate, starttime, endtime, comment, patientID)";
        String values = "('"  + date + "', '"+ starttime+ "', '" 
                + endtime + "', '" + comment + "', " + userID +")";
        
        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "The appointment has been scheduled!");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
        }
        
        //when showing available appointments
        Appointment app = new Appointment();
        app.setComment("this is the comment mate");
        Appointments.add(app);
        
        //show on the patientLanding
        request.setAttribute("Appointments", Appointments);
        
        
        //when deleting an appointment
        
        return request;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String viewPath = "views/landing/patientLanding.jsp";
        
        
        String action = request.getParameter("action");
        
        if(action.equals("Book Appointment")){
            request = bookAppointment(request);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(viewPath);
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
