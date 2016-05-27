package com.midea.trade.sharding.core.util;

/**
 * StringUtils
 */
public abstract class StringUtils {

	public static boolean isBlank(String s) {
		return s == null || s.trim().length() <= 0;
	}

	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}
}
