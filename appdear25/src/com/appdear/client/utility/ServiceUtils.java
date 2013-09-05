/**
 * SystemUtils.java
 * created at:2011-5-11下午05:55:31
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.utility;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.appdear.client.AlterUserRegActivity;
import com.appdear.client.AlterUserShare163Activity;
import com.appdear.client.MainActivity;
import com.appdear.client.QianmingDialog;
import com.appdear.client.R;
import com.appdear.client.SoftwareMainDetilActivity;
import com.appdear.client.UpdateAppService;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.download.FileDownloaderService;
import com.appdear.client.download.ListAdatperDataProcessListener;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum;
import com.appdear.client.model.HomeObj;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.model.WeiBoUser;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.AppdearService;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MessageHandler;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.UpdateService;
import com.appdear.client.utility.cache.ListviewSourceCache;
import com.appdear.client.utility.cache.SourceCommon;
  
/** 
 * 业务工具类
 * 
 * @author zqm 
 */
public class ServiceUtils {
	public static int [] info=null;
	/**
	 * 微博 sharedPreference 名字
	 */
	private static final String  SINA_WEIBOACCOUNT = "sina_weiboaccount";
	private static final String  TECENT_WEIBOACCOUNT = "tecent_weiboaccount";
	private static final String  NETEASY_WEIBOACCOUNT = "neteasy_weiboaccount";
	private static final String  SOUHU_WEIBOACCOUNT = "sohu_weiboaccount";
	private static AlertDialog  dialog = null;
	private static  boolean isShowNetDialog = false;
	private static int readcount = 0;
	
	private static final String SCHEME = "package";
	
	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
	 */
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	
	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
	 */
	private static final String APP_PKG_NAME_22 = "pkg";
	
	/**
	 * InstalledAppDetails所在包名
	 */
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	
	/**
	 * InstalledAppDetails类名
	 */
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	/**
	 * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
	 * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
	 * 
	 * @param context
	 * 
	 * @param packageName
	 *            应用程序的包名
	 */
	public static void showInstalledAppDetails(Context context, String packageName) {
		
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		context.startActivity(intent);
	}
	
	/**
	 * 判断已安装软件是否可移动
	 * @param packageName
	 */
	public static int getPackageInstallLocation(Context context, String packageName) {
		try {
			AssetManager am = context.createPackageContext(packageName, 0).getAssets();
			XmlResourceParser xml = am.openXmlResourceParser("AndroidManifest.xml"); 
			int eventType = xml.getEventType(); 
			xmlloop: 
			while (eventType != XmlPullParser.END_DOCUMENT) { 
			    switch (eventType) { 
			        case XmlPullParser.START_TAG: 
			            if (! xml.getName().matches("manifest")) { 
			                break xmlloop; 
			            } else {
			                for (int j = 0; j < xml.getAttributeCount(); j++) { 
			                    if (xml.getAttributeName(j).matches("installLocation")) { 
			                    	String installLocation = xml.getAttributeValue(j);
			                    	if (installLocation.equals("auto"))
			                    		return 0;
			                    	else if (installLocation.equals("internalOnly"))
			                    		return 1;
			                    	else if (installLocation.equals("preferExternal"))
			                    		return 2;
			                    	else if (!installLocation.equals("") && 
			                    			installLocation.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"))
			                    		return Integer.parseInt(xml.getAttributeValue(j));
			                    	else 
			                    		return -1;
			                    } 
			                } 
			            } 
			            break; 
			        } 
			    	eventType = xml.nextToken();
			    } 
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static synchronized void removeSoftStateUninstall(String pName)
	{
		if(pName==null)return;
		int softState=Math.abs(pName.hashCode());	
		 Map<Integer, Integer> softMap = MyApplication.getInstance().getSoftMap();
		 int key=-1;
		 if(softMap.containsValue(softState)){
			 for(Map.Entry<Integer, Integer> v:softMap.entrySet()){
				 if(v.getValue()==softState){
					 key=v.getKey();
					 break;
				 }
			 }
		 }
		 if(key>0){
			 softMap.remove(key);
		 }
		
	}
	/**
	 * 处理soft 状态
	 * @param context
	 */
	public static void setSoftState( Context context,List<SoftlistInfo> listData) {
		if (AppContext.getInstance().taskList.size() == 0&&readcount==0){
			readcount=-1;
			AppContext.getInstance().taskList = AppContext.getInstance().downloader.downDb.read();
		}
		 if(listData!=null&&listData.size()>0)
		 {
			 Map<Integer, Integer> softMap = MyApplication.getInstance().getSoftMap();
		//	 System.out.println("map size="+softMap.size());
			 for(SoftlistInfo item:listData)
			 {
				
				  int tempState=0;
				  String apkname;
				  int  appNameHashCode=0;
				  if(softMap.containsKey(item.softid)&&softMap.get(item.softid)==1)
				  {				  
					  continue;	
				  }
				  SiteInfoBean bean = ServiceUtils.checkTaskState(item.softid);
					//下载中判断是否完成下载
					
					if (bean != null && bean.state == 2) {
						//apk文件完整
						//item.state = 2;
						softMap.put(item.softid, 2);
						continue;	
					} else if (bean != null && bean.state == 1) {
						//暂停，显示继续
						softMap.put(item.softid, 5);
						continue;	
					} else if (bean != null && (bean.state == 0 || bean.state == -1)) {
						//正在下载，显示下载中
						softMap.put(item.softid, 1);
					}else {
						if (bean != null) {		
							//item.state = 1;	
							softMap.put(item.softid, 1);
							continue;	
						}
					}
					if (tempState == 0) {
						if (!item.downloadurl.equals("")&&bean!=null) {
							apkname=bean.sFileName;
							if (ServiceUtils.isWhileFile(apkname)) {
								//item.state = 2;
								softMap.put(item.softid, 2);
								continue;	
							}
						}
					}	
				 // System.out.println("softname="+item.appid+"   softid="+softMap.get(item.softid));
				 if (ServiceUtils.isPkgInstalled(context, item.appid)) 
				 { 					
					  
				//	 item.state = 3;	
					 appNameHashCode=Math.abs(item.appid.hashCode());
					 tempState=appNameHashCode;
					 Vector<PackageinstalledInfo> list=	AppContext.getInstance().updatelist;
					//Log.i("info9898", list+"");
						for(Iterator iterator = list.iterator(); iterator.hasNext();) {
							PackageinstalledInfo packageinstalledInfo = (PackageinstalledInfo) iterator
									.next();
 							
							if(packageinstalledInfo.softID==item.softid)
							{
 									tempState=4;
								break;
							}
						}
						Vector<PackageinstalledInfo> elideupdatelist=	AppContext.getInstance().elideupdatelist;
						for(Iterator iterator = elideupdatelist.iterator(); iterator.hasNext();)
						{
							PackageinstalledInfo packageinstalledInfo = (PackageinstalledInfo) iterator
									.next();
							if(packageinstalledInfo.softID==item.softid)
							{
							    tempState=4;
								break;
							}
						}
					softMap.put(item.softid, tempState);					 
					continue;	
				 }
				
			 }
		 }
	}
	/**
	 * 弹出联网提示
	 * @param context 界面显示
	 * @param flag  是否关闭,true 关闭
	 */
	public static void showNoNetStateAlert(final Context context,boolean tag) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.alertdialog_layout1, null);
		if(tag&&!isShowNetDialog){
		dialog  = new AlertDialog.Builder(context)
		.setView(dialogView)
		.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						((Activity) context).startActivityForResult(
								new Intent(
										Settings.ACTION_WIRELESS_SETTINGS), 0);
					}
		}).setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						((Activity) context).finish();
						System.exit(0);
						UpdateService.UPDATE_SERVICE_STARTTIME = System.currentTimeMillis();
						SharedPreferencesControl.getInstance().putLong("updatetime", UpdateService.UPDATE_SERVICE_STARTTIME,
								null, context);
					}
		}).show();
			isShowNetDialog = true;
		}else{
			isShowNetDialog = false;
			if(dialog!=null)
				dialog.dismiss();
		}
	}
	
	
//	/**
//	 * 显示退出提示框
//	 * @param context
//	 */
//	public static void showExitDialogOld(final Context context) {
//		new AlertDialog.Builder(context)
//		.setMessage("确定退出爱皮应用下载吗？")
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				//AppContext.getInstance().downloader.downDb.updatePause();
//				softListHandlerForHomePage(context);
//				((Activity) context).finish();
//				if(MainActivity.timer!=null){
//					MainActivity.timer.cancel();
//					MainActivity.timer=null;
//				}
//				 SharedPreferencesControl.getInstance().putLong("updatetime",new java.util.Date().getTime(),
//							null, context);
//				((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.UPDATE_INFO_ID);
//				((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.CLIENT_UPDATE_INFO);
//				ScreenManager.getScreenManager().exitApp();
//				MyApplication.getInstance().password163=null;
//				MyApplication.getInstance().username163=null;
//				//System.exit(0);
//				
//				if (FileDownloaderService.task_num == -1 
//						|| FileDownloaderService.task_num == 0){
//					System.exit(0);
//				}else{
//					AppContext.getInstance().exitflag=false;
//				}
//				UpdateService.UPDATE_SERVICE_STARTTIME = System.currentTimeMillis();
//				SharedPreferencesControl.getInstance().putLong("updatetime", UpdateService.UPDATE_SERVICE_STARTTIME,
//						null, context);
//			}
//		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		}).show();
//	}
	/**
	 * 显示退出提示框showExitToast
	 * @param context
	 */
	public static void showExitDialog(final Context context) {
		 
		    if(MyApplication.getInstance().exitTime==-1)
		    {
		    	Toast mScreenHint = Toast.makeText(context, "再按一次返回键将退出市场",Toast.LENGTH_SHORT);
				mScreenHint.show();
				MyApplication.getInstance().exitTime=System.currentTimeMillis();
		    }else     
		    {
		       long interval=(System.currentTimeMillis()-MyApplication.getInstance().exitTime)/1000;
 		       if(interval>3)
		       {
		    	   Toast mScreenHint = Toast.makeText(context, "再按一次返回键将退出市场",Toast.LENGTH_SHORT);
					mScreenHint.show();
					MyApplication.getInstance().exitTime=System.currentTimeMillis();
		       }else
		       {
		    	 //  System.out.println("----推出--");
		    	   
		    	   /**
		    	    * 首页数据缓存处理
		    	    * 如果首页有更新数据过滤
		    	    * 最多过滤5条更新数据
		    	    */
		    	   softListHandlerForHomePage(context);
		    	   MyApplication.getInstance().exitTime=-1;
		    	   ((Activity) context).finish();
					if(MainActivity.timer!=null){
						MainActivity.timer.cancel();
						MainActivity.timer=null;
					}
					 SharedPreferencesControl.getInstance().putLong("updatetime",new java.util.Date().getTime(),
								null, context);
					((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.UPDATE_INFO_ID);
					((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.CLIENT_UPDATE_INFO);
					((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(UpdateAppService.notification_id);
					ScreenManager.getScreenManager().exitApp();
					MyApplication.getInstance().password163=null;
					MyApplication.getInstance().username163=null;
					if(AppContext.getInstance().taskList.values()!=null&& AppContext.getInstance().taskList.values().size()>0)
					{
						 for(SiteInfoBean bean :AppContext.getInstance().taskList.values())
						 {
							 System.out.println("name="+bean.softName+"   state="+bean.state);
							 if(bean.state==0)
							 {
								return;
							 }
						 }
					}
					UpdateService.UPDATE_SERVICE_STARTTIME = System.currentTimeMillis();
					SharedPreferencesControl.getInstance().putLong("updatetime", UpdateService.UPDATE_SERVICE_STARTTIME,
							null, context);
						android.os.Process.killProcess(android.os.Process.myPid());

					/*if (FileDownloaderService.task_num == -1 || FileDownloaderService.task_num == 0)
					{
						android.os.Process.killProcess(android.os.Process.myPid());
						UpdateService.UPDATE_SERVICE_STARTTIME = System.currentTimeMillis();
						SharedPreferencesControl.getInstance().putLong("updatetime", UpdateService.UPDATE_SERVICE_STARTTIME,
								null, context);
						//ActivityManager activityMgr = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
						//activityMgr.killBackgroundProcesses(context.getPackageName());
						// activityMgr.restartPackage(context.getPackageName());
					}*/
						// System.exit(0);
		       }
		    }
		
		
				//AppContext.getInstance().downloader.downDb.updatePause();
				/*((Activity) context).finish();
				if(MainActivity.timer!=null){
					MainActivity.timer.cancel();
					MainActivity.timer=null;
				}
				 SharedPreferencesControl.getInstance().putLong("updatetime",new java.util.Date().getTime(),
							null, context);
				((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.UPDATE_INFO_ID);
				((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.CLIENT_UPDATE_INFO);
				ScreenManager.getScreenManager().exitApp();
				MyApplication.getInstance().password163=null;
				MyApplication.getInstance().username163=null;
				//System.exit(0);
				if (FileDownloaderService.task_num == -1 
						|| FileDownloaderService.task_num == 0)
					System.exit(0);*/
		 
	}
	public static void softListHandlerForHomePage(Context context){
		HomeObj homeobj=(HomeObj)ServiceUtils.getListview(SourceCommon.HOME_FIRST_NEW,context);
		if(homeobj!=null){
			if(AppContext.getInstance().dynamicnew==null)return;
			homeobj.setDynamic(AppContext.getInstance().dynamicnew);
			List<SoftlistInfo> listData1=new ArrayList<SoftlistInfo>();
			List<SoftlistInfo> listData=homeobj.getListData();
			int count=0;
			for(SoftlistInfo info:listData){
				 if(count<5){
					if(AppContext.getInstance().installlists.containsKey(info.appid)){
//							if(AppContext.getInstance().updatelist.contains(AppContext.getInstance().installlists.get(info.appid))){
//									count++;
//									continue;
//							}else if(AppContext.getInstance().elideupdatelist.contains(AppContext.getInstance().installlists.get(info.appid))){
//								    count++;
//								    continue;
//							}else{
//								count++;
//								continue;
//							}
						count++;
						continue;
					}
					if(AppContext.getInstance().installlistssys.containsKey(info.appid)){
						count++;
						continue;
//						if(AppContext.getInstance().updatelist.contains(AppContext.getInstance().installlistssys.get(info.appid))){
//								count++;
//								continue;
//						}else if(AppContext.getInstance().elideupdatelist.contains(AppContext.getInstance().installlistssys.get(info.appid))){
//						    count++;
//						    continue;
//					     }
					}
				 }
				listData1.add(info);
			}
			int total=SharedPreferencesControl.getInstance().getInt(SourceCommon.HOME_FIRST_NEW, com.appdear.client.commctrls.Common.LISTVIEWSOURCE_XML, context);
			homeobj.setListData(listData1);
			ServiceUtils.addListview(SourceCommon.HOME_FIRST_NEW, homeobj,context,total-count);
		}
	}
	/**
	 * 退出应用
	 * @param context
	 */
	public static void exitSystem(final Context context) {
		softListHandlerForHomePage(context);
		((Activity) context).finish();
		if(MainActivity.timer!=null){
			MainActivity.timer.cancel();
			MainActivity.timer=null;
		}
		 SharedPreferencesControl.getInstance().putLong("updatetime",new java.util.Date().getTime(),
					null, context);
		((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.UPDATE_INFO_ID);
		((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AppdearService.CLIENT_UPDATE_INFO);
		((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(UpdateAppService.notification_id);
		ScreenManager.getScreenManager().exitApp();
		MyApplication.getInstance().password163=null;
		MyApplication.getInstance().username163=null;
		//System.exit(0);
		if (FileDownloaderService.task_num == -1 
				|| FileDownloaderService.task_num == 0)
			{
			android.os.Process.killProcess(android.os.Process.myPid());
			UpdateService.UPDATE_SERVICE_STARTTIME = System.currentTimeMillis();
			SharedPreferencesControl.getInstance().putLong("updatetime", UpdateService.UPDATE_SERVICE_STARTTIME,
					null, context);
		//ActivityManager activityMgr = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			//activityMgr.killBackgroundProcesses(context.getPackageName());
			// activityMgr.restartPackage(context.getPackageName());
			   //System.exit(0);
			}else{
				AppContext.getInstance().exitflag=false;
			}
	}
	
	/**
	 * 提示有新版本
	 * @param context
	 */
	public static void showUpdateDialog(final Context context, 
			final ListAdatperDataProcessListener listener) {
		new AlertDialog.Builder(context)
		.setMessage("有新版本，是否要更新？")
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null) {
					listener.keyPressProcess(-1, 1);
				}
			}
		}).setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}
	
	/**
	 * 检查网络状态
	 * @param activity 界面显示
	 * @return 网络状态
	 */
	public static synchronized boolean checkNetState(Context context) {
		ConnectivityManager connectManager = (
				ConnectivityManager) context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = null;
		if(connectManager!=null) info = connectManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			if (info.getTypeName().equals("mobile") && info.getSubtypeName().equals("UNKNOWN"))
				return false;
			else{
				if(NetworkInfo.State.CONNECTED.compareTo(info.getState())==0||info.isConnected()){
					return true;
				}else{
					return false;
				}
			}
				
		} else
			return false;
	}

	/**
	 * 获得可用的手机内存大小
	 */
	public static long readDeviceAvailableInternalSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return  availableBlocks * blockSize;
    }
	 public static String formatFileSize(long length) {
			String result = null;
			int sub_string = 0;
			if (length >= 1073741824) {
				sub_string = String.valueOf((float) length / 1073741824).indexOf(
						".");
				result = ((float) length / 1073741824 + "000").substring(0,
						sub_string + 3)
						+ "GB";
			}  else if (length >= 1048576) {
				sub_string = String.valueOf((float) length / 1048576).indexOf(".");
				result = ((float) length / 1048576 + "000").substring(0,
						sub_string + 3)
						+ "MB";
			}else if (length >= 1024) {
				sub_string = String.valueOf((float) length / 1024).indexOf(".");
				result = ((float) length / 1024 + "000").substring(0,
						sub_string + 3)
						+ "KB";
			} else if (length < 1024)
				result = Long.toString(length) + "B";
 
			return result;
		}

	/**
	 * 文件保存在SD卡的目录
	 * @return
	 */
	public static File getSDCardUrl() {
		String path = "";
		File f = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			path = Environment.getExternalStorageDirectory().getPath()+Constants.CACHE_DATA_DIR;
			f = new File(path);
			if(!f.exists()){
				f.mkdirs();
				if (Constants.DEBUG)
					Log.i("crate app data dir", ""+f.getAbsolutePath());
			}
			return f;
		}else{
			return null;
		}
	}

	/**
	 * 图片保存目录
	 * @return
	 */
	public static File getSDCARDImg(String url) {
		String path = "";
		File f = null;
		if (Environment.getExternalStorageDirectory()!=null&&!Environment.getExternalStorageDirectory().equals("")&&Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			path = Environment.getExternalStorageDirectory().getPath() + url;
			f = new File(path);
			if(!f.exists()){
				f.mkdirs();
				if (Constants.DEBUG)
					 Log.i("crate image cache dir", ""+f.getAbsolutePath());
			}
			return f;
		}else{
			return null;
		}
	}
	/**
	 * 弹出提示对话框
	 * @param context
	 * @param msg
	 */
	public static void showTipMsg(final Context  context,String msg){
	new AlertDialog.Builder(context).setIcon(
			R.drawable.dialog_tip_32_32).setTitle("提示")
			.setMessage(msg).setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							// Do thing .
						}
					}).show();
	}
	/**
	 * 弹出提示对话框,自定义View
	 * @param context
	 * @param msg
	 */
	public static void showMsg(final Context  context,View v){
		new AlertDialog.Builder(context).setIcon(R.drawable.dialog_tip_32_32).setView(v).show();
	}
	/**
	 * SD卡的总大小
	 * @return
	 */
	public static long readSDCardTotalSize() {
		return readSDCardBlockCount() * readSDCardBlockSize();
	}
	
	/**
	 * SD卡的可用大小
	 * @return
	 */
	public static long readSdCardAvailableSpace() {
		return readSDCardAvailCount() * readSDCardBlockSize();
	}
	
	/**
	 * 转换
	 * @param bt
	 * @return
	 */
	public static String returnSpace(long bt) {
		try{
		if (bt == 0 || bt == -1 || bt == -2)
			return 0 + "";
		int g=(int)(bt>>30);
		int m=(int)(bt-(g<<30))>>20;
		int k=(int )(bt-(g<<30)-(m<<20))>>10;
		if (g <= 0)
			g = 0;
		if (m <= 0)
			m = 0;
		if (k <= 0)
			k = 0;
		if (g == 0) {
			if (m == 0)
				return k + "K";
			else {
				if (k != 0) {
					double d = returnDouble(new Double(m + "." + k).doubleValue());  //保留一位小数
					return d + "M";
//					return m + "." + k + "M";
				} else {
					return m +"M";
				}
			}
		} else {
			if (m != 0) {
				double d = returnDouble(new Double(g + "." + m).doubleValue());  //保留一位小数
				return d + "G";
//				return g + "." + m + "G";
			}
			return g + "G";
		}
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 四舍五入,小数点后面保留一位小数
	 * @return double值
	 */
	public static double returnDouble(double k){
		BigDecimal b = new BigDecimal(k);  
		double d = b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
		return d;
	}
	
	/**
	 * 读取可用的Block数量
	 * @return 可用的Block的数量
	 */
	public static long readSDCardAvailCount() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(sdcardDir.getPath());
			return  statfs.getAvailableBlocks();
		}
		return -1;
	}
	
	/**
	 * 读取Block的数量
	 * @return Block的数量
	 */
	public static long readSDCardBlockCount() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(sdcardDir.getPath());
			return  statfs.getBlockCount();
		}
		return -1;
	}
	
	/**
	 * 读取Block的数量
	 * @return Block的数量
	 */
	public static long readSDCardBlockSize() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(sdcardDir.getPath());
			return  statfs.getBlockSize();
		}
		return -1;
	}
	
	public static long getAvailableStore(String filePath) {

		// 取得sdcard文件路径

		StatFs statFs = new StatFs(filePath);

		// 获取block的SIZE

		long blocSize = statFs.getBlockSize();

		// 获取BLOCK数量

		long totalBlocks = statFs.getBlockCount();

		// 可使用的Block的数量

		long availaBlock = statFs.getAvailableBlocks();

		long total = totalBlocks * blocSize;

		long availableSpare = availaBlock * blocSize;

		return availableSpare;

	}
	
	/**
	 * 获取手机信息
	 * 手机屏幕尺寸+系统版本+用户电话号（如有卡）+手机型号
		格式[LCD:480*800][systemversion:android%202.2][phonenumber:13432343434][HTC%20Desire][IMEI:5424264536543243]
	 * @return
	 */
	public static String getPhoneInfo(Context context) {
		StringBuffer sb = new StringBuffer();

		Display dm = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
		String imei = tm.getDeviceId();  
		String tel = tm.getLine1Number();  
		
		sb.append("[LCD:");
		sb.append(Integer.toString(dm.getWidth()));
		sb.append("*");
		sb.append(Integer.toString(dm.getHeight()));
		sb.append("]");
		
		sb.append("[systemversion:");
		sb.append(android.os.Build.VERSION.RELEASE);
		sb.append("]");
		
		sb.append("[phonenumber:");
		sb.append(tel);
		sb.append("]");
		
		sb.append("[");
		sb.append(android.os.Build.MODEL);
		sb.append("]");
		
		sb.append("[IMEI:");
		sb.append(imei);
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * IMEI
	 * abstract
	 * <p>description
	 * <p>example:
	 * <blockquote><pre>
	 * </blockquote></pre>
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String deviceId=null;
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		if(tm==null||tm.getDeviceId()==null||tm.getDeviceId().equals("")){
			deviceId=getLocalMacAddress(context);
			if(deviceId!=null)deviceId.replace(":","");
			AppContext.getInstance().MAC=deviceId;
		}else{
			deviceId= tm.getDeviceId();
		}
		return deviceId;
	}
	 private  static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
	}
	/**
	 * 判断程序是否已安装
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static boolean isPkgInstalled(Context ctx, String packageName){
        PackageManager pm = ctx.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return false;
        }
        return true;
    }
	
	public static int  getSoftSize(String path,ApplicationInfo p){
		File file=new File(p.publicSourceDir);
		
		if(file.exists()){
			return (int)file.length();
		}else{
			return 0;
		}
	}
	public static List<PackageinstalledInfo> getUpdatelist(List<PackageinstalledInfo> list){
		List<PackageinstalledInfo> l=new ArrayList();
		for(PackageinstalledInfo info:list){
			if(invaildS(info.pname)){
				l.add(info);
			}
		}
		return l;
	}
	static boolean invaildS(String str){
		if(str!=null&&!str.equals("")){
			if(str.startsWith("com.android")||str.startsWith("com.sonyericsson")
					||str.startsWith("com.google")
					||str.startsWith("com.htc")
					||str.startsWith("com.sec")
					||str.startsWith("com.lge")
					||str.startsWith("com.motorola")){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	/**
	 * 获取当前程序安装列表信息
	 * @param context
	 * @param getSysPackages
	 * @param order 是否按照字母排序
	 * @return
	 */
	public static synchronized  Hashtable<String,PackageinstalledInfo> getInstalledApps(Context context, boolean getSysPackages,boolean isloading) {   
	
		if(isloading&&com.appdear.client.service.AppContext.getInstance().installlists.size()>0){
			return com.appdear.client.service.AppContext.getInstance().installlists;
		}else{
			Hashtable<String,PackageinstalledInfo> res =new Hashtable<String,PackageinstalledInfo>();
			List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
		//	Log.i("infosize1", packs.size()+"=packs");
	
			for(int i=0;i<packs.size();i++) {   
		    	PackageInfo p = packs.get(i);
		    	ApplicationInfo appInfo = p.applicationInfo;
		    	if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
		    		continue;
		    	}
//		    	try {
//					context.getPackageManager().getPackageInfo(p.packageName, PackageManager.GET_CONFIGURATIONS);
//				
//		    	} catch (NameNotFoundException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
		    	if (p.packageName.startsWith("com.appdear.serviceforpc")) {
		    		continue;
		    	}
				PackageinstalledInfo newInfo = new PackageinstalledInfo();   
				String sourceDir=appInfo.sourceDir;

		    	newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
		    	newInfo.pname = p.packageName;
		    	newInfo.versionName = p.versionName;   
		    	newInfo.versionCode = p.versionCode;
		    	newInfo.signatures = p.signatures;
		    	newInfo.formatsofttsize=ServiceUtils.returnSpace(getSoftSize(sourceDir,p.applicationInfo));
	//    	Log.i("file",sourceDir);
		    	if(sourceDir.toLowerCase().contains("/sdcard")||sourceDir.toLowerCase().startsWith("/mnt/")){
		    		newInfo.softsd=1;
		    	}
		    	try{
		    		newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager()); 
		    	}catch (OutOfMemoryError e) {
					// AsyLoadImageService.getInstance().getImageCache().clear();
		    		//System.gc();
					e.printStackTrace();
					Log.e("load image", "内存溢出啦");
				} 
		    	newInfo.firstC=ChineseConvert.ChineseToPing(newInfo.appname,true);
		    	newInfo.installlocation = ServiceUtils.getPackageInstallLocation(context, newInfo.pname);
		    	//newInfo.prettyPrint();
		    	Log.i("infosize1", newInfo.pname);
		    	res.put(newInfo.pname,newInfo); 
			}
		
			return res;    
		}
	} 
	/**
	 * 获取当前程序安装列表信息
	 * @param context
	 * @param getSysPackages
	 * @param order 是否按照字母排序
	 * @return
	 */
	public static Hashtable<String,PackageinstalledInfo> getInstalledAppsForServer(Context context, boolean getSysPackages) {   
			Hashtable<String,PackageinstalledInfo> res = com.appdear.client.service.AppContext.getInstance().installlists;           
			List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
			for(int i=0;i<packs.size();i++) {   
		    	PackageInfo p = packs.get(i);
		    	ApplicationInfo appInfo = p.applicationInfo;
		    	if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
		    		continue;
		    	}
		    	if (p.packageName.startsWith("com.appdear.serviceforpc")) {
		    		continue;
		    	}
				PackageinstalledInfo newInfo = new PackageinstalledInfo();   
		    	newInfo.pname = p.packageName;
		    	newInfo.versionName = p.versionName;   
		    	newInfo.versionCode = p.versionCode;	    	
		    	res.put(newInfo.pname,newInfo); 
			}
			return res;    
	} 
	public static Hashtable<String,PackageinstalledInfo> getInstalledAppsSysForServer(Context context, boolean getSysPackages) {   
			Hashtable<String,PackageinstalledInfo> res = com.appdear.client.service.AppContext.getInstance().installlistssys;           
			List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
			for(int i=0;i<packs.size();i++) {   
		    	PackageInfo p = packs.get(i);
		    	if(invaildS(p.packageName)==false){
		    		continue;
		    	}
		    	ApplicationInfo appInfo = p.applicationInfo;
		    	if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
		    		PackageinstalledInfo newInfo = new PackageinstalledInfo();   
			    	newInfo.pname = p.packageName;
			    	newInfo.versionCode = p.versionCode;
			    	res.put(newInfo.pname,newInfo); 
		    	}
		    	if (p.packageName.startsWith("com.appdear.client")||p.packageName.startsWith("com.appdear.serviceforpc")) {
		    		continue;
		    	}
			}
			return res;    
	} 
	/**
	 * 获取当前程序安装列表信息
	 * @param context
	 * @param getSysPackages
	 * @param order 是否按照字母排序
	 * @return
	 */
	public static synchronized  Hashtable<String,PackageinstalledInfo> getInstalledAppsSys(Context context, boolean getSysPackages) {   
		
		if(com.appdear.client.service.AppContext.getInstance().installlistssys.size()>0){
			return com.appdear.client.service.AppContext.getInstance().installlistssys;
		}else{
			Hashtable<String,PackageinstalledInfo> res = com.appdear.client.service.AppContext.getInstance().installlistssys;           
			List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
			for(int i=0;i<packs.size();i++) {   
		    	PackageInfo p = packs.get(i);
		    	if(invaildS(p.packageName)==false){
		    		continue;
		    	}
		    	ApplicationInfo appInfo = p.applicationInfo;
		    	if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
		    		PackageinstalledInfo newInfo = new PackageinstalledInfo();   
					String sourceDir=appInfo.sourceDir;
			  //  	newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
			    	newInfo.pname = p.packageName;
			    	newInfo.versionName = p.versionName;   
			    	newInfo.versionCode = p.versionCode;
			    	newInfo.signatures = p.signatures;
			    	//newInfo.formatsofttsize=ServiceUtils.returnSpace(getSoftSize(sourceDir,p.applicationInfo));
		//    	Log.i("file",sourceDir);
//			    	if(sourceDir.toLowerCase().contains("/sdcard")||sourceDir.toLowerCase().startsWith("/mnt/")){
//			    		newInfo.softsd=1;
//			    	}
//			    	try{
//			    		newInfo.icon = appInfo.loadIcon(context.getPackageManager()); 
//			    	}catch (OutOfMemoryError e) {
//						//AsyLoadImageService.getInstance().getImageCache().clear();
//			    		 //System.gc();
//						e.printStackTrace();
//						Log.e("load image", "内存溢出啦");
//					} 
//			    	newInfo.firstC=ChineseConvert.ChineseToPing(newInfo.appname,true);
//			    	newInfo.prettyPrint();
			    	newInfo.installlocation = ServiceUtils.getPackageInstallLocation(context, newInfo.pname);
			    	res.put(newInfo.pname,newInfo); 
		    	}
		    	if (p.packageName.startsWith("com.appdear.client")||p.packageName.startsWith("com.appdear.serviceforpc")) {
		    		continue;
		    	}
			}
		
			return res;    
		}
	} 
	/**
	 * 获得按照字母排序的列表
	 * @param context
	 * @param getSysPackages
	 * @param order 是否按照字母排序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static InstallList getInstalledAppsForOrder(Hashtable<String,PackageinstalledInfo> table, boolean order) {   
		InstallList res=null;
		Set<String> set=null;
		Collection<PackageinstalledInfo> list=table.values();
	
		if(list!=null&&list.size()>0){
			res = new InstallList();
			res.addAll(list);
			if(order==true){
				java.util.Collections.sort(res);
			}else{
				java.util.Collections.sort(res,new Comparator(){

					public int compare(final Object o1, final Object o2) {
						// TODO Auto-generated method stub
						return ((PackageinstalledInfo)o2).compareTo(o1);
					}
					
				});
			}
			set=new HashSet();
			PackageinstalledInfo pack=null,info=null;
			int temp=0;
			for(int i=0;i<res.size();i++){
				info=res.get(i);
			//	Log.i("info1",temp+"="+res.size()+"=");
				if(set.contains(info.firstC)==false){
					pack=new PackageinstalledInfo();
					pack.isCharProxy=true;
					pack.firstC=info.firstC;
					res.add(i,pack);
					set.add(info.firstC);
					i++;
				}
			}
		}
		return res;
	} 
	
	/**
	 * 通过appid得到已安装的缩略图
	 * @param context
	 * @param getSysPackages
	 * @return
	 */
	public static Drawable getInstallIcon(Context context,String appid) {    
//		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);   
//		if(appid==null)return null;
//		for(int i=0;i<packs.size();i++) {   
//	    	PackageInfo p = packs.get(i);
//	    	if(p.packageName.equals(appid)){
//	    		
//	    		return p.applicationInfo.loadIcon(context.getPackageManager());
//	    	}
//		}
		try {
			PackageInfo pack=context.getPackageManager().getPackageInfo(appid,0);
			return pack.applicationInfo.loadIcon(context.getPackageManager());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		}
	} 
	
	/**
	 * 获得软件的权限，
	 * @param context
	 * @param archiveFilePath  
	 */
	public static void getUninatllApkInfo(Context context, String archiveFilePath){   
		PackageManager  pm= context.getPackageManager();   
        PackageInfo info;   
            info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES); 
			String result = null;     
			String[] packagePermissions = info.requestedPermissions;  
			PermissionInfo[] permissions = info.permissions;
			for(int i=0;i<permissions.length;i++){
				if (Constants.DEBUG)
					Log.i("permissions :", permissions[i].name+" : "+permissions[i].describeContents());
			}
			if (Constants.DEBUG)
				Log.i("name", info.packageName);     
			if(packagePermissions != null) {     
			    for (int j = 0; j < packagePermissions.length; j++) {     
			    	if (Constants.DEBUG)
			    		 Log.i("requestedPermissions",packagePermissions[j]);     
			    }     
			}else {     
				if (Constants.DEBUG)
					 Log.i("name", info.packageName + ": no permissions");     
			} 
	}
	
	/**
	 * 判断下载任务是否已存在
	 * @param bean
	 */
	public synchronized static SiteInfoBean checkTaskState(int softid) {
		
		if (AppContext.getInstance().taskList != null)
			return (SiteInfoBean)AppContext.getInstance().taskList.get(softid);
		return null;
	}
	
	/**
	 * 获取当前窗口信息Metrics
	 * @param context
	 * @param getSysPackages
	 * @return  jdan 2011-06-29
	 */
	public static DisplayMetrics getMetrics(WindowManager  manager) {   
		DisplayMetrics metrics = new DisplayMetrics(); 
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
	/**
	 * 处理提示信息类提示框（创建两个按钮）
	 * @param context
	 * @param title
	 * @param message
	 * @param handler 处理方法
	 * jdan 2011-06-29
	 */
	public static void getAlertDialogForString(Context context,String title,String message,final MessageHandler handler){
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("确定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.messageHandlerOk();
			}
		})     
		.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.messageHandlerCannel();
			}
		}).show();
	}
	
	/**
	 * 处理提示信息类提示框（根据flag来创建不同的按钮）
	 * @param context
	 * @param title
	 * @param message
	 * @param handler 处理方法
	 * @param flag 为1表示只创建“确定”按钮，为-1表示只创建“取消“按钮,
	 *             为2表示“确定”在左，“取消”在右，为-2表示“取消”在左，“确定”在右
	 *             为其他数值时和为flag=2一样（即默认的左”确认“右”取消“）
	 */
	public static void getAlertDialogForString(Context context,String title,String message,final MessageHandler handler, int flag){
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message);
		
		if (flag == 1) {
			builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.messageHandlerOk();
				}
			}).show(); 
		}else if(flag == -1){
			builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.messageHandlerCannel();
				}
			}).show();
		}else if(flag == -2){
			new AlertDialog.Builder(context).setTitle(title).setMessage(message)
			.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.messageHandlerCannel();
				}
			})     
			.setNegativeButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.messageHandlerOk();
				}
			}).show();
		}else{
			new AlertDialog.Builder(context).setTitle(title).setMessage(message)
			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.messageHandlerOk();
				}
			})     
			.setNegativeButton("取消",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.messageHandlerCannel();
				}
			}).show();
		}
	}
	
	/**
	 * 
	 * @param context 检查用户是否已登录
	 * @param flag	  true显示提示框，false不显示提示框
	 * @return jdan 2011-06-29
	 */
	public static boolean checkLogin(final Context context,boolean flag,int... softid){
		if(SharedPreferencesControl.getInstance().getString("username", Common.USERLOGIN_XML,context).equals("")){
			if (flag == true) {
				/*
				 * ServiceUtils.getAlertDialogForString(context,"用户提示","您未登录!确认要登录吗"
				 * ,new MessageHandler(){
				 * 
				 * @Override public void messageHandlerCannel() { // TODO
				 * Auto-generated method stub
				 * 
				 * }
				 * 
				 * @Override public void messageHandlerOk() { // TODO
				 * Auto-generated method stub Intent intent = new Intent();
				 * intent.setClass(context,AlterUserRegActivity.class);
				 * 
				 * context.startActivity(intent);
				 * 
				 * } });
				 */
				// 2.0修改，收藏时不再提示是否登录，直接显示登录对话框
				Intent intent = new Intent();
				if(softid!=null&&softid.length==1){
					intent.putExtra("favorite", softid[0]);
				}
				intent.setClass(context, AlterUserRegActivity.class);
				context.startActivity(intent);
			}
			return false;
		 }else{
			 return true;
		 }
		 
	}
	/**
	 * 
	 * @param context 检查用户是否已登录163微博
	 */
	public static boolean check163Login(final Context context,String content){
		if(MyApplication.getInstance().username163!=null&&!"".equals(MyApplication.getInstance().username163)&&MyApplication.getInstance().password163!=null&&!"".equals(MyApplication.getInstance().password163)){
			    
				return true;
		}else
		{
			Intent intent = new Intent();
			intent.setClass(context, AlterUserShare163Activity.class);
			intent.putExtra("content", content);
			context.startActivity(intent);
			return false;
		}
		 
	}
	/**
	 * 读取保存的已下载文件大小
	 * @throws ApiException 
	 */
	public static int read_size_download(String filepath) throws ApiException {
		try {
			File sizeFile = new File(filepath + ".size");
			DataInputStream input = new DataInputStream(new FileInputStream(sizeFile));
			int nCount = input.readInt();
			input.close();
			return nCount;
		} catch (IOException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 读取文件长度
	 * @param filepath
	 * @return
	 * @throws ApiException
	 */
	public static int read_size_file(String filepath) {
		File sizeFile = new File(filepath);
		int size = (int) sizeFile.length();
		return size;
	}
	
	/**
	 * 安装文件是否可用
	 * @param filename
	 * @param fileSize
	 * @return
	 * @throws ApiException 
	 */
	public static boolean isWhileFile(String filename) {
		File file=ServiceUtils.getSDCARDImg(Constants.APK_DATA);
		if(file!=null&&file.isDirectory()){
			String filepath = file.getPath() + "/" + filename;
			try {
				if (filepath != null && !filepath.equals("")) {
					if (filepath.lastIndexOf("?") > 0)
						filepath = filepath.substring(0, filepath.lastIndexOf("?"));
 				}
				if (read_size_download(filepath) == read_size_file(filepath)){
					
					return true;
				} else {
					return false;
				}
			} catch (ApiException e) {
				return false;
			}
		}else{
			return false;
		}
		
	}
	
//	/**
//	 * jdan 2011-07-05
//	 * 
//	 * @param context
//	 * @param softlistInfo
//	 */
//	public static void showPayDialog(Context context,SoftlistInfo softlistInfo){
//		final AlertDialog dlg = new AlertDialog.Builder(context).create();
//		dlg.show();
//		//没加空构造方法暂不用于view类的生成
//	//	PayWaySelectView view=(PayWaySelectView) RequestResultFactory.getInstance().getObjectSingle(PayWaySelectView.class);
//		Handler handler =new Handler(){
//			@Override
//			public void handleMessage(Message msg) {
//				switch(msg.what){
//					case Common.OFFVIEW:
//						dlg.dismiss();	
//						break;
//					default:
//				}
//			}
//			
//		};
//		dlg.getWindow().setContentView(new PayWaySelectView(context,softlistInfo,handler));
//	}
	/**
	 * jdan 
	 * 
	 * @param context
	 * @param softlistInfo
	 */
	public static int showAccount(Context context,String userid,String sessionid){
		return 0;
	}
	/**
	 * jdan 2011-07-05
	 * 
	 * @param context
	 * @param softlistInfo
	 */
	public static void showPayCommitDialog(Context context,SoftlistInfo softlistInfo){
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		//没加空构造方法暂不用于view类的生成
		//	PayWaySelectView view=(PayWaySelectView) RequestResultFactory.getInstance().getObjectSingle(PayWaySelectView.class);
		Handler handler =new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
					case Common.OFFVIEW:
						dlg.dismiss();	
						break;
					default:
				}
			}
			
		};
		//dlg.getWindow().setContentView(new PayWayCommitView(context,softlistInfo,handler));
	}
	
	/**
	 * 处理列表选择框的方法
	 * @param context
	 * @param title
	 */
	public static void getDialogForSelect(Context context,String title,int itemlayout,final MessageHandler handler){
		new AlertDialog.Builder(context)
        .setTitle(title)
        .setItems(itemlayout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	handler.messageHandlerSelect(dialog,which);
            }
        }).setNegativeButton("取消", null)
        .create().show();
	}
	
	public static WeiBoUser getUserWeiboInfo(Context context,WeiBoUserType type){
		WeiBoUser u = new WeiBoUser();
		SharedPreferences sh = null;
		switch(type){
		case sinaUser:
			 sh = context.getSharedPreferences(SINA_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		case tencentUser:
			 sh = context.getSharedPreferences(TECENT_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		case netEasyUser:
			 sh = context.getSharedPreferences(NETEASY_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		case souhuUser:
			 sh = context.getSharedPreferences(SOUHU_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		}
		
		u.setAccessToken(sh.getString("accessToken", ""));
		u.setAccessSecret(sh.getString("accessSecret", ""));
		u.setUid(sh.getLong("uid",0l));
		
		return u;
	}
	
	public  static void saveWeiboUser(WeiBoUser u,Context context,WeiBoUserType type){
		SharedPreferences sh = null;
		switch(type){
		case sinaUser:
			 sh = context.getSharedPreferences(SINA_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		case tencentUser:
			 sh = context.getSharedPreferences(TECENT_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		case netEasyUser:
			 sh = context.getSharedPreferences(NETEASY_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		case souhuUser:
			 sh = context.getSharedPreferences(SOUHU_WEIBOACCOUNT, Context.MODE_PRIVATE);
			 break;
		}
		Editor editor = sh.edit();
		editor.putLong("uid", u.getUid());
		editor.putString("accessToken", u.getAccessToken());
		editor.putString("accessSecret", u.getAccessSecret());
		editor.commit();
	}

	
	/**
	 * 删除文件
	 * @param path
	 */
	public static void deleteSDFile(String path){
		File file = new File(path);
		if(file != null && file.exists()) {
			file.delete();
		}
	}
	
	/**
	 * 否有这个文件
	 * @param path
	 * @return
	 */
	public static boolean isHasFile(String path){
		boolean ishas = false;
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			ishas = true;
		}
		return ishas;
	}
	
//	/**
//	 * 
//	 * @param context
//	 * @param resource 本机资源id
//	 * @param flag     资源状态true(该资源是本机资源),false（该资源是网络资源,resource为背景图片id）
//	 */
//	public static void sendBroadcast(Context context,int resource,boolean flag){
//		int bgid=SharedPreferencesControl.getInstance().getInt(
//				Common.CURRENTBG, null,
//				context);
//		if(bgid!=resource&&Common.BGDRAWABLE!=null){
//			BitmapDrawable drawable=(BitmapDrawable)Common.BGDRAWABLE;
//			Common.bgbitmap=drawable.getBitmap();
//			getDrawableForInternet(context,resource);
//		}
//		Intent i = new Intent(Common.BACKGROUNDBROADCAST); 
//		i.putExtra(Common.CURRENTBG,resource);
//		i.putExtra(Common.BACKGROUNDBFLAG,flag);
//		context.sendBroadcast(i);  
//	}
//	public static Drawable getDrawableForInternet(Context context,int bgid) {
//		Bitmap bmp = null;
//		// if(Common.BGDRAWABLE.size()<=0){
//		if (Environment.getExternalStorageDirectory() == null
//				|| Environment.getExternalStorageDirectory().equals("")) {
//			return Common.BGDRAWABLE=new BitmapDrawable(readBitMap(context, R.drawable.activity_bg));
//		}
//		File updateDir = new File(Environment.getExternalStorageDirectory(),
//				Global.downloadDir);
//		if (!updateDir.exists()) {
//			updateDir.mkdirs();
//		}
//
//			File file = new File(updateDir + "/" + bgid + ".png");
//			try{
//			if (file.exists()) {
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 2;
//				bmp=BitmapFactory.decodeFile(file.getAbsolutePath());
//				WeakReference<Bitmap> weakRerference = new WeakReference<Bitmap>(bmp);   
//				Common.BGDRAWABLE=new BitmapDrawable(weakRerference.get());
//			} else {
//				return Common.BGDRAWABLE=new BitmapDrawable(readBitMap(context, R.drawable.activity_bg));
//			}
//			}
//			catch (OutOfMemoryError e) {
//				AsyLoadImageService.getInstance().getImageCache().clear();
//				Log.e("load image", "memory");
//			} 
//		// }
//		return Common.BGDRAWABLE;
//	}
	 public static Bitmap readBitMap(Context context, int resId){  
		  BitmapFactory.Options opt = new BitmapFactory.Options();  
		    opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		    opt.inPurgeable = true;  
		   opt.inInputShareable = true;  
	   //获取资源图片  
		  InputStream is = context.getResources().openRawResource(resId);  
		  WeakReference<Bitmap> weakRerference =new WeakReference<Bitmap>(BitmapFactory.decodeStream(is,null,opt)); 
		  if(is!=null){
			  try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	    return weakRerference.get();  
	 }
	/**
	 * jdan 2011-08-05
	 * 下载文件
	 * @param url
	 * @param updatefile
	 * @return
	 */
	public static boolean downloadFileForUpdate(String url,File updatefile) throws ApiException{
		HttpGet httpGet = new HttpGet(url);
		long totalsize=0;
		int updateTotalSize = 0; 
		InputStream is = null;
		FileOutputStream fos =null;
	  try
	  {
	   HttpResponse httpResponse = new DefaultHttpClient()
	     .execute(httpGet);
	   if (httpResponse.getStatusLine().getStatusCode() == 200)
	   {
		int count=0;
	    is = httpResponse.getEntity().getContent();
	    totalsize=httpResponse.getEntity().getContentLength();
	    fos = new FileOutputStream(updatefile);
	    byte[] buffer = new byte[8192];
	    while ((count = is.read(buffer)) != -1)
	    {
	     fos.write(buffer, 0, count);
	     updateTotalSize+=count;
	    }
	   }
	  }
	  catch (Exception e)
	  {		
		 throw new ApiException(ExceptionEnum.ApiExceptionCode.API_SERVER_ERR.getValue());
	  }finally{
		  if(fos!=null){
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  if(is!=null){
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }
	  return updateTotalSize==totalsize;
	}
	
	 public static String toMd5(byte[] bytes) {
		  if(bytes==null)return "";
	      try {
	              MessageDigest algorithm = MessageDigest.getInstance("MD5");
	              algorithm.reset();
	              algorithm.update(bytes);
	              return toHexString(algorithm.digest(), "");
	      } catch (NoSuchAlgorithmException e) {
	    	  throw new RuntimeException(e);
	      }

	  }



	private static String toHexString(byte[] bytes, String separator) {
			StringBuilder hexString = new StringBuilder();
			for (byte b : bytes) {
	              hexString.append(Integer.toHexString(0xFF & b)).append(separator);
			}
			return hexString.toString();
	}
	
	/**
	 * 读取文本文件
	 * @param in
	 * @param filename
	 * @return
	 */
	public static String getXIEYI(InputStream in){
		StringBuffer sb=new StringBuffer("");
		String temp=null;
		if(in!=null){
			BufferedReader file;
			try {
				file = new BufferedReader(new InputStreamReader(in,"utf-8"));
				while((temp=file.readLine())!=null){
					sb.append(temp).append("\n");
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
//	判断wifi  是否开启，在真机可判断
	
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 2011 09 19 zxy 添加获得手机宽高的函数
	 * @param context 
	 * @return 数组的第一位 宽，第二位高
	 */
	public static  int [] getCurrentPhonePixInfo(Context context){
		if(info==null){
			info=new int[2];
			 Activity ac = (Activity)context;
			 info [0] = ac.getWindowManager().getDefaultDisplay().getWidth();
			 info [1] = ac.getWindowManager().getDefaultDisplay().getHeight();
		}
		 return  info;
	}
	
	/**
	 * 列表加缓存
	 * @param key 关键字
	 * @param source 列表对象
	 * @param context
	 * @param totalcount 列表对象总数
	 * @return
	 */
	public static boolean addListview(String key,Object source,Context context,int totalcount){
		
		if(ListviewSourceCache.getInstance().addInitModel(key,source)==true){
			SharedPreferencesControl.getInstance().putInt(key,totalcount,com.appdear.client.commctrls.Common.LISTVIEWSOURCE_XML,context);
			return true;
		}else{
			return false;
		}
	}
	
	public static Object getListview(String key,Context context){
		if(SharedPreferencesControl.getInstance().getInt(key,com.appdear.client.commctrls.Common.LISTVIEWSOURCE_XML,context)==0){
			return null;
		}else{
			 Object obj= ListviewSourceCache.getInstance().getInitModel(key);
			 if(obj!=null&&obj instanceof HomeObj){
				 HomeObj o=(HomeObj)obj;
				 if(o.getTimestamp()<new java.util.Date().getTime()){
					 return null;
				 }else{
					 return o;
				 }
			 }else{
				 return obj;
			 }
		}
	}
//	/**
//	 * 列表加缓存
//	 * @param key 关键字
//	 * @param source 列表对象
//	 * @param context
//	 * @param totalcount 列表对象总数
//	 * @return
//	 */
//	public static boolean addListview1(String key,Object source,Context context,int totalcount){
//		
//		if(ListviewSourceCache.getInstance().addListview(key,source)==true){
//			SharedPreferencesControl.getInstance().putInt(key,totalcount,com.appdear.client.commctrls.Common.LISTVIEWSOURCE_XML,context);
//			return true;
//		}else{
//			return false;
//		}
//	}
//	
//	public static Object getListview1(String key,Context context){
//		if(SharedPreferencesControl.getInstance().getInt(key,com.appdear.client.commctrls.Common.LISTVIEWSOURCE_XML,context)==0){
//			return null;
//		}else{
//			 return ListviewSourceCache.getInstance().getListview(key);
//		}
//	}
	 public static String getSimsi(Context context){
		  TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
		  if(tm.getSimState()!=TelephonyManager.SIM_STATE_ABSENT){
			  String subscriberid= tm.getSubscriberId();
			  /**
			   * imsi码固定15位如果不为15位就从服务器取
			   */
			  if(subscriberid!=null&&subscriberid.length()==15){
				  return subscriberid;
			  }else{
				  return "";
			  }
		  }else{
			  return null;
		  }
	  }
	//添加短信信息
		public static boolean  SmsSend(Context context,String imsi,String message){
			if(imsi==null)return false;
			message+=getIMEI(context)+","+imsi;
			SmsManager sms = SmsManager.getDefault();

			// 如果短信没有超过限制长度，则返回一个长度的List。
			List<String> texts = sms.divideMessage(message.toString());
			for (String text : texts) {
				String SENT = "SMS_SENT"; 
				String DELIVERED = "SMS_DELIVERED"; 
				//当消息发出时，成功或者失败的信息报告通过PendingIntent来广播
				PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0); 
				//发送到收件人的广播
				PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,new Intent(DELIVERED), 0); 
				sms.sendTextMessage(AppContext.smsService, null,message.toString(), sentPI, deliveredPI); 
			}
			return true;
			
	}
		
	/**
	 * 读取文本文件
	 * @param in
	 * @param filename
	 * @return
	 */
	public static String getCode(InputStream in){
		String temp="";
		if(in!=null){
			BufferedReader file;
			try {
				file = new BufferedReader(new InputStreamReader(in));
				while((temp=file.readLine())!=null){
					return temp;
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public static String getImeiTimeUrl(String str,String... flag){
		StringBuffer sb=new StringBuffer();
		String f="";
		if(flag!=null&&flag.length==1){
			f=flag[0];
		}
		if(str.contains("imei=")){
			if(f.equals("detail")&&!str.contains("sdetail=")){
				str=str+"&sdetail=1";
			}
			if(f.equals("update")&&!str.contains("supdate=")){
				str=str+"&supdate=1";
			}
			return str;
		}else{
			if(str.indexOf("?")==-1){
				str=str+sb.append("?imei=").append(AppContext.getInstance().IMEI).append("&time=").append(String.valueOf(new java.util.Date().getTime())).toString();
			}else{
				str=str+sb.append("&imei=").append(AppContext.getInstance().IMEI).append("&time=").append(String.valueOf(new java.util.Date().getTime())).toString();
			}
			if(f.equals("detail")&&!str.contains("sdetail=")){
				str=str+"&sdetail=1";
			}
			if(f.equals("update")&&!str.contains("supdate=")){
				str=str+"&supdate=1";
			}
		}
		return str;
	}
	public static String getUrl(String pattern,String repvalue,String url){
		StringBuffer sb=new StringBuffer();
		 boolean s=url.contains(pattern);
		 String str0=null,str1=null,str2=null,str3=null;
		 if(s==true){
			 str0=url.substring(0, url.indexOf(pattern));
			 str1=url.substring(url.indexOf(pattern));
			 if(str1.contains("&")){
				 str2=pattern+repvalue;
				 str3=str1.substring(str1.indexOf("&"), str1.length());
			 }else{
				 str2=pattern+repvalue;
				 str3="";
			 }
			 
			 sb.append(str0).append(str2).append(str3);
			return sb.toString();
		 }else{
			 return url;
		 }
	}
	
	public static String encryptByMD5(String str) throws Exception{
		if(str==null){
			str=""; 
		}
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bts = md.digest(str.getBytes());
		int i;
		StringBuffer buf = new StringBuffer("");
		 for (int offset = 0; offset < bts.length; offset++) {
		    i = bts[offset];
		    if (i < 0)
		     i += 256;
		    if (i < 16)
		     buf.append("0");
		    buf.append(Integer.toHexString(i));
		 }
		//采用Base64算法，将加密后的字节变成字符串
		return buf.toString();
	}
	
	public static String getApkname(String url) {
		if (null == url || url.equals("")) {
			return "";
		}
		Log.i("", "downloadurl=getApkname=" + url);
		String filename = url;
		if (filename.indexOf("?") > 0) {
			filename = filename.substring(0, filename.indexOf("?"));
		}
		Log.i("", "downloadurl=getApkname=quwenhao=" + filename);
		try {
			return ServiceUtils.encryptByMD5(filename) + ".apk";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	

	// 查询需哦有
	public static  void getAllPackages(Context context) {

		String str = SharedPreferencesControl.getInstance().getString(
				"allelidepackages", null, context);
		
		if (str != null && !"".equals(str) && str.contains("##")) {
			String strs[] = str.split("##");
			Log.i("info091", strs.length+"=");
			for (String packageone : strs) {
				
				if (packageone != null && !"".equals(packageone)) {
					AppContext.getInstance().elideupdatelistpackages
							.add(packageone);
				}
			}
		}
	}

	// 添加一个
	public static boolean putOneElidePackages(Context context, String packageone) {

		String allpackages = SharedPreferencesControl.getInstance().getString(
				"allelidepackages", null, context);

		return SharedPreferencesControl.getInstance().putString(
				"allelidepackages", allpackages + "##" + packageone, null,
				context);
	}

	// 删除一个
	public static boolean removeOneElidePackages(Context context,
			String packageone) {

		String allpackages = SharedPreferencesControl.getInstance().getString(
				"allelidepackages", null, context);
		if (allpackages.contains("##" + packageone)) {
			
			allpackages = allpackages.replace("##" + packageone, "");
			
			return SharedPreferencesControl.getInstance().putString(
					"allelidepackages", allpackages, null, context);
		} else {
			return true;
		}
	}
	 /*public static String getSysVersion(Context context){
		  String release=android.os.Build.VERSION.RELEASE;
		  final int apiLevel = Build.VERSION.SDK_INT;
		  //System.out.println("release==="+release+"   apiLevel="+apiLevel);
		  
		return release==null?"":release;
	  }*/
	
	/**
	 * 安装应用列表广播接收处理
	 * @param intent
	 * @param context
	 * @param isupload
	 */
	public static void handlerAddApp(Intent intent,Context context){
		if(intent!=null&&intent.getAction()!=null){
		
			String packageName = intent.getDataString().substring(8);   
			PackageManager manager=context.getPackageManager();
			PackageInfo p=null;
			try {
				p = manager.getPackageInfo(packageName,PackageManager.GET_PROVIDERS);
			} catch (NameNotFoundException e1) {
				return;
			}
			PackageinstalledInfo newInfo = new PackageinstalledInfo();   
			String sourceDir=p.applicationInfo.sourceDir;
	    	newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
	    	newInfo.pname = p.packageName;
	    	newInfo.versionName = p.versionName;   
	    	newInfo.versionCode = p.versionCode;
	    	newInfo.formatsofttsize=ServiceUtils.returnSpace(getSoftSize(sourceDir,p.applicationInfo));
	    	try{
	    		newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager()); 
	    		if(AppContext.getInstance().updatelist.contains(newInfo)){
	    			AppContext.getInstance().updatelist.remove(newInfo);
	    		}
	    		int softid=0;
	    		if((softid=intent.getIntExtra("installappdear",0))!=0){
	    			MyApplication.getInstance().getSoftMap().put(softid, Math.abs(packageName.hashCode()));
	    		}
	    	}catch (OutOfMemoryError e) {
			    AsyLoadImageService.getInstance().getImageCache().clear();
	    		System.gc();
				e.printStackTrace();
				Log.e("load image", "内存溢出啦");
			} 
	    	newInfo.firstC=ChineseConvert.ChineseToPing(newInfo.appname,true);
	    	newInfo.prettyPrint();
	    	AppContext.getInstance().installlists.put(packageName, newInfo);
		}
		
	}
	public static String getProcessMsg(){
		Random rand=new Random(new java.util.Date().getTime());
		int i=rand.nextInt(Constants.PROGRESS_MSG.length);
		return Constants.PROGRESS_MSG[i];
	}
	
	public static void setSelectedValues(Context context,List<SoftlistInfo> listData,int position,String... intentcentent){
		if (!ServiceUtils.checkNetState(context)) {
			Toast.makeText(context,"网络错误，请检查网络状态",Toast.LENGTH_SHORT).show();
			return;
		}
		if(listData==null)return;
		SoftlistInfo info = listData.get(position);
		Intent intent = new Intent(context, 
				SoftwareMainDetilActivity.class);
		//清除进程activity
		intent.putExtra("softid", info.softid);
		intent.putExtra("softicon",  info.softicon);
		intent.putExtra("downloadurl",info.downloadurl);
		intent.putExtra("downloadcount",info.download);
		if(intentcentent!=null&&intentcentent.length>=1){
			if(intentcentent[0].equals("ad")){
				intent.putExtra("ad",false);
			}else if(intentcentent[0].contains("subid-")){
				intent.putExtra("cataid",intentcentent[0]);
			}else if(intentcentent[0].contains("labelid-")){
				intent.putExtra("cataid",intentcentent[0]);
			}else if(intentcentent[0].contains("cataid-")){
				intent.putExtra("cataid",intentcentent[0]);
			}
		}
		//清除进程activity
		context.startActivity(intent);
	}
	public static void setSelectedValuesDownload(Context context,int softid,String softicon,String downloadurl ){
		if (!ServiceUtils.checkNetState(context)) {
			Toast.makeText(context,"网络错误，请检查网络状态",Toast.LENGTH_SHORT).show();
			return;
		}
		 
		Intent intent = new Intent(context, 
				SoftwareMainDetilActivity.class);
		//清除进程activity
		intent.putExtra("softid",  softid);
		intent.putExtra("softicon",   softicon);
		intent.putExtra("downloadurl", downloadurl);
		//清除进程activity
		context.startActivity(intent);
	}
	
	public static boolean isInstall(Context context,File apk,String appid){
			PackageinstalledInfo infoinstalled = AppContext.getInstance().installlists.get(appid);
			if (infoinstalled == null){
				infoinstalled=AppContext.getInstance().installlistssys.get(appid);
				if(infoinstalled==null){
					return true;
				}
			}
		PackageInfo info=null;
		try {
			info=context.getPackageManager().getPackageInfo(appid,PackageManager.GET_SIGNATURES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Signature[] signatures = info.signatures;
		Log.i("info909", signatures+"="+ apk+"="+apk.exists());
		if(apk!=null&&apk.exists()){
			info=PackageParserT.getPackageInfo(context,apk.getPath(),PackageManager.GET_SIGNATURES);
		}
		
		if(info!=null){
			return PackageParserT.IsSignaturesSame(signatures, info.signatures);
		}
		return true;
	}
	
	public static void Install(final Context context,String path,final String appid,int softid,Handler... handler){
	   File file = new File(path);
	   if(ServiceUtils.isInstall(context,file,appid)){
			MyApplication.getInstance().getAppMap()
					.put(appid, softid);
			Intent installInent = new Intent(Intent.ACTION_VIEW);
			installInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installInent.setDataAndType(
					Uri.parse("file://" + path),
					"application/vnd.android.package-archive");
			context.startActivity(installInent);
	   }else{
		   /**
		    * 弹出 卸载框
		    */
		   if(handler!=null && handler.length != 0){
//			   handler[0].post(new Runnable(){
//
//				@Override
//				public void run() {
//					Activity startcontext = ((Activity)context).getParent();
//					if (startcontext == null)
//						startcontext = (Activity) context;
//					final Activity start = startcontext;
//					new AlertDialog.Builder(start)
//					.setMessage("文件签名不一致,是否卸载原安装软件？")
//					.setPositiveButton("卸载", 
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									Uri packageURI = Uri.fromParts("package", appid, null);
//									Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
//									start.startActivity(intent);
//								}
//					}).setNegativeButton("取消", 
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									
//								}
//					}).show();
//				}
//				   
//			   });
			   dojumpqian(context,"该软件",appid);
		   }
		}
	}
	public static void dojumpqian(Context context,String softname,String appid){
		Intent intent=new Intent(context,QianmingDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("softname",softname);
		intent.putExtra("appid", appid);
		context.startActivity(intent);
	}
	 public static int dip2px(float dipValue,Activity context){ 
		 float ds = ServiceUtils.getMetrics(context.getWindowManager()).density;

		 return (int)(dipValue * ds + 0.5f); 
	} 
}

 