/**
 * CatalogListInfo.java
 * created at:2011-5-25下午03:03:00
 *
 * Copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 */
package com.appdear.client.model;

import java.io.Serializable;

/**
 * <code>title</code>
 * abstract
 * <p>description
 * <p>example:
 * <blockquote><pre>
 * </blockquote></pre>
 * @author Author
 * @version Revision Date
 */
public class CatalogListInfo implements Serializable {

	/**
	 * 类别图片地址
	 */
	public String catalogicon = "";
	
	/**
	 * 类别ID
	 */
	public String  catalogid  ;
	
	/**
	 * 类别名称
	 */
	public String catalogname = "";
	
	/**
	 * 类别描述
	 */
	public String catalogdesc = "";
	
	/**
	 * 类别内的资源数量
	 */
	public String catalognum = "";

	/**
	 * 默认图片资源ID
	 * @return
	 */
	public int defaultIcon;
	
	/**
	 * 应用数量
	 * @return
	 */
	public String appCount="10";
	
	/**
	 * 类别更新时间
	 * @return
	 */
	public String catalogTime;
	
	
	/**
	 * 万花筒返回的内容类型
	 * （0 子标签分类列表 1 标签列表）
	 */
	public String ctype = "";
}
