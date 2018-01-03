package com.fang.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strs {
	/**
	 * 验证是否为数字正则
	 */
	public static final Pattern NUMBER_PATTERN = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
	/**
	 * 下划线
	 */
	public static final String UNDERLINE = "_";

	/**
	 * 判断整个字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigit(CharSequence str) {
		Matcher matcher = NUMBER_PATTERN.matcher(str);
		return matcher.matches();
	}

	/**
	 * 判断字符串是否为空白串
	 * 
	 * @param str
	 * @return boolean 是空串返回true
	 */
	public static boolean isBlank(CharSequence str) {
		if (str == null) {// 空值
			return true;
		} else {
			int length = str.length();
			if (length == 0) {// 长度为0,
				return true;
			} else {// 长度不为0，判断每一个字符是否为""
				for (int i = 0; i < length; i++) {
					char c = str.charAt(i);
					if (c > 32) {// 包含非空字符返回true，空格
						// ascii值为32，小于32的字符全部为空白字符
						return false;
					}
				}
				return true;
			}
		}
	}

	/**
	 * 判断字符串是否为空白串
	 * 
	 * @param str
	 * @return boolean 不是空串返回true
	 */
	public static boolean isNotBlank(CharSequence str) {
		return !isBlank(str);
	}

	/**
	 * 所有字符串都为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllBlank(CharSequence... str) {
		for (CharSequence c : str) {
			if (isNotBlank(c))// 包含不为空的情况即返回false
				return false;
		}
		return true;
	}

	/**
	 * 所有字符串都不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllNotBlank(CharSequence... str) {
		for (CharSequence c : str) {
			if (isBlank(c))// 包含为空的情况即返回false
				return false;
		}
		return true;
	}

	/**
	 * 将首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLetter(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
	
	/**
	 * 将首字母 小写
	 * @param str
	 * @return
	 */
	public static String lowerCaseFirstLetter(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'A' && ch[0] <= 'Z') {
			ch[0] = (char) (ch[0] + 32);
		}
		return new String(ch);
	}
	
	/**
	 * 驼峰 转换成 分隔符形式 
	 * userName --> user_name,user-name,user.name,  ...
	 * UserName --> user_name,user-name，user.name,  ...
	 * @param camel
	 * @param split
	 * @return
	 */
	public static String camelToSplit(String camel,String split){
		char[] strArr = camel.toCharArray();
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toLowerCase(strArr[0]));
		for(int i= 1,j=strArr.length;i<j;i++){
			char c = strArr[i];
			if(Character.isUpperCase(c)){
				sb.append(split);
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}
	
	/**
	 * 分割符形式 转换成 驼峰命名
	 * @param str
	 * @param split
	 * @return
	 */
	public static String splitToCamel(String str,String split){
		String[] temp = str.split(split);
		StringBuilder sb = new StringBuilder(temp[0]);
		for(int i = 1,j= temp.length;i<j;i++){
			sb.append(upperCaseFirstLetter(temp[i]));
		}
		return sb.toString();
	}
	
	/**
	 * 下划线转换成驼峰
	 * @param str
	 * @return
	 */
	public static String underlineToCamel(String str){
		return splitToCamel(str,UNDERLINE );
	}
	
	/**
	 * 驼峰转换成下划线分割
	 * @param camel
	 * @return
	 */
	public static String camelToUnderline(String camel){
		return camelToSplit(camel, UNDERLINE);
	}
}