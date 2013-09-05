package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.BeiFenActivity.DialogListener;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.InstalledBackupControler;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ContactUtil;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.SmsUtil;

public class ContactOperateActivity extends BaseActivity implements
		OnClickListener {
	
	public static final int RECOMMEND_UPDATE = 0;
	public static final int DAREN_DETECT=1;
	public static final int INSTALLED_BACKUP=2;
	public static final int INSTALLED_RESTORE=3;
	public static final int INSTALLED_FAIL=4;
	private static final String BACK_INFO ="您是否需要备份？\n登录会员后备份 可支持在不同手机中还原。"; 
	private static final String RESTORE_INFO ="您是否需要还原？\n如还原信息与本地没有发生变化，将不做还原处理。"; 
	int xh_count = 0;
	// 声明进度条对话框
	ProgressDialog xh_pDialog;
	TextView detectButton; // 检测button
	TextView darenNumView; // 已安装多少应用
	/*
	 * 初级达人：用户安装的软件为10个以下时 中级达人：用户安装软件为10-19时 高级达人：用户安装软件为20-39时
	 * 终极玩家：用户安装软件为40个及以上时
	 */
	TextView darenLevelView; // 等级 0 未检测 1 初级 2 中级 3 高级 4终极玩家
	TextView darenWarnView; // 提示信息
	ImageView darenImage;
//	Button darenUpdateButton;
//	GridView recommendListGrid; // 推荐应用
	int darenInstalled;
	int darenLevel;
	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;

	private List<SoftlistInfo> listData = null;
	private int page = 1;
	/**
	 * 总页码
	 */
	protected int PAGE_TOTAL_SIZE = 1;
	/**
	 * 每页显示10条
	 */
	protected int PAGE_SIZE = 8;
	int recommendCount; // 推荐总数量
	List<String> recommendIconUrls = new ArrayList<String>();
	List<String> recommendNames = new ArrayList<String>();
//	GetRecommendListRunnble myRunnable = null;
	boolean isLoading = false;
	boolean isFirst = true;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactoperate);
		ScreenManager.getScreenManager().pushActivity(this);// 进栈
 		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		LinearLayout ll_personal_back = (LinearLayout) findViewById(R.id.ll_personal_back);
		LinearLayout ll_personal_restore = (LinearLayout) findViewById(R.id.ll_personal_restore);
		ll_personal_back.setOnClickListener(this);
		ll_personal_restore.setOnClickListener(this);
		LinearLayout ll_personal_guarantee = (LinearLayout) findViewById(R.id.ll_personal_guarantee);
		LinearLayout ll_personal_store_address = (LinearLayout) findViewById(R.id.ll_personal_store_address);
		ll_personal_guarantee.setOnClickListener(this);
		ll_personal_store_address.setOnClickListener(this);
//		Button btn_smsback = (Button) findViewById(R.id.btn_smsback);
//		Button btn_smsrestore = (Button) findViewById(R.id.btn_smsrestore);
//		btn_smsback.setOnClickListener(this);
//		btn_smsrestore.setOnClickListener(this);
		initDaren();
	//	myRunnable = new GetRecommendListRunnble();
	//	new Thread(myRunnable).start();
	}

	public void initDaren() {
		// 手机达人View
		// SharedPreferencesControl.getInstance().putString("password",
		// password,com.appdear.client.commctrls.Common.USERLOGIN_163_XML,MyApplication.getInstance());
		detectButton = (TextView) this.findViewById(R.id.detectButton);
		darenNumView = (TextView) this.findViewById(R.id.darenNumView);
		darenLevelView = (TextView) this.findViewById(R.id.darenLevelView);
		darenWarnView = (TextView) this.findViewById(R.id.darenWarnView);
		darenImage = (ImageView) this.findViewById(R.id.darenImage);
//		darenUpdateButton = (Button) this.findViewById(R.id.darenUpdateButton);
//		recommendListGrid = (GridView) this
//				.findViewById(R.id.recommendListGrid);
//		darenUpdateButton.setOnClickListener(this);
		detectButton.setOnClickListener(this);
		darenLevel = SharedPreferencesControl.getInstance().getInt(
				"darenLevel", null, this);
		darenInstalled = SharedPreferencesControl.getInstance().getInt(
				"darenInstalled", null, this);
//		System.out.println("-darenLevel=" + darenLevel + "  darenInstalled="+ darenInstalled);
		initDarenDetect();
	}

	public void initDarenDetect() {
		switch (darenLevel) {
		case 0:
			darenNumView.setVisibility(View.GONE);
 			darenLevelView.setText("请检测");
			darenWarnView.setText("您还未检测达人等级，请点击“检测”按钮开始检测！");
			darenImage.setImageResource(R.drawable.dr1);
			break;
		case 1:
			darenNumView.setVisibility(View.VISIBLE);
			darenWarnView.setVisibility(View.VISIBLE);
			darenNumView.setText("已安装软件个数" + darenInstalled + "个");
			darenLevelView.setText("等级：初级达人");
			darenImage.setImageResource(R.drawable.dr1);
			darenWarnView
					.setText("再下载" + (10 - darenInstalled) + "个应用就成为中级达人了");
			break;
		case 2:
			darenNumView.setVisibility(View.VISIBLE);
			darenWarnView.setVisibility(View.VISIBLE);
			darenImage.setImageResource(R.drawable.dr2);
			darenNumView.setText("已安装软件个数" + darenInstalled + "个");
			darenWarnView
					.setText("再下载" + (20 - darenInstalled) + "个应用就成为高级达人了");
			darenLevelView.setText("等级：中级达人");
			break;
		case 3:
			darenNumView.setVisibility(View.VISIBLE);
			darenWarnView.setVisibility(View.VISIBLE);
			darenImage.setImageResource(R.drawable.dr3);
			darenNumView.setText("已安装软件个数" + darenInstalled + "个");
			darenWarnView
					.setText("再下载" + (40 - darenInstalled) + "个应用就成为终极玩家了");
			darenLevelView.setText("等级：高级达人");
			break;
		case 4:
			darenNumView.setVisibility(View.VISIBLE);
			darenWarnView.setVisibility(View.VISIBLE);
			darenImage.setImageResource(R.drawable.dr4);
			darenNumView.setText("已安装软件个数" + darenInstalled + "个");
			darenWarnView.setText("您已经是终极玩家，请继续使用爱皮应用下载。");
			darenLevelView.setText("等级：终极玩家");
			break;
		}
	}

	 

	public void dialog() {
		if (xh_pDialog == null)
			xh_pDialog = new ProgressDialog(this);
		// 设置进度条风格，风格为圆形，旋转的
		xh_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		//xh_pDialog.setTitle("提示");
		// 设置ProgressDialog提示信息
		xh_pDialog.setMessage("操作中，请稍后.....");
		// 设置ProgressDialog标题图标
//		xh_pDialog.setIcon(R.drawable.icon);
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		xh_pDialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回键取消
		xh_pDialog.setCancelable(true);
		// 设置ProgressDialog 的一个Button
//		xh_pDialog.setButton("确定", new Bt1DialogListener());
	}

	public void dialog_daren() {
		if (xh_pDialog == null)
			xh_pDialog = new ProgressDialog(this);
		// 设置进度条风格，风格为圆形，旋转的
		xh_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
//		xh_pDialog.setTitle("提示");
		// 设置ProgressDialog提示信息
		xh_pDialog.setMessage("正在检测中请稍后");
		// 设置ProgressDialog标题图标
		// xh_pDialog.setIcon(R.drawable.icon);
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		xh_pDialog.setIndeterminate(false);
		xh_pDialog.show();
		// 设置ProgressDialog 是否可以按退回键取消
		// xh_pDialog.setCancelable(true);
		// 设置ProgressDialog 的一个Button
		// xh_pDialog.setButton("确定", new Bt1DialogListener());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.ll_personal_back:
			Intent intent=new Intent(this,BeiFenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("backup","true");
			this.startActivity(intent);
			break;
		case R.id.ll_personal_restore:
			Intent intent1=new Intent(this,BeiFenActivity.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			this.startActivity(intent1);
			break;
		case R.id.detectButton:
			dialog_daren();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					myHandler.sendEmptyMessage(DAREN_DETECT);
				};
			}.start();
			break;
		case R.id.ll_personal_guarantee:
			Intent i1=new Intent(this,MorePhoneStoreInfoActivity.class);
			i1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			this.startActivity(i1);
			break;
		case R.id.ll_personal_store_address:
			Intent i=new Intent(this,MorePhoneStoreActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			this.startActivity(i);
			break;
		default:
			break;
		}
	}

	// xhButton01的监听器类
	class Bt1DialogListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// 点击“确定”按钮取消对话框
			dialog.cancel();
		}
	}
	
	public interface DialogListener{
		void postListener();
		//void negaListener();
	}
	private void createDialog(String message ,final DialogListener dialogListener){
		AlertDialog.Builder builder = new Builder(ContactOperateActivity.this);
		builder.setMessage(message);//您确定要备份吗？
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogListener.postListener();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//dialogListener.negaListener();
			}
		});
		builder.create().show();
	}
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DAREN_DETECT:  // 达人检测
				xh_pDialog.cancel();
				List<PackageInfo> packs = ContactOperateActivity.this.getPackageManager().getInstalledPackages(0);
			//	System.out.println("packs==="+packs.size());
				int j=0;
				for(int i=0;i<packs.size();i++) {   
			    	PackageInfo p = packs.get(i);
			    	ApplicationInfo appInfo = p.applicationInfo;
			    	if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			    		//非系统
			    		//System.out.println("packageName="+appInfo.packageName+"     name="+appInfo.name);
			    		if(!"com.appdear.serviceforpc".equals(appInfo.packageName))
			    		{
			    			j++;
			    		}
			    		
			    	//	System.out.println(" not system :"+appInfo.packageName);
			    		 
			    	}else
			    	{
			    		//系统
				      //  System.out.println("  system :"+appInfo.packageName);
			    	}
				}
 				darenInstalled =  j;
				if (darenInstalled > 0 && darenInstalled <= 9) {
					darenLevel = 1;
				} else if (darenInstalled > 9 && darenInstalled < 20) {
					darenLevel = 2;
				} else if (darenInstalled >= 20 && darenInstalled < 40) {
					darenLevel = 3;
				} else {
					darenLevel = 4;
				}
				initDarenDetect();

				SharedPreferencesControl.getInstance().putInt("darenLevel",
						darenLevel, null, ContactOperateActivity.this);
				SharedPreferencesControl.getInstance().putInt("darenInstalled",
						darenInstalled, null, ContactOperateActivity.this);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							ApiManager.daren();
						} catch (ApiException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				
				break;
		}
	  }
	};
  }
