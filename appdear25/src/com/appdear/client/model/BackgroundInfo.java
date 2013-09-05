package com.appdear.client.model;

import java.io.Serializable;

/**
 * 背景详情信息
 * @author zqm
 *
 */
public class BackgroundInfo implements Serializable {
	
	/**
	 * 图标
	 */
	public String icon = "";
	
	/**
	 * id
	 */
	public int id = 0;
	
	/**
	 * 图标地址
	 */
	public String backurl = "";
	
	/**
	 * 详情
	 */
	public String desc = "";
	
	/**
	 * 是否被选中
	 */
	public boolean ischeck = false;
}
