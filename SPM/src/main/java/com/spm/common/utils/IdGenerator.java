package com.spm.common.utils;

import java.util.Random;

/**
 * 
 * @author Agustin Sgarlata
 */
public class IdGenerator {
	
	private static int ID = 100;
	
	public static long getLongId() {
		return ID++;
	}
	
	public static int getIntId() {
		return ID++;
	}
	
	public static long getRandomLongId() {
		return new Random().nextLong();
	}
	
	public static int getRandomIntId() {
		return new Random().nextInt();
	}
}
