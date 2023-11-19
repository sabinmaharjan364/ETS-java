/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.ets.app;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sabinmaharjan
 */
public class DBConn {
    String url="jdbc:mysql://localhost:3306/ETS?serverTimezone=Australia/Sydney";
    String user="root";
    String password="";
    Connection con = null;
    
    Statement stmt = null;
    ResultSet rs = null;
    QueryInterfce sqlinterfce;
	
     public Connection getConnection(){
       
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(url,user,password);
            return con;
        }catch(Exception error){
            error.printStackTrace();
            throw new RuntimeException();
        }
    }
     
    public void executeSQLQuery(QueryInterfce intrfce,String query, String message) {
        sqlinterfce=intrfce;
        try{
            stmt=con.createStatement();
            
            if(stmt.executeUpdate(query)==1){
                sqlinterfce.onQueryExec(message);
            }else{
                sqlinterfce.onError("Couldnot execute query");
            }
            
        }catch(HeadlessException | SQLException error){
            error.printStackTrace();
            sqlinterfce.onError(error.getMessage());
        }
    }
	
    
    
}
