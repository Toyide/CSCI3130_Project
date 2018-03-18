package com.toyide.csci3130_project;

/**
 * Created by aya on 2018-02-24.
 */

public class checkPassword {
    private String users,  password;
    public boolean check(String name, String pass, String dbuser, String dbpass ){

        if (name.equals(users)){
            if (pass.equals(password)) {
                return true;
            }
        }
        return false;
    }
}
