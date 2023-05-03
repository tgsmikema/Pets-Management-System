package com.example.mobile.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {


    //get current timestamp
    public static long curTimeStamp(){
        return System.currentTimeMillis();
    }

    //return the Date from the timestamp
    public static Date getDateFromTimeStamp(long timestamp){
        return new Date(timestamp);
    }

    public static String getDateSimplyString(long timestamp){
        Date date = getDateFromTimeStamp(timestamp);
        return (date.getMonth() + 1) +"." + date.getDate();
    }

    public static long getPreviousWeekTimeStamp(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        // Get the timestamp for one week ago
        long oneWeekAgoInMillis = calendar.getTimeInMillis();
        return oneWeekAgoInMillis;
    }

    public static long getNextWeekTimeStamp(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        // Get the timestamp for one week ago
        long oneWeekAfterInMillis = calendar.getTimeInMillis();
        return oneWeekAfterInMillis;
    }

}
