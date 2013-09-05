/**
 * NewSoftware.java
 * created at:2011-5-11下午03:37:33
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
  
/** 
 * 软件信息
 * 
 * @author zqm 
 */
public class NewSoftwareInfo implements Serializable {
	
	/**
	 * 软件id
	 */
	public String id = "";
	
	/**
	 * 软件名称
	 */
	public String name = "";
	
	/**
	 * 星级
	 */
	public String star = "";
	
	/**
	 * 作者
	 */
	public String author = "";
	
	/**
	 * 图标
	 */
	public Drawable icon;
	
}

 