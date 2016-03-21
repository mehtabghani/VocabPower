package com.bathem.vocabpower.Helper;

/**
 * Created by mehtab on 2/6/16.
 */
public class StringUtil {

    public  static boolean stringEmptyOrNull (String str) {

        if(str == null || str.equals(""))
            return true;

        return  false;
    }

    public static String capitalizeFirstLetter(String txt) {
        return txt.substring(0,1).toUpperCase() + txt.substring(1).toLowerCase();
    }
}
