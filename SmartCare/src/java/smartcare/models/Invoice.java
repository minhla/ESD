/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import smartcare.models.database.Jdbc;

/**
 *
 * @author asia
 */
public class Invoice {
    
    String patientID;
    String service;
    String detail;
    String amount;
    String paymenttype;  
    
    //get jdbc object
    Jdbc jdbc = Jdbc.getJdbc();
    
    public Invoice(){}
    
    public Invoice(String patientID, String service, String detail, String amount,String paymenttype) 
    {  
        this.patientID = patientID;
        this.service = service;
        this.detail = detail;
        this.amount = amount;
        this.paymenttype = paymenttype; 
    }
    
    public int createInvoice() 
    
    {
       //get current date
       LocalDate currentDate = java.time.LocalDate.now();
       Date date= java.sql.Date.valueOf(currentDate);
       //get calendar
       Calendar cl = Calendar. getInstance();
       //set date
       cl.setTime(date);
         //get week num
       int weekNum = cl.WEEK_OF_YEAR;
       
       //Add details of prescription to database
        String table = "invoice (servicetype, detail, amount, patientid, issuedate, weeknum, paymenttype)";
        String values = "('"  + this.service + "', '"+ this.detail+ "', "+ this.amount + ", " + this.patientID+",'"+currentDate+"',"+weekNum+",'"+paymenttype+"')";

        int success = jdbc.addRecords(table, values);

        return success;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getService() {
        return service;
    }

    public String getDetail() {
        return detail;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaymenttype() {
        return paymenttype;
    }
    
}