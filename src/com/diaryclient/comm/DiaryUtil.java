package com.diaryclient.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryUtil {
	
	public static void copyFile2(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}
	
	public static void copyFile(File source, File dest) throws IOException {
		FileInputStream fis = new FileInputStream(source);
	    FileOutputStream fos = new FileOutputStream(dest);

	    int n = fis.available();
	    byte[] bs = new byte[n]; 
	    while ((n = fis.read(bs)) != -1) {
	    	fos.write(bs);
	    }

	    fis.close();
	    fos.close();
		
	}

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}
	
	 public static Date strToDate(String strDate) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		  ParsePosition pos = new ParsePosition(0);
		  Date strtodate = formatter.parse(strDate, pos);
		  return strtodate;
	}
}
