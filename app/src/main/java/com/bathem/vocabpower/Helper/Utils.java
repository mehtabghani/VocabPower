package com.bathem.vocabpower.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by mehtab on 1/30/16.
 */
public class Utils {

    private  static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static Date getDateFromString(String date) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Date dateObj = null;
        try {
            dateObj = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    public static String getStringDate(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date getFormattedDate(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return getDateFromString(sdf.format(date));
    }

    public static int getRandomNumber(int min, int max) {

        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

}
