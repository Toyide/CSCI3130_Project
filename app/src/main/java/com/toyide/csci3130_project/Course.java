package com.toyide.csci3130_project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SHAN on 18/2/21.
 */

public class Course {
    private  String cid;
    private String courseTitle;     //Eg. CSCI3130
    private String courseType;      //Eg. lecture, lab or tutorial
    private String courseWeekday;   //Eg. Monday, Tuesday
    private String courseTime;      //Eg. 13:20
    private String courseInfo;      //course information Eg. Software Engineering

    //constructor
    public Course(String cid, String courseTitle, String courseType, String courseWeekday, String courseTime, String courseInfo) {
        this.cid = cid;
        this.courseTitle = courseTitle;
        this.courseType = courseType;
        this.courseWeekday = courseWeekday;
        this.courseTime = courseTime;
        this.courseInfo = courseInfo;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getCourseWeekday() {
        return courseWeekday;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public String getCourseInfo() {
        return courseInfo;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("cid", cid);
        result.put("courseTitle", courseTitle);
        result.put("courseType", courseType);
        result.put("courseWeekday", courseWeekday);
        result.put("courseTime", courseTime);
        result.put("courseInfo", courseInfo);
        return result;
    }
}

