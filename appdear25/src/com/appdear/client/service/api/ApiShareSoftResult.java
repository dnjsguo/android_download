package com.appdear.client.service.api;

import java.util.ArrayList;

import com.appdear.client.model.SharePageModel;
import com.appdear.client.model.SoftlistInfo;

public class ApiShareSoftResult {
	public  String    resultcode;
	public  String   imei;
	public  String  sv;
 
	/**
	 * 分享用户分页项
	 */
	public String page;
	/**
	 * 页码
	 */
	public int pageno;
	/**
	 * 每页记录数
	 */
	public int pagenum ;
	/**
	 * 总数量
	 */
	public int totalcount;
	public ArrayList<SoftlistInfo> myshareList = new ArrayList<SoftlistInfo>();
	public String nickname;
	public int popcount;
	public String userid;
	public int type;
	public int gender = 0;
 
}
