
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
    * Deletes a specified user.
    * Deletes a user by id and returns a success message.
    *
    * @param username The username of a user in the database.
    * @return      Whether the deletion was successful or not.
    */
    public String deleteUser(String username){
        int success = this.jdbc.delete("Users","(username = '" + username+"')");
        //username = 'u-pellizzari'
        String deleteSuccess = new String();
        if(success == 0){
            deleteSuccess = "Failed to delete user";
        }else{
            deleteSuccess = "The user has successfully been deleted";
        }
        return deleteSuccess;
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
    
    
        /*
    Method: userExists
    Description: checks if a username exists in the database.
    Params: String username - the username to be searched for.
    Returns: True if username already exists. Otherwise it will return false. 
    */
    public boolean userExists(String username)
    {
        ArrayList<String> existingUser;
        
        try
        {
            existingUser = jdbc.getResultList("username", "username = '" + username + "'", "USERS", 1);
        }
        catch(IllegalStateException e )
        {
            existingUser = new ArrayList<String>();
        }
        
        if(existingUser.isEmpty())
            return false;
        else
            return true;
    }
    
    /*
    Method: getUsernameNumOccur
    Description: Returns the position of the first number in a string. Should be
                 used in the creation of usernames.
    Parameters: String username - the string to search for number index.
    Returns: Int - the index position of the first number in username. 
    */
    public int getUsernameNumOccur(String username)
    {
        for (int x = 0; x < username.length(); x++)
        {
            if(Character.isDigit(username.charAt(x)))
            {
                return x;
            }
        }
        return 0;
    }
    
    /*
    Method: incrementUsername
    Description: Increases the number at the end of a username
    Parameters: String username - the username to be incremented.
    Returns: String - the new username. 
    */
    public String incrementUsername(String username)
    {
        int firstInt = getUsernameNumOccur(username);
        return username.substring(0, firstInt) + 
        Integer.toString((Integer.parseInt(username.substring(firstInt, username.length())) + 1));
    }
    
    /*
    Method: usernameHasNum
    Description: checks if a given username has a number at the end of it.
    Params: String username - the username to be checked.
    Returns: True if username has a number at the end. Otherwise it will return false. 
    */
    public String usernameWithNum(String username, int index)
    {
        //search for username+1 in db
        //if it exists repeat the search
        //if it does not exist, return username + index (unless index is 0)
        if(userExists(username + Integer.toString(index)))
            return usernameWithNum(username, index + 1);
        else if(index == 0)
            return username;
        
        return username + (index - 1);
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
