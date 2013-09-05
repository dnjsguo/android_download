/**
 * BaseActivity.java
 * created at:2011-5-10下午05:21:53
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */
package com.appdear.client.commctrls;

import java.lang.ref.WeakReference;
import java.util.Vector;
import java.util.concurrent.AbstractExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.appdear.client.MoreFeedBackActivity;
import com.appdear.client.MoreHelpMainActivity;
import com.appdear.client.MoreSettingsActivity;
import com.appdear.client.MoreUserCenterMainActivity;
import com.appdear.client.MoreUserLoginInActivity;
import com.appdear.client.R;
import com.appdear.client.UpdateDialog;
import com.appdear.client.commctrls.BaseActivity.LoadThread;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;
import dalvik.system.VMRuntime;

/**
 * 窗口基类
 * 
 * @author zqm
 */
public abstract class BaseGroupActivity extends ActivityGroup  {

	public static final String SETTINGS_T = "设置";
	public static final String HELP_DOC = "帮助";
	public static final String FEEDBACK_T = "反馈";
	public static final String EXIT = "退出";
	public static WeakReference<Bitmap> weakRerference; 
//	protected Toast mScreenHint;
	public static Menu menu;
	protected static int width=0;
	protected static int height=0;
	protected MProgress loadingView;
	// MENU ID

	/**
	 * MENU ID--设置
	 */
	public static final int SETTINGS_ID = Menu.FIRST;
	
	/**
	 * MENU ID -- 市场升级
	 */
	public static final int UPDATE_MARKET_ID = Menu.FIRST + 1;

	/**
	 * MENU ID -- 会员
	 */
	public static final int USER_ID = Menu.FIRST + 2;
	
	/**
	 * MENU ID -- 帮助
	 */
	public static final int HELP_DOC_ID = Menu.FIRST + 3;

	/**
	 * MENU ID -- 反馈
	 */
	public static final int FEEDBACK_ID = Menu.FIRST + 4;

	/**
	 * MENU ID -- 退出
	 */
	public static final int EXIT_ID = Menu.FIRST + 5;

	// 更新UI的TAG
	/**
	 * 开始Tag
	 */
	public static final int START_TAG = 0;
	
	/**
	 * 
	 */
	public static final int UPDATE = 3;
	
	/**
	 * 结束Tag
	 */
	public static final int END_TAG = 1;

	/**
	 * 异常Tag
	 */
	public static final int ERR_TAG = 2;

	/**
	 * 提示信息
	 */
	public static final int INFO_TAG = 4;

	/**
	 * 之类需要重绘ui的操作
	 */
	public static final int REFRESH_UI = 5;

	/**
	 * 
	 */
	public final static int MNUEHANDLERLONGIN = 6;

	/**
	 * 
	 */
	public final static int MNUEHANDLERLONGOUT = 7;
	
	/**
	 * 
	 */
	public final static int LOADV = 11;
	
	/**
	 * 
	 */
	public final static int LOADG = 12;
	/**
	 * 错误信息的key
	 */
	public final String ERR_INFO_KEY = "err";

	/**
	 * 提示信息的key
	 */
	public final String INFO_KEY = "info";

	/**
	 * 是否有UI数据需要更新
	 */
	public boolean isUpdate = false;

	/**
	 * 更多界面二级type
	 */
	public static int moreChildType = -1;
	public static int userChildType = -1;
	public static int searchResultChildType = -3;

	public int resource;
	/**
	 * 当前窗口类型
	 */
	public static int currentType = 0;

	/**
	 * 是否提示正在加载数据
	 */
	public   boolean isShowAlert = true;

	/**
	 * 
	 */
	public boolean bgshow = false;

	public IntentFilter filter;

	/**
	 * 是否允许弹出对话框
	 */
	public static boolean netStateDialog = true;

	/**
	 * 线程池
	 */
//	AbstractExecutorService pool=new ThreadPoolExecutor(10,20,15L,TimeUnit.SECONDS,new SynchronousQueue(),new ThreadPoolExecutor.DiscardOldestPolicy());

	

	//protected ExecutorService pool = Executors.newFixedThreadPool(10);
	private ExecutorService pool = Executors.newCachedThreadPool();
	private final static float TARGET_HEAP_UTILIZATION = 0.75f;

	/**
	 * menu是否加载成功
	 */
	public boolean isMenuLoad = true;
	
	public static Vector<BaseGroupActivity> list = new Vector<BaseGroupActivity>();

	public static int num = 0;

	public static synchronized int getNum() {
		return num;
	}

	public static synchronized void addNum() {
		num++;
	}
	public static synchronized void subNum() {
		num--;
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION); 
		init();
		initThread();
	}

	public abstract void init();

	@Override
	protected void onStart() {
	/*	filter = new IntentFilter();
		filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, filter);*/
		super.onStart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (Constants.DEBUG)
			if (Constants.DEBUG)
				Log.i("keyEvent", "event:" + event.getAction() + ",keyCode"
						+ keyCode);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SETTINGS_ID, 0, "设置").setIcon(R.drawable.sz);
		menu.add(0, UPDATE_MARKET_ID, 1, "版本升级").setIcon(R.drawable.scsj);
		menu.add(0, HELP_DOC_ID, 2, "帮助").setIcon(R.drawable.help);
		menu.add(0, USER_ID, 1, "会员").setIcon(R.drawable.menu_user);
		menu.add(0, FEEDBACK_ID, 3, "反馈").setIcon(R.drawable.fk);
		menu.add(0, EXIT_ID, 4, "退出").setIcon(R.drawable.exit);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null)
			return false;
		if (Constants.DEBUG)
			Log.i("onOptionsItemSelected", "event" + item.getItemId());
		switch (item.getItemId()) {
		case SETTINGS_ID:
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(this, MoreSettingsActivity.class);
			startActivity(intent);
			break;
		case UPDATE_MARKET_ID:
			//爱皮更新
			if (AppContext.getInstance().info == null) {
				showMessageInfo("爱皮应用下载已是最新版本无需更新");
			} else {
				if (AppContext.getInstance().isUpdateStarted) {
					 if (null==ServiceUtils.getSDCardUrl()){
							Toast.makeText(this,"您的SD卡不存在，或者插入不正确！",Toast.LENGTH_LONG).show();
					 }
					 else {
						 showMessageInfo("更新已开始");
					 }
				} else {
					final String updateurl = AppContext.getInstance().info.updateurl;
					if (updateurl != null && !updateurl.trim().equals("")) {
						if (Constants.DEBUG)
							Log.i("jineefo", updateurl);
						Intent intent1 = new Intent();
						intent1.setClass(BaseGroupActivity.this, UpdateDialog.class);
						intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent1.putExtra("info", AppContext.getInstance().info);
						startActivity(intent1);
					} else {
						showMessageInfo("爱皮应用下载已是最新版本无需更新");
					}	
				}
			}
			break;
		case USER_ID:
			if (!ServiceUtils.checkLogin(this, false)) {
				//用户未登录，显示登录界面
				intent = new Intent(this,
						MoreUserLoginInActivity.class).addFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
						Common.USERLOGINFLAG, "true");
				startActivity(intent);
			} else {
				//用户已登录,显示会员主界面
				intent = new Intent(this,
						MoreUserCenterMainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			break;
		case HELP_DOC_ID:
			intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(this, MoreHelpMainActivity.class);
			startActivity(intent);
			break;
		case FEEDBACK_ID:
			intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(this, MoreFeedBackActivity.class);
			startActivity(intent);
			break;
		case EXIT_ID:
			ServiceUtils.exitSystem(this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 启动UI更新线程
	 */
	public void initThread() {
 		if (AppContext.getInstance().isNetState) {
		//	pool.execute(new LoadThread());
 			new LoadThread().start();
		} else {
			 Toast.makeText(this, "网络错误，请检查网络状态",Toast.LENGTH_SHORT).show();
			list.add(this);
			if (Constants.DEBUG)
				Log.i("BaseActivity", "启动界面数量:" + getNum());
			if (isUpdate) {
				//根据不同需求界面需要加载的情况
				pool.execute(new LoadThread());
				//new LoadThread().start();
			}
		}
	}

	/**
	 * 提示异常提示
	 * 
	 * @param e
	 *            异常信息
	 */
	public void showException(Exception e) {
		Message message = new Message();
		message.what = 2;
		Bundle bundle = new Bundle();
		String error = e.getMessage();
		bundle.putString(ERR_INFO_KEY, error);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	/**
	 * 提示异常信息
	 * 
	 * @param error
	 *            异常提示信息
	 */
	public void showException(String error) {
		Message message = new Message();
		message.what = 2;
		Bundle bundle = new Bundle();
		bundle.putString(ERR_INFO_KEY, error);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public void showMessageInfo(String info) {
		Message message = new Message();
		message.what = INFO_TAG;
		Bundle bundle = new Bundle();
		bundle.putString(INFO_KEY, info);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public void refreshUI(String tag, String info) {
		Message message = new Message();
		message.what = REFRESH_UI;
		Bundle bundle = new Bundle();
		bundle.putString(tag, info);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public class LoadThread extends  Thread {
		@Override
		public void run() {
			handler.sendEmptyMessage(START_TAG);
			initData();
			handler.sendEmptyMessage(UPDATE);
			handler.sendEmptyMessage(END_TAG);
		}
	}

	/**
	 * 更新UI
	 * 
	 * @author
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_TAG:
				updateUIStart();
				break;
			case UPDATE:
				try {
					updateUI();
				} catch (OutOfMemoryError e) {
						//AsyLoadImageService.getInstance().getImageCache().clear();
 						e.printStackTrace();
						Log.e("load image", "内存溢出啦");
				}
			case END_TAG:
				updateUIEnd();
				break;
			case ERR_TAG:
				updateUIErr(msg.getData().getString(ERR_INFO_KEY));
				break;
			case INFO_TAG:
				updateInfo(msg.getData().getString(INFO_KEY));
				break;
			case REFRESH_UI:
				refreshUI(msg.getData());
				break;
			case LOADV:
				if(loadingView!=null)loadingView.setVisibility(View.VISIBLE);
				break;
			case LOADG:
				if(loadingView!=null)loadingView.setVisibility(View.GONE);
				break;
			}
		}
	};

	/**
	 * 加载初始化数据
	 */
	public void initData() {

	}

	/**
	 * 更新UI
	 */
	public void updateUI() {
	}

	/**
	 * 开始更新
	 */
	protected void updateUIStart() {
//		if (isShowAlert) {
//			if (mScreenHint != null)
//				mScreenHint.cancel();
//			mScreenHint = Toast.makeText(this, "正在加载数据....",Toast.LENGTH_SHORT);
//			mScreenHint.show();
//		}
	}

	/**
	 * 更新结束
	 */
	protected void updateUIEnd() {
		
	}

	/**
	 * 更新UI出现错误
	 */
	protected void updateUIErr(String msg) {
		if (isShowAlert) {
//			if (mScreenHint != null)
//				mScreenHint.cancel();
			 Toast.makeText(this, "加载错误：" + msg,Toast.LENGTH_SHORT).show();
//		mScreenHint.show();
		}
	}

	/**
	 * 弹出提示信息
	 * 
	 * @param msg
	 */
	public void updateInfo(String msg) {
//		if (mScreenHint != null)
//			mScreenHint.cancel();
		 Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
//		mScreenHint.show();
	}

	/**
	 * 刷新界面
	 */
	public void refreshUI(Bundle budle) {
	}



	/*protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.DEBUG)
				Log.i("BaseActivity", "action=" + action);
			if (action
					.equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {
				// 获得网络连接服务
				boolean checkState = ServiceUtils.checkNetState(context);
				if (AppContext.getInstance().isNetState != checkState)
					netStateDialog = true;
				AppContext.getInstance().isNetState = checkState;
				if (AppContext.getInstance().isNetState) {
					netStateDialog = false;
					startActivity(new Intent(BaseGroupActivity.this,
							AlertDialogView.class).putExtra("type", "close"));
				}

				Log.e("ConnectivityReceiver",
						"isState:" + AppContext.getInstance().isNetState);
				if (!AppContext.getInstance().isNetState && netStateDialog) {
					netStateDialog = false;
					startActivity(new Intent(BaseGroupActivity.this,
							AlertDialogView.class).setFlags(
							Intent.FLAG_ACTIVITY_SINGLE_TOP).putExtra("type",
							"open"));
				} else {
					startActivity(new Intent(BaseGroupActivity.this,
							AlertDialogView.class).putExtra("type", "close"));
				}

				// 暂停当前所有正在下载的线程
				if (!AppContext.getInstance().isNetState)
					AppContext.getInstance().downloader.downDb.updatePause();

				if (AppContext.getInstance().isNetState) {
					for (int i = list.size() - 1; i >= 0; i--) {
						list.get(i).initThread();
						Log.i("refresh", "refresh:" + i);
					}
					list.clear();
				}
			}
			this.clearAbortBroadcast();
		}
	};*/

	

	protected Object getDrawable(int flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onDestroy() {
	/*	if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}*/
		subNum();
		Log.i("num",getNum()+"=onDestroy");

//		if (mScreenHint != null)
//			mScreenHint.cancel();
		System.gc();
		super.onDestroy();
	}

	@Override
	public void overridePendingTransition(int enterAnim, int exitAnim) {
		super.overridePendingTransition(0, 0);
	}
	
}
