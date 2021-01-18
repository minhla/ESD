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
