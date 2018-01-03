package com.fang.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Dates {
	public static final String COMMON_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String COMMON_DATE_PATTERN = "yyyy-MM-dd";
	public static final String COMMON_TIME_PATTERN = "HH:mm:ss";
	private static final SimpleDateFormat COMMONFORMAT = new SimpleDateFormat(COMMON_DATETIME_PATTERN);

	/**
	 * 将date 格式化成字符串 pattern : yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String toString(java.util.Date date) {
		if (date == null)
			return null;
		return COMMONFORMAT.format(date);
	}

	/**
	 * 将date类型格式化成字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(java.util.Date date, String pattern) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将时间戳格式化
	 * 
	 * @param s
	 * @return
	 */
	public static String toString(String s) {
		if (s != null && Strs.isDigit(s)) {
			java.util.Date d = toDate(Long.valueOf(s));
			return toString(d);
		}
		return null;
	}

	/**
	 * 将时间戳格式化
	 * 
	 * @param s
	 * @return
	 */
	public static String toString(String s, String pattern) {
		if (s != null && Strs.isDigit(s)) {
			java.util.Date d = toDate(Long.valueOf(s));
			return toString(d, pattern);
		}
		return null;
	}

	/**
	 * 将格式化的时间 转换成另一格式 eg:2012-12-12 yyyy-MM-dd yyyyMMdd 20121212
	 * 
	 * @param src
	 * @param srcPattern
	 * @param destPattern
	 * @return
	 */
	public static String transPattern(String src, String srcPattern, String destPattern) {
		java.util.Date date = toDate(src, srcPattern);
		return toString(date, destPattern);
	}

	/**
	 * 将字符串转换成日期
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static java.util.Date toDate(String str, String pattern) {
		if (str == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将字符串转换成日期<br/>
	 * pattern : yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 */
	public static java.util.Date toDate(String str) {
		if (str == null)
			return null;
		try {
			return COMMONFORMAT.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 时间戳转换成date
	 * 
	 * @param l
	 * @return
	 */
	public static java.util.Date toDate(long l) {
		return new java.util.Date(l);
	}

}