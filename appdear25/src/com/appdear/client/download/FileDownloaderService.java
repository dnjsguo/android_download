package com.appdear.client.download;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.QianmingDialog;
import com.appdear.client.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * 下载service
 * @author zqm
 *
 */
public class FileDownloaderService extends Service implements DownloadListener {

	/**
	 * 下载通知消息管理
	 */
	public static NotificationManager notificationManager;
	
	/**
	 * 下载中的数量
	 */
	public static int task_num = -1;
	
	/**
	 * 正在下载中通知ID
	 */
	public static final int NOTIFICATION_DOWNLOADING_ID = -1000;
	
	/**
	 * 下载中的通知
	 */
	public static Notification notification;
	
	/**
	 * 下载完成的通知
	 */
	public static Notification finishNotification;
	
	/**
	 * 下载失败的通知
	 */
	public static Notification errNotification;
	
	/**
	 * 下载完成的通知内容
	 */
	private PendingIntent finishIntent;
	
	/**
	 * 下载中的通知内容
	 */
	public static PendingIntent contentIntent;
	
	/**
	 * 下载失败的通知内容
	 */
	public static PendingIntent errIntent;
	
	/**
	 * 点击通知弹出管理下载界面的intent
	 */
	public Intent managerIntent;
	
	/**
	 * 下载列表的线程池（每次执行一个下载任务）
	 */
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	
	/**
	 * 正在执行删除任务
	 */
	public static boolean delete_all = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onStart(final Intent intent, final int startId) {
		if (intent != null) {
			//获取下载任务信息
			final SiteInfoBean bean = (SiteInfoBean) intent.getSerializableExtra("download_bean");
			//判断是否是通过升级界面点的下载任务（需要从系统获取应用程序图标）
			if (intent.getStringExtra("Dicon").equals("true"))
				bean.dicon = ServiceUtils.getInstallIcon(this, bean.appID);
			
			//设置通知消息的监听
			bean.notification = this;
			
			//重置下载任务参数，保存下载任务信息
			//-1为等待下载
			bean.state = -1;
			//设置下载界面的监听
			if (AppContext.getInstance().downloadlistener != null)
				bean.listener = AppContext.getInstance().downloadlistener;
			//获取程序sd的保存路径
			if (ServiceUtils.getSDCARDImg(Constants.APK_DATA) != null)
				ServiceUtils.getSDCARDImg(Constants.APK_DATA).getPath();
			//添加下载任务到下载列表(下载列表根据状态决定是否开始下载)
			SiteFileFetch siteFileFetch = new SiteFileFetch(bean);
			
			if(siteFileFetch.fileEquesStop)
			{
				return;
			}
			bean.siteFileFecth = siteFileFetch;
			//添加到线程池
			executor.execute(siteFileFetch);
			
			//保存DB
			AppContext.getInstance().downloader.downDb.save(bean);
			//添加到任务队列
			AppContext.getInstance().taskList.put(bean.softID,bean);
			
			if(!AppContext.getInstance().taskSoftList.contains(String.valueOf(bean.softID))){
				AppContext.getInstance().taskSoftList.add(String.valueOf(bean.softID));
			}
			//添加通知消息
			addNotification(bean);
			showNotification(NOTIFICATION_DOWNLOADING_ID, true, notification);
		}
		super.onStart(intent, startId);
	}
	
	private void addNotification(SiteInfoBean bean) {
		if (notificationManager == null)
			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (notification == null) {
			//初始化通知消息,设置通知点击后显示下载管理界面
			notification = new Notification(R.drawable.app_download_task, 
					"正在下载", 
					System.currentTimeMillis());
			managerIntent = new Intent();
			managerIntent.putExtra("notificaiton", "true");
			managerIntent.setClass(FileDownloaderService.this, MoreManagerDownloadActivity.class);
			managerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			
			contentIntent = PendingIntent.getActivity(this, 0, managerIntent, 0);
		}
		
		//设置通知栏显示内容  
		if (task_num == -1) {
			task_num = 0;
			for (SiteInfoBean numbean : AppContext.getInstance().taskList.values()) {
				if (numbean.state != 2) {
					//再次变更当前任务时不需要再次更改通知数量
					numbean.isShowNotification = false;
					task_num ++;
				}
			}
		} else {
			//判断当前任务需要再次更新通知数量
			if (bean.isShowNotification) {
				bean.isShowNotification = false;
				task_num ++;
			}
		}
		notification.setLatestEventInfo(this, "爱皮应用下载", task_num + "个软件正在下载中， 请点击查看", contentIntent);
	}
	
	public static void showNotification(int id, boolean isclear, Notification notification) {
		if (id == NOTIFICATION_DOWNLOADING_ID) {
			if (task_num <= 0) {
				notificationManager.cancel(id);
				return;
			}
		}
		if (isclear) {
			//点击后清除通知信息
			if(notification !=null){
				notification.flags |= Notification.FLAG_AUTO_CANCEL;  
			}
		}
		notificationManager.notify(id, notification);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				final SiteInfoBean bean = (SiteInfoBean)msg.getData().getSerializable("softinfo");
				//发通知刷新界面状态
				Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
				intent.putExtra("softid", bean.softID);
				intent.putExtra("downloadfinsh",1);
				sendBroadcast(intent);
				
				//是否是自动安装
				boolean autoinstall = SharedPreferencesControl.getInstance().getBoolean(
						"autoinstall",com.appdear.client.commctrls.Common.SETTINGS, FileDownloaderService.this);
				notification.setLatestEventInfo(FileDownloaderService.this, "爱皮应用下载", task_num + "个软件正在下载中， 请点击查看", contentIntent);
				showNotification(NOTIFICATION_DOWNLOADING_ID, true, notification);
				String filepath = bean.sFilePath + "/"
				+ bean.sFileName;
//				String filepath = "/sdcard/TestScreen.apk";
				File file = new File(filepath);
				if (autoinstall) {
					//弹出安装对话框
				 if(ServiceUtils.isInstall(FileDownloaderService.this,file,bean.appID)){
//					if(ServiceUtils.isInstall(FileDownloaderService.this,file,"com.jineefo")){
					ServiceUtils.Install(FileDownloaderService.this, bean.sFilePath+"/"+bean.sFileName, bean.appID, bean.softID);
					new Thread(new Runnable() {				
						@Override
						public void run() {
							//上传下载完成日志
							ApiManager.downloadcomplete(bean.sSiteURL, "2");
						}
					}).start();
				 }else{
					 dojumpqian(bean.softName,bean.appID);
				 }
				} else {
					
					   if(ServiceUtils.isInstall(FileDownloaderService.this,file,bean.appID)){
						   finishIntent = PendingIntent.getActivity(FileDownloaderService.this, 0, 
									new Intent(Intent.ACTION_VIEW)
									.setDataAndType(Uri.parse("file://"+ filepath),
									"application/vnd.android.package-archive"), 0);
					   }else{
						   /**
						    * 弹出 卸载框
						    */
						   dojumpqian(bean.softName,bean.appID);
					   }
					
					
					finishNotification = new Notification(R.drawable.app_download_task, 
							"下载完成", 
							System.currentTimeMillis());
					finishNotification.setLatestEventInfo(FileDownloaderService.this, bean.softName, "下载完成，点击安装", finishIntent);
					showNotification(bean.softID, true, finishNotification);
				}
				break;
			case 2:
				notification.setLatestEventInfo(FileDownloaderService.this, "爱皮应用下载", task_num + "个软件正在下载中， 请点击查看", contentIntent);
				showNotification(NOTIFICATION_DOWNLOADING_ID, true, notification);
				break;
			case 3:
				//下载失败提示
				String message = msg.getData().getString("msg");
				if (message == null || message.equals("")) {
					message = "下载失败";
				}
				if (errIntent == null)
					errIntent = PendingIntent.getActivity(FileDownloaderService.this, 0, managerIntent, 0);
				
				String softname = msg.getData().getString("softname");
				errNotification = new Notification(R.drawable.app_download_task, softname + "下载失败，点击查看", 
						System.currentTimeMillis());
				errNotification.setLatestEventInfo(FileDownloaderService.this, "爱皮应用下载", softname + "下载失败，点击查看", errIntent);
				showNotification(msg.getData().getInt("softid"), true, errNotification);
			}
		};
	};
	public void dojumpqian(String softname,String appid){
		Intent intent=new Intent(this,QianmingDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("softname",softname);
		intent.putExtra("appid", appid);
		this.startActivity(intent);
	}
	@Override
	public void updateProcess(final Object object) {
	}
	
	@Override
	public synchronized void updateProcess(Exception e, final String message, final Object object) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (delete_all)
					return;
				SiteInfoBean bean = (SiteInfoBean) object;
				MyApplication.getInstance().getSoftMap().remove(bean.softID);
				if (bean.siteFileFecth != null) {
					bean.siteFileFecth.siteStop();
				}
				bean.state = 3;
				Message msg = new Message();
				msg.what = 3;
				msg.getData().putString("softname", bean.softName);
				msg.getData().putInt("softid", bean.softID);
				msg.getData().putString("msg", message);
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	public static void setNotification(Context context) {
		if (notification != null) {
			notification.setLatestEventInfo(context, "爱皮应用下载", task_num + "个软件正在下载中， 请点击查看", contentIntent);
			showNotification(NOTIFICATION_DOWNLOADING_ID, true, notification);
		}
	}
	
	@Override
	public void updateFinish(final Object object) {
		task_num --;
		if(object==null)return;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (delete_all)
					return;
				final SiteInfoBean bean = (SiteInfoBean) object;
				MyApplication.getInstance().getSoftMap().put(bean.softID, 2);
				bean.siteFileFecth = null;
				Message msg = new Message();
				msg.what = 1;
				msg.getData().putSerializable("softinfo", bean);
				handler.sendMessage(msg);
				//上传下载完成标记
				DownloadUtils.uploadTag(bean);
			}
		}).start();
	}
}
