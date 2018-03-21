package com.toyide.csci3130_project;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JingyunYang on 2018/3/16.
 */
/**
 * Registration Class that defines how we access the user ID and the corresponding course ID they slected
 * project Firebase databse. This is converted to a JSON format
 */
public class Registration implements Serializable {     //registeration ID,
    public String UserID;                          //user ID
    public String CourseID;                   //a list containing coruse reference IDs
    //constructor

    public Registration(){
        UserID = "";
        CourseID = "";
    }
    public  Registration( String uid, String cidList){
        this.UserID = uid;
        this.CourseID = cidList;
    }
    //remove empty at first
    public void add_withFirst(String add) {
        this.CourseID += add;
        this.CourseID += ",";
    }



    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("UserID", UserID);
        result.put("CourseID", CourseID);
        return result;
    }


    // Return a list of courses in the form of array
    public String[] getCourses() {
        return CourseID.split(",");
        //NOTICE: the last string element is "".
    }
}


