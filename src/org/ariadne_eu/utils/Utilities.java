package org.ariadne_eu.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

public abstract class Utilities {

    public static String escape(String str) {
        return str;
        //return StringEscapeUtils.escapeJavaScript(str);
    }

    public static int getInteger(String str) {
        return getInteger(str, 0);
    }

    public static int getInteger(String str, int defaultvalue) {
        int number = defaultvalue;
        try {
            number = Integer.parseInt(StringEscapeUtils.escapeJava(str));
        } catch (NumberFormatException ex) {
            number = defaultvalue;
        }
        return number;
    }

    public static Date getDate(String date) {
        try {
            return new Date(Date.parse(date));
        } catch (Exception ex) {
            return null;
        }
    }

    public static Calendar getCalendar(String date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(getDate(date));
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    public static String shortenUsername(String username) {
        String result = username;
        if (username.length() > 40) {
            result = username.substring(0, 15) +
                    "..." + username.substring(username.length() - 15, username.length());
        }
        return result;
    }
}
