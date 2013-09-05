package com.appdear.client.download;

import java.io.File;
import java.util.Hashtable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MessageHandler;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;

/**
 * 下载界面
 * 
 * @author zqm
 * 
 */
public class MoreManagerDownloadActivity extends ListBaseActivity implements
		ListAdatperDataProcessListener, DownloadListener {

	//private ListViewRefresh listview;

	public final int CANCEL = 0;
	public final int CONTINUE = 1;
	public final int PAUSE = 2;
	public final int RETRY = 3;
	public final int INSTALL = 4;
	public final int FINISH = 5;
	public final int DELETE = 7;
	private LinearLayout smile_face;
	private RelativeLayout manager_layout;

	/**
	 * 保存当前操作的信息
	 */
	private SiteInfoBean saveBean = null;

	ImageButton btn_return;
	private static boolean updatemsgflag=true;
	/**
	 * 显示删除按钮的布局
	 */
	private RelativeLayout delete_all_downloadtask;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_soft_list_layout);
		AppContext.getInstance().downloadlistener = this;
		params = new LayoutParams(width, height * 3 / 4);
		loadingView = new MProgress(this, false);
		this.addContentView(loadingView, params);
	}

	@Override
	public void init() {
		TextView text=(TextView)findViewById(R.id.title_font);
		text.setText("软件管理--下载管理");
		manager_layout = (RelativeLayout) findViewById(R.id.manager_layout);

		// 删除任务按钮
		delete_all_downloadtask = (RelativeLayout) findViewById(R.id.delete_all_downloadtask);
		
		smile_face = (LinearLayout) getLayoutInflater().inflate(
				R.layout.smile_face, null);
		smile_face.setGravity(Gravity.CENTER);
		TextView face_txt = (TextView) smile_face.findViewById(R.id.face_txt);
		face_txt.setText("没有正在下载中的内容哦！");

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		smile_face.setLayoutParams(params);
		smile_face.setVisibility(View.GONE);
		manager_layout.addView(smile_face);

		if (getIntent().getStringExtra("notificaiton") != null
				&& getIntent().getStringExtra("notificaiton").equals("true")) {
			RelativeLayout title_manager = (RelativeLayout) findViewById(R.id.title_manager);
			title_manager.setVisibility(View.VISIBLE);
			btn_return = (ImageButton) findViewById(R.id.btn_return);
			btn_return.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			title_manager.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			face();
		}else{
			RelativeLayout title_manager = (RelativeLayout) findViewById(R.id.title_manager);
			title_manager.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			//返回键
			ImageButton btn_return = (ImageButton) findViewById(R.id.btn_return);
			btn_return.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
		isUpdate = true;
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setFadingEdgeLength(0);
		listView.requestFocus();
		listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);

		LinearLayout click_button_update = (LinearLayout) findViewById(R.id.click_button_update);
		click_button_update.setVisibility(View.GONE);
		LinearLayout click_button_update1 = (LinearLayout) findViewById(R.id.click_button_update1);
		click_button_update1.setVisibility(View.GONE);

		delete_all_downloadtask.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(final View v) {
			Activity activity=getParent();
			if (activity == null)
				activity = MoreManagerDownloadActivity.this;
			if(activity!=null){
				final Activity showActivity = activity;
					ServiceUtils.getAlertDialogForString(activity, "删除",
							"确认要删除所有下载任务吗", new MessageHandler() {
								@Override
								public void messageHandlerOk() {
									FileDownloaderService.delete_all = true;
									loadingalert = new ProgressDialog(showActivity);
									loadingalert.setMessage("正在删除资源文件,请等待...");
									loadingalert.show();
									int size=0,count=0;
									if ((size=AppContext.getInstance().taskList.size()) == 0)
										return;
									String[][] str=new String[size][2];
									SiteInfoBean bean=null;
									for(String softid:AppContext.getInstance().taskSoftList){
										bean=AppContext.getInstance().taskList.get(Integer.parseInt(softid));
										if(bean==null)continue;
										if(bean.state!=2){
											FileDownloaderService.task_num--;
										}
										if (bean.siteFileFecth != null) {
											bean.siteFileFecth.siteStop();
										}
										

										// 删除任务的安装包
										str[count]=new String[2];
										str[count][0]=bean.sFilePath + "/" + bean.sFileName;
										str[count][1]=String.valueOf(bean.softID);
										count++;
									}
									
									
									new Thread(new MyRunnable(str)).start();
								}
							});
				}
			}
		});
	}
	class MyRunnable implements Runnable{
		private String[][] paths;

		public MyRunnable(String[][] paths) {
			super();
			this.paths = paths;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			deleteAll(paths);
		}
		
	}
	private ProgressDialog loadingalert;

	@Override
	public void initData() {
		initDownloadTask();
 		adapter.notifyDataSetChanged();
		super.initData();
	}

	/**
	 * 初始化下载信息
	 */
	private void initDownloadTask() {
		if (AppContext.getInstance().taskList.size() == 0) {
			AppContext.getInstance().downloader.readDB();
		}

		sycDownloadlist();
		adapter = new DownloadAdapter(this, listView, this);
		adapter = (DownloadAdapter) adapter;
	}

	/**
	 * 同步下载列表
	 */
	private void sycDownloadlist() {
		// 设置监听
		for (SiteInfoBean bean : AppContext.getInstance().taskList.values()) {
			bean.listener = this;
		}
	}

	@Override
	public void updateUI() {
		face();
		if(listView==null)return;
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
		super.updateUI();
	}

	@Override
	public void refreshDataUI() {
	}

	@Override
	public void doRefreshData() {
	}

	@Override
	protected void onStart() {
		if (filter == null) {
			filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
			filter.addDataScheme("package");
			registerReceiver(mIntentReceiver, filter);
		}
		super.onStart();
	}

	/**
	 * 安装监听
	 */
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//System.out.println("--------MoreManagerDownloadActivity----onReceive---");
			
			// 安装成功
			// 从下载队列中删除任务信息
			if (intent == null || intent.getDataString() == null
					|| intent.getDataString().equals(""))
				return;
			String packagename = intent.getDataString().substring(8);
			if (saveBean == null)
				return;
			
			/**
			 * jindan-modify start
			 */
			if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED")){
			if (saveBean.appID.equals(packagename)) {
				AppContext.getInstance().downloader.removeTask(saveBean);
			} else {
				for (SiteInfoBean bean : AppContext.getInstance().taskList
						.values()) {
					if (bean.appID.equals(packagename)) {
						saveBean = bean;
						AppContext.getInstance().downloader
								.removeTask(saveBean);
						break;
					}
				}
			}
			}
			/**
			 * jindan-modify end
			 */
			handler.sendEmptyMessage(1);
			/**
			 * jindan-modify start
			 */
			// 判断是否删除apk文件
			if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED")){
			boolean isdelete = SharedPreferencesControl.getInstance()
					.getBoolean("deleteApk",
							com.appdear.client.commctrls.Common.SETTINGS,
							MoreManagerDownloadActivity.this);
			if (isdelete) {
				String filepath = saveBean.sFilePath + "/" + saveBean.sFileName;
				ServiceUtils.deleteSDFile(filepath);
				ServiceUtils.deleteSDFile(filepath + ".size");
			}
			}
			/**
			 * jindan-modify end
			 */
			// 从更新列表中移除刚刚安装成功的软件
			for (PackageinstalledInfo info : AppContext.getInstance().updatelist) {
				if (info.pname.equals(saveBean.appID)) {
					AppContext.getInstance().updatelist.remove(info);
					break;
				}
			}
		}
	};

	/**
	 * 刷新界面
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (adapter == null)
					return;
				face();
				adapter.notifyDataSetChanged();
				break;
			case 2:
				adapter = new DownloadAdapter(MoreManagerDownloadActivity.this,
						listView, MoreManagerDownloadActivity.this);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				// 修改下载状态为重试
				SiteInfoBean bean = (SiteInfoBean) msg.getData()
						.getSerializable("item");
				if(bean==null||listView==null)return;
				ImageView buttonLeft = (ImageView) listView.findViewWithTag(bean.softID + "_");
				if (buttonLeft != null) {
					buttonLeft.setBackgroundResource(R.drawable.retry);
					buttonLeft.invalidate();
				}				
				/*View childView = bean.view;
				if (childView != null) {
					ImageView buttonleft = (ImageView) childView
							.findViewById(R.id.button_left);
					buttonleft.setBackgroundResource(R.drawable.retry);
				}*/
				break;
			case 4:
				Bundle b=msg.getData();
				if(b==null)return;
				int sid=  b.getInt("sid");
				int fs=  b.getInt("fs");
				int ds=  b.getInt("ds");
				ProgressBar	bar=null;
				TextView downloadt=null;
				try {
					 	bar = (ProgressBar) listView.findViewWithTag(sid+1000000);
					 	 
					 	
					 	//Log.i("info1","updateProcess="+bar);
				} catch (Exception e) {
				}
				if (bar != null) {
					 if (bar.getMax() == 0)
				    bar.setMax(fs);
					bar.setProgress(ds);
					downloadt=(TextView) ((RelativeLayout)bar.getParent()).findViewById(R.id.downloadt);
					float size = (float)ds/fs;
					if(downloadt!=null){
						downloadt.setText((float) Math.floor(size*1000)/10+"%");
					}
				}
				break;
			case 5:
				if(loadingalert!=null&&loadingalert.isShowing()==true){
					loadingalert.cancel();
					loadingalert.dismiss();
				}
				break;
			case 6:
				Object id=msg.obj;
				if(id!=null&&id instanceof Integer){
					FileDownloaderService.notificationManager.cancel((Integer)id);
				}
				break;
			case 7:
				AppContext.getInstance().taskSoftList.clear();
				break;
			case 8:
				Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
				sendBroadcast(intent);
				break;
			}
		}
	};
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
	//	Log.i("info333","onPause");
		updatemsgflag=false;
		super.onPause();
	}

	@Override
	public void updateProcess(Object object) {
		SiteInfoBean bean = (SiteInfoBean) object;
		MyApplication.getInstance().getSoftMap().put(bean.softID, 1);
		ProgressBar bar = null;
		try {
			bar = (ProgressBar) listView.findViewWithTag(bean.softID+1000000);
	//		Log.i("info1","updateProcess="+bar);
		} catch (Exception e) {
		}
		if(bar!=null&&updatemsgflag==true){
			Message message =handler.obtainMessage();
		 
			message.getData().putInt("sid",bean.softID);
			message.getData().putInt("fs",bean.fileSize);
			message.getData().putInt("ds",bean.downloadLength);
			message.what = 4;
			handler.sendMessage(message);
		}
		/*try {
			bar = (ProgressBar) listview.findViewWithTag(bean.softID);
			Log.i("info1","updateProcess="+bar);
		} catch (Exception e) {
		}
		if (bar != null) {
			 if (bar.getMax() == 0)
		    bar.setMax(bean.fileSize);
			bar.setProgress(bean.downloadLength);
		}*/
		// Log.i("", "button=" + ",softid=" +
		// bean.softID +", bar=" + bar + ",softname="+bean.softName);;
	}

	/**
	 * 删除下载任务
	 */
	public void deleteAll(String[][] paths) {
		AppContext.getInstance().taskList = new Hashtable<Integer, SiteInfoBean>();
		
		handler.sendEmptyMessage(7);
		if(loadingalert!=null){
			for (String[] path : paths) {
				AppContext.getInstance().downloader.downDb.delete(Integer.parseInt(path[1]));
				MyApplication.getInstance().getSoftMap().remove(Integer.parseInt(path[1]));
				
				if (FileDownloaderService.notificationManager != null){
					Message m=handler.obtainMessage();
					m.what=6;
					m.obj=	Integer.parseInt(path[1]);
					handler.sendMessage(m);
				}
			}
			handler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					FileDownloaderService.setNotification(MoreManagerDownloadActivity.this);
				}
				
			});
			handler.sendEmptyMessage(1);
			// 发通知刷新界面状态
			handler.sendEmptyMessage(8);
			
			FileDownloaderService.delete_all = false;
			handler.sendEmptyMessage(5);	
			
			for (String[] path : paths) {
				//if(loadingalert.isShowing()){
 					ServiceUtils.deleteSDFile(path[0]);
					ServiceUtils.deleteSDFile(path[0] + ".size");
					
				/*}else{
					break;
				}*/
			}
		}
		
	}

	@Override
	public synchronized void keyPressProcess(Object object, int processTye) {
		saveBean = (SiteInfoBean) object;
		switch (processTye) {
		case CONTINUE:// 继续
			// 继续下载删除失败通知
			FileDownloaderService.setNotification(this);
			if (FileDownloaderService.notificationManager != null)
				FileDownloaderService.notificationManager
						.cancel(saveBean.softID);

			boolean loadwifi = SharedPreferencesControl.getInstance()
					.getBoolean("loadwifi",
							com.appdear.client.commctrls.Common.SETTINGS, this);
			if (!ServiceUtils.isWiFiActive(this) && loadwifi) {
				showMessageInfo("您设置仅在Wi-Fi环境下载,请检查网络或更改设置.");
				return;
			}
			MyApplication.getInstance().getSoftMap().put(saveBean.softID, 1);
			DownloadUtils.addDownloadTask(saveBean,MoreManagerDownloadActivity.this);
			
			// 此处加刷新是为了刷新到最新的view
			// handler.sendEmptyMessage(1);
			break;
		case DELETE: 
			//先停止再删除
			if (saveBean.siteFileFecth != null)
				saveBean.siteFileFecth.siteStop();
			 ServiceUtils.deleteSDFile(saveBean.sFilePath + "/" + saveBean.sFileName);
			 ServiceUtils.deleteSDFile(saveBean.sFilePath + "/" + saveBean.sFileName + ".size");
			 
			AppContext.getInstance().downloader.removeTask(saveBean);
			MyApplication.getInstance().getSoftMap().remove(saveBean.softID);
			FileDownloaderService.task_num --;
			
			FileDownloaderService.setNotification(this);
			if (FileDownloaderService.notificationManager != null)
				FileDownloaderService.notificationManager.cancel(saveBean.softID);
			handler.sendEmptyMessage(1);
			//发通知刷新界面状态
			Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
			sendBroadcast(intent);
			break;
 
		case PAUSE:// 暂停
			AppContext.getInstance().downloader.pauseTask(saveBean);
			MyApplication.getInstance().getSoftMap().put(saveBean.softID, 5);
			break;
		case RETRY:// 重试
			MyApplication.getInstance().getSoftMap().put(saveBean.softID, 1);
			if (ServiceUtils.getSDCardUrl() == null) {
				showMessageInfo("SD卡不存在");
				return;
			} else if (ServiceUtils.readSdCardAvailableSpace() < (saveBean.fileSize - saveBean.downloadLength)) {
				showMessageInfo("SD卡存储空间已满，请先清理SD卡再下载.");
				return;
			}
			DownloadUtils.addDownloadTask(saveBean,
					MoreManagerDownloadActivity.this);
			break;
		case INSTALL:// 安装
			if (ServiceUtils.isHasFile(saveBean.sFilePath + "/"
					+ saveBean.sFileName)) {
				ServiceUtils.Install(this,saveBean.sFilePath + "/"
					+ saveBean.sFileName,saveBean.appID,saveBean.softID, handler);
			} else {
				showMessageInfo("安装文件不存在");
			}

			// 上传日志，点安装时
			new Thread(new Runnable() {

				@Override
				public void run() {
					ApiManager.downloadcomplete(saveBean.sSiteURL, "2");
				}
			}).start();
			break;
		}
	}

	@Override
	protected void onResume() {
		AppContext.getInstance().downloadlistener = this;
		updatemsgflag=true;
		super.onResume();
		if (ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR) == null)
			return;
		// face();
		handler.sendEmptyMessage(1);
	}

	@Override
	public synchronized void updateProcess(Exception e, String msg,
			Object object) {
		SiteInfoBean bean = (SiteInfoBean) object;
		MyApplication.getInstance().getSoftMap().remove(bean.softID);
		if (bean.siteFileFecth != null) {
			bean.siteFileFecth.siteStop();
		}
		bean.state = 3;
		Message message = new Message();
		message.getData().putSerializable("item", bean);
		message.what = 3;
		handler.sendMessage(message);
	}

	@Override
	public void dataNotifySetChanged() {
		if (adapter == null)
			return;
		adapter.notifyDataSetChanged();
		face();
		super.dataNotifySetChanged();
	}

	@Override
	public void updateFinish(Object object) {
		SiteInfoBean bean = (SiteInfoBean) object;
		MyApplication.getInstance().getSoftMap().put(bean.softID, 2);
		// View childView = bean.view;
		// if (childView != null) {
		// childView.findViewById(R.id.button_left).setBackgroundResource(R.drawable.download_image_install);
		// }
       if(listView==null)
       {
    	   return;
       }
		ProgressBar bar = null;
		try {
			bar = (ProgressBar) listView.findViewWithTag(bean.softID+1000000);
			
		} catch (Exception e) {
		}
		ImageView buttonLeft = null;
		buttonLeft = (ImageView) listView.findViewWithTag(bean.softID + "_");
		if (buttonLeft != null) {
			buttonLeft.setBackgroundResource(R.drawable.download_image_install);
			buttonLeft.invalidate();
		}
		if (bar != null) {
			TextView downloadt=(TextView) ((RelativeLayout)bar.getParent()).findViewById(R.id.downloadt);
			bar.setVisibility(View.GONE);
			if(downloadt!=null){
				downloadt.setVisibility(View.GONE);
			}
		}
		// Log.i("", "button=" + buttonLeft + ",softid=" +
		// bean.softID +", bar=" + bar + ",softname="+bean.softName);;
	}

	@Override
	protected void onDestroy() {
		if (mIntentReceiver != null) {
			unregisterReceiver(mIntentReceiver);
		}
		super.onDestroy();
	}

	public void face() {
		if (AppContext.getInstance().taskList.size() == 0) {
			smile_face.setVisibility(View.VISIBLE);
			delete_all_downloadtask.setVisibility(View.GONE);
		} else {
			smile_face.setVisibility(View.GONE);
			delete_all_downloadtask.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// if (adapter == null)
		// return;
		face();
		// adapter.notifyDataSetChanged();
		super.onNewIntent(intent);
	}

	@Override
	public void keyPressProcess(Object object, int processTye, int position) {
		// TODO Auto-generated method stub
		
	}
}
