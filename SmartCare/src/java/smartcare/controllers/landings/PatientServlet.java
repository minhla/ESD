/*
Class: PatientServlet
Description: the servlet for handing patient interactions
Created: 14/12/2020
Updated: 16/12/2020
Author/s: Asia Benyadilok
*/
package smartcare.controllers.landings;

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
import smartcare.models.Prescription;
import smartcare.models.database.Jdbc;
import smartcare.models.User;

/**
 *
 * @author jitojar
 */
public class PatientServlet extends HttpServlet {

    final String JSP = "/views/landing/patientLanding.jsp";
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
    private HttpServletRequest bookAppointmentWithDoctor(HttpServletRequest request){
        HttpSession session = request.getSession();
        
        //get parameters from form
        String starttime = request.getParameter("starttime");
        // TODO add 30 minutes to start time for apointment duration
        String endtime = starttime;
        String date = request.getParameter("date");
        String comment = request.getParameter("comment");
        
        //get the right user ID from the database
        String userEmail = (String)session.getAttribute("userEmail");
        String userID = jdbc.getValueStmt("uuid", "email = '"+ userEmail +"'", "Users");
        System.out.println("userID = " + userID);
        
        
        
        //check if that time slot is free
       
        String availableDoctorId = "0";
        
        String column = "uuid, firstname, lastname";
        String tableAv = "users";
        String condition = "usertype = 'D'";
                            // TO DO check for available doctors
                            String test = " SELECT lastname FROM smartcare.USERS "
                            +" where usertype = 'D' "
                            + " and UUID not in ( "
                            + " SELECT u.UUID FROM SMARTCARE.USERS u "
                            + " join smartcare.APPOINTMENTS a on u.UUID = a.DOCTORID "
                            + " where a.STARTTIME <= '" +starttime+ "'  and a.ENDTIME >=  '" + endtime + "' and appointmentdate =  '"+ date +"')";
                            
    
        //Get all of the appointments for the doctor
        availableDoctorId = jdbc.getValueStmt(column, condition, tableAv);
        
        
        //Add to database
        String table = "appointments (appointmentdate, starttime, endtime, comment, patientID)";
        String values = "('"  + date + "', '"+ starttime+ "', '" 
                + endtime + "', '" + comment + "', " + userID +")";
        
        int success = jdbc.addRecords(table, values);
        
        String doctorName = jdbc.getValueStmt("lastname", "uuid = '"+ availableDoctorId +"'", "users");
        
        if(success != 0){
            request.setAttribute("updateSuccess", "The appointment has been scheduled with doctor " + doctorName);
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
        }    
        
    return request;
    }
        
    private HttpServletRequest bookAppointment(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        
       //get parameters from form
        String starttime = request.getParameter("starttime");
        // TODO add 30 minutes to start time for apointment duration
        String endtime = starttime;
        String date = request.getParameter("date");
        String comment = request.getParameter("comment");
        
        //get the right user ID from the session variable
        User user = (User)session.getAttribute("user");
        System.out.println("userID = " + user.getUserID());
        
        //check if that time slot is free (not implemented yet)
        
        //Add to database
        String table = "appointments (appointmentdate, starttime, endtime, comment, patientID)";
        String values = "('"  + date + "', '"+ starttime+ "', '" 
                + endtime + "', '" + comment + "', " + user.getUserID() +")";
        
        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "The appointment has been scheduled!");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
        }
        
        return request;
    }
    
    private HttpServletRequest showAppointments(HttpServletRequest request){
        
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        String appointments = new String();
        HttpSession session = request.getSession();
        
        User user = (User)session.getAttribute("user");
        
        //retreve all available appointments from database
        String column = "appointmentid, starttime, endtime, appointmentdate, comment";
        int numOfColumns = 5;
        String table = "Appointments";
        String condition = "patientID = " + user.getUserID();
        
        //Get all of the appointments for this user
        appointments = jdbc.getResultSet(column, condition, table, numOfColumns);
        System.out.println("the appointments for this user are, appointments = " + appointments);
        
        if(appointments.length()>=1){
            //split the data received and put it into appointmentList
            String singleAppointment[] = appointments.split("<br>");
            for (String element : singleAppointment){
                String val[] = element.split(" ");
                System.out.println("the element is:" + element);
                Appointment temp = new Appointment(val[0], val[1], val[2], val[3], val[4], user.getUserID());
                appointmentList.add(temp);
                System.out.println(element);

            }
        }
        
        
        //show on the patientLanding
        request.setAttribute("Appointments", appointmentList);
        
        return request;
    }
    
    private HttpServletRequest deleteAppointment(HttpServletRequest request){
        //after pressing the delete button the appointment will be deleted
        String appointmentId = request.getParameter("appointmentId");
        System.out.println("deleting appointment" + appointmentId);
        
        int success = jdbc.delete("Appointments", "appointmentId = " + appointmentId);
        String deleteSuccess = new String();
        if(success == 0){
            deleteSuccess = "Failed to cancel appointment";
        }else{
            deleteSuccess = "Successfully cancelled appointment";
        }
        
        request.setAttribute("deleteSuccess", deleteSuccess);
        return request;
    }
    
      private HttpServletRequest reIssuePrescription(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        
                
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String issuedate = request.getParameter("issuedate");

       //create prescription
       Prescription prescription = new Prescription();
       
       prescription.reIssuePrescription(patientID, issuedate);
       
       //get prescription from database

            ArrayList<String> result = prescription.reIssuePrescription(patientID, issuedate);
            
            //check if patient and prescription are available or not
            if(result.size() !=0)
            {

                    session.setAttribute("prescriptionDetail","Patient Name: "+result.get(0)+"<br/>"+
                                                              "Patient Surname: "+result.get(1)+"<br/>"+
                                                              "Date of Birth : "+result.get(2)+"<br/>"+
                                                              "Weight : "+result.get(3)+"<br/>"+
                                                              "Allergies : "+result.get(4)+"<br/>"+
                                                              "Medicine : "+result.get(5)+"<br/>");
            } 
            else
            {
                session.setAttribute("prescriptionDetail","Prescription not found!");
            }

        
        //when deleting an appointment
        
        return request;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        
        //get action from patient landing
        String action = request.getParameter("action");
        if(action != null)
        {
            if(action.equals("Book Appointment")){
                request = bookAppointmentWithDoctor(request);
            }
            else if(action.equals("request for re-issue"))
            {
                request = reIssuePrescription(request);
            }
            else if(action.equals("Cancel")){
                request = deleteAppointment(request);
            }
            
            request = showAppointments(request);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(JSP);
        view.forward(request, response);
        
        
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
