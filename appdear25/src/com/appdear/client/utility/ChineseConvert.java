package com.appdear.client.utility;

import android.util.Log;

/**
 * 获得中文的汉语拼音
 * @author jindan
 *
 */
public class ChineseConvert {
	static final String include="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 
	 * @param str      字符串
	 * @param isfirstC 是否显示首字符
	 * @return  返回汉语拼音
	 */
	public static  String ChineseToPing(String str,boolean isfirstC){
	
		if(str==null)return "";
		str=str.trim();
		int t0=str.length();
		String t2 = CnToSpell.getFirstSpell(str);
			if(t2==null||t2.equals("")){
				if(isfirstC==true){
					return "其他";
				} 
			}else{
				if(isfirstC==true){
					t2=t2.toUpperCase();
					return String.valueOf(includeS(t2.charAt(0))?t2.charAt(0):"其他").toUpperCase();
				}
			}
		return String.valueOf(t2.charAt(0)).toUpperCase();
	}
	
	private static boolean includeS(char a){
		char[] chars=include.toCharArray();
		for(char c:chars){
			if(a==c)return true;
		}
		return false;
	}
}
