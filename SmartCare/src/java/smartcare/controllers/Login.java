/*
Class: login
Description: the servlet for handing login interactions
Created: 01/12/2020
Updated: 06/12/2020
Author/s: Michael Tonkin
*/
package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.validator.internal.util.logging.Log;
import smartcare.models.Admin;
import smartcare.models.Nurse;
import smartcare.models.Doctor;
import smartcare.models.Patient;
import smartcare.models.database.Jdbc;
import smartcare.models.User;


@WebServlet(name = "Login", urlPatterns = {"/Login.do"})
public class Login extends HttpServlet {

    private Jdbc db = Jdbc.getJdbc();
    
    private void setupUserSession(User user, String[] details, HttpSession session)
    {
        user.setUserID(details[0]);
        user.setName(details[1]);

        session.setAttribute("user", user);
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userType", user.getUserType());
    }


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("errorMsg", "");
        HttpSession session = request.getSession();

        //get the email and password entered by the user.
        String entrdEmail = (String)request.getParameter("email");
        String entrdPass = (String)request.getParameter("password");

        System.out.println(entrdEmail+" "+entrdPass);

        //attempt a login
        if(db.loginStmt("Users", entrdEmail, entrdPass))
        {
            //set the session variable
            User user = new User();
            String details[] = db.getResultSet("uuid, firstName", "email = '"+entrdEmail+"'", "Users", 2).split(" ");
            user.setUserID(details[0]);
            user.setName(details[1]);
            session.setAttribute("user", user);
            //send to a different landing page depending on the user's account type.
            String accType = db.getValueStmt("USERTYPE", "Email='" + entrdEmail + "'", "Users");
            session.setAttribute("userEmail", entrdEmail);
            switch(accType)
            {
                case "A": //admin

                    Admin admin = new Admin();
                    admin.setUserType("A");
                    setupUserSession(admin, details, session);

                    setCookies(entrdEmail, session, response);
                    response.sendRedirect(request.getContextPath() + "/AdminServlet.do");
                    break;

                case "P": //patient

                    Patient patient = new Patient();
                    patient.setUserType("P");
                    setupUserSession(patient, details, session);

                    setCookies(entrdEmail, session, response);
                    response.sendRedirect(request.getContextPath() + "/PatientServlet.do");
                    break;

                case "N": //nurse

                    Nurse nurse = new Nurse();
                    nurse.setUserType("N");
                    setupUserSession(nurse, details, session);

                    setCookies(entrdEmail, session, response);
                    response.sendRedirect(request.getContextPath() + "/NurseServlet.do");
                    break;
                case "D": //doctor

                    Doctor doctor = new Doctor();
                    doctor.setUserType("D");
                    setupUserSession(doctor, details, session);

                    setCookies(entrdEmail, session, response);
                    response.sendRedirect(request.getContextPath() + "/DoctorServlet.do");
                    break;
                default:
                    request.setAttribute("errorMsg", "Login failed - account type not recognised.");
                    request.getRequestDispatcher("views/login.jsp").forward(request, response);
                    break;
            }
        }
        else
        {   //print error message in the event that we cannot find the account
            request.setAttribute("errorMsg", "Login failed - account not found.");
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
        }

    }

    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for the login page";
    }// </editor-fold>


    private void setCookies(String user, HttpSession session, HttpServletResponse response)
    {
        //setting session to expiry in 30 mins
        session.setMaxInactiveInterval(30*60);
        Cookie userName = new Cookie("user", user);
        userName.setMaxAge(30*60);
        response.addCookie(userName);
    }


}
