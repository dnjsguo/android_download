package com.appdear.client.model;

import java.io.Serializable;

import com.appdear.client.service.Constants;

import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 已安装列表信息
 * 
 * @author zqm
 *
 */
public class PackageinstalledInfo implements Serializable,Comparable {
	/**
	 * label
	 */
	public String appname = "";
	/**
	 * 首字符
	 */
	public String firstC = "Z";
	/**
	 * package name
	 */
	public String pname = "";
	
	/**
	 * 最新版本versionName
	 */
	public String updateVesrionName = "";
	
	/**
	 * version name
	 */
	public String versionName = "";
	
	/**
	 * version code
	 */
	public int versionCode = 0;
	
	/**
	 * icon
	 */
	public Drawable icon;
	
	/**
	 * is update
	 */
	public String isupdate = "";
	
	/**
	 * 更新地址
	 */
	public String downloadUrl = "";
	
	/**
	 * 爱皮的软件ID
	 */
	public int softID;
	
	/**
	 * 软件大小
	 */
	public int softsize = 0;
	
	/**
	 * 软件大小
	 */
	public String  formatsofttsize = "";
	/**
	 * 软件状态是否在sd
	 */
	public int  softsd = 0;
	
	/**
	 * 软件的选中状态
	 */
	
	public boolean isCheck = false;
	
	/**
	 * 应用可移动状态
	 * auto = 0; 默认安装到内存，可移动
	 * internalOnly = 1; 默认值，只能被安装在内存里
	 * preferExternal = 2; 默认安装到存储卡
	 * 为-1时不存在此属性等同与1
	 */
	public int installlocation = -1;
	
	/**
	 * 签名
	 */
	public Signature[] signatures;
	
	public String updatedesc;
	public int udlinenum;
	/**
	 * 是否只代表字符
	 */
	public boolean isCharProxy=false;
	public void prettyPrint() {   
		if (Constants.DEBUG)
			Log.i("installedInfo", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode + "\t" + "是否可移动=" + installlocation +"\t");   
	}
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		if(another!=null){
			PackageinstalledInfo pak=(PackageinstalledInfo)another;
			if(pak.firstC.charAt(0)>this.firstC.charAt(0)){
				return -1;
			}else if(pak.firstC.charAt(0)<this.firstC.charAt(0)){
				return 1;
			}else{
				return 0;
			}
		}
		return 0;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return pname+"="+softID+"="+appname;
	}
	public int status=0;
	
}
