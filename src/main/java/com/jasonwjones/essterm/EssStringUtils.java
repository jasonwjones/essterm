package com.jasonwjones.essterm;

public class EssStringUtils {

	public static boolean isNullOrEmpty(String text) {
		return text == null || text.isEmpty();
	}
	
	public static String nullsafeString(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}
}
