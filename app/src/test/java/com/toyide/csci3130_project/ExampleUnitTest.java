package com.toyide.csci3130_project;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    Courses course = new Courses("CSCI 2102","Lec","MWF","10:00-11:00" ,"test sd a","LSC");
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void check_profile_map() throws Exception {
        Profile profile = new Profile("B00123456", "names","123456789", "Computer Science", "Undergraduate");
        Map<String, Object>  check2 =profile.toMap();
        assertEquals("B00123456", check2.get("UserID") );
        assertEquals("names", check2.get("UserName"));
        assertEquals("123456789",check2.get("Password"));
        assertEquals("Computer Science", check2.get("Department"));
        assertEquals("Undergraduate", check2.get("UserDegree"));

    }

    @Test
    public  void  check_registraion() throws  Exception{
        Registration registration = new Registration("B00123456", "12345,23456,");
        Map<String,Object> checks = registration.toMap();
        assertEquals("B00123456",checks.get("UserID"));
        assertEquals("12345,23456,",checks.get("CourseID"));
    }
    @Test
    public void check_courses() throws  Exception{
        assertEquals("CSCI 2102", course.CourseTitle);
        assertEquals("Lec", course.CourseType);
        assertEquals("MWF", course.CourseWeekday);
        assertEquals("10:00-11:00",course.CourseTime);
        assertEquals("test sd a",course.CourseInfo);
    }
    @Test
    public void check_timeConflict() throws  Exception {
        String time = "10:00-12:00";
        String time_ = "11:00-13:00";
        String[] time1 = time.split("-");
        String[] time2 = time_.split("-");
        boolean checkConflict = false;
        if ((time1[0].compareTo(time2[0]) >= 0 && time1[0].compareTo(time2[1]) < 0) || (time2[0].compareTo(time1[0]) >= 0 && time2[0].compareTo(time1[1]) < 0)) {
            checkConflict = true;
            assertEquals(true, checkConflict);
        }
    }
    @Test
    public void check_upperbound() throws Exception {
        Integer SpotCurrent = 100;
        Integer SpotMax = 100;
        ArrayList<String> courseFull = new ArrayList<>();
        if (SpotCurrent == SpotMax) {
            courseFull.add(course.CourseTitle);
        }
        assertEquals(false,courseFull.isEmpty());
    }
}