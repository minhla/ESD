/*
Class: PatientServlet
Description: the servlet for handing patient interactions
Created: 14/12/2020
Updated: 16/12/2020
Author/s: Asia Benyadilok
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
    
      private HttpServletRequest reIssuePrescription(HttpServletRequest request){
        
        //create obj of database
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
                
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String issuedate = request.getParameter("issuedate");

       String prescription = null;
       String patientDetail = null;


       //get prescription from database
       try 
        {
            //get patient detail from database
            patientDetail = jdbc.getResultSet("firstname, lastname, dob", "uuid = "+patientID, "users",3);
            prescription = jdbc.getResultSet("weight, allergies, medicine", "(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",3);
            String weight = jdbc.getResultSet("weight","(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",1);
            String allergies = jdbc.getResultSet("allergies","(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",1);
            String medicine = jdbc.getResultSet("medicine", "(issuedate = '"+issuedate.toString()+"' AND patientid = "+patientID+")","prescription",1);
            
            //check if patient and prescription are available or not
            if(!patientDetail.equals(""))
            {
                if(!prescription.equals(""))
                {
                    String detailList [] = patientDetail.split(" ");
                    session.setAttribute("prescriptionDetail","Patient Name: "+detailList[0]+"<br/>"+
                                                              "Patient Surname: "+detailList[1]+"<br/>"+
                                                              "Date of Birth : "+detailList[2]+"<br/>"+
                                                              "Weight : "+weight+"<br/>"+
                                                              "Allergies : "+allergies+"<br/>"+
                                                              "Medicine : "+medicine+"<br/>");
                } 
                else
                {
                    session.setAttribute("prescriptionDetail","Prescription not found!");
                }
            }
            else
            {
                session.setAttribute("prescriptionDetail","Patient not found!");
                
            }
        }

        catch(Exception e)
        {
            session.setAttribute("prescriptionDetail","Patient not found!");
        }

        
        //when deleting an appointment
        
        return request;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String viewPath = "views/landing/patientLanding.jsp";
        
        //get action from patient landing
        String action = request.getParameter("action");
        
        if(action.equals("Book Appointment")){
            request = bookAppointment(request);
        }
        else if(action.equals("request for re-issue"))
        {
            request = reIssuePrescription(request);
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
