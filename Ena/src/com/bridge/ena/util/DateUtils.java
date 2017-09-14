/*
 * DateUtils.java
 * 
 * Created on Aug 24, 2007, 11:02:53 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.MutableDateTime;

/**
 *
 * @author db2admin
 */
public class DateUtils {
    
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String ISO_8601_DATE_FORMAT = "yyyyMMdd'T'HH:mm:ssZ";
    private static final String BOGUS_ISO_8601_DATE_FORMAT = "yyyyMMdd'T'HH:mm:ss";
    private static final String XSD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String JSON_DATE_FORMAT = "2017-09-26"; 

    public static String convertString(Date date) {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }
    
    public static Date convertXSDDate(String str) throws RuntimeException {
        try {
            DateFormat format = new SimpleDateFormat(XSD_DATE_FORMAT);
            return  format.parse(str);
        } catch(Exception e)  {
            throw new RuntimeException(e);
        }
    }
    
    public static Date convertDate(String str) throws RuntimeException {
        try {
            DateFormat format = new SimpleDateFormat(DATE_FORMAT);
            return  format.parse(str);
        } catch(Exception e)  {
            throw new RuntimeException(e);
        }
    }
    
    public static String convertISO8601String(Date date) {
        DateFormat format = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
        return format.format(date);
    }
    
    public static Date convertISO8601Date(String str) throws RuntimeException {
        try {
            DateFormat format = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
            Date date = format.parse(str);
            //if we don't do this(setHours), for 20070718T00:00:00+0000 produces Wed Jul 18 03:00:00 EEST 2007!
            date.setHours(0);
            return date;
        } catch(Exception e) {
            Date date = convertBogusISO8601Date(str);
            date.setHours(0);
            return date;
        }
    }
    
    public static Date convertISO8601DateForTest(String str) throws RuntimeException {
        try {
            DateFormat format = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
            Date date = format.parse(str);
            MutableDateTime dt = new MutableDateTime(date.getTime());
            return dt.toDate();
        } catch(Exception e) {
            Date date = convertBogusISO8601Date(str);
            MutableDateTime dt = new MutableDateTime(date.getTime());
            return dt.toDate();
        }
    }
    
    public static Date convertISO8601DateExact(String str) throws RuntimeException {
        try {
            DateFormat format = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
            Date date = format.parse(str);
            return date;
        } catch(Exception e) {
            return convertBogusISO8601Date(str);
        }
    }
    
    private static Date convertBogusISO8601Date(String str) throws RuntimeException {
        try {
            DateFormat format = new SimpleDateFormat(BOGUS_ISO_8601_DATE_FORMAT);
            Date date = format.parse(str);
            return date;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Date findDayAfter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.roll(Calendar.DATE, 1);
        return cal.getTime();
    }
    
    public static Date findDayBefore(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.roll(Calendar.DATE, -1);
        return cal.getTime();
    }
}
