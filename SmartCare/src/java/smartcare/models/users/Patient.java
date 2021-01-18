package smartcare.models.users;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import smartcare.models.Appointment;
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
    public String addAppointment(String startTime, String date, String comment, String locationID){
        String updateSuccess;
        String endtime = startTime;
                
        //check if that time slot is free (not implemented yet)
        
        //sanitize the comment input
        comment = comment.replace("'", "''");
        //check the length of the comment
        if(comment.length() > 50){
            comment = comment.substring(0, Math.min(comment.length(), 50));
        }
        
        //Add to database
        String table = "appointments (appointmentdate, starttime, endtime, comment, patient_username, locationID)";
        String values = "('"  + date + "', '"+ startTime+ "', '" 
                + endtime + "', '" + comment + "', '"+ this.getUsername()+ "', "+locationID+")";
        
        int success = this.jdbc.addRecords(table, values);
        if(success != 0){
            updateSuccess = "The appointment has been scheduled!";
        }else{
            updateSuccess = "There was a problem adding an appointment";
        }
        
        return updateSuccess;
    }
    
    public String reIssuePrescription(String patientID, String issuedate)
    {
        String result;

        ArrayList<String> details = new ArrayList<String>();
        
        //get patient detail from database
        ArrayList<String> patientDetail = jdbc.getResultList("firstname, lastname, dob", "(username = '"+patientID+"' AND usertype = 'P')", "users",3);
        ArrayList<String> prescription = jdbc.getResultList("weight, allergies, medicine", "(issuedate = '"+issuedate+"' AND patient_username = '"+patientID+"')","prescription",3);

        //check if prescription is valid or not
        if(patientDetail.size() != 0 && prescription.size() != 0)
        {
            details.add(patientDetail.get(0));
            details.add(patientDetail.get(1));
            details.add(patientDetail.get(2));
            
            for (int i=0;i<prescription.size();i++)
            {
                details.add(prescription.get(i));
            }
            
            if(prescription.size() <= 3)
            {
                result ="Prescription <br/>"+  
                        "================= <br/>"+
                        "Patient Name: "+details.get(0)+"<br/>"+
                         "Patient Surname: "+details.get(1)+"<br/>"+
                         "Date of Birth : "+details.get(2)+"<br/>"+
                         "Weight : "+details.get(3)+"<br/>"+
                         "Allergies : "+details.get(4)+"<br/>"+
                         "Medicine : "+details.get(5)+"<br/>";
            }
            else
            {
                 result ="First prescription <br/>"+ 
                         "==================== <br/>"+
                         "Patient Name: "+details.get(0)+"<br/>"+
                         "Patient Surname: "+details.get(1)+"<br/>"+
                         "Date of Birth : "+details.get(2)+"<br/>"+
                         "Weight : "+details.get(3)+"<br/>"+
                         "Allergies : "+details.get(4)+"<br/>"+
                         "Medicine : "+details.get(5)+"<br/><br/>"+
                         "Second prescription <br/>"+
                         "==================== <br/>"+
                         "Weight : "+details.get(6)+"<br/>"+
                         "Allergies : "+details.get(7)+"<br/>"+
                         "Medicine : "+details.get(8)+"<br/>";
            }
            
        }
        else
        {
            result = "Prescription not found!";
        }

        return result;
    }
    
}
