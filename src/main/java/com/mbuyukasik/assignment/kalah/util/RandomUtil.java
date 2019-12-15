package com.mbuyukasik.assignment.kalah.util;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;


/**
 * Used to keep common random methods
 * 
 * @author TTMBUYUKASIK
 *
 */
public class RandomUtil {

	/**
	 * @param codeLength : Length of required code
	 * @return randomly generated value in specified length
	 */
	public static String generateCode(int codeLength) {
		return RandomStringUtils.randomNumeric(codeLength);
	}
	
	/**
	 * 
	 * @param min - Minimum value (Inclusive)
	 * @param max - Maximum value (Inclusive)
	 * @return a random value in range min - max
	 */
	public static int generateNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
}
