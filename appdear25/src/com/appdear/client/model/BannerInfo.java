/**
 * BannerInfo.java
 * created at:2011-5-24下午05:27:15
 *
 * Copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 */
package com.appdear.client.model;

import java.io.Serializable;

/**
 * Banner信息
 * 
 * @author zqm
 *
 */
public class BannerInfo implements Serializable {
	
	/**
	 * 类型
	 */
	public String adtype = "";
	
	/**
	 * 广告id
	 */
	public int adid;
	
	/**
	 * 广告名称
	 */
	public String adtitle = "";
	
	/**
	 * 广告图片地址
	 */
	public String adurl = "";
	
	/**
	 * 广告地址
	 */
	public String imgurl = "";
}
