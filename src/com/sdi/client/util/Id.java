package com.sdi.client.util;

public class Id {
	private static int i=0;

	public static String next() {
		i=i++;
		return ""+i;
	}

}
