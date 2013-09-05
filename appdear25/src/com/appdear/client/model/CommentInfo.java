/**
 * CommentInfo.java
 * created at:2011-5-25下午03:22:43
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
public class CommentInfo implements Serializable {

	/**
	 * 评论ID
	 */
	public int commentid;
	
	/**
	 * 评论人昵称
	 */
	public String username = "";
	
	/**
	 * 评论人ID
	 */
	public int userid;
	
	/**
	 * 评论时间
	 */
	public String time = "";
	
	/**
	 * 评论内容
	 */
	public String text = "";
	
	/**
	 * 评级信息
	 */
	public int grade;
}
