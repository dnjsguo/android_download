/**
 * MyApplication.java
 * created at:2011-5-11下午04:44:54
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.service;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.utility.ServiceUtils;

import dalvik.system.VMRuntime;
  
/** 
 * 全局实例
 * @author zqm 
 */
public class MyApplication extends Application {

	public static Map<String, View> mView = new HashMap<String, View>();
	public static void setInstance(MyApplication instance) {
		MyApplication.instance = instance;
	}
	public Activity  mainActivity=null;
	public ListBaseActivity  searchResultActivity=null;
	/**
	 * 全局控制所有应用显示状态的变量集合
	 */
	 Map<Integer, Integer> softMap=new Hashtable<Integer, Integer>();
	 public String username163;
	 public String password163;
	 /**
	  * 用户点击安装按钮 记录应用appid 对应的 softid，目的是通过广播接收器拦截到安装appid后和此集合对比，如果有则说明是在我们爱皮下载的，并取出对应的softid
	  */
	 Map<String, Integer> AppMap=new HashMap<String, Integer>();
	 /**
	  * 用户点击卸载按钮时， 记录应用appid。目的是通过广播接收器拦截到卸载应用的appid后和此集合对比，如果有则说明是在我们爱皮卸载的。
	  */
	 public  Map<String, String> appUninstalledMap=new HashMap<String, String>();
     //控制推出爱皮时间
	 public  long  exitTime=-1;
	 // android系统版本号
	 public  int androidLevel;
	 // 详情界面同类推荐过滤掉以前显示过的集合
	 public  List<Integer>    detailSoftidList= new ArrayList<Integer>();
	 // 升级列表显示数量  
	 public  int elideupdate;
	 
	 //机型专区 上传到服务器
	 public String  modelCompany="";
	 public String  modelCompanyPhone="";
	 // 是否加载过购机时间
	 public boolean isLoadBuyTime=false;
	 
	/**
	 * 全局实例
	 */
	private static MyApplication instance;
    
	public static MyApplication getInstance() {
		if(instance==null){
			if (Constants.DEBUG)
				Log.i("instance :", "instance");
		}else{
			 
		}
		return instance;
	}

	public Map<String, Integer> getAppMap() {
		return AppMap;
	}

	public void setAppMap(Map<String, Integer> appMap) {
		AppMap = appMap;
	}
	//public List<String> urlDetailList=new ArrayList<String>();
	//全局图片缓存
	private Map<String, SoftReference<Bitmap>> imageCache ;
	//private Map<String,Boolean>  imageFalgCache;//recycled
	public static String serial="";
	@Override
	public void onCreate() {
		super.onCreate();
		if (Constants.DEBUG)
			Log.i("onCreate", "onCreate");
		instance = this;
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		//SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,AlterUserRegActivity.this)
		AppContext.getInstance().IMEI=ServiceUtils.getIMEI(this);
		username163=SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERLOGIN_163_XML,this);
		password163=SharedPreferencesControl.getInstance().getString("password",com.appdear.client.commctrls.Common.USERLOGIN_163_XML,this);

		//清除用户当前登录信息
		SharedPreferencesControl.getInstance().clear(Common.USERLOGIN_XML,this);
		androidLevel = Build.VERSION.SDK_INT;	
		 Class<?> c=null;
			try {
				c = Class.forName("android.os.SystemProperties");
				 Method get=c.getMethod("get", String.class);
				
					serial = (String)get.invoke(c,"ro.serialno");
				
		     } catch (Exception e) {
				// TODO Auto-generated catch block
				
			 }   
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		//清除用户登录信息
		
		super.onTerminate();
	}
	public Map<Integer, Integer> getSoftMap() {
		return softMap;
	}

	public void setSoftMap(Map<Integer, Integer> softMap) {
		this.softMap = softMap;
	}
	public Map<String, SoftReference<Bitmap>> getImageCache() {
		return imageCache;
	}
  /*  public boolean isRecycled(String url)
    {
    	if(imageFalgCache.containsKey(url))
    	{
    		return  true;
    	}else
    	{
    		return false;
    	}
      
    }*/
   /* public  void setRecycledFlag(String url,boolean flag)
    {
    	imageFalgCache.put(url,flag);
    }*/
	public Bitmap  getBitmapByUrl(String imageUrl)
	{
	 	VMRuntime.getRuntime().setMinimumHeapSize(10*1024*1024);
	/*	Collection<SoftReference<Bitmap>> collection=imageCache.values();
		int i=0;
		int count=0;
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			
			SoftReference<Bitmap> softReference = (SoftReference<Bitmap>) iterator.next();
			if( softReference.get()==null)
			{
				count++;
			}
			 i++;
		} 
		System.out.println("i======"+i+";    count======"+count);*/
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap bitmap=softReference.get();
			if (bitmap != null) {
		 		//System.out.println("-----------softReference.get()---------------");
				return bitmap;
			} 
		}
		
		return null;
	}
	public void  setBitmapByUrl(String imageUrl,Bitmap bitmap)
	{
		imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
	}
}

 