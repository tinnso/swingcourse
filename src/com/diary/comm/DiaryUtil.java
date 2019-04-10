package com.diary.comm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DiaryUtil {

	public static void copyFile(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
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

}
