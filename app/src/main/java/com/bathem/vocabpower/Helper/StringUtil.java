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

        char firstCharacter = txt.substring(0,1).charAt(0);
        int characterVal = (int) firstCharacter;

        if(( characterVal >= 60 && characterVal <= 90 ) || ( characterVal >= 97 && characterVal <= 122 )) {
            txt =  (firstCharacter +"").toUpperCase() + txt.substring(1).toLowerCase();
        }

        return  txt;
    }
}
