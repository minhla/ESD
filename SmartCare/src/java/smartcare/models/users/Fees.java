/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models.users;

/**
 *
 * @author David
 */
public class Fees {
    
    private int feeId;
    private int price;
    private int period;
    private String serviceType;

    public Fees() {
    }

   
    public Fees(int price, int period, String serviceType) {
        this.price = price;
        this.period = period;
        this.serviceType = serviceType;
    }

    public Fees(int feeId, int price, int period, String serviceType) {
        this.feeId = feeId;
        this.price = price;
        this.period = period;
        this.serviceType = serviceType;
    }
    
    

    public int getPrice() {
        return price;
    }

    public int getPeriod() {
        return period;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getFeeId() {
        return feeId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    
}
