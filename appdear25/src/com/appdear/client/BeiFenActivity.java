package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdear.client.ContactOperateActivity.DialogListener;
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

public class BeiFenActivity extends BaseActivity implements
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
	InstalledBackupControler installedControler=new InstalledBackupControler();
	TextView tag1=null;
	LinearLayout back,retore;
	ImageButton btn_return;
	private LinearLayout tab_ll_linear;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		installedControler.setActivity(this,myHandler);
		setContentView(R.layout.beifen);
		ScreenManager.getScreenManager().pushActivity(this);// 进栈
 		Intent intent=this.getIntent();
 		String tag=intent.getStringExtra("backup");
 		back=(LinearLayout) this.findViewById(R.id.beifen);
 		retore=(LinearLayout) this.findViewById(R.id.huanyuan);
 		tag1=(TextView)this.findViewById(R.id.tv_navigation);
 		if(tag==null||tag.equals("")){
 			retore.setVisibility(View.VISIBLE);
 			back.setVisibility(View.GONE);
 			tag1.setText("云还原");
 		}else if(tag.equals("true")){
 			back.setVisibility(View.VISIBLE);
 			retore.setVisibility(View.GONE);
 			tag1.setText("云备份");
 		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		Button Button01 = (Button) findViewById(com.appdear.client.R.id.Button01);
		Button Button02 = (Button) findViewById(com.appdear.client.R.id.Button02);
		Button01.setOnClickListener(this);
		Button02.setOnClickListener(this);
		Button btn_smsback = (Button) findViewById(R.id.btn_smsback);
		Button btn_smsrestore = (Button) findViewById(R.id.btn_smsrestore);
		btn_smsback.setOnClickListener(this);
		btn_smsrestore.setOnClickListener(this);
		installedControler.initView();
		tab_ll_linear = (LinearLayout) findViewById(R.id.ll_navigation);
			tab_ll_linear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		btn_return = (ImageButton) findViewById(R.id.btn_return);
		   btn_return.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});	
	//	myRunnable = new GetRecommendListRunnble();
	//	new Thread(myRunnable).start();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.Button01:
			createDialog(BACK_INFO,new DialogListener() {
				@Override
				public void postListener() {
					backupContact();
				}
			});
			break;
		case R.id.Button02:
			createDialog(RESTORE_INFO,new DialogListener() {
				@Override
				public void postListener() {
					recoverContact();
				}
			});
			break;
	
		case R.id.installed_backup:
			if (!AppContext.getInstance().isNetState) {
				showMessageInfo("网络错误，不能进行该操作");
				return;
			}
			createDialog(BACK_INFO,new DialogListener() {
				@Override
				public void postListener() {
					installedControler.clickBackup();
				}
			});
			
			break;
		case R.id.installed_restore:
			if (!AppContext.getInstance().isNetState) {
				showMessageInfo("网络错误，不能进行该操作");
				return;
			}
			createDialog(RESTORE_INFO,new DialogListener() {
				@Override
				public void postListener() {
					installedControler.clickRestore();
				}
			});
			
			break;
		case R.id.btn_smsback:
			createDialog(BACK_INFO,new DialogListener() {
				@Override
				public void postListener() {
					sms_back();
				}
			});
			break;
		case R.id.btn_smsrestore:
			createDialog(RESTORE_INFO,new DialogListener() {
				@Override
				public void postListener() {
					sms_recover();
				}
			});
			
			break;
		default:
			break;
		}
	}

	private void recoverContact() {
		dialog();
		if (!AppContext.getInstance().isNetState) {
			showMessageInfo("网络错误，不能进行该操作");
			return;
		}
//		if (!ServiceUtils.checkLogin(BeiFenActivity.this, true)) {
//			SoftwareMainDetilActivity.isClickFavorite = true;
//			return;
//		} else {
			// 让ProgressDialog显示
			xh_pDialog.show();
			new Thread() {
				@Override
				public void run() {
					try {
						String userid = SharedPreferencesControl
								.getInstance()
								.getString(
										"userid",
										com.appdear.client.commctrls.Common.USERLOGIN_XML,
										BeiFenActivity.this);
						//String imei = ServiceUtils.getIMEI(BeiFenActivity.this);
						ApiNormolResult apiSoftListResult = ApiManager
								.recovercontact(userid);
						
						String contact = apiSoftListResult.contact;
//						contact = JsonUtil.uncompress(contact);
						Log.i("gefy", "o===="+apiSoftListResult.isok);
						if (apiSoftListResult.isok == 2) {
							xh_pDialog.cancel();
							showMessageInfo("云端没有任何备份数据，请先备份！");
						}else {
							JSONObject contactJson = new JSONObject(contact);
							int i = ContactUtil.handlerContactAdd(
									BeiFenActivity.this,
									contactJson, 0);
							xh_pDialog.cancel();
							if (apiSoftListResult.isok == 1) {
								if(i == 0){
									showMessageInfo("本地没有变化，无需还原!");
									return;
								}
								showMessageInfo("恭喜您!已成功还原记录" + i + "条");
							} else
								showMessageInfo("数据还原失败！");
						}
					} catch (Exception e) {
						xh_pDialog.cancel();
						showMessageInfo("数据还原失败！");
						e.printStackTrace();
					}
				}
			}.start();
		}
//	}

	private void backupContact() {
		dialog();
		if (!AppContext.getInstance().isNetState) {
			showMessageInfo("网络错误，不能进行该操作");
			return;
		}
		/*if (!ServiceUtils.checkLogin(BeiFenActivity.this, true)) {
			SoftwareMainDetilActivity.isClickFavorite = true;
			return;
		} else {*/
			// 让ProgressDialog显示
			xh_pDialog.show();
			new Thread() {
				@Override
				public void run() {
					try {
   
						String userid = SharedPreferencesControl
								.getInstance()
								.getString(
										"userid",
										com.appdear.client.commctrls.Common.USERLOGIN_XML,
										BeiFenActivity.this);
						String imei = ServiceUtils.getIMEI(BeiFenActivity.this);
						ApiNormolResult apiSoftListResult = ApiManager
								.backupcontact(userid,
										BeiFenActivity.this);
						
						if(apiSoftListResult == null || apiSoftListResult.contactcount.equals("0")){
							sleep(1000);
							xh_pDialog.cancel();
							showMessageInfo("手机中没有数据，无法备份！");
							return;
						}
						xh_pDialog.cancel();
						if (apiSoftListResult == null || apiSoftListResult.isok == 1) 
							showMessageInfo("已经成功备份！已备份数据条数" + apiSoftListResult.contactcount + "条");
						else
							showMessageInfo("备份数据失败！");
					} catch (Exception e) {
						xh_pDialog.cancel();
						showMessageInfo("备份数据失败！");
						e.printStackTrace();
					}
				}
			}.start();
		//}
	}

	// xhButton01的监听器类
	class Bt1DialogListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// 点击“确定”按钮取消对话框
			dialog.cancel();
		}
	}
	/**
	 * sms back
	 */
	private void sms_back(){
		dialog();
		if (!AppContext.getInstance().isNetState) {
			showMessageInfo("网络错误，不能进行该操作");
			return;
		}
		xh_pDialog.show();
		new Thread() {
			@Override
			public void run() {
				try {

					String userid = SharedPreferencesControl
							.getInstance()
							.getString(
									"userid",
									com.appdear.client.commctrls.Common.USERLOGIN_XML,
									BeiFenActivity.this);
					//String imei = ServiceUtils.getIMEI(BeiFenActivity.this);
					String imsi = ServiceUtils.getSimsi(BeiFenActivity.this);
					ApiNormolResult smsResult = ApiManager
							.backupsms(userid,imsi, BeiFenActivity.this);

					
					if(smsResult == null || smsResult.contactcount.equals("0")){
						sleep(1000);
						xh_pDialog.cancel();
						showMessageInfo("手机中没有数据，无法备份！");
						return;
					}
					xh_pDialog.cancel();
					if (smsResult == null || smsResult.isok == 1){
						showMessageInfo("已经成功备份！已备份数据条数"
								+ smsResult.contactcount + "条");
					}
					else{
						showMessageInfo("备份数据失败！");
					}
				} catch (Exception e) {
					xh_pDialog.cancel();
					showMessageInfo("备份数据失败！");
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * sms recover
	 */
	private void sms_recover(){
		dialog();
		if (!AppContext.getInstance().isNetState) {
			showMessageInfo("网络错误，不能进行该操作");
			return;
		}xh_pDialog.show();
		new Thread() {
			@Override
			public void run() {
				try {
					String userid = SharedPreferencesControl
							.getInstance()
							.getString(
									"userid",
									com.appdear.client.commctrls.Common.USERLOGIN_XML,
									BeiFenActivity.this);
					//String imei = ServiceUtils.getIMEI(BeiFenActivity.this);
					String imsi = ServiceUtils.getSimsi(BeiFenActivity.this);
					ApiNormolResult smsResult = ApiManager.recoversms(userid,imsi);
					
					String sms = smsResult.sms;
					//System.out.println("recorve"+sms);
//					contact = JsonUtil.uncompress(contact);
					Log.i("gefy", "o===="+smsResult.isok);
					if (smsResult.isok == 2) {
						xh_pDialog.cancel();
						showMessageInfo("云端没有任何备份数据，请先备份！");
					}else {
						//JSONObject contactJson = new JSONObject(contact);
						//int i = ContactUtil.handlerContactAdd(
						int i = SmsUtil.smsRestroe(BeiFenActivity.this,sms);
						
						xh_pDialog.cancel();
						if(i==0){
							showMessageInfo("本地没有变化，无需还原!");
						}else{
							showMessageInfo("恭喜您!已成功还原短信"+ i +"条");
						}
						//if (smsResult.isok == 1) {
							
//						} else
//							showMessageInfo("数据还原失败！");
					}
				} catch (Exception e) {
					xh_pDialog.cancel();
					showMessageInfo("数据还原失败！");
					e.printStackTrace();
				}
			}
		}.start();
	}
	public interface DialogListener{
		void postListener();
		//void negaListener();
	}
	private void createDialog(String message ,final DialogListener dialogListener){
		AlertDialog.Builder builder = new Builder(BeiFenActivity.this);
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
			case  INSTALLED_BACKUP: //安装列表备份
				xh_pDialog.cancel();
				installedControler.handleBackup();
				break;
			case  INSTALLED_RESTORE: //安装列表备份
				xh_pDialog.cancel();
				installedControler.handleRestore();
				break;
			case  INSTALLED_FAIL:
				xh_pDialog.cancel();
				break;
			}
		}
	};
	
}
