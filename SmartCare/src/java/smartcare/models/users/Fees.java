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
    
    private int price;
    private int period;

    public Fees() {
    }

    public Fees(int price, int period) {
        this.price = price;
        this.period = period;
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
    
    
}
