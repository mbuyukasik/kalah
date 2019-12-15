package com.mbuyukasik.assignment.kalah.util;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import com.mbuyukasik.assignment.kalah.util.RandomUtil;

public class TestRandomUtil {

	private static String CODE_ERROR_MESSAGE = "Genarated code should be in specified length and consist of digits!";
	private static String VALUE_IN_RANGE_ERROR_MESSAGE = "Genarated value should be in given range: %d - %d!";
	
	@Test
	public void testGenarateCode() {
		int codeLength = 10;
		
		String code = RandomUtil.generateCode(codeLength);
		assertTrue(CODE_ERROR_MESSAGE,
				code != null && code.length() == codeLength && NumberUtils.isDigits(code));
	}

	@Test
	public void testGenerateRandomIndex() {
		int minValue = 1;
		int maxValue = 10;
		int value = RandomUtil.generateNumber(minValue, maxValue);
		assertTrue(String.format(VALUE_IN_RANGE_ERROR_MESSAGE, minValue, maxValue),
				value >= minValue && value <= maxValue);
	}

}
