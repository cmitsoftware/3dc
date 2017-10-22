package org.climbing.util;

import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

	public static String readStringFromFile(String file, String encoding) throws IOException{
		FileInputStream fis = new FileInputStream(file);
		String out = "";
		byte buf[] = new byte[1024*10];
		int c = fis.read(buf, 0, buf.length);
		while(c>-1){
			out += new String(buf,0,c,encoding);
			c = fis.read(buf, 0, buf.length);
		}
		fis.close();
		return out;
	}
	
}
