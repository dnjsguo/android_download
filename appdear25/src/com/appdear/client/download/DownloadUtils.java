package com.appdear.client.download;

import java.io.File;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ServiceUtils;

/**
 * 下载工具类
 * @author zqm
 *
 */
public class DownloadUtils {
	/**
	 * 添加下载任务，判断是否符合下载条件
	 * @param bean
	 * @param context
	 * @return
	 */
	public static String[] download(SiteInfoBean bean, Context context) {
		int taskState = checkPlace(bean, context);
		//检测网络状态
		if (!ServiceUtils.checkNetState(context)) {
			return new String[]{"网络错误，请检查网络状态", "-1"};
		}
		//判断是否在wifi下下载
		boolean loadwifi = SharedPreferencesControl.getInstance().getBoolean(
				"loadwifi", com.appdear.client.commctrls.Common.SETTINGS, context);
		if (!ServiceUtils.isWiFiActive(context) && loadwifi) {
			return new String[]{"您设置仅在Wi-Fi环境下载,请检查网络或更改设置.", "-2"};
		}
		
		if (taskState == -3)
			return new String[]{"您的SD不存在，或插入不正确", "-3"};
		else if (taskState == -6)
			return new String[]{"SD卡空间已满", "-6"};
		else if (taskState == -7)
			return new String[]{"内存空间已满", "-7"};
		else if (taskState == -8)
			return new String[]{"存储空间已满", "-8"};
		
		SiteInfoBean hasbean = null;
		if((hasbean=AppContext.getInstance().taskList.get(bean.softID))!=null){
			//已经存在下载任务信息
			if (hasbean.state == 0 || hasbean.state == -1) {
				taskState = 0;//表明任务已存在,处于下载状态
				MyApplication.getInstance().getSoftMap().put(bean.softID, 1);
			} else if (hasbean.state == 1) {
				taskState = 1;//下载任务已处于暂停状态
				MyApplication.getInstance().getSoftMap().put(bean.softID, 1);
			} else if (hasbean.state == 2) {
				/*
				 * 在已存在的下载任务中是否有安装文件
				 */
				if (ServiceUtils.isHasFile(hasbean.sFilePath+"/"+bean.sFileName)) {
					//表明任务已存在,不需要重新下载
					taskState = 0;
					MyApplication.getInstance().getSoftMap().put(bean.softID, 1);
				} else {
					//如果不存在安装文件，重新下载
					taskState = 2;
					hasbean.downloadLength = 0;
				}
			}
			if (taskState != 0) {
				//暂停状态恢复下载,数据丢失文件重新下载,其他情况不执行
				addDownloadTask(hasbean, context);
			}
		}
		//以前不存在下载任务直接下载
		if (taskState == -2) {
			addDownloadTask(bean, context);
			MyApplication.getInstance().getSoftMap().put(bean.softID, 1);
		}
		if (taskState == 0)
			return new String[]{bean.softName+ "下载已存在，查看请到下载管理!", "0"};
		return new String[]{bean.softName+ "已开始下载，查看请到下载管理!", "0"};
	}
	
	/**
	 * 启动Service或重试、继续
	 * @param threadID
	 */
	public static void addDownloadTask(SiteInfoBean bean, Context context) {
		if (Constants.DEBUG) Log.i("info","addDownloadTask");
		//重置下载任务为等待下载状态
		bean.state = -1;
		//下载header为继续（用户服务端日志统计）
		bean.downloadstateHeader = 2;
		
		//启动Service
		Intent taskIntent = new Intent();
		taskIntent.setClass(context, FileDownloaderService.class);
		//因为Drawble在这里传递不过去，在这里保存一下状态值
		if (bean.dicon != null) {
			taskIntent.putExtra("Dicon", "true");
			bean.dicon = null;
		} else {
			taskIntent.putExtra("Dicon", "false");
		}
		
		bean.notification = null;
		bean.listener = null;
		//bean.view = null;
		bean.siteFileFecth = null;
		taskIntent.putExtra("download_bean", bean);
		context.startService(taskIntent);
		
		//同步下载信息
		AppContext.getInstance().downloader.downDb.update(bean);
	}
	
	/**
	 * 上传刚下载完成的文件
	 */
	public static void uploadTag(SiteInfoBean bean) {
		ApiManager.downloadcomplete(bean.sSiteURL, "1");
	}
	// -8    （存储空间已满）内存空间已满 && sdcard存储空间已满  && SD卡不存在   -7 内存空间已满   -6 sdcard存储空间已满    -3  SD卡不存在       -2 默认值正常下载
	public static int checkPlace(SiteInfoBean bean, Context context) {
		if (AppContext.getInstance().taskList.size() == 0)
			AppContext.getInstance().downloader.readDB();
		SiteInfoBean hasbean=null;
		
		if((hasbean=AppContext.getInstance().taskList.get(bean.softID))!=null){
			bean=hasbean;
			//sdcard
			if(bean.place==0)
			{
				if (null == ServiceUtils.getSDCardUrl()) {
					return -3;
				}
				//存储空间已满
				if (ServiceUtils.readSdCardAvailableSpace() < bean.fileSize) {
					return -6;
				}
			}else
			{
				if( ServiceUtils.readDeviceAvailableInternalSpace() < bean.fileSize)
				{
					 return -7;
				}
			}
			 return -2; 
		}else  //新下载
		{
			// 先判断sdcard 是否存在并且可用空间是否足够大
			File f=null;
			if (null !=(f=ServiceUtils.getSDCardUrl())&&ServiceUtils.readSdCardAvailableSpace() > bean.fileSize) {
				 bean.place=0;   //路径
				 bean.sFilePath=f.getPath()+"/apk";
				return -2;
			}else
			{
				// 先判断手机内存可用空间是否足够大
				if( ServiceUtils.readDeviceAvailableInternalSpace() > bean.fileSize)
				{
					 bean.place=1;
					 bean.sFilePath=Constants.DATA_APK;
					 return -2;
				}
			}
			return -8;
		}
		
	}
}
