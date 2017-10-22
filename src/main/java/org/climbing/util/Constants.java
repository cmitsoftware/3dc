package org.climbing.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Constants {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
	public static SimpleDateFormat timeFormat() {
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		return sdf;
	}
}
