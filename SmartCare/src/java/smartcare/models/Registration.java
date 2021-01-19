package smartcare.models;

import java.util.ArrayList;
import smartcare.models.database.Jdbc;

/*
Class: Registration
Description: Handles the logic in the registration process for both staff and patients.
Created: 19/01/2021
Updated: 19/01/2021
Author/s: Michael Tonkin
*/
public class Registration {
    
    
    Jdbc jdbc;
    
    public Registration(){
        super();
        this.jdbc = Jdbc.getJdbc();
    }
    
        /*
    Method: dateToPassword
    Description: takes the date (usually a registering user's date of birth) and converts it
                 into the ddmmyy format to be used as a password.
    Params: String date - the date to be converted.
    */
    public String dateToPassword(String date)
    {
        String dd = "";
        String mm = "";
        String yy = "";
        String concatinated = date.replace("-", "");
        
        //if the date is in ddmmyyyy format
        if (date.charAt(2) == '-')
        {
        dd = concatinated.substring(0, 2);
        mm = concatinated.substring(2, 4);
        dd = concatinated.substring(6, 8);
        }
        else
        {
        //if date is in yyyymmdd format
        yy = concatinated.substring(2, 4);
        mm = concatinated.substring(4, 6);
        dd = concatinated.substring(6, 8);
        }
        return dd + mm + yy;
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
}
