package com.example.mobile.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static String[] month = new String[]{
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    //get current timestamp
    public static long curTimeStamp() {
        return System.currentTimeMillis();
    }

    //return the Date from the timestamp
    public static Date getDateFromTimeStamp(long timestamp) {
        return new Date(timestamp);
    }

    public static String getDateSimplyString(long timestamp) {
        Date date = getDateFromTimeStamp(timestamp);
        return (date.getMonth() + 1) + "." + date.getDate();
    }

    public static long getPreviousWeekTimeStamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        // Get the timestamp for one week ago
        long oneWeekAgoInMillis = calendar.getTimeInMillis();
        return oneWeekAgoInMillis;
    }

    public static long getNextWeekTimeStamp(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        // Get the timestamp for one week ago
        long oneWeekAfterInMillis = calendar.getTimeInMillis();
        return oneWeekAfterInMillis;
    }

    public static String getMonthSimplyString(long timestamp){
        Date date = new Date(timestamp);
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        return month[date.getMonth()] + "-" + localDate.getYear();
    }

    public static long getMidDayOfMonth(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH) / 2);
        return calendar.getTimeInMillis();
    }

    public static long getPreviousMonth(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.add(Calendar.MONTH, -1);
        return TimeUtil.getMidDayOfMonth(cal.getTimeInMillis());
    }

    public static long getNextMonth(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.add(Calendar.MONTH, 1);
        return TimeUtil.getMidDayOfMonth(cal.getTimeInMillis());
    }

    public static long getFirstDayOfMonth(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getLastDayOfMonth(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);

        return calendar.getTimeInMillis();
    }

    public static String getFormatDataString (long timestamp){

        LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Pacific/Auckland"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return datetime.format(formatter);
    }

    public static String getFormatDataStringForDogDate(long timesStamp){
        Date date = new Date(timesStamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        return date.getDate() + "/" + (date.getMonth() + 1) + "/" + year;
    }

    public static String getFormatDataStringForSimplyDogDate(long timesStamp){
        Date date = new Date(timesStamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        return date.getDate() + "/" + (date.getMonth() + 1) + "/" + String.valueOf(year).substring(2);
    }
}
