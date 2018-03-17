package com.toyide.csci3130_project;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aya on 2018-03-17.
 */

public class login implements Serializable {
    public String username;
    public String password;

    public login() {

    }

    public login( String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUserName(){
        return this.username;
    }
    public String getPassword(){
        return  this.password;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        return result;
    }
}