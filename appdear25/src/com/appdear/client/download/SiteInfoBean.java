/**
 * SiteInfoBean.java
 * created at:2011-5-10下午02:19:22
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.download;

import java.io.File;
import java.io.Serializable;

import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
  
/** 
 * 文件信息
 * 
 * @author zqm 
 */
public class SiteInfoBean implements Comparable, Serializable{
	/**
	 * Site‘s URL
	 */
	public String sSiteURL;
	
	/**
	 * Saved File‘s Path
	 */
	public String sFilePath;
	
	/**
	 * Saved File‘s Name
	 */
	public String sFileName;
	
	/**
	 * Files的存储位置  0 sdcard  1 内存
	 */
	public int place;
	
	/**
	 * 软件的名称
	 */
	public String softName;
	
	/**
	 * Count of Splited Downloading File
	 */
	public int nSplitter;
	
	/**
	 * soft icon
	 */
	public String softIcon;
	private  String softiconPath="";
	
	
	
	public String getSofticonPath() 
	{
		if(softiconPath.equals(""))
		{
			File fileDir = ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR);

			String f[] =  softIcon.replace("http://", "")
					.split("/");
			  softiconPath = f[f.length - 1];
			  
		} 
		return softiconPath;
	}

	/**
	 * 版本号
	 */
	public String versionID;
	
	/**
	 * 下载进度
	 */
	public int downloadLength;
	
	/**
	 * 文件大小
	 */
	public int fileSize;
	
	/**
	 * 应用ID
	 */
	public String appID;
	
	/**
	 * 软件ID
	 */
	public int softID = 0;
	
	/**
	 * 下载线程
	 */
	public SiteFileFetch siteFileFecth;
	
	/**
	 * 下载进度监听
	 */
	public DownloadListener listener;
	
	/**
	 * notification进度更新
	 */
	public DownloadListener notification;
	

	/**
	 * 文件下载状态-1 等待下载  0 --下载中  1 -- 手动暂停  2 -- 下载完成 3 -- 下载失败
	 */
	public int state;
	
	/**
	 * 下载界面的列表项
	 */
//	public View view;
	
	/**
	 * 下载界面的图标
	 */
	public Drawable dicon;
	
	/**
	 * 1--新下载 2--断点续传 3--重新下载
	 */
	public int downloadstateHeader = 1;
	
	/**
	 * handler更新进度或状态
	 */
	public Handler handler;
	
	/**
	 * 当前任务是否需要更新通知数量
	 */
	public boolean isShowNotification = true;
	
	public SiteInfoBean() {
		//default value of nSplitter is 1
		this("", "", "", "", "", "", 0, "", 0, 0, 1, null, null, null);
	}
	
	public SiteInfoBean(String sURL, String sPath, String sName, String softName, 
			String softIcon, String versionID, int softID, String appID, int fileSize, int state,
			int nSpiltter, DownloadListener listener, DownloadListener notification, Handler handler,String... params) {
		if(sURL!=null&&!sURL.equals("")){
			StringBuffer sb=new StringBuffer(sURL);
			if(sURL.lastIndexOf("?")==-1){
				sb.append("?imei=").append(AppContext.getInstance().IMEI).append("&time=").append(String.valueOf(new java.util.Date().getTime()));
			}else{
				if(sURL.contains("time=")){	
					sb.delete(0, sb.length());
					sb.append(ServiceUtils.getUrl("time=",String.valueOf(new java.util.Date().getTime()),sURL));
				}else{
					sb.append("&imei=").append(AppContext.getInstance().IMEI).append("&time=").append(String.valueOf(new java.util.Date().getTime()));
				}
			}
				for(String param:params){
					if(param.equals(Constants.SOFTPARAM)){
						if(sb.indexOf(Constants.SOFTPARAM)==-1)
							sb.append("&").append(Constants.SOFTPARAM).append("=").append("1");
					}else if(param.equals(Constants.UPDATEPARAM)){
						if(sb.indexOf(Constants.UPDATEPARAM)==-1)
							sb.append("&").append(Constants.UPDATEPARAM).append("=").append("1");
					}else{
						continue;
					}
				}
		    sSiteURL= sb.toString();
		}else{
			sSiteURL= sURL;
		}
		sFilePath = sPath;
		sFileName = sName;
		this.softName = softName;
		this.softIcon = softIcon;
		this.versionID = versionID;
		this.softID = softID;
		this.appID = appID;
		this.fileSize = fileSize;
		this.state = state;
		this.listener = listener;
		this.nSplitter = nSpiltter;
		this.notification = notification;
		this.handler = handler;
	}
	
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		if(another!=null){
			if(((SiteInfoBean)another).state>this.state){
				return -1;
			}else{
				return 1;
			}
		}
		return 0;
	}
	public void setFlagProcess(int downflag){
		flagprocess=getProgress()/downflag+1;
    }
	public int flagprocess=1; 
	public int getProgress(){
		if(fileSize>0){
			float size = (float)downloadLength/fileSize;
		//格式化小数，不足的补0
			
		    return (int) Math.floor(size*1000);
		}else{
			return 0;
		}
	}
	public float getProgress1(){
		if(fileSize>0){
			float size = (float)downloadLength/fileSize;
		//格式化小数，不足的补0
			
		    return ((float)(int) Math.floor(size*1000))/10;
		}else{
			return 0f;
		}
	}
}

 