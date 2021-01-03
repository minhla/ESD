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
import smartcare.models.users.Patient;
import smartcare.models.Prescription;
import smartcare.models.Location;
import smartcare.models.Map;
import smartcare.models.database.Jdbc;
import smartcare.models.users.User;

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
    @Deprecated
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

    /**
    * Retrieves appointments for particular patient.
    * Finds the appointment for this patient and alerts the patientLanding.
    * Links the Patient.getAppointments with patientLanding.
    *
    * @param request The servlet request variable.
    * @param patient The patient object to find the appointments of.
    */
    private void showAppointments(HttpServletRequest request, Patient patient){
        ArrayList<Appointment> appointments;
        appointments = patient.getAppointments();
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
    private void deleteAppointment(HttpServletRequest request, Patient patient){
        String appointmentId = request.getParameter("appointmentId");
        String deleteSuccess = patient.deleteAppointment(appointmentId);
        request.setAttribute("deleteSuccess", deleteSuccess);
    }

    /**
    * Adds an appointment to the database.
    * Adds the appointment for this patient and alerts the patientLanding.
    * Links the Patient.addAppointment with the patientLanding.
    *
    * @param request The servlet request variable.
    * @param patient The patient object for which to add appointment.
    */
    private void addAppointment(HttpServletRequest request, Patient patient){
        String startTime = request.getParameter("starttime");
        String date = request.getParameter("date");
        String comment = request.getParameter("comment");
        String locationID = request.getParameter("locationID");
        System.out.println("locationId" + locationID);
        String addSuccess = patient.addAppointment(startTime, date, comment);
        request.setAttribute("updateSuccess", addSuccess);
    }
    
    /**
    * Pass the locations to show on Google maps.
    * Gets all of the locations that should be shown on the map from the map model
    * and passes them as an array of Strings.
    *
    * @param request The servlet request variable.
    */
    private void passLocations(HttpServletRequest request){
        Map map = new Map();
        ArrayList<Location> locs = map.getLocations();
        String[] locations = new String[locs.size()];
        int i = 0;
        for(Location loc:locs){
            locations[i] = loc.getString();
            System.out.println("the location at " + String.valueOf(i) +" is: " + locations[i]);
            i++;
        }
        request.setAttribute("locations", locations);
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        //Make a new patient instance
        Patient patient;
        patient = (Patient)(User)session.getAttribute("user");
        
        //show the locations in the database
        passLocations(request);
        
        //Show all of the scheduled appointments
        showAppointments(request, patient);

        //get action from patient landing
        String action = request.getParameter("action");
        if(action != null)
        {
            switch (action) {
                case "Book Appointment":
                    addAppointment(request, patient);
                    break;
                case "request for re-issue":
                    request = reIssuePrescription(request);
                    break;
                case "Cancel":
                    deleteAppointment(request, patient);
                    break;
                default:
                    break;
            }

            showAppointments(request, patient);
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
