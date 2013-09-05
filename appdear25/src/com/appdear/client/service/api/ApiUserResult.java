package com.appdear.client.service.api;

import com.appdear.client.model.UserInfo;

/**
 * 用户注册
 * @author zqm
 *
 */
public class ApiUserResult implements ApiResult {
	
	/**
	 * 请求结果码
	 */
	public String resultcode = "";
	
	/**
	 * 错误信息
	 */
	public String resultinfo = "";
	
	/**
	 * imei
	 */
	public String imei = "";
	
	/**
	 * 版本号
	 */
	public String sv = "";
	
	/**
	 * 是否成功
	 */
	public int isok = 0;
	
	/**
	 * 用户令牌
	 */
	public String token = "";
	
	/**
	 * 用户ID
	 */
	public String userid = "0";
	
	/**
	 * 用户昵称
	 */
	public String nickname = "";
	
	/**
	 * 账户
	 */
	public int account = 0;
	
	/**
	 * 总积分
	 */
	public int totalpoint = 0;
	
	/**
	 * 等级
	 */
	public String level = "";
	
	/**
	 * 用户信息
	 */
	public UserInfo userinfo = new UserInfo();
}
