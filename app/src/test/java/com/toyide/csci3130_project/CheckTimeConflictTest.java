package com.toyide.csci3130_project;

import org.junit.BeforeClass;

import static junit.framework.Assert.assertEquals;

public class CheckTimeConflictTest {
    private static String time1 = "11:30-13:00";;
    private static String time2 = "11:30-15:00";
    private static String time3 = "12:30-13:00";
    private static String time4 = "09:00-11:00";

    private static String day1 = "TR";
    private static String day2 = "MR";
    private static String day3 = "TF";
    private static String day4 = "MF";

    @BeforeClass
    public static void init()
    {
        assertEquals(time1.length(),11);
        assertEquals(time2.length(),11);
        assertEquals(time3.length(),11);
        assertEquals(time4.length(),11);
        assertEquals(time1.charAt(5),'-');
        assertEquals(time2.charAt(5),'-');
        assertEquals(time3.charAt(5),'-');
        assertEquals(time4.charAt(5),'-');


    }
    //FIXME： fix JUnit test under exception


}
