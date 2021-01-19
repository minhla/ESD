
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
    public ArrayList<Appointment> getAppointments(String type){
        ArrayList<Appointment> appointmentList = new ArrayList<>();
        ArrayList<String> r;

        int numOfColumns = 5;
        String column = "appointmentid, starttime, endtime, appointmentdate, comment";
        String table = "Appointments A INNER JOIN Locations L ON A.locationID = L.locationID";
        String condition = "L.\"TYPE\" = '"+type+"'" + " ORDER BY appointmentdate ASC, starttime ASC";

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
    * Returns list of fees .
    * Retrieves data from Fees table in the database and returns it
    *
    * @return     list of fees.
    */
    public ArrayList<Fees> getFees(){
        ArrayList<Fees> feesList = new ArrayList<>();
        ArrayList<String> r;

        int numOfColumns = 2;
        String column = "price, period";
        String table = "Fees";
        String condition = "1=1";

        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);

        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            String price = r.get(i);
            String period = r.get(i+1);

            Fees fee = new Fees(Integer.parseInt(price), Integer.parseInt(period));

            feesList.add(fee);
        }

        return feesList;
    }


    /**
    * Updates the price and period of an appointment.
    *
    * @param price and period from the table fees in the database.
    * @return Whether the update was successful .
    */
    public int updateFees(int price, int period){

        String table = "fees (period, price)";
        String values = "('" + period  + "', '"+  price + "')";


        int success = jdbc.updateQuery("Update Fees Set price = " +price+", period = "+period);

        return success;
    }
}
