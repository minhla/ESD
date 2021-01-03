package smartcare.models.users;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import smartcare.models.Appointment;
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
}
