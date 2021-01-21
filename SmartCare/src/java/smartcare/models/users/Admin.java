
package smartcare.models.users;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import smartcare.models.database.Jdbc;
import smartcare.models.Appointment;
import smartcare.models.Invoice;
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

        /**
    * Returns list of fees .
    * Retrieves data from Fees table in the database and returns it
    *
    * @return     list of fees.
    */
    public ArrayList<Fees> getFees(){
        ArrayList<Fees> feesList = new ArrayList<>();
        ArrayList<String> r;

        int numOfColumns = 3;
        String column = "price, period, service_type";
        String table = "Fees";
        String condition = "1=1";

        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);

        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            String id = r.get(i);
            String price = r.get(i);
            String period = r.get(i+1);
            String serviceType = r.get(i+2);

            Fees fee = new Fees(Integer.parseInt(id),Integer.parseInt(price), Integer.parseInt(period), serviceType);

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
    public int updateFees(int price, int period, String serviceType){

        int success = jdbc.updateQuery("Update Fees Set price = " +price+", period = "+period+" WHERE service_type = '" + serviceType+"'");

        return success;
    }
    
     public String calTurnover(String startDate,String endDate) 
    {
        
        String result;
        
        int turnoverNum = 0;
        int payPrivateNum = 0;
        int payNHSNum = 0;
        
        
        //compare dates input 
        Date sdate= java.sql.Date.valueOf(startDate);
        Date edate= java.sql.Date.valueOf(endDate);
        
        //convert date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String sDate = simpleDateFormat.format(sdate);
        String eDate = simpleDateFormat.format(edate);

        //if start date is greater than end date
        if (sdate.compareTo(edate) > 0) 
        {          
            result = "Error selected period!";       
        }     
        
        else
        {
            //invoice detail from database
            ArrayList<String> turnover = this.jdbc.getResultList("amount", "(issuedate >= '"+startDate+"' AND issuedate <= '"+endDate+"')", "invoice",1);
            ArrayList<String> payPrivate = this.jdbc.getResultList("paymenttype", "(issuedate >= '"+startDate+"' AND issuedate <= '"+endDate+"' AND paymenttype = 'Private')", "invoice",1);
            ArrayList<String> payNHS =  this.jdbc.getResultList("paymenttype", "(issuedate >= '"+startDate+"' AND issuedate <= '"+endDate+"' AND paymenttype = 'NHS')", "invoice",1);

            //calculate turnover over the period selected
            if ((turnover.size() != 0))
            {
                for(int i=0;i<turnover.size();i++)
                {
                    turnoverNum += Integer.parseInt(turnover.get(i));
                }
            }

            //count number per payment type
            payPrivateNum = payPrivate.size();
            payNHSNum = payNHS.size();
            

            if(turnoverNum == 0)
            {
                result = "turnover: 0 <br/> private payment: 0 <br/> pay through NHS: 0";
            }
            else
            {
                result = "From "+sDate+" To "+eDate+"<br/> turn over: "+turnoverNum+"<br/>"+
                                                                    "private payment: "+payPrivateNum+"<br/>"+ 
                                                                    "NHS payment: "+payNHSNum+"<br/>" ;
            }
        }
        
        return result;
    }
      
    
    public String createInvoice(Invoice invoice)
    {
        String result;
      
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
        String table = "invoice (servicetype, detail, amount, patient_username, issuedate, weeknum, paymenttype)";
        String values = "('"  + invoice.getService() + "', '"+ invoice.getDetail()+ "', "+ invoice.getAmount() + ", '" + invoice.getPatientID()+"','"+currentDate+"',"+weekNum+",'"+invoice.getPaymenttype()+"')";

        int success = jdbc.addRecords(table, values);
        
        //validate the patient id
        ArrayList<String> validation = jdbc.getResultList("firstname, lastname, dob", "(username ='"+invoice.getPatientID()+"' AND usertype = 'P')", "users",3);

        if(validation.size() != 0)
        {
            //check if the database is successfully updated or not
            if(success != 0)
            {
                result = "The invoice has been added!";
            }
            else
            {
                result =  "There has been a problem!";
            }
        }
        else
        {
            result =  "Patient not found!";
        }
     
         return result;
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
    

    
}
