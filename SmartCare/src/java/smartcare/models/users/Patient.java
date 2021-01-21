package smartcare.models.users;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import smartcare.models.Appointment;
import smartcare.models.Invoice;
import smartcare.models.database.Jdbc;

public class Patient extends User
{
    Jdbc jdbc;
    
    public Patient(){
        super();
        this.jdbc = Jdbc.getJdbc();
    }
    
    /**
    * Returns all of the appointments scheduled for this user.
    * Retrieves data from Appointments table in the
    * database and returns it.
    *
    * @return      All of the appointments scheduled for this user.
    */
    public ArrayList<Appointment> getAppointments(){
        ArrayList<Appointment> appointmentList = new ArrayList<>();
        ArrayList<String> r;
        
        int numOfColumns = 5;
        String column = "appointmentid, starttime, endtime, appointmentdate, comment";
        String table = "Appointments";
        String condition = "patient_username = '" + this.getUsername() + "'";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Appointment app = new Appointment(r.get(i), r.get(i+1), r.get(i+2), r.get(i+3), r.get(i+4), this.getUsername());
            appointmentList.add(app);
        }
        
        return appointmentList;
    }
    
    /**
    * Deletes a specified appointment.
    * Deletes an appointment by id and returns an success message.
    *
    * @param appointmentId The id of appointment in the database.
    * @return      Whether the deletion was successful or not.
    */
    public String deleteAppointment(String appointmentId){
        int success = this.jdbc.delete("Appointments", "appointmentId = " + appointmentId);
        String deleteSuccess = new String();
        if(success == 0){
            deleteSuccess = "Failed to cancel appointment";
        }else{
            deleteSuccess = "Successfully cancelled appointment";
        }
        return deleteSuccess;
    }
    
    /**
    * Adds an appointment to the appointment table in the database.
    * Adds an appointment to the appointment table and returns 
    * a success message.
    *
    * @param startTime start time of the appointment.
    * @param date The date of the appointment.
    * @param comment The reason of the appointment.
    * @param locationID the id of the location of the appointment.
    * @return      String with a success message about adding the appointment.
    */
    public String addAppointment(String startTime, String date, String comment, String locationID, String staffID){
        String updateSuccess;
        String endtime = startTime;
        
        LocalTime tS = LocalTime.parse(startTime);
        LocalTime tE = LocalTime.parse(startTime).plusMinutes(30);
        
        //change format
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String sStart = tS.format(myFormatObj);
        String sEnd = tE.format(myFormatObj);
    
        //check if that time slot is free (not implemented yet)
        
        //sanitize the comment input
        comment = comment.replace("'", "''");
        //check the length of the comment
        if(comment.length() > 50){
            comment = comment.substring(0, Math.min(comment.length(), 50));
        }
        
        //Add to database
        String table = "appointments (appointmentdate, starttime, endtime, comment, patient_username, doctor_username, locationID)";
        String values = "('"  + date + "', '"+ sStart+ "', '" 
                + sEnd + "', '" + comment + "', '"+ this.getUsername()+ "', '"+ staffID+ "', "+locationID+")";
        
        int success = this.jdbc.addRecords(table, values);
        if(success != 0){
            updateSuccess = "The appointment has been scheduled!";
        }else{
            updateSuccess = "There was a problem adding an appointment";
        }
        
        return updateSuccess;
    }

    public ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoiceList = new ArrayList<>();
        ArrayList<String> r;
        
        int numOfColumns = 6;
        String column = "invoiceid, servicetype, detail, amount, issuedate, paymenttype";
        String table = "Invoice";
        String condition = "patient_username = '" + this.getUsername() + "' AND paid='0' ORDER BY invoiceid ASC, issuedate ASC";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Invoice app = new Invoice(r.get(i), r.get(i+1), r.get(i+2), r.get(i+3),this.getUsername(), r.get(i+4), r.get(i+5));
            invoiceList.add(app);
        }
        
        return invoiceList;
    }
    
        public String payInvoice(String invoiceID) {
        int success = this.jdbc.update("Invoice", "invoiceid = " + invoiceID, "paid='1'");
        String deleteStatus = new String();
        if (success == 0) {
            deleteStatus = "Failed to pay invoice";
        } else {
            deleteStatus = "Invoice paid successfully";
        }
        return deleteStatus;

    }

    
}
