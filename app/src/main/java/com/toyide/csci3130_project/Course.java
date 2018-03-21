package com.toyide.csci3130_project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JingyunYang, YideGe on 18/3/17.
 */

public class Course {
    private String CourseTitle;     //Eg. CSCI3130
    private String CourseType;      //Eg. lecture, lab or tutorial
    private String CourseWeekday;   //Eg. Monday, Tuesday
    private String CourseTime;      //Eg. 13:20
    private String CourseInfo;      //course information Eg. Software Engineering
    private String Location;
    private int SpotCurrent;        //number of current enrolled in the course
    private int SpotMax;            //maximum number of students enrolled

    //constructor
    public Course(String courseTitle, String courseType, String courseWeekday, String courseTime, String courseInfo, String location) {
        this.CourseTitle = courseTitle;
        this.CourseType = courseType;
        this.CourseWeekday = courseWeekday;
        this.CourseTime = courseTime;
        this.CourseInfo = courseInfo;
        this.Location = location;

    }

    //constructor for registration
    public Course(String courseTitle, String courseType, String courseWeekday, String courseTime, String courseInfo, String location, int spotCurrent, int spotMax ) {
        this.CourseTitle = courseTitle;
        this.CourseType = courseType;
        this.CourseWeekday = courseWeekday;
        this.CourseTime = courseTime;
        this.CourseInfo = courseInfo;
        this.Location = location;
        this.SpotCurrent = spotCurrent;
        this.SpotMax = spotMax;

    }

    public String getCourseTitle() {
        return CourseTitle;
    }

    public String getCourseType() {
        return CourseType;
    }

    public String getCourseWeekday() {
        return CourseWeekday;
    }

    public String getCourseTime() {
        return CourseTime;
    }

    public String getLocation() {
        return Location;
    }

    public String getCourseInfo() {
        return CourseInfo;
    }

    public int getSpotCurrent() {return SpotCurrent;}

    public int getSpotMax() {return  SpotMax;}

    public void setCourseTitle(String courseTitle) {
        this.CourseTitle = courseTitle;
    }

    public void setCourseType(String courseType) {
        this.CourseType = courseType;
    }

    public void setCourseWeekday(String courseWeekday) {
        this.CourseWeekday = courseWeekday;
    }

    public void setCourseTime(String courseTime) {this.CourseTime = courseTime;}

    public void setCourseInfo(String courseInfo) {
        this.CourseInfo = courseInfo;
    }

    public void setSpotCurrent(int spotCurrent) {this.SpotCurrent = spotCurrent;}

    public void setSpotMax(int spotMax) {this.SpotMax = spotMax;}
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("CourseTitle", CourseTitle );
        result.put("CourseType", CourseType);
        result.put("CourseWeekday", CourseWeekday);
        result.put("CourseTime", CourseTime);
        result.put("CourseInfo", CourseInfo);
        result.put("SpotCurrent", SpotCurrent);
        result.put("Location", Location);
        result.put("SpotMax", SpotMax);
        return result;
    }
}