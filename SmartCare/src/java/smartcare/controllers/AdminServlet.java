/*
Class: AdminServlet
Description: the servlet for handing admin interactions
Created: 14/12/2020
Updated: 16/12/2020
Author/s: Asia Benyadilok
*/
package smartcare.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smartcare.models.database.Jdbc;

/**
 *
 * @author asia
 */
public class AdminServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
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
     
           /*
    Method: getPatientDetail
    Description: method to get patient detail
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
     private HttpServletRequest getWeeklyDocument(HttpServletRequest request){
        
        
        //create obj of database and get session
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
        
       //get current date
       LocalDate currentDate = java.time.LocalDate.now();
       Date date= java.sql.Date.valueOf(currentDate);
       //get calendar
       Calendar cl = Calendar. getInstance();
       //set date
       cl.setTime(date);
       //get week num
       int weekNum = cl.WEEK_OF_YEAR;
       String patientDetail = null;
       String turnover = null;
       String payPrivate = null;
       String payNHS = null;
       
       try 
       {
           
           //get patient detail from database
           turnover = jdbc.getAllResultSet("amount", "weeknum ="+weekNum, "invoice",1);
           payPrivate = jdbc.getAllResultSet("paymenttype", "(weeknum ="+weekNum+" AND paymenttype = 'Private')", "invoice",1);
           payNHS =  jdbc.getAllResultSet("paymenttype", "(weeknum ="+weekNum+" AND paymenttype = 'NHS')", "invoice",1);
           
           //calculate turnover
           int totalTurnover =0;
           String amount[] = turnover.split(" ");
           for (int i=0;i<amount.length;i++)
           {
               totalTurnover += Integer.parseInt( amount[i]) ;          
           }
           
           //count paymenttypes
           int privateCount = payPrivate.split(" ").length;
           int nhsCount = payNHS.split(" ").length;
           
           
           if(turnover.equals(""))
           {
               session.setAttribute("turnover","turnover: 0 <br/> private payment: 0 <br/> pay through NHS: 0");
           }
           else
           {
               session.setAttribute("turnover","this week turn over: "+totalTurnover+"<br/>"+
                                                                   "private payment: "+privateCount+"<br/>"+ 
                                                                   "NHS payment: "+nhsCount+"<br/>" );
               /*
               String detailList [] = patientDetail.split(" ");

               session.setAttribute("patientDetail","Patient Name: "+detailList[0]+"<br/>"+
                                                    "Patient Surname: "+detailList[1]+"<br/>"+
                                                    "Date of Birth: "+detailList[2]+"<br/>");
               */
           }
       }
       
       catch(Exception e)
       {
           session.setAttribute("patientDetail","Patient not found!");
       }
     
        
       return request;
        
    }
     
      /*
    Method: createInvoice
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
    private HttpServletRequest createInvoice(HttpServletRequest request){
        
        
        //create obj of database and get session
        Jdbc jdbc = new Jdbc();
        HttpSession session = request.getSession();
        
        
       //get parameters from prescription form
       String patientID = request.getParameter("patientID");
       String service = request.getParameter("services");
       String detail = request.getParameter("detail");
       String amount = request.getParameter("amount");
       String paymenttype = request.getParameter("paymenttype");
       boolean status = false;

       //get current date
       LocalDate currentDate = java.time.LocalDate.now();
       Date date= java.sql.Date.valueOf(currentDate);
       //get calendar
       Calendar cl = Calendar. getInstance();
       //set date
       cl.setTime(date);
         //get week num
       int weekNum = cl.WEEK_OF_YEAR;
       
       //validate the patient id
       String validation = jdbc.getResultSet("firstname, lastname, dob", "(uuid = "+patientID+" AND usertype = 'P')", "users",3);
       
       if (!validation.equals(""))
       {
        //Add details of prescription to database
        String table = "invoice (servicetype, detail, amount, patientid, issuedate, weeknum, paymenttype)";
        String values = "('"  + service + "', '"+ detail+ "', "+ amount + ", " + patientID+",'"+currentDate.toString()+"',"+weekNum+",'"+paymenttype+"')";


         int success = jdbc.addRecords(table, values);

         //check if the database is successfully updated or not
         if(success != 0)
         {
             session.setAttribute("updateSuccess", "The invoice has been added!");
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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String viewPath = "/views/landing/adminLanding.jsp";
        
         //get action type from the admin landing
        String action = request.getParameter("action");
        
        if (action.equals("Get patient details"))
        {
            request = getPatientDetail(request);
        }
        else if(action.equals("Issue Invoice"))
        {
            request = createInvoice(request);
        }
        else if(action.equals("Produce Weekly Documents"))
        {
            request = getWeeklyDocument(request);
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
