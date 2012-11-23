package net.sourceforge.minor.lucene.core.utils;

public class Check {
	public static void checkObject(Object o){
		if (o == null) throw new IllegalArgumentException("Invalid Object");
	}

	public static void checkString(String query) {
		if (query == null || query.trim().equals("")) throw new IllegalArgumentException("Invalid String");
	}
}
