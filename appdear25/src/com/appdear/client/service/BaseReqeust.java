/**
 * BaseReqeust.java
 * created at:2011-5-10上午10:44:08
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.service;

import org.xml.sax.SAXParseException;

import com.appdear.client.exception.ApiException;
import com.appdear.client.utility.StringHashMap;
  
/** 
 * 协议处理接口
 * 
 * @author zqm 
 * @param <StringHashMap>
 */
public interface BaseReqeust<T> {

	/**
	 * 请求方式POST
	 */
	public static final int POST = 0;
	
	/**
	 * 请求方式GET
	 */
	public static final int GET = 1;
	
	/**
	 * 发送请求
	 * @param url 接口地址
	 * @param requesttype 请求方式
	 * @param params 请求参数
	 * @return 结果集
	 * @throws ApiException异常信息
	 */
//	public T sendRequest(String url, int requesttype, StringHashMap params) throws ApiException;
	public T sendRequest(StringHashMap params) throws ApiException;
	
	/**
	 * 解析协议
	 * 
	 * @param xmlText 协议返回的信息
	 * @return 解析后的结果集
	 * @throws ApiException 异常信息
	 * @throws SAXParseException 
	 */
	public T parserXml(String xmlText) throws ApiException;
}

 