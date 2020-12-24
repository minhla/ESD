/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models.items;

/**
 *
 * @author Michael Tonkin >:(
 */
public class Prescriptions {
    //create obj of database and get session
        Jdbc jdbc = new Jdbc();
        
    public Presecription(ArrayList<String> attributes)
    {
        setPrice(attributes.get(0));
    }
    /*
    Method: createPrescription
    Description: handle interactions with prescription form
    Params: HttpServletRequest request
    Returns: HttpServletRequest request
    */
    public int createPrescription(){
        
        //this needs to be the method taht accesses the database
                
        
       //get parameters from prescription form
       String patientID = request.getParameter(getPatientID());
       String weight = request.getParameter("weight");
       String allergies = request.getParameter("allergies");
       String med = request.getParameter("med");
       //get current date
       LocalDate currentDate = java.time.LocalDate.now();

       //validate the patient id
       String validation = jdbc.getResultSet("firstname, lastname, dob", "(uuid = "+patientID+" AND usertype = 'P')", "users",3);
       
       if (!validation.equals(""))
       {
        //Add details of prescription to database
        String table = "prescription (weight, allergies, medicine, patientid, issuedate)";
        String values = "("  + weight + ", '"+ allergies+ "', '"+ med + "', " + patientID+",'"+currentDate.toString()+"')";


         int success = jdbc.addRecords(table, values);
         
        return success;
        
    }
    
}
