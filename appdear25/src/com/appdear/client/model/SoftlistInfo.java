/**
 * CatalogsoftlistInfo.java
 * created at:2011-5-25下午03:13:58
 *
 * Copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 */
package com.appdear.client.model;

import java.io.File;
import java.io.Serializable;

import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;

import android.graphics.drawable.Drawable;

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
public class SoftlistInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 112312L;
	
	/**
	 * 是否选中d
	 */
	public boolean isCheck = false;
	
	/**
	 * 安卓软件应用id
	 */
	public String appid = "";
	
	/**
	 * 爱皮市场软件id
	 */
	public int softid = 0;
	
	/**
	 * 分享id
	 */
	public String shareid = "";
	
	/**
	 * 本地软件图标
	 */
	public Drawable icon;
	
	/**
	 * 软件图标
	 */
	public String softicon = "";
	
//	 软件图标本地位置 
	public String softiconPath = "";
	
	
	/**
	 * 软件名称
	 */
	public String softname = "";

	/**
	 * 软件描述
	 */
	public String softdesc = "";

	/**
	 * 软件价格
	 */
	public int softprice = 0;
	
	/**
	 * 软件消费积分
	 */
	public int softpoints = 0;

	/**
	 * 软件评级
	 */
	public int softgrade = 0;
	
	/**
	 * 评论的IMEI
	 */
	public String commentimei = "";
	
	/**
	 * vesrioncode 版本代号
	 */
	public int versioncode = 0;
	
	/**
	 * downloadurl
	 */
	public String downloadurl = "";
	
	/**
	 * 下载次数
	 */
	public int download = 0;
	
	/**
	 * 评论次数
	 */
	public int comment = 0;
	
	/**
	 * 软件大小
	 */
	public int softsize = 0;
	
	/**
	 * 软件语言版本
	 */
	public String language = "";
	
	/**
	 * 软件发布日期
	 */
	public String publishtime = "";
	
	/**
	 * 软件版本   版本名称
	 */
	public String version = "";
	
	/**
	 * 软件概要描述
	 */
	public String summary = "";
	
	/**
	 * 软件详细介绍
	 */
	public String detail = "";
	
	/**
	 * 积分
	 */
	public int point = 0;
	
	/**
	 * 时间
	 */
	public String time = "";
	
	/**
	 * 类型
	 */
	public int type = 0;
	
	/**
	 * 积分信息
	 */
	public String pointinfo = "";
	
	/**
	 * 评论ID
	 */
	public String commentid = "0";
	
	/**
	 * 评论人昵称
	 */
	public String username = "";
	
	/**
	 * 评论人ID
	 */
	public String userid = "0";
	
	/**
	 * 评论内容
	 */
	public String text = "";
	
	/**
	 * 类型
	 */
	public String adtype = "";
	
	/**
	 * 广告id
	 */
	public int adid = 0;
	
	/**
	 * 广告名称
	 */
	public String adtitle = "";
	
	/**
	 * 广告图片地址
	 */
	public String adurl = "";
	
	/**
	 * 广告地址
	 */
	public String imgurl = "";
	
	/**
	 * 类别图片地址
	 */
	public String catalogicon = "";
	
	/**
	 * 类别ID
	 */
	public String catalogid = "";
	
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
	public int defaultIcon = 0;
	
	/**
	 * 软件详情的分类id
	 */
	public int catid = 0;

	
	public String isfirst="";
	
	public String isexclusive="";
	
	public String istop="";
	/**
	 * 性别
	 */
	public String  gender = "1";
	
	public String getSofticonPath() 
	{
		if(softiconPath.equals(""))
		{
			File fileDir = ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR);

			String f[] =  softicon.replace("http://", "")
					.split("/");
			  softiconPath = f[f.length - 1];
			  
		} 
		return softiconPath;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof SoftlistInfo){
			SoftlistInfo soft=(SoftlistInfo)o;
			return soft.softid==this.softid; 
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.softid;
	}
	
	
}
