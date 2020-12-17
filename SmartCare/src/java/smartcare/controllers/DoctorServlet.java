/*
Class: DoctorServlet
Description: the servlet for handing doctor interactions
Created: 14/12/2020
Updated: 16/12/2020
Author/s: Asia Benyadilok
*/
package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.database.Jdbc;

/**
 *
 * @author jitojar
 */
public class DoctorServlet extends HttpServlet {

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
        request.setAttribute("status", "button pressed");
        
        return request;
        
    }
    
    /*
    Method: createPrescription
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
    private HttpServletRequest createPrescription(HttpServletRequest request){
        
        
        //create obj of database and get session
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
        
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String weight = request.getParameter("weight");
       String allergies = request.getParameter("allergies");
       String med = request.getParameter("med");
       //get current date
       LocalDate currentDate = java.time.LocalDate.now();

       //validate the patient id
       String validation = jdbc.getResultSet("firstname, lastname, dob", "(uuid = "+patientID+" AND usertype = 'P')", "users",3);
       
       if (!validation.equals(""))
       {
        //Add details of prescription to database
        String table = "prescription (weight, allergies, medicine, patientid, issuedate)";
        String values = "("  + weight + ", '"+ allergies+ "', '"+ med + "', " + patientID+",'"+currentDate.toString()+"')";


         int success = jdbc.addRecords(table, values);

         //check if the database is successfully updated or not
         if(success != 0)
         {
             session.setAttribute("updateSuccess", "The prescription has been added!");
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
    
    /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private HttpServletRequest getPatientDetail(HttpServletRequest request){
        
        
        //create obj of database and get session
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
        
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");

       String patientDetail = null;
       
       try 
       {
           //get patient detail from database
           patientDetail = jdbc.getResultSet("firstname, lastname, dob", "(uuid = "+patientID+" AND usertype = 'P')", "users",3);
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
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String viewPath = "/views/landing/doctorLanding.jsp";
        
        //show appointment
        request = showAppointments(request);
        
        //get action type from the doctor landing
        String action = request.getParameter("action");
        
        if (action.equals("Get patient detail"))
        {
            request = getPatientDetail(request);
        }
        else if (action.equals("Create Prescription"))
        {
            request = createPrescription(request);
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
