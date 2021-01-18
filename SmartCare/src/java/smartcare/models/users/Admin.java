
package smartcare.models.users;

import java.util.ArrayList;
import smartcare.models.database.Jdbc;
import smartcare.models.Appointment;
public class Admin extends User
{    
    
    Jdbc jdbc;
    
    public Admin(){
        super();
        this.jdbc = Jdbc.getJdbc();
    }
    
    /**
    * Returns all of the appointments scheduled.
    * Retrieves data from Appointments table in the database and returns it
    *
    * @return      All of the appointments scheduled for every user.
    */
    public ArrayList<Appointment> getAppointments(){
        ArrayList<Appointment> appointmentList = new ArrayList<>();
        ArrayList<String> r;
        
        int numOfColumns = 5;
        String column = "appointmentid, starttime, endtime, appointmentdate, comment";
        String table = "Appointments";
        String condition = "1=1" + " ORDER BY appointmentdate ASC, starttime ASC";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Appointment app = new Appointment(r.get(i), r.get(i+1), r.get(i+2), r.get(i+3), r.get(i+4), this.getUserID());
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
}
