/*
Class: RegisterStaff
Description: allows Admin users to handle the registration of staff members.
Created: 10/12/2020
Updated: 28/12/2020
Author/s: Michael Tonkin
*/
package smartcare.controllers.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.Account;
import smartcare.models.database.Jdbc;
import smartcare.models.util.RegistrationUtils;


public class RegisterStaff extends HttpServlet 
{

    private Jdbc jdbc = Jdbc.getJdbc();
    private RegistrationUtils ru = new RegistrationUtils();
    
    
    /*
    Method: userExists
    Description: checks if a username exists in the database.
    Params: String username - the username to be searched for.
    Returns: True if username already exists. Otherwise it will return false. 
    */
    private boolean userExists(String username)
    {
        ArrayList<String> existingUser;
        
        try
        {
            existingUser = jdbc.getResultList("username", "username = '" + username + "'", "USERS", 1);
        }
        catch(IllegalStateException e )
        {
            existingUser = new ArrayList<String>();
        }
        
        if(existingUser.isEmpty())
            return false;
        else
            return true;
    }
    
    /*
    Method: getUsernameNumOccur
    Description: Returns the position of the first number in a string. Should be
                 used in the creation of usernames.
    Parameters: String username - the string to search for number index.
    Returns: Int - the index position of the first number in username. 
    */
    private int getUsernameNumOccur(String username)
    {
        for (int x = 0; x < username.length(); x++)
        {
            if(Character.isDigit(username.charAt(x)))
            {
                return x;
            }
        }
        return 0;
    }
    
    private String incrementUsername(String username)
    {
        int firstInt = getUsernameNumOccur(username);
        return username.substring(0, firstInt) + 
        Integer.toString((Integer.parseInt(username.substring(firstInt, username.length())) + 1));
    }
    
    /*
    Method: usernameHasNum
    Description: checks if a given username has a number at the end of it.
    Params: String username - the username to be checked.
    Returns: True if username has a number at the end. Otherwise it will return false. 
    */
    private String usernameWithNum(String username, int index)
    {
        //search for username+1 in db
        //if it exists repeat the search
        //if it does not exist, return username + index (unless index is 0)
        if(userExists(username + Integer.toString(index)))
            return usernameWithNum(username, index + 1);
        else if(index == 0)
            return username;
        
        return username + index;
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
        response.setContentType("text/html;charset=UTF-8");
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
        
        Account ac = new Account();
        
        String viewPath = "/views/landing/adminLanding.jsp";
                
        //get parameters from form
        String firstname = request.getParameter("new_acc_firstname");
        String lastname = request.getParameter("new_acc_lastname");
        String username = firstname.charAt(0) + "-" + lastname;
        username = username.toLowerCase();
        String dob = request.getParameter("new_acc_dob");
        String phone = request.getParameter("new_acc_phone");
        String email = request.getParameter("new_acc_email");
        String address = request.getParameter("new_acc_address");
        String password = ru.dateToPassword(dob);
        String userType = request.getParameter("new_acc_type");

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime date = LocalDateTime.now();
        String regdate = date.format(dtFormatter);
        
        if(userExists(username)) //increment the number at the end of username if the username already exists
        {
            System.out.println("user by the name of " + username + " already exists. Attempting username incrementation...");
            
            username = usernameWithNum(username, 1); //find the version of this username with the highest number at the end
            username = incrementUsername(username);
            
            System.out.println("New username is: " + username);
        }
        
        //Add to database
        String table = "users (username, firstname, lastname, usertype, dob, phone, email, address, password, regdate)";
        String values = "('" + username  + "', '"+  firstname + "','" + lastname + "', '"+ userType
                              + "', '" + dob + "', '" + phone +"', '"
                              + email + "', '" + address + "', '" + ac.generatePasswordHash(password)  + "', '" + regdate +"')";

 
        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "The account has been added!");
            System.out.println("Account added");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
            System.out.println("Account could not be added");
        }
        
        System.out.println(request.getContextPath() + viewPath);
        response.sendRedirect(request.getContextPath() + viewPath);
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
