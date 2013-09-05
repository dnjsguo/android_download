package com.appdear.client.utility;

import java.util.regex.Pattern;

public class VaildValue {
	public static boolean VaildEmail(String email){
		if(email!=null&&email.trim().equals("")){
			return true;
		}
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("\\w+(\\.\\w+)*@\\w+(\\.\\w+)+");
		return pattern.matcher(email).matches();
	}
	//检查手机号
	public static boolean VaildMobile(String mobile){
		if(mobile!=null&&mobile.trim().equals("")){
			return true;
		}
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); 
		return pattern.matcher(mobile).matches();
	}
	//检查职业
	public static boolean VaildProfession(String profession){
		if(profession!=null&&profession.trim().equals("")){
			return true;
		}
		if(profession.length()<1||profession.length()>20){
			return false;
		}
		return true;
	}
	
	//检查手机号
	public static boolean VaildQQ(String qq){
		if(qq!=null&&qq.trim().equals("")){
			return true;
		}
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^\\d{5,13}$");
		return pattern.matcher(qq).matches();
	}
	public static boolean VaildUsername(String username){
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^[a-zA-Z][a-zA-Z\\d]{5,25}$");
		return pattern.matcher(username).matches();
	}
	public static boolean Vaildlonginname(String username){
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^[a-zA-Z\\d]{6,25}$");
		return pattern.matcher(username).matches();
	}
	public static boolean VaildPasswd(String passwd){
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^[a-zA-Z\\d]{6,16}$");
		return pattern.matcher(passwd).matches();
//		if(passwd!=null&&!passwd.equals("")&&passwd.trim().length()>=6&&passwd.trim().length()<=16){
//			return true;
//		}
//		return false;
	}
	

}
