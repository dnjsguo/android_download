package com.appdear.client;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.Adapter.InstalledAdatper;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.download.ListAdatperDataProcessListener;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.InstallList;
import com.appdear.client.utility.LogcatObserver;
import com.appdear.client.utility.LogcatScanner;
import com.appdear.client.utility.ServiceUtils;

/**
 * 已安装列表
 * 
 * @author zqm
 *
 */
public class MoreManagerInstalledActivity extends ListBaseActivity implements 
	ListAdatperDataProcessListener, LogcatObserver {
 
	/**
	 * 移动动作
	 */
	public static final int REMOVE_ACTION = 1;
	
	/**
	 * 卸载动作
	 */
	public static final int MOVE_ACTION = 2;
	
	/**
	 * 打开动作
	 */
	public static final int OPEN_ACTION = 3;
	
	/**
	 * 卸载项
	 */
	private int position;
	
	private IntentFilter filter;
	private LinearLayout availableText;
	private String text;
	
	private InstalledAdatper adapter;
	
	private boolean uninstallflag=false;
	
	public static boolean isupload=false;
	/**
	 * 已安装列表的数量
	 */
	private static int intallsize = 0;
	
//	/**
//	 * 已安装列表的数量
//	 */
//	private static int intalledsize = 0;
	
	/**
	 * 点击卸载的软件的Appid
	 */
	private String pname;
	
	private InstallList packlist=null;
	
	/**
	 * 保存将要移动的程序的packagename
	 */
	private String packgename;
	
	 
	public static List<SoftlistInfo> listData = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_soft_list_layout);
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		listView.setFadingEdgeLength(0);
		LinearLayout u=(LinearLayout) findViewById(R.id.click_button_update);
		u.setVisibility(View.GONE);
		availableText = (LinearLayout) findViewById(R.id.text_layout);
	//	availableText.setVisibility(View.GONE);
		TextView text=(TextView)findViewById(R.id.title_font);
		text.setText("软件管理--可卸载");
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
		params = new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.FILL_PARENT);
		loadingView=new MProgress(this,true);
		this.addContentView(loadingView, params);
		/*params = new LayoutParams(width,
				height*3/4);
		loadingView=new MProgress(this,false);
		this.addContentView(loadingView, params);*/
	}
	@Override
	public void init() {

		if (filter == null) {
			filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
			filter.addAction(Intent.ACTION_PACKAGE_ADDED);
			filter.addDataScheme("package");
			registerReceiver(mIntentReceiver, filter);
		}
//		if (!AppContext.getInstance().isNetState && adapter == null) {
//			new Thread(new LoadThread()).start();
//		}
		isupload=true;
	}
	@Override
	public void initData() {
	//	Log.i("info","initData");
	//	Log.i("infosize",AppContext.getInstance().installlists.size()+"=size1");
		//if (AppContext.getInstance().installlists.size() == 0) {
			AppContext.getInstance().installlists = 
				ServiceUtils.getInstalledApps(this, false,false);
//			Log.i("infosize",AppContext.getInstance().installlists.size()+"=size2");
//		}
		
		intallsize = AppContext.getInstance().installlists.size();
	//	Log.i("info111",intallsize+"="+AppContext.getInstance().installlists);
	//	Log.i("infosize",intallsize+"=intallsize");
	//	intalledsize=intallsize;
	//	adapter = new InstalledAdatper(this, AppContext.getInstance().installlists, this);
		packlist=ServiceUtils.getInstalledAppsForOrder(AppContext.getInstance().installlists,true);
	//	Log.i("infosize",packlist.size()+"=packlist");
	//	Log.i("info111",packlist+"="+packlist);
	}
	
	@Override
	public void updateUI() {
		if (Constants.DEBUG)
			if (Constants.DEBUG) Log.i("installed", "更新界面");
	//	Log.i("info111",adapter+"=adapter");
		if(adapter==null){
			adapter = new InstalledAdatper(this,packlist, this, listView);
			uninstallflag=true;
			adapter = (InstalledAdatper) adapter;
			adapter.notifyDataSetChanged();
		}else{
			adapter.notifyDataSetChanged();
		}
		if(listView!=null){
			listView.setAdapter(adapter);
		}
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

		@Override
		public synchronized void onReceive(Context context, Intent intent) {

			if (Constants.DEBUG)
				Log.i("卸载", "卸载成功");
		//	Log.i("info", "mIntentReceiver
			if(intent.getAction()!=null){
				if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
					

						uninstallflag=true;
				//		Log.i("info", packlist+"");
						if(packlist==null){
							packlist=ServiceUtils.getInstalledAppsForOrder(AppContext.getInstance().installlists,true);
						}
						
						if (intent == null || intent.getDataString() == null 
								|| intent.getDataString().equals(""))
							return;
						String packageName = intent.getDataString().substring(8);
						ServiceUtils.removeSoftStateUninstall(packageName);
						
						Intent intentrefresh = new Intent(Common.DOWNLOAD_NOTIFY);
						sendBroadcast(intentrefresh);
						packlist.remove(position);
						if(AppContext.getInstance().installlists!=null&&packageName!=null){
							if(AppContext.getInstance().installlists.remove(packageName)!=null){
								intallsize--;
							}
						}
						
						//判断更新列表中是否存在刚刚卸载的软件
						for (PackageinstalledInfo info : AppContext.getInstance().updatelist) {
							if (info.pname.equals(packageName)) {
								AppContext.getInstance().updatelist.remove(info);
								break;
							}
						}
						
				//		AppContext.getInstance().installlists.remove(position);
				//		handler1.sendEmptyMessage(1);
				//		adapter.setList(AppContext.getInstance().installlists);
						if(adapter!=null){
							adapter.setList(packlist);
							adapter.notifyDataSetChanged();
						}
					   
						if(MyApplication.getInstance().appUninstalledMap.containsKey(packageName))
						{
							String appname=MyApplication.getInstance().appUninstalledMap.get(packageName);
 							 new UninstallRecommendThread(packageName,appname).start();
							// MyApplication.getInstance().appList.remove(packageName);
							 MyApplication.getInstance().appUninstalledMap.remove(packageName);
						}
						
				}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
					ServiceUtils.handlerAddApp(intent, context);
					packlist=ServiceUtils.getInstalledAppsForOrder(AppContext.getInstance().installlists,true);
					//handler1.sendEmptyMessage(1);
					   //		adapter.setList(AppContext.getInstance().installlists);
					adapter.setList(packlist);
					adapter.notifyDataSetChanged();
				}
			}
			
		}
	};
	
	@Override
	public void refreshDataUI() {
		
	}

	@Override
	public void doRefreshData() {
		
	}
	
//	@Override
//	protected void onNewIntent(Intent intent) {
//		Log.i("info","onNewIntent");
//		uninstallflag=false;
//		super.onNewIntent(intent);
//		changeInstalled();
//	}

	@Override
	protected void onResume() {
		//uninstallflag=false;
		Log.i("info", "onResume="+uninstallflag);
	//	if(uninstallflag==false){
			changeInstalled();
	//	}
		uninstallflag=false;
		super.onResume();
		
	}
	
	/**
	 * 检查安装列表变化
	 */
	private void changeInstalled() {
		Log.i("num",AppContext.getInstance().installlists.size()+"="+intallsize);
			if (intallsize != AppContext.getInstance().installlists.size()&&uninstallflag==false) {
				intallsize = AppContext.getInstance().installlists.size();
				packlist=ServiceUtils.getInstalledAppsForOrder(AppContext.getInstance().installlists,true);
				adapter = new InstalledAdatper(MoreManagerInstalledActivity.this,
						packlist, MoreManagerInstalledActivity.this, listView);
				handler.sendEmptyMessage(UPDATE);
			}else{
				intallsize = AppContext.getInstance().installlists.size();
			}
	}

	@Override
	public synchronized void keyPressProcess(Object object, int processTye) {
	}
	
	@Override
	public void keyPressProcess(Object object, int processTye, int position) {
		pname = (String) object;
		this.packgename = pname;
		this.position = position;
		if (processTye == MoreManagerInstalledActivity.REMOVE_ACTION) {
			//System.out.println("uninstall  package="+((String) object));
			AppContext.getInstance().installlists = ServiceUtils.getInstalledApps(this, false,false);
			PackageinstalledInfo info= AppContext.getInstance().installlists.get(pname);
			MyApplication.getInstance().appUninstalledMap.put(pname,info.appname );
			Uri packageURI = Uri.fromParts("package", (String) object, null);
			Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
			startActivity(intent);
		} else if (processTye == MoreManagerInstalledActivity.MOVE_ACTION) {
 			ServiceUtils.showInstalledAppDetails(MoreManagerInstalledActivity.this, pname);
			LogcatScanner.startScanLogcatInfo(MoreManagerInstalledActivity.this);
		} else if (processTye == MoreManagerInstalledActivity.OPEN_ACTION) {
			Intent intent = getPackageManager().getLaunchIntentForPackage(pname);
			startActivity(intent);
		}
	}
	
	@Override
	public void handleNewLine(String line) {
		if (packgename != null) {
			if (line != null && line.contains("move") && line.contains("->") && line.contains(packgename)) {
				Message msg = new Message();
				msg.getData().putString("packagename", packgename);
				msg.what = 1;
				handler1.sendMessage(msg);
			}
		}
	}
	class UninstallRecommendThread extends Thread
	{
		private String appid;
		private String appname;
		UninstallRecommendThread(String appid,String appname)
		{
			this.appid=appid;
			this.appname=appname;
		}
		@Override
		public void run()
		{
 			Hashtable<String,PackageinstalledInfo>  installlists=null;
 			int pageCount=1;
			   	try
				{
			   		try
					{
						Thread.sleep(3000);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			   		ApiSoftListResult	result = ApiManager.softlistbyappid(appid, 0+"", 0+"", 1+"", 5+"");
			   		//ApiSoftListResult	result = ApiManager.catalogsoftlist("100025", 0+"", 1+"", page+"", 5+"");
					if (result == null ) 
					{
 						return;
					}
					listData=result.softList;
 				   installlists= ServiceUtils.getInstalledApps(MoreManagerInstalledActivity.this, false,true);
				   pageCount=result.totalcount/5;
					for(SoftlistInfo item:listData  )
					{
 						if(installlists.get(item.appid)!=null)
						{
							item.type=1;// 1 已经安装  0没有安装 
						}else
						{ 
							item.type=0;
						}
					}
				} catch (ApiException e)
				{
					e.printStackTrace();
					showException(e);
					return;
				}
			 /*   try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			    
			    Message msg = new Message();
				msg.getData().putString("appid", appid);
				msg.getData().putString("appname", appname!=null?appname:"");
 				msg.getData().putInt("pageCount",pageCount );
				msg.what = 2;
			   	handler1.sendMessage(msg);
		}
	}
	public Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String packagename = msg.getData().getString("packagename");
				PackageinstalledInfo info = AppContext.getInstance().installlists.get(packagename);
				if (info.softsd == 0) {
					//内存
					info.softsd = 1;
				} else if (info.softsd == 1) {
					//存储卡
					info.softsd = 0;
				}
				TextView location = (TextView) listView.findViewWithTag(packagename);
				if (location != null) {
					location.setText("存储位置：" + (info.softsd==1?"SD卡(":"手机内存(")+info.formatsofttsize+")");
				}
				break;
			case 2:
 				String appid= msg.getData().getString("appid");
				String appname=msg.getData().getString("appname");
				int pageCount=msg.getData().getInt("pageCount");
 				Intent intent = new Intent(MoreManagerInstalledActivity.this,
						AlterUninstalledRecommendActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("appid", appid);
				intent.putExtra("appname", appname);
				intent.putExtra("pageCount", pageCount);
				MoreManagerInstalledActivity.this.startActivity(intent);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mIntentReceiver!=null){
			this.unregisterReceiver(mIntentReceiver);
		}
		super.onDestroy();
	}
	
}
