/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.ets.app.report;

import main.com.ets.app.report.*;

/**
 *
 * @author sabinmaharjan
 */
public class Report {
    private int id;
    private String name, description;

    public Report() {
    }

   

    public int getId() {
        return id;
    }

    public Report(int id,String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public Report(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    
}
