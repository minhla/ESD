package smartcare.controllers.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Account;
import smartcare.models.Registration;
import smartcare.models.database.Jdbc;
import smartcare.models.users.User;

/**
 *
 * @author David
 */
@WebServlet(name = "/AddPatient")
public class RegisterPatient extends HttpServlet {

        private Jdbc jdbc = Jdbc.getJdbc();
        private Registration reg = new Registration();
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
        response.setContentType("text/html;charset=UTF-8");
        
        Account ac = new Account();
        
        String viewPath = "views/registration/newPatient.jsp";

        HttpSession session = request.getSession();

        //get parameters from form
        String title = request.getParameter("titles");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String username = firstname.charAt(0) + "-" + lastname;
        username = username.toLowerCase();
        String dob = request.getParameter("dob");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String password = reg.dateToPassword(dob);

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime date = LocalDateTime.now();
        String regdate = date.format(dtFormatter);

         //check that username doesn't already exist
        ArrayList<String> existingUser = jdbc.getResultList("username", "username = '" + username + "'", "USERS", 1);
        
        if(reg.userExists(username)) //increment the number at the end of username if the username already exists
        {
            System.out.println("user by the name of " + username + " already exists. Attempting username incrementation...");
            
            username = reg.usernameWithNum(username, 1); //find the version of this username with the highest number at the end
            username = reg.incrementUsername(username);
            
            System.out.println("New username is: " + username);
        }
        
        //Add to database
        String table = "users (title, username, firstname, lastname, usertype, dob, phone, email, address, password, regdate)";
        String values = "('" + title  + "', '" + username  + "', '"+  firstname + "','" + lastname + "', '"+ "P"
                              + "', '" + dob + "', '" + phone +"', '"
                              + email + "', '" + address + "', '" + ac.generatePasswordHash(password)  + "', '" + regdate +"')";


        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "You have register successfully. Head to the <a href='/SmartCare'>Login page</a> to sign in.");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
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
