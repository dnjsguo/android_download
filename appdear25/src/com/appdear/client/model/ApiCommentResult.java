package com.appdear.client.model;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.service.api.ApiResult;

/**
 * 评论结果返回
 * @author zqm
 *
 */
public class ApiCommentResult implements ApiResult{

	/**
	 * 平台服务版本号
	 */
	public String sv;
	
	/**
	 * 手机身份识别码
	 */
	public String imei;
	
	/**
	 * 请求结果码
	 */
	public int resultcode;
	
	/**
	 * 当前页码
	 */
	public int pageno;
	
	/**
	 * 每页记录数
	 */
	public int pagenum;
	
	/**
	 * 记录总数
	 */
	public int totalcount;
	
	/**
	 * 评论列表
	 */
	public List<SoftlistInfo> list = new ArrayList<SoftlistInfo>();
	
}
