/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;

/**
 *
 * @author jitojar
 */
public class Appointment {
    String ID;
    String starttime;
    String endtime;
    String date;
    String comment;
    String userID;

    public Appointment(String ID, String starttime, String endtime, String date, String comment, String userID) {
        this.ID = ID;
        this.starttime = starttime;
        this.endtime = endtime;
        this.date = date;
        this.comment = comment;
        this.userID = userID;
    }
    
    public Appointment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    
    
    
}
