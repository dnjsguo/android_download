/**
 * User.java
 * created at:2011-5-10上午11:13:20
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.model;

import java.io.Serializable;
  
/** 
 * 用户信息类
 * 
 * @author zqm 
 */
public class UserInfo implements Serializable {

	/**
	 * 用户ID
	 */
	public String userid = "";
	
	/**
	 * 昵称
	 */
	public String nickname = "";
	
	/**
	 * E_mail
	 */
	public String email = "";
	
	/**
	 * 地区
	 */
	public String area = "";
	
	/**
	 * 性别
	 */
	public String gender = "";
	
	/**
	 * 职业
	 */
	public String profession = "";
	
	/**
	 * 手机
	 */
	public String mobile = "";
	
	/**
	 * qq号码
	 */
	public String qq = "";
	
	/**
	 * 个人描述
	 */
	public String desc = "";
	
	/**
	 * 2011 09 20 zxy add 用户签名
	 */
	public String singnure = "";

}

 