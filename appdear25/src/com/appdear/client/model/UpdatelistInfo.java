package com.appdear.client.model;

import java.io.Serializable;

/**
 * 更新列表信息
 * @author zqm
 *
 */
public class UpdatelistInfo implements Serializable {

	/**
	 * 应用ID
	 */
	public String appid = "";
	
	/**
	 * 软件ID
	 */
	public int softid;
	
	/**
	 * softname
	 */
	public String softname = "";
	
	/**
	 * 最新版本号-versionName
	 */
	public String versionname = "";
	
	/**
	 * 软件下载地址
	 */
	public String downloadurl = "";
	
	/**
	 * 安装文件大小
	 */
	public int softsize = 0;
	
	/**
	 * 安装文件大小
	 */
	public String updatedesc ="";
	/**
	 * 换行数量
	 */
    public int udlinenum;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return appid;
	}
	
	
	
}
