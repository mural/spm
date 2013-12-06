package com.spm.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.spm.common.exception.UnexpectedException;

/**
 * @author Luciano Rey
 */
public class EncryptionUtils {
	
	private static final String SHA1 = "SHA-1";
	
	/**
	 * @param value
	 * @return String encrypted value.
	 */
	public static String encrypt(String value) {
		MessageDigest sha1 = getSHA1();
		sha1.reset();
		sha1.update(value.getBytes());
		byte[] raw = sha1.digest();
		
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < raw.length; i++) {
			String h = Integer.toHexString(0xFF & raw[i]);
			while (h.length() < 2) {
				h = "0" + h;
			}
			hexString.append(h);
		}
		
		return hexString.toString();
	}
	
	/**
	 * @return SHA1
	 */
	private static MessageDigest getSHA1() {
		try {
			return MessageDigest.getInstance(SHA1);
		} catch (NoSuchAlgorithmException e) {
			throw new UnexpectedException(e);
		}
	}
}
