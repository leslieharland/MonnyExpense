package com.example.leslie.monnyfree.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Leslie on 3/26/2018.
 */

public class DateUtil {
    final static String myFormat = "MM/dd/yy"; //In which you need put here

    public static Date convertToDate(String date){
        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            result = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String formatDateString(Date date){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean dateEquals(Date date1, Date date2){
        String dateString = formatDateString(date1);
        return dateString.equals(formatDateString(date2));
    }


}
