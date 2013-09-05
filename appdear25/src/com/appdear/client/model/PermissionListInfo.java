package com.appdear.client.model;

import java.io.Serializable;

/**
 * 访问权限列表
 * @author zqm
 *
 */
public class PermissionListInfo implements Serializable  {
	/**
	 * 权限编码
	 */
	public String permcode = "";
	
	/**
	 * 权限中文描述
	 */
	public String permdescch = "";
	
	/**
	 * 权限英文描述
	 */
	public String permdescen = "";
	
	/**
	 * 权限类型
	 */
	public int permtype;

}
