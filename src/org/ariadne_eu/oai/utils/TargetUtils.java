package org.ariadne_eu.oai.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TargetUtils{
	
	protected static Calendar FAR_AWAY = GregorianCalendar.getInstance();
	
	static {
		FAR_AWAY.set(9000, 01, 01);
	}
	
	public static String convertToLocaleIbmDB2DateTime(String date) throws ParseException {
		String patternIn = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		String patternOut = "yyyy-MM-dd' 'HH:mm:ss";
		
		SimpleDateFormat sdfIn = new SimpleDateFormat(patternIn);
		sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date parsedDate = sdfIn.parse(date);
		SimpleDateFormat sdfOut = new SimpleDateFormat(patternOut);
		sdfOut.setTimeZone(TimeZone.getDefault());
		return sdfOut.format(parsedDate);
	}
	
	public static String convertLocaleIbmDB2DateTimeToUTC(String date) throws ParseException {
		String patternOut = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		String patternIn = "yyyy-MM-dd' 'HH:mm:ss";
		
		SimpleDateFormat sdfIn = new SimpleDateFormat(patternIn);
		sdfIn.setTimeZone(TimeZone.getDefault());

		Date parsedDate = sdfIn.parse(date);
		SimpleDateFormat sdfOut = new SimpleDateFormat(patternOut);
		sdfOut.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdfOut.format(parsedDate);
	}
	
	public static String convertUNTILToLocaleIbmDB2DateTime(String date) throws ParseException {
		String patternIn = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		String patternOut = "yyyy-MM-dd' 'HH:mm:ss";
		
		SimpleDateFormat sdfIn = new SimpleDateFormat(patternIn);
		sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date parsedDate = sdfIn.parse(date);
		Calendar parsedCalendar = GregorianCalendar.getInstance();
		parsedCalendar.setTime(parsedDate);
		SimpleDateFormat sdfOut = new SimpleDateFormat(patternOut);
		if(!parsedCalendar.after(FAR_AWAY)) {
			parsedCalendar.add(Calendar.SECOND, 1);
			sdfOut.setTimeZone(TimeZone.getDefault());
			return sdfOut.format(parsedCalendar.getTime());
		}
		else {
			sdfOut.setTimeZone(TimeZone.getTimeZone("UTC"));
			return sdfOut.format(parsedCalendar.getTime());
		}

	}
	
}