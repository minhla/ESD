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
import smartcare.models.Invoice;
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
    private HttpServletRequest createPrescription(HttpServletRequest request) {

        HttpSession session = request.getSession();

        //get parameters from prescription form
        String patientID = request.getParameter("patientID");
        String weight = request.getParameter("weight");
        String allergies = request.getParameter("allergies");
        String med = request.getParameter("med");

        //create prescription 
        Prescription prescription = new Prescription(patientID, weight, allergies, med);

        //validate the patient id
        String validation = jdbc.getResultSet("firstname, lastname, dob", "(username = '" + patientID + "' AND usertype = 'P')", "users", 3);

        if (!validation.equals("")) {

            int success = prescription.createPrescription();

            //check if the database is successfully updated or not
            if (success != 0) {
                session.setAttribute("updateSuccess", "The prescription has been added!");
            } else {
                session.setAttribute("updateSuccess", "There has been a problem.");
            }
        } else {
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
    private HttpServletRequest getPatientDetail(HttpServletRequest request) {

        HttpSession session = request.getSession();

        //get parameters from prescription form
        String patientID = request.getParameter("patientID");

        String patientDetail = null;

        try {
            //get patient detail from database
            patientDetail = jdbc.getResultSet("firstname, lastname, dob", "(username = '" + patientID + "' AND usertype = 'P')", "users", 3);
            if (patientDetail.equals("")) {
                session.setAttribute("patientDetail", "Patient not found!");
            } else {
                String detailList[] = patientDetail.split(" ");

                session.setAttribute("patientDetail", "Patient Name: " + detailList[0] + "<br/>"
                        + "Patient Surname: " + detailList[1] + "<br/>"
                        + "Date of Birth: " + detailList[2] + "<br/>");
            }
        } catch (Exception e) {
            session.setAttribute("patientDetail", "Patient not found!");
        }

        return request;

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

        //create invoice object
        Invoice invoice = new Invoice(patientID, service, detail, amount, paymenttype);

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

    /**
     * Retrieves appointments for current doctor (scheduled for today). Finds
     * the appointment for this patient and alerts the doctor Landing. Links the
     * Doctor.getAppointments with doctorLanding
     *
     * @param request The servlet request variable.
     * @param patient The patient object to find the appointments of.
     */
    private void showAppointments(HttpServletRequest request, Doctor doctor) {
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
        doctor = (Doctor) (User) session.getAttribute("user");

        showAppointments(request, doctor);

        //get action type from the doctor landing
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("Get patient detail")) {
                request = getPatientDetail(request);
            } else if (action.equals("Create Prescription")) {
                request = createPrescription(request);
            } else if (action.equals("Issue Invoice")) {
                request = createInvoice(request);
            }

            //show appointment
            showAppointments(request, doctor);
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
