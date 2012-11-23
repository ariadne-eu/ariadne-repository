package net.sourceforge.minor.lucene.core.utils;

public class Formatter {
	
	public static String formatDate(String date){
		if (date.length() != 8) throw new IllegalArgumentException("wrong original date format");
		return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	}
	
}
