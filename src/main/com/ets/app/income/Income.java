/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.ets.app.income;
import java.util.Date;

/**
 *
 * @author sabinmaharjan
 */
public class Income {
    private int id;
    private int quantity,cost;
    private String title, description;   
    private String category;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }    

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCost() {
        return cost;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

   
    public Income(int id, int quantity,String category, int cost, String title, String description, Date date) {
        this.id = id;
        this.quantity = quantity;      
        this.category = category;
        this.cost = cost;
        this.title = title;
        this.description = description;
        this.date = date;
    }
    
}
