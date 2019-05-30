package com.backbase.kalah.utils;

public class UriUtils {
	
	public static String trimUrl(String uri) {
		int index = uri.indexOf("/pits/");
		return uri.substring(0, index);
	}

}
