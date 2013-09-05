package com.appdear.client.download;

import android.content.Context;

import com.appdear.client.service.AppContext;

/**
 * 下载管理，检测下载状态 
 * @author zqm
 *
 */
public class FileDownloader {

	//下载中的几种状态
	/**
	 * 暂停状态（手动暂停）
	 */
	private final int PAUSE = 1;
	
	/**
	 * 下载列表中的下载信息存储
	 */
	public FileDownloadDB downDb;
	
	public FileDownloader(Context context) {
		//初始化下载列表
		downDb = new FileDownloadDB(context);
	}
	
	/**
	 * 读取数据库中的下载列表信息
	 */
	public void readDB() {
		//读取数据库
		AppContext.getInstance().taskList = downDb.read();
	}

	/**
	 * 暂停下载任务
	 * @param bean
	 */
	public void pauseTask(SiteInfoBean bean) {
		if(bean==null)return;
		bean.state = PAUSE;
		//原这里是暂停线程后来由于什么需求改成线程停止,点继续后使用断点续传继续下载(具体原因记不清了)
		if (bean.siteFileFecth != null)
			bean.siteFileFecth.siteStop();
		//同步下载信息
		downDb.update(bean);
	}
	
	/**
	 * 删除下载任务
	 * @param bean
	 */
	public void removeTask(SiteInfoBean bean) {
		downDb.delete(bean.softID);
		AppContext.getInstance().taskList.remove(bean.softID);
		AppContext.getInstance().taskSoftList.remove(String.valueOf(bean.softID));
	}
}
