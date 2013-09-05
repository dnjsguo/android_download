package com.appdear.client.service.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.SoftlistInfo;

public class ApiInstalledRecoverSoftListResult implements ApiResult, Serializable{
 
	
 
/**
 * SoftListInfo.java
 * created at:2011-5-24下午12:02:07
 *
 * Copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 */
//	sv":"1.0",
//	"imei":"00000000000",


	/**
	 * 请求结果码
	 */
	public String resultcode = "";
 
	
	/**
	 * 平台服务版本号
	 */
	public String sv = "";
	
	/**
	 * 手机身份识别码
	 */
	public String imei = "";

	 
	/**
	 * 返回是否成功
	 */
	public int isok = 0;
	
	 
	/**
	 * 软件列表
	 */
	public List<SoftlistInfo> softList = new ArrayList<SoftlistInfo>();

	 
}
