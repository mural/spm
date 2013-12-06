package com.spm.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.spm.common.exception.UnexpectedException;

/**
 * @author Luciano Rey
 */
public final class Utils {
	
	private static final int buffer_size = 1024;
	
	public static void copyStream(InputStream is, OutputStream os) {
		int count = 0;
		byte[] bytes = new byte[buffer_size];
		try {
			while ((count = is.read(bytes, 0, buffer_size)) != -1) {
				os.write(bytes, 0, count);
			}
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}
	
}