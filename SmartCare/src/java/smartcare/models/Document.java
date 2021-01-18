/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;

import java.sql.Date;
import java.util.ArrayList;
import smartcare.models.database.Jdbc;

/**
 *
 * @author asia
 */
public class Document {
    
    int turnoverNum = 0; 
    int payPrivateNum = 0;
    int payNHSNum = 0;
    
        
    //get jdbc object
    Jdbc jdbc = Jdbc.getJdbc();
   
    
    public Document(){}
    
    public Document(int turnover, int payPrivate, int payNHS) 
    {  
        this.turnoverNum = turnover;
        this.payPrivateNum = payPrivate;
        this.payNHSNum = payNHS;

    }
    
    public void setTurnoverNum(int turnoverNum) {
        this.turnoverNum = turnoverNum;
    }

    public void setPayPrivateNum(int payPrivateNum) {
        this.payPrivateNum = payPrivateNum;
    }

    public void setPayNHSNum(int payNHSNum) {
        this.payNHSNum = payNHSNum;
    }

    public void setJdbc(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public int getTurnoverNum() {
        return turnoverNum;
    }

    public int getPayPrivateNum() {
        return payPrivateNum;
    }

    public int getPayNHSNum() {
        return payNHSNum;
    }

    public Jdbc getJdbc() {
        return jdbc;
    }
    

    
    
    
    
}
