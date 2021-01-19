/*
Class: login
Description: the servlet for handing login interactions
Created: 01/12/2020
Updated: 24/12/2020
Author/s: Michael Tonkin
*/
package smartcare.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Account;
import smartcare.models.users.Admin;
import smartcare.models.users.Nurse;
import smartcare.models.users.Doctor;
import smartcare.models.users.Patient;
import smartcare.models.users.User;
import smartcare.models.database.Jdbc;


@WebServlet(name = "Login", urlPatterns = {"/Login.do"})
public class Login extends HttpServlet {

    private Jdbc db = Jdbc.getJdbc();
    private Account ac = new Account();


    private HttpSession setupUserSession(User user, HttpSession session)
    {
        session.setAttribute("user", user);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userType", user.getUserType());
        System.out.println("session username => "+ session.getAttribute("username"));
        return session;
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

        //attempt a login
        if(ac.loginStmt("Users", entrdEmail, entrdPass, db))
        {
            ArrayList<String> details = db.getResultList("username, usertype, firstname, lastname", "email='" + entrdEmail + "'", "USERS", 4);
            
            System.out.println(details.toString());
            
            User user = new User();
            user.setUsername(details.get(0));
            user.setEmail(entrdEmail);
            user.setUserType(details.get(1));
            user.setName(details.get(2) + " " + details.get(3));
            
            //send to a different landing page depending on the user's account type.
            String accType = user.getUserType();
            switch(accType)
            {
                case "A": //admin

                    Admin admin = new Admin();
                    admin.setUserType("A");
                    admin.setUsername(user.getUsername());
                    session = setupUserSession(admin, session);

                    setCookies(user.getUsername(), session, response);
                    response.sendRedirect(request.getContextPath() + "/AdminServlet.do");
                    break;

                case "P": //patient

                    Patient patient = new Patient();
                    patient.setUserType("P");
                    patient.setUsername(user.getUsername());
                    patient.setName(user.getName());
                    patient.setEmail(user.getEmail());
                    session = setupUserSession(patient, session);

                    setCookies(user.getUsername(), session, response);
                    response.sendRedirect(request.getContextPath() + "/PatientServlet.do");
                    break;

                case "N": //nurse

                    Nurse nurse = new Nurse();
                    nurse.setUserType("N");
                    nurse.setUsername(user.getUsername());
                    session = setupUserSession(nurse, session);

                    setCookies(user.getUsername(), session, response);
                    response.sendRedirect(request.getContextPath() + "/NurseServlet.do");
                    break;
                case "D": //doctor

                    Doctor doctor = new Doctor();
                    doctor.setUserType("D");
                    doctor.setUsername(user.getUsername());
                    doctor.setName(user.getName());
                    doctor.setEmail(user.getEmail());
                    session = setupUserSession(doctor, session);

                    setCookies(user.getUsername(), session, response);
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
        //set the existing session to expire in 30 mins
        session.setMaxInactiveInterval(1800);
        Cookie userName = new Cookie("user", user);
        userName.setMaxAge(1800);
        response.addCookie(userName);
    }


}
