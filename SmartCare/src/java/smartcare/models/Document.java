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
    
    public ArrayList<Integer> calTurnover(String startDate,String endDate) 
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        //convert date to string and remove hyphen
        String sDate = startDate.replaceAll("-","");
        String eDate = endDate.replaceAll("-","");       
        
        //invoice detail from database
        ArrayList<String> turnover = jdbc.getResultList("amount", "(issuedate >= '"+startDate+"' AND issuedate <= '"+endDate+"' AND paid='1')", "invoice",1);
        ArrayList<String> payPrivate = jdbc.getResultList("paymenttype", "(issuedate >= '"+startDate+"' AND issuedate <= '"+endDate+"' AND paymenttype = 'Private' AND paid='1')", "invoice",1);
        ArrayList<String> payNHS =  jdbc.getResultList("paymenttype", "(issuedate >= '"+startDate+"' AND issuedate <= '"+endDate+"' AND paymenttype = 'NHS' AND paid='1')", "invoice",1);
        
        //calculate turnover over the period selected
        if ((turnover.size() != 0))
        {
            for(int i=0;i<turnover.size();i++)
            {
                this.turnoverNum += Integer.parseInt(turnover.get(i));
            }
        }
        
        //count number per payment type
        this.payPrivateNum = payPrivate.size();
        this.payNHSNum = payNHS.size();
        
        result.add(this.turnoverNum);
        result.add(this.payPrivateNum);
        result.add(this.payNHSNum);
        
        
        return result;
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
