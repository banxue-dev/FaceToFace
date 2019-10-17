package com.ftf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * 包括空字符串和"null" 及null
	 * 
	 * @param value
	 * @return 2019年8月29日 作者：fengchase
	 */
	public static boolean isNull(String value) {
		if (value == null || "".equals(value) || "null".equals(value)) {
			return true;
		}
		return false;
	}
	/**
	 * 包括空字符串和"null" 及null
	 * 
	 * @param value
	 * @return 2019年8月29日 作者：fengchase
	 */
	public static boolean isNotNull(String value) {
		if (isNull(value)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否为空，只要这些有一个为空，就会返回true
	 * @param objs
	 * @return
	 * 2019年9月25日
	 * 作者：fengchase
	 */
	public static boolean isNulls(Object ... objs) {
		for(Object obj:objs) {
			if(obj==null) {
				return true;
			}
		}
		return false;
	}
	public static String getNull(String value, String defalut_value) {
		if (isNull(value)) {
			return defalut_value;
		}
		return value;
	}

	private static Pattern linePattern = Pattern.compile("_(\\w)");

	/** 下划线转驼峰 */
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static Pattern humpPattern = Pattern.compile("[A-Z]");

	/** 驼峰转下划线,效率比上面高 */
	public static String humpToLine2(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 构造like语句sql
	 * 就是增加一个%号的包围
	 * @param souce
	 * @return
	 * 2019年10月14日
	 * 作者：fengchase
	 */
	public static String structLikeSql(String souce) {
		return "%"+souce+"%";
	}
	public static void main(String[] args) {
		System.out.println(getNull(null + "", "2112"));
	}
	
	/**
	 * 将符号分隔的字符串转为list 符号包含：逗号、#号
	 * @param string
	 * @return
	 * 2019年9月3日
	 * 作者：fengchase
	 */
	public static <T> List<T> StringsToList(String string){
		if(isNull(string)) {
			return null;
		}
		string=string.replace("#", ",");
		String[] strs=string.split(",");
		List<T> lst=new ArrayList<T>();
		for(String s:strs) {
			lst.add((T) s);
		}
		return lst;
	}
	/**
	 * 去掉字符串最后一个符号
	 * @param str 目标字符串
	 * @param rep 符号 eg:逗号、#号
	 * 2019年10月15日
	 * 作者：fengchase
	 */
	public static String ClearLastComma(String str,String rep) {
		if(isNulls(str,rep)) {
			return null;
		}
		return str.substring(0,str.lastIndexOf(rep));
	}
}
