/**
 * SoftListInfo.java
 * created at:2011-5-24下午12:02:07
 *
 * Copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 */
package com.appdear.client.service.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.BackgroundInfo;
import com.appdear.client.model.CannelIDinfo;
import com.appdear.client.model.OrderlistInfo;
import com.appdear.client.model.PermissionListInfo;
import com.appdear.client.model.Recommentlist;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.model.UpdatelistInfo;

/**
 * 二级频道软件内容列表返回信息
 * 
 * @author zqm
 */
public class ApiSoftListResult implements ApiResult, Serializable {

	/**
	 * 请求结果码
	 */
	public String resultcode = "";
	
	/**
	 * 错误信息
	 */
	public String resultinfo = "";
	
	/**
	 * 平台服务版本号
	 */
	public String sv = "";
	
	/**
	 * 手机身份识别码
	 */
	public String imei = "";
	
	/**
	 * 当前页码
	 */
	public int pageno = 0;
	
	/**
	 * 每页记录数
	 */
	public int pagenum = 0;
	
	/**
	 * 记录总数
	 */
	public int totalcount = 0;
	
	/**
	 * 昵称
	 */
	public String nickname = "";
	
	/**
	 * 级别
	 */
	public String level = "";
	
	/**
	 * 作者ID
	 */
	public String authorid = "0";
	
	/**
	 * 作者名称
	 */
	public String author = "";
	
	/**
	 * 联系方式
	 */
	public String email = "";
	
	/**
	 * 作者主页地址
	 */
	public String homeurl = "";
	
	/**
	 * 服务地址前缀1
	 */
	public String spreurl = "";
	
	/**
	 * 服务地址前缀0
	 */
	public String dpreurl = "";
	
	/**
	 * wapurl
	 */
	public String wapurl = "";
	
	/**
	 * 是否需要升级爱皮市场客户端
	 * 0--否 1--是
	 */
	public String isupdate = "";
	
	/**
	 * 爱皮市场升级地址
	 */
	public String updateurl = "";
	
	/**
	 * 软件推荐列表数量
	 */
	public int recommentcount = 0;
	
	/**
	 * 版块模板版本号
	 */
	public int sectionversion = 0;
	
	/**
	 * 返回是否成功
	 */
	public int isok = 0;
	
	/**
	 * 软件详细信息
	 */
	public SoftlistInfo detailinfo = new SoftlistInfo();
	
	/**
	 * 软件列表
	 */
	public List<SoftlistInfo> softList = new ArrayList<SoftlistInfo>();

	/**
	 * 关键字列表
	 */
	public List<String> keywordList = new ArrayList<String>();
	
	/**
	 * 交易列表
	 */
	public List<OrderlistInfo> orderList = new ArrayList<OrderlistInfo>();

	/**
	 * 更新信息
	 */
	public List<UpdatelistInfo> updatelist = new ArrayList<UpdatelistInfo>();
	
	/**
	 * 权限信息
	 */
	public List<PermissionListInfo> permissionlist = new ArrayList<PermissionListInfo>();
	
	/**
	 * 搜索列表信息
	 */
	public List<SoftlistInfo> searchlist = new ArrayList<SoftlistInfo>();
	
	/**
	 * 频道ID
	 */
	public List<CannelIDinfo> initlist = new ArrayList<CannelIDinfo>();

	/**
	 * 软件截图的url
	 */
	public List<String> imgurl = new ArrayList<String>();
	
	/**
	 * 推荐软件列表
	 */
	public List<Recommentlist> recommentlist = new ArrayList<Recommentlist>();
	public List<String> autolist = new ArrayList<String>();
	
	/**
	 * 设置背景列表
	 */
	public List<BackgroundInfo> backgroundlist = new ArrayList<BackgroundInfo>();

	/**
	 * 升级信息
	 */
	public String softSize;

	public String softUpdateTip;

	public String softVersion;
	
	/**
	 * 测试链路url
	 */
	public String linkurl;
	
	/**
	 * 测试链路开关 1开启 0关闭
	 */
	public int linkflag;

	/**
	 * 专题副标题
	 */
	public String catdesc = "";
	
	public String dynamics="";
	
	public String def=null;
	
	public long timpstamp;
	
	public String asids=null;
}
