package smartcare.models.users;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import smartcare.models.Appointment;
import smartcare.models.Prescription;
import smartcare.models.database.Jdbc;

public class Doctor extends User
{
    
    Jdbc jdbc;
    
    public Doctor(){
        super();
        this.jdbc = Jdbc.getJdbc();
    }
    
    public Doctor(String id, String name, String lastname){
        super(id, name, lastname);
    }
    
    
    /**
    * Returns all of the appointments scheduled for today.
    * Retrieves data from database and returns it.
    *
    * @return      The appointments scheduled for current date
    */
    public ArrayList<Appointment> getAppointments(){
        ArrayList<Appointment> appointmentList = new ArrayList<>();
        ArrayList<String> r = new ArrayList<>();
        
        //get current date
        LocalDate currentDate = java.time.LocalDate.now();
        
        //retreve all available appointments from database
        int numOfColumns = 5;
        String column = "appointmentid, comment, starttime, endtime, appointmentdate";
        String table = "Appointments";
        String condition = "appointmentdate = '" + currentDate.toString()+"' ORDER BY starttime ASC";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Appointment app = new Appointment(r.get(i), r.get(i+1), r.get(i+2), r.get(i+3), r.get(i+4));
            appointmentList.add(app);
        }
        
        return appointmentList;
    }
    
     public String getPatientDetails(String patientID)
    {
        String result;
        //get patient detail from database
        ArrayList<String> patientDetail = jdbc.getResultList("firstname, lastname, dob", "(username = '"+patientID+"' AND usertype = 'P')", "users",3);
        
        if(patientDetail.size() == 0)
        {
            result = "Patient not found!";
        }
        else
        {

            result = "Patient Name: "+patientDetail.get(0)+"<br/>"+
                    "Patient Surname: "+patientDetail.get(1)+"<br/>"+
                    "Date of Birth: "+patientDetail.get(2)+"<br/>";         
        }
        
        return result;
    }
     
      public String createPrescription(Prescription prescription)
    {
       String result;  

       //validate the patient id
       ArrayList<String> validation = jdbc.getResultList("firstname, lastname, dob", "(username = '"+prescription.getPatientID()+"' AND usertype = 'P')", "users",3);
       
       if (validation.size() != 0)
       {
           
            //get current date
            LocalDate currentDate = java.time.LocalDate.now();

            //Add details of prescription to database
            String table = "prescription (weight, allergies, medicine, patient_username, issuedate)";
            String values = "("  + prescription.getWeight() + ", '"+ prescription.getAllergies()+ "', '"+ prescription.getMed() + "', '" + prescription.getPatientID()+"','"+ currentDate.toString()+"')";

            //add prescription to the database
            int success = jdbc.addRecords(table, values);
           

            //check if the database is successfully updated or not
            if(success != 0)
            {
                result = "The prescription has been added!";
            }
            else
            {
                result = "There has been a problem.";
            }
       }
       else
       {
            result = "Patient not found!";
       }

        
        return result;
        
    }
    
}
