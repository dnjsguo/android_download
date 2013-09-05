
package com.appdear.client.model;

import java.io.Serializable;

/**
 * 类别详细列表实体类
 * @author zxy
 *
 */
public class CategoryAppDetailInfo implements Serializable {
	/**
	 * soft icon 
	 */
	public String softIconUrl = "";
	/**
	 * default soft icon 
	 */
	public int softdefaultImage;
	/**
	 * soft title 
	 */
	public String softTitle = "";
	/**
	 * 每一个item显示的头几个软件名字
	 */
	public String softTip = "";
	/**
	 * soft tag
	 */
	public String sfotTag = "";
	/***
	 * soft star rank 
	 */
	public int rank;
	/**
	 * fee flag ,free  or fee  or other
	 */
	public String feeFlag = "";
	
}
