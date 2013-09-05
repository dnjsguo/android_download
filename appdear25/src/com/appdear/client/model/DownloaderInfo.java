package com.appdear.client.model;

import java.io.Serializable;

public class DownloaderInfo implements Serializable  {

	/**
	 * 爱皮市场软件ID
	 */
	public String softid = "";
	
	/**
	 * 软件图标
	 */
	public String softIcon = "";
	
	/**
	 * 软件下载地址
	 */
	public String softUrl = "";
	
	/**
	 * 软件下载进度
	 */
	public int progresssize;
	
	/**
	 * 版本ID
	 */
	public int versionid;
	
	/**
	 * 软件大小
	 */
	public int softSize;
	
	/**
	 * 软件名称
	 */
	public int softName;
	
}
