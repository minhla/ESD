/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;

import java.time.LocalDate;
import java.util.ArrayList;
import smartcare.models.database.Jdbc;

/**
 *
 * @author asia
 */
public class Prescription {

    String patientID;
    String weight;
    String allergies;
    String med;   
    //get jdbc object
    Jdbc jdbc = Jdbc.getJdbc();
    
    public Prescription(){}
    
    public Prescription(String patientID, String weight, String allergies, String med) 
    {
        this.patientID = patientID;
        this.weight = weight;
        this.allergies = allergies;
        this.med = med;

    }
    
    public int createPrescription() 
    {
        //get current date
        LocalDate currentDate = java.time.LocalDate.now();
        
        //Add details of prescription to database
        String table = "prescription (weight, allergies, medicine, patientid, issuedate)";
        String values = "("  + this.weight + ", '"+ this.allergies+ "', '"+ this.med + "', " + this.patientID+",'"+ currentDate.toString()+"')";

        //add prescription to the database
        int success = jdbc.addRecords(table, values);


        return success;
    }
    
    public ArrayList<String> reIssuePrescription(String patientID, String issuedate)
    {
        ArrayList<String> result = new ArrayList<String>();
           //get patient detail from database
        ArrayList<String> patientDetail = jdbc.getResultList("firstname, lastname, dob", "(uuid = "+patientID+" AND usertype = 'P')", "users",3);
        ArrayList<String> prescription = jdbc.getResultList("weight, allergies, medicine", "(issuedate = '"+issuedate+"' AND patientid = "+patientID+")","prescription",3);

        if(patientDetail.size() != 0 && patientDetail.size() != 0)
        {
            result.add(patientDetail.get(0));
            result.add(patientDetail.get(1));
            result.add(patientDetail.get(2));
            result.add(prescription.get(0));
            result.add(prescription.get(1));
            result.add(prescription.get(2));
            
        }
            
        return result;
    }
    

    public String getPatientID() {
        return patientID;
    }

    public String getWeight() {
        return weight;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getMed() {
        return med;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setMed(String med) {
        this.med = med;
    }

}
