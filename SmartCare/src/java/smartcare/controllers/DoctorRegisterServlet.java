package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import smartcare.dao.DoctorDAO;
import smartcare.models.Doctor;

/**
 *
 * @author minh-la
 */
@WebServlet("/register")
public class DoctorRegisterServlet extends HttpServlet {

    private DoctorDAO DoctorDAO;

    public void init() {
        DoctorDAO = new DoctorDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String doctorID = request.getParameter("id");
        String firstname = request.getParameter("firstName");
        String lastname = request.getParameter("lastName");
        String userType = "D"; //DOCTOR
        String DOB = request.getParameter("DOB");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        
        Doctor doctor = new Doctor();
        doctor.setDoctorID(doctorID);
        doctor.setFirstName(firstname);
        doctor.setLastName(lastname);
        doctor.setUserType(userType);
        doctor.setDOB(DOB);
        doctor.setPhone(phone);
        doctor.setEmail(email);
        doctor.setAddress(address);
        doctor.setPassword(password);
        
        try {
            DoctorDAO.registerDoctor(doctor);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        response.sendRedirect("registration/registersuccess.jsp");
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/web/views/registration/registersuccess.jsp");
//        dispatcher.forward(request, response);
    }

}
