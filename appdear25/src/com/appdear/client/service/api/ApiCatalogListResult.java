/**
 * SoftListInfo.java
 * created at:2011-5-24下午12:02:07
 *
 * Copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 */
package com.appdear.client.service.api;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.BannerInfo;
import com.appdear.client.model.CannelIDinfo;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.model.CommentInfo;
import com.appdear.client.model.MessagelistInfo;
import com.appdear.client.model.OrderlistInfo;
import com.appdear.client.model.PermissionListInfo;
import com.appdear.client.model.PointlistInfo;
import com.appdear.client.model.Recommentlist;
import com.appdear.client.model.Searchlistinfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.model.UpdatelistInfo;

/**
 * 类别列表返回信息
 * 
 * @author zxy
 */
public class ApiCatalogListResult implements ApiResult {

	/**
	 * 返回结果码
	 */
	public int resultcode = 0;
	
	/**
	 * totalcount
	 */
	public int totalcount = 0;
	/**
	 * items
	 */
	public List<CatalogListInfo> catalogList = new ArrayList<CatalogListInfo>();
	/**
	 * pagenum
	 */
	public int pagenum = 0;
	/**
	 * pageno
	 * 
	 */
	public int  pageno = 0;
	/**
	 * imei
	 */
	public String imei = "";
	/**
	 * sv
	 */
	public String sv = "";
}
