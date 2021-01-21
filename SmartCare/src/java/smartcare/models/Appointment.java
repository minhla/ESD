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
    String staff;
    String patient;

    public Appointment(String ID, String starttime, String endtime, String date, String comment, String userID) {
        this.ID = ID;
        this.starttime = starttime;
        this.endtime = endtime;
        this.date = date;
        this.comment = comment;
        this.userID = userID;
    }
    
    public Appointment(String ID, String starttime, String endtime, String date, String comment) {
        this.ID = ID;
        this.starttime = starttime;
        this.endtime = endtime;
        this.date = date;
        this.comment = comment;
    }
    
    public Appointment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Appointment{" + "ID=" + ID + ", starttime=" + starttime + ", endtime=" + endtime + ", date=" + date + ", comment=" + comment + ", userID=" + userID + '}';
    }

    public String getID() {
        return ID;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getDate() {
        return date;
    }

    public void setStaff(String staff)
    {
        this.staff = staff;
    }
    
    public String getStaff()
    {
        return staff;
    }
    
    public void setPatient(String patient)
    {
        this.patient = patient;
    }
    
    public String getPatient()
    {
        return patient;
    }
}
