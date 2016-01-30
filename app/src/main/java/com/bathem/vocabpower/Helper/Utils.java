package com.bathem.vocabpower.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mehtab on 1/30/16.
 */
public class Utils {

    public static Date getDateFromString(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date dateObj = null;
        try {
            dateObj = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

}
