
package smartcare.util;

public class RegistrationUtils {
   
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
    
}
