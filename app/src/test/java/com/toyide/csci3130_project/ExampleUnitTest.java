package com.toyide.csci3130_project;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    checkPassword check = new checkPassword();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void check_profile_map() throws Exception {
        Profile profile = new Profile("B00123456", "name","12345678", "Computer Science", "undergraduate");
        Map<String, Object>  check2 =profile.toMap();
        assertEquals("B00123456", check2.get("UserID") );
        assertEquals("name", check2.get("UserName"));
        assertEquals("12345678",check2.get("Password"));
        assertEquals("Computer Science", check2.get("Department"));
        assertEquals("undergraduate", check2.get("UserDegree"));

    }


}