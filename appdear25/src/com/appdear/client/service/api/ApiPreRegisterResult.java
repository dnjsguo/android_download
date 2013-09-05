package com.appdear.client.service.api;

public class ApiPreRegisterResult implements ApiResult {
	
	/**
	 * 平台服务版本号
	 */
	public String sv;
	
	/**
	 * 手机身份识别码
	 */
	public String imei;
	
	/**
	 * 请求结果码
	 */
	public String resultcode;
	
	/**
	 * 是否成功
	 */
	public int isok;
	
	/**
	 * seesionid
	 */
	public String sessionid;
	
	/**
	 * 用户id
	 */
	public String userid="";
	
	/**
	 * 用户名称
	 */
	public String username;
	
	/**
	 * 密码
	 */
	public String passwd;
	
	/**
	 * 昵称
	 */
	public String nickname;
	/**
	 * 丨username状态
	 */
	public static int usernameflag;
	public static int usernamecount;
	
}
