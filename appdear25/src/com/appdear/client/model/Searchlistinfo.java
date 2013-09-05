/**
 * searchlistinfo.java
 * created at:2011-5-25下午01:53:01
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
public class Searchlistinfo implements Serializable {
	
	/**
	 * 内容名称
	 */
	public String contentname = "";
	
	/**
	 * 内容搜索次数
	 */
	public int searchnum;

}
