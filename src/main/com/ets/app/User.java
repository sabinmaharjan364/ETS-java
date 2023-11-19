/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.ets.app;

/**
 *
 * @author sabinmaharjan
 */
public class User {
    private int id;
    private String username,password;
    public static int user_session_id ;
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static int getUser_session_id() {
        return user_session_id;
    }

    public static void setUser_session_id(int user_session_id) {
        User.user_session_id = user_session_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    
}
