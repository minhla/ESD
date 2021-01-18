/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;
import smartcare.models.users.Nurse;
import smartcare.models.users.Doctor;
import java.util.ArrayList;
import smartcare.models.database.Jdbc;

/**
 *
 * @author jitojar
 */
public class Helper {
    
    Jdbc jdbc;
    
    public Helper(){
        super();
        this.jdbc = Jdbc.getJdbc();
    }
    
    public ArrayList<Location> getLocations(){
        ArrayList<Location> locationList = new ArrayList<>();
        ArrayList<String> r;
        
        int numOfColumns = 5;
        String column = "locationid, name, type, lat, lng";
        String table = "Locations";
        String condition = "1=1";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Location loc = new Location(r.get(i), r.get(i+1), r.get(i+2), r.get(i+3), r.get(i+4));
            locationList.add(loc);
        }
        
        return locationList;
    }
    
    public ArrayList<Doctor> getDoctors(){
        ArrayList<Doctor> doctorList = new ArrayList<>();
        ArrayList<String> r;
        
        int numOfColumns = 3;
        String column = "uuid, firstname, lastname";
        String table = "Users";
        String condition = "usertype = 'D'";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Doctor doc = new Doctor(r.get(i), r.get(i+1), r.get(i+2));
            doctorList.add(doc);
        }
        
        return doctorList;
    }
    
    public ArrayList<Nurse> getNurses(){
        ArrayList<Nurse> nurseList = new ArrayList<>();
        ArrayList<String> r;
        
        int numOfColumns = 3;
        String column = "uuid, firstname, lastname";
        String table = "Users";
        String condition = "usertype = 'N'";
        
        //Get all of the appointments for this user
        r = this.jdbc.getResultList(column, condition, table, numOfColumns);
        
        //Get the received data and put it into appointment objects
        for(int i = 0; i < r.size(); i+=numOfColumns){
            Nurse nurse = new Nurse(r.get(i), r.get(i+1), r.get(i+2));
            nurseList.add(nurse);
        }
        
        return nurseList;
    }
}
