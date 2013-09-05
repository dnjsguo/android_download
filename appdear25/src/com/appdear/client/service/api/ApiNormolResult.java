package com.appdear.client.service.api;

import org.json.JSONObject;


public class ApiNormolResult implements ApiResult {

	/**
	 * 请求结果码
	 */
	public String resultcode = "";
	
	/**
	 * 错误信息
	 */
	public String resultinfo = "";
	
	

	/**
	 * 是否提交成功
	 */
	public int isok = 0;
	
	/**
	 * 是否收藏 0--未收藏 1--已收藏
	 */
	public int isfavorited = 0;
	
	/**
	 * 会员用户名
	 */
	public String username = "";
	
	/**
	 * imei
	 */
	public String imei = "";
	
	/**
	 * imsi
	 */
	public String imsi = "";
	
	/**
	 * sv
	 */
	public String sv = "";
	
	/**
	 * contact 联系人信息
	 */
	public String contact = "";
	
	/**
	 * contact 联系人信息数量
	 */
	public String contactcount = "";
	
	/**
	 * failidx  上传安装列表（appid）备份功能，返回的失败应用的索引
	 */
    public  String failidx="";
    
    @Override
	public String toString() {
		return "ApiNormolResult [resultcode=" + resultcode + ", resultinfo="
				+ resultinfo + ", isok=" + isok + ", isfavorited="
				+ isfavorited + ", username=" + username + ", imei=" + imei
				+ ", imsi=" + imsi + ", sv=" + sv + ", contact=" + contact
				+ ", contactcount=" + contactcount + ", failidx=" + failidx
				+ "]";
	}
	/**
	 * sms 联系人信息
	 */
	public String sms = "";
	
	/**
	 * 购买手机日期
	 */
	public String buydate="";
}
