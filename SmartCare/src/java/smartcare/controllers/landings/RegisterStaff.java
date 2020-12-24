/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.controllers.landings;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.database.Jdbc;

/**
 *
 * @author Michael
 */
public class RegisterStaff extends HttpServlet {

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
        
        String viewPath = "views/landing/adminLanding.jsp";
        
        HttpSession session = request.getSession();
        
        //get parameters from form
        String firstname = request.getParameter("new_acc_firstname");
        String lastname = request.getParameter("new_acc_lastname");
        String dob = request.getParameter("new_acc_dob");
        String phone = request.getParameter("new_acc_phone");
        String email = request.getParameter("new_acc_email");
        String address = request.getParameter("new_acc_address");
        String password = request.getParameter("new_acc_password");
        String userType = request.getParameter("new_acc_type");
   
        //Add to database
        String table = "users (firstname, lastname, usertype, dob, phone, email, address, password)";
        String values = "('"  + firstname + "','" + lastname + "', '"+ userType
                              + "', '" + dob + "', '" + phone +"', '"
                              + email + "', '" + address + "', '" + password +"')";
        
        
        int success = jdbc.addRecords(table, values);
        if(success != 0){
            request.setAttribute("updateSuccess", "The account has been added!");
        }else{
            request.setAttribute("updateSuccess", "There has been a problem.");
        }
        
        RequestDispatcher view = request.getRequestDispatcher(viewPath);
        view.forward(request,response);
        
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
