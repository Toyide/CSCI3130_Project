package com.toyide.csci3130_project;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;



// Check time conflict of two time intervals
public class CheckTimeConflict {


    //@Input: string XXXX-XXXX
    //@Return: true there is conflict
    public boolean confliCtcheck(String str1,String str2){
        //Split input time interval into two seperate time XXXX
        String startTime_1 = str1.split("-")[0];
        String endTime_1 = str1.split("-")[1];
        String startTime_2 = str2.split("-")[0];
        String endTime_2 = str2.split("-")[1];

        if ((startTime_1.compareTo(startTime_2) >= 0 && startTime_1.compareTo(endTime_2) < 0) || (startTime_2.compareTo(startTime_1) >= 0 && startTime_2.compareTo(endTime_1) < 0))
            return true;
        else
            return false;

/*        //Add ":" to fit the formatter
        String startTime_1 = firstStartTime.substring(0,2)+ firstStartTime.substring(2,4);
        String endTime_1 = firstEndTime.substring(0,2)+ firstEndTime.substring(2,4);
        String startTime_2 = secondStartTime.substring(0,2)+ secondStartTime.substring(2,4);
        String endTime_2 = secondEndTime.substring(0,2)+ secondEndTime.substring(2,4);
        //Format string to time
        DateFormat sdf = new SimpleDateFormat("HHmm");
        System.out.print(startTime_1);
        //System.out.print(endTime_1);

        Date startTime1;
        Date endTime1;
        Date startTime2;
        Date endTime2;


        startTime1 = sdf.parse(startTime_1);
        endTime1 = sdf.parse(endTime_1);
        startTime2 = sdf.parse(startTime_2);
        endTime2 = sdf.parse(endTime_2);


        try{

            //Convert to Date object for comparision
            /*
            System.out.print(startTime1);
            System.out.print(endTime1);
            System.out.print(startTime2);
            System.out.print(endTime2);
            System.out.print("Bool:");
            System.out.print((startTime1.before(startTime2) && endTime1.after(startTime2)) || (startTime2.before(startTime1) && endTime2.after(startTime1)));

        }
        catch ( Exception ex ){
            System.out.println(ex);
        }

        System.out.println(startTime1);
        System.out.println(endTime1);
        System.out.println(startTime2);
        System.out.println(endTime2);
        System.out.println("Bool:");
        System.out.println((startTime1.before(startTime2) && endTime1.after(startTime2)) || (startTime2.before(startTime1) && endTime2.after(startTime1)));

        // There is intersaction
        if((startTime1.before(startTime2) && endTime1.after(startTime2)) || (startTime2.before(startTime1) && endTime2.after(startTime1)) )
            return true;
        else
            return false;
*/


    }
    //Check if two String contain the same character
    //Parameter: two strings
    //Return: true: there is overlap
    public boolean sameChars(String firstStr, String secondStr) {
        for(int i=0;i <firstStr.length();i++){
            for(int j=0;j<secondStr.length();j++){
                if(firstStr.charAt(i)==secondStr.charAt(j))
                    return true;
            }
        }
        return false;

    }
}
