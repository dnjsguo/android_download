/**
 * TestTabActivity.java
 * created at:2011-5-17下午05:09:12
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.LocalActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.Updateinfo;
import com.appdear.client.model.UpdatelistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.AppdearService;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MessageHandler;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiUserResult;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.utility.ServiceUtils;

public class MainActivity extends BaseGroupActivity {

	/**
	 * 内容显示
	 */
	private LinearLayout container = null;
	public static boolean flag = true;
	private ApiUserResult result = null;
	/**
	 * 主页
	 */
	private RelativeLayout firstClick;
	private ImageView firstpage;
	private ImageView firstpagebg;

	/**
	 * 类别
	 */
	private RelativeLayout categoryClick;
	private ImageView category;
	private ImageView categorybg;

	/**
	 * 排行
	 */
	private RelativeLayout rankingClick;
	private ImageView ranking;
	private ImageView rankingbg;

	/**
	 * 搜索
	 */
	private RelativeLayout searchClick;
	private ImageView search;
	private ImageView searchbg;

	/**
	 * 更多
	 */
	private RelativeLayout moreClick;
	private ImageView more;
	private ImageView morebg;

	/**
	 * 主页
	 */
	private View homepageView;

	/**
	 * 类别
	 */
	private View categoryView;

	/**
	 * 匹配（即万花筒）
	 */
	private View matchView;

	/**
	 * 搜索
	 */
	private View searchView;

	/**
	 * 更多
	 */
	private View moreView;

	/**
	 * 更多--管理
	 */
	private View more_managerView;

	/**
	 * acitivty Manager
	 */
	private LocalActivityManager manager;

	public static Timer timer;

	private RotateAnimation animation = null;

	private ImageView cursor;
	private TextView numView;
	private int bmpW;
	int screenW;
	public int offset = 0;// 动画图片偏移量
	public int initOffset = 0;
	Matrix matrix;
	int one, two, three, four, five;
	int currentitem;

	public static int bottomLogFlag=0;
	public static int topLogFlag=0;
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (dipValue * scale + 0.5f);
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置视图
		setContentView(R.layout.menutoolbar_tab_layout);
		// Debug.startMethodTracing("aipixiazai");

		// animation=AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_in);
		animation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(1000);
		ScreenManager.getScreenManager().pushActivity(this);// 压栈
		MyApplication.getInstance().mainActivity = this;

	}

	@Override
	protected void onStop() {
		// Debug.stopMethodTracing();
		super.onStop();
	}

	/**
	 * 初始化动画
	 */
	public void initImageView(int startPage) {

		cursor = (ImageView) this.findViewById(R.id.cursor);
		// bmpW = BitmapFactory.decodeResource(activity.getResources(),
		// R.drawable.a)
		// .getWidth();// 获取图片宽度 120

		bmpW = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.head_).getWidth();// 获取图片宽度 120

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;// 获取分辨率宽度 480
		Log.i("info999", "bmpW=" + bmpW + "=screenW=" + screenW + "=startPage="
				+ startPage);
		offset = (screenW / 5 - bmpW) / 2;// 计算偏移量 20

		if (startPage == 0) {
			initOffset = offset;
		} else {
			initOffset = getInitOffset(startPage);

		}
		matrix = new Matrix();
		matrix.postTranslate(initOffset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		one = 0;
		two = one + offset * 2 + bmpW;
		three = two + offset * 2 + bmpW;
		four = three + offset * 2 + bmpW;
		five = four + offset * 2 + bmpW;
	}

	private int getInitOffset(int start) {
		if (screenW == 320 || screenW == 800) {
			return offset + 1 + (start - 1) * 3 + screenW / 5 * start;
		} else {
			return offset + screenW / 5 * start;
		}
	}

	@Override
	public void init() {
		manager = getLocalActivityManager();

		container = (LinearLayout) findViewById(R.id.body);
		firstClick = (RelativeLayout) findViewById(R.id.home);
		firstpage = (ImageView) findViewById(R.id.firstpage_img);
		firstpagebg = (ImageView) findViewById(R.id.firstpage_bg);

		categoryClick = (RelativeLayout) findViewById(R.id.category);
		category = (ImageView) findViewById(R.id.category_img);
		categorybg = (ImageView) findViewById(R.id.category_bg);

		rankingClick = (RelativeLayout) findViewById(R.id.ranking);
		ranking = (ImageView) findViewById(R.id.ranking_img);
		rankingbg = (ImageView) findViewById(R.id.ranking_bg);

		searchClick = (RelativeLayout) findViewById(R.id.search);
		search = (ImageView) findViewById(R.id.search_img);
		searchbg = (ImageView) findViewById(R.id.search_bg);

		moreClick = (RelativeLayout) findViewById(R.id.more);
		more = (ImageView) findViewById(R.id.more_img);
		morebg = (ImageView) findViewById(R.id.more_bg);

		firstpage.setImageResource(R.drawable.main_page_btn_1);
		firstpagebg.setVisibility(View.VISIBLE);
		numView = (TextView) this.findViewById(R.id.numView);
		// 默认显示首页
		unbindDrawables(container);
		if (homepageView == null) {
			homepageView = manager.startActivity(
					"firstpage",
					new Intent(MainActivity.this, HomePageMainActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();
		}
		container.addView(homepageView);

		// new Thread(new loadMainPage()).start();
		// 设置按键监听
		firstClick.setOnClickListener(firstpageListener);
		categoryClick.setOnClickListener(categoryListener);
		rankingClick.setOnClickListener(userListener);
		searchClick.setOnClickListener(searchListener);
		moreClick.setOnClickListener(moreListener);

		// 第一次使用当前版本弹出新功能体验
		int updateversion = SharedPreferencesControl.getInstance().getInt(
				"updateversion",
				com.appdear.client.commctrls.Common.UPDATE_VERSION, this);

		if (updateversion != Constants.VERSIONCODE) {
			// Intent intent = new Intent(this, NewUsersInfo.class);
			// startActivity(intent);
			Intent intent = new Intent();
			intent.putExtra("roll_cancel", "true");
			intent.setClass(this, NoviceGuideActivity.class);
			startActivity(intent);
			ServiceUtils.getAlertDialogForString(this, "提示", "是否要在桌面创建快捷方式？",
					new MessageHandler() {

						@Override
						public void messageHandlerOk() {
							// TODO Auto-generated method stub
							addShortcut();
							super.messageHandlerOk();

						}

						@Override
						public void messageHandlerCannel() {
							// TODO Auto-generated method stub
							super.messageHandlerCannel();
						}

					}, 0);
		}
		// 存DB
		if (updateversion != Constants.VERSIONCODE) {
			SharedPreferencesControl.getInstance().putInt("updateversion",
					Constants.VERSIONCODE,
					com.appdear.client.commctrls.Common.UPDATE_VERSION, this);
		}

	}

	private void addShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
		ComponentName comp = new ComponentName(this.getPackageName(),
				this.getPackageName() + ".SplashActivity");
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(comp);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new
		// Intent(this,SplashActivity.class));

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.logo);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		sendBroadcast(shortcut);
	}

	/**
	 * 主页按键处理
	 */
	public OnClickListener firstpageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// if(firstpagebg.getVisibility()==View.GONE){
			bottomLogFlag=0;
			topLogFlag=HomePageMainActivity.topFlag;
			currentType = SoftFormTags.MAIN_FORM;
			setFocus(currentType);
			unbindDrawables(container);
			// if (homepageView == null) {
			homepageView = manager.startActivity(
					"firstpage",
					new Intent(MainActivity.this, HomePageMainActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();

			// }

			if (MyApplication.getInstance().androidLevel > 10) {
				int i = HomePageMainActivity.pagerContolerVersion.currIndex;
				HomePageMainActivity.pagerContolerVersion
						.initImageView_version(i);
			}
			container.addView(homepageView);
		}
		// }
	};

	/**
	 * 类别按键处理
	 */
	public OnClickListener categoryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// if(categorybg.getVisibility()==View.GONE){
			bottomLogFlag=1;
			topLogFlag=CateGoryMainActivity.topFlag;
			currentType = SoftFormTags.CATEGORY_FORM;
			setFocus(currentType);

			unbindDrawables(container);
			// if (categoryView == null) {
			categoryView = manager.startActivity(
					"category",
					new Intent(MainActivity.this, CateGoryMainActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();
			// }
			if (MyApplication.getInstance().androidLevel > 10) {
				int i = CateGoryMainActivity.pagerContolerVersion.currIndex;
				CateGoryMainActivity.pagerContolerVersion
						.initImageView_version(i);
			}
			container.addView(categoryView);
			// }
		}
	};
	/**
	 * 万花筒按键处理
	 */
	public OnClickListener userListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// if(rankingbg.getVisibility()==View.GONE){
			bottomLogFlag=-1;
			topLogFlag=-1;
			currentType = SoftFormTags.USER_MAIN_CENTER;
			setFocus(currentType);
			unbindDrawables(container);
			userChildType = SoftFormTags.USER_MAIN_CENTER;
			// userMainRegster = manager.startActivity(
			// SoftFormTags.USER_LIST_CENTER + "",
			// new Intent(MainActivity.this,
			// MoreUserCenterMainActivity.class)
			// .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
			// .getDecorView();
			// container.removeAllViews();
			// container.addView(userMainRegster);

			matchView = manager.startActivity(
					SoftFormTags.USER_LIST_CENTER + "",
					// new Intent(MainActivity.this,
					// MatchMainActivity.class)
					// .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					// .getDecorView();

					// 备份还原的转向
					new Intent(MainActivity.this, ContactOperateActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();
			container.removeAllViews();

			/*
			 * if(MyApplication.getInstance().androidLevel>10) { int i=
			 * MatchMainActivity.pagerContolerVersion.currIndex;
			 * MatchMainActivity.pagerContolerVersion.initImageView_version(i);
			 * }
			 */
			container.addView(matchView);
		}
		// }
	};

	/**
	 * 搜索按键处理
	 */
	public OnClickListener searchListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			bottomLogFlag=2;
			topLogFlag=0;
			// if(searchbg.getVisibility()==View.GONE){
			if (MyApplication.getInstance().mView.containsKey("searchresult")) {
				currentType = SoftFormTags.SEARCH_RESULT;
				setFocus(currentType);
				unbindDrawables(container);
				searchView = MyApplication.getInstance().mView.get("searchresult");
				container.addView(searchView);
				if(MyApplication.getInstance().searchResultActivity!=null&&MyApplication.getInstance().searchResultActivity.adapter!=null)
				{				 
					MyApplication.getInstance().searchResultActivity.adapter.notifyDataSetChanged();
				}
				
				 
			} else {
				currentType = SoftFormTags.SEARCH_FORM;
				setFocus(currentType);
				unbindDrawables(container);
				searchView = manager.startActivity(
						"search",
						new Intent(MainActivity.this, SearchActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
						.getDecorView();
				searchView.clearFocus();
				container.addView(searchView);
				MyApplication.getInstance().mainActivity = MainActivity.this;
			}
		}
		// }
	};

	/**
	 * 更多按键处理
	 */
	public OnClickListener moreListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// if(morebg.getVisibility()==View.GONE){
			bottomLogFlag=4;
			topLogFlag=0;
			currentType = SoftFormTags.MORE_ITEM;
			setFocus(currentType);
			moreView = manager.startActivity(
					"more",
					new Intent(MainActivity.this, MoreManagerActivity.class)
							.putExtra("notificaiton", "false")
							.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
							.putExtra("from", "moreView")).getDecorView();
			unbindDrawables(container);
//			if (MyApplication.getInstance().androidLevel > 10) {
//				int i = MoreManagerActivity.pagerContolerVersion.currIndex;
//				MoreManagerActivity.pagerContolerVersion
//						.initImageView_version(i);
//			}
			container.addView(moreView);
			moreView.clearFocus();

		}
		// }
	};

	/**
	 * 设置焦点
	 * 
	 * @param index
	 */

	public void setFocus(int index) {
		// 主页
		if (index == SoftFormTags.MAIN_FORM) {
			firstpage.setVisibility(View.GONE);
			firstpagebg.setVisibility(View.VISIBLE);
			if (currentitem != 0) {
				startAnimationIt(currentitem, 0);
			}
			currentitem = 0;
			// if(animation!=null)
			// firstpagebg.startAnimation(animation);

		} else {
			firstpage.setVisibility(View.VISIBLE);
			firstpagebg.setVisibility(View.GONE);
			// firstpagebg.clearAnimation();
			// firstpagebg.setVisibility(View.GONE);
		}
		// 类别
		if (index == SoftFormTags.CATEGORY_FORM) {
			category.setVisibility(View.GONE);
			categorybg.setVisibility(View.VISIBLE);
			if (currentitem != 1) {
				startAnimationIt(currentitem, 1);
			}
			currentitem = 1;
			// if(animation!=null)
			// categorybg.startAnimation(animation);
		} else {
			categorybg.setVisibility(View.GONE);
			category.setVisibility(View.VISIBLE);
		}
		// 会员主界面
		if (index == SoftFormTags.USER_MAIN_CENTER) {
			ranking.setVisibility(View.GONE);
			rankingbg.setVisibility(View.VISIBLE);
			if (currentitem != 2) {
				startAnimationIt(currentitem, 2);
			}
			currentitem = 2;
			// if(animation!=null)
			// rankingbg.startAnimation(animation);
		} else {
			// ranking.setImageResource(R.drawable.ranking_page_btn_1);
			rankingbg.setVisibility(View.GONE);
			ranking.setVisibility(View.VISIBLE);
		}
		// 搜索
		if (index == SoftFormTags.SEARCH_FORM
				|| index == SoftFormTags.SEARCH_RESULT) {
			search.setVisibility(View.GONE);
			searchbg.setVisibility(View.VISIBLE);
			if (currentitem != 3) {
				startAnimationIt(currentitem, 3);
			}
			currentitem = 3;
			// if(animation!=null)
			// searchbg.startAnimation(animation);
		} else {
			searchbg.setVisibility(View.GONE);
			search.setVisibility(View.VISIBLE);
		}
		// 更多
		if (index == SoftFormTags.MORE_ITEM) {
			more.setVisibility(View.GONE);
			morebg.setVisibility(View.VISIBLE);
			if (currentitem != 4) {
				startAnimationIt(currentitem, 4);
			}
			currentitem = 4;
			// if(animation!=null)
			// morebg.startAnimation(animation);
			// more.setImageResource(R.drawable.more_page_btn_2);
		} else {
			morebg.setVisibility(View.GONE);
			more.setVisibility(View.VISIBLE);
		}
	}

	public void startAnimationIt(int start, int end) {
		int s = 0, e = 0;
		switch (start) {
		case 0:
			s = one;
			break;
		case 1:
			s = two;
			break;
		case 2:
			s = three;
			break;
		case 3:
			s = four;
			break;
		case 4:
			s = five;
			break;
		}
		switch (end) {
		case 0:
			e = one;
			break;
		case 1:
			e = two;
			break;
		case 2:
			e = three;
			break;
		case 3:
			e = four;
			break;
		case 4:
			e = five;
			break;
		}
		TranslateAnimation animation1 = new TranslateAnimation(s, e, 0, 0);
		animation1.setFillAfter(true);// True:图片停在动画结束位置
		animation1.setDuration(500);
		if (cursor != null)
			cursor.startAnimation(animation1);
	}

	/**
	 * 处理返回按键
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (currentType == SoftFormTags.SEARCH_FORM) {
				if (SearchActivity.iskeyShowFlag) {
					hideInputMode();
					SearchActivity.iskeyShowFlag = false;
					return true;
				} else {
					ServiceUtils.showExitDialog(this);
					return true;
				}
			} else if (currentType == SoftFormTags.SEARCH_RESULT) {
				hideInputMode();
				currentType = SoftFormTags.SEARCH_FORM;
				setFocus(currentType);
				unbindDrawables(container);
				searchView = manager.startActivity(
						"search",
						new Intent(MainActivity.this, SearchActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
						.getDecorView();
				searchView.clearFocus();
				container.addView(searchView);
				MyApplication.getInstance().mView.remove("searchresult");
				return true;
			} else if (currentType == SoftFormTags.USER_MAIN_CENTER) {
				// 当前界面在一键注册界面
				/*
				 * if (userChildType == SoftFormTags.CHILD_USER_REGIST) {
				 * ServiceUtils.showExitDialog(this); return true; }
				 */
				if (MoreUserLoginInActivity.iskeyShowFlag) {
					hideInputMode();
					MoreUserLoginInActivity.iskeyShowFlag = false;
					return true;
				} else {
					ServiceUtils.showExitDialog(this);
					return true;
				}

			} else {
				ServiceUtils.showExitDialog(this);
				return true;
			}
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
			// 屏蔽搜索键
			return true;
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideInputMode() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		View view = getCurrentFocus();
		if (view != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);// 隐藏软键盘
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Log.i("info000","intent");
		int flag = intent
				.getIntExtra(
						com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG,
						0);
		this.unbindDrawables(container);
		switch (flag) {
		case SoftFormTags.SEARCH_FORM:
			currentType = SoftFormTags.SEARCH_FORM;
			searchView = manager.startActivity(
					"search",
					new Intent(MainActivity.this, SearchActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
					.getDecorView();
			container.addView(searchView);
			searchView.clearFocus();
			searchResultChildType = -1;
			break;
		case 0:
			if (homepageView == null) {
				homepageView = manager.startActivity(
						"firstpage",
						new Intent(MainActivity.this,
								HomePageMainActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
						.getDecorView();
			}
			container.addView(homepageView);
			setFocus(SoftFormTags.MAIN_FORM);
			break;
		}
		if (flag == 0) {
			return;
		} else {
			setFocus(currentType);
		}
		super.onNewIntent(intent);
	}

	@Override
	public void initData() {
		AppContext.getInstance().info = (Updateinfo) getIntent()
				.getSerializableExtra("update");
		if (AppContext.getInstance().spreurl == null
				|| AppContext.getInstance().spreurl.equals("")) {
			AppContext.getInstance().spreurl = SharedPreferencesControl
					.getInstance().getString("spreurl",
							com.appdear.client.commctrls.Common.SETTINGS, this);
		}
		if (AppContext.getInstance().dpreurl == null
				|| AppContext.getInstance().dpreurl.equals("")) {
			AppContext.getInstance().dpreurl = SharedPreferencesControl
					.getInstance().getString("dpreurl",
							com.appdear.client.commctrls.Common.SETTINGS, this);
		}
		boolean isflag = SharedPreferencesControl.getInstance().getBoolean(
				"softUpdateTip", com.appdear.client.commctrls.Common.SETTINGS,
				this);
		// if(isflag){
		// new Thread(){
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// // timer=new Timer();
		// // timer.schedule(new TimeTask(),15000);
		// startService();
		// //startRecycledImageService();
		// }
		//
		// }.start();
		// }
		// if(flag==true){
		// flag=false;
		// if(SharedPreferencesControl.getInstance().getBoolean("issave",com.appdear.client.commctrls.Common.USERPASSWDXML,MainActivity.this)){
		// String
		// username=SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,MainActivity.this);
		// String
		// passwd=SharedPreferencesControl.getInstance().getString("passwd",com.appdear.client.commctrls.Common.USERPASSWDXML,MainActivity.this);
		// if(username!=null&&passwd!=null&&!username.equals("")&&!passwd.equals("")){
		// try {
		// result=ApiManager.userlogin(SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,MainActivity.this),SharedPreferencesControl.getInstance().getString("passwd",com.appdear.client.commctrls.Common.USERPASSWDXML,MainActivity.this));
		// } catch (ApiException e) {
		// e.printStackTrace();
		// return;
		// }
		// try{
		// SharedPreferencesControl.getInstance().putString("sessionid",result.token,com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// SharedPreferencesControl.getInstance().putString("userid",result.userid+"",com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// SharedPreferencesControl.getInstance().putString("nickname",result.nickname,com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// SharedPreferencesControl.getInstance().putString("username",SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,MainActivity.this),com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// }catch(Exception e){
		// return;
		// }
		// try {
		// result=ApiManager.userprofile(SharedPreferencesControl.getInstance().getString("userid",com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this),SharedPreferencesControl.getInstance().getString("sessionid",com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this));
		// } catch (ApiException e) {
		// e.printStackTrace();
		// return;
		// }
		// SharedPreferencesControl.getInstance().putString("account",String.valueOf(result.account),com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// SharedPreferencesControl.getInstance().putString("level",result.level.equals("")?"爱皮小将":result.level,com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// SharedPreferencesControl.getInstance().putString("point",String.valueOf(result.totalpoint),com.appdear.client.commctrls.Common.USERLOGIN_XML,MainActivity.this);
		// SharedPreferencesControl.getInstance().putString("gender",String.valueOf(result.userinfo.gender),com.appdear.client.commctrls.Common.USERLOGIN_XML,this);
		// super.initData();
		// return;
		// }
		// }
		// }
		// handler1.sendEmptyMessage(1);

	}

	@Override
	public void updateUI() {
		firstpage.setVisibility(View.GONE);
		firstpagebg.setVisibility(View.VISIBLE);
		initImageView(0);
		AppContext.getInstance().info = (Updateinfo) getIntent()
				.getSerializableExtra("update");
		if (AppContext.getInstance().info != null
				&& !AppContext.getInstance().info.equals("")) {
			Intent intent = new Intent(this, UpdateDialog.class);
			intent.putExtra("info", AppContext.getInstance().info);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		}
		super.updateUI();
	}

	private void startService() {
		Intent backgroundService = new Intent(MainActivity.this,
				AppdearService.class);
		backgroundService.putExtra("update", AppContext.getInstance().info);
		this.startService(backgroundService);
	}

	public void unbindDrawables(View view) {
		if (view instanceof LinearLayout) {
			for (int i = 0; i < ((LinearLayout) view).getChildCount(); i++) {
				unbindDrawables(((LinearLayout) view).getChildAt(i));
			}
			((LinearLayout) view).removeAllViews();
		}
		if (view instanceof RelativeLayout) {
			for (int i = 0; i < ((RelativeLayout) view).getChildCount(); i++) {
				unbindDrawables(((RelativeLayout) view).getChildAt(i));
			}
			((LinearLayout) view).removeAllViews();
		}
		if (view instanceof AsynLoadImageView) {
			((AsynLoadImageView) view).destroyDrawingCache();
		}
	}

	Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 102) {
				if (timer != null) {
					timer.cancel();
				}
			} else if (msg.what == 1) {
				unbindDrawables(container);
				if (homepageView == null) {
					homepageView = manager.startActivity(
							"firstpage",
							new Intent(MainActivity.this,
									HomePageMainActivity.class)
									.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
							.getDecorView();
				}
				container.addView(homepageView);
			}
		}

	};

	@Override
	protected void onDestroy() {
		AppContext.getInstance().restart = true;
		super.onDestroy();
	}

	public void updateNumView() { // 动画 R.anim.myanimation_simple
									// my_scale_action alpha_scale
		numView.setVisibility(View.VISIBLE);
		int elideupdate = MyApplication.getInstance().elideupdate;
		Animation animation = null;
		if (elideupdate <= 0) {
			numView.setVisibility(View.GONE);
		} else if (elideupdate > 9) {
			numView.setVisibility(View.VISIBLE);
			numView.setText("" + MyApplication.getInstance().elideupdate);
			animation = AnimationUtils.loadAnimation(this,
					R.anim.my_scale_action);
			numView.startAnimation(animation);

		} else {
			numView.setVisibility(View.VISIBLE);
			numView.setText("" + MyApplication.getInstance().elideupdate);
			animation = AnimationUtils.loadAnimation(this,
					R.anim.my_scale_action);
			numView.startAnimation(animation);
		}

		/*
		 * numView.setText(""+MyApplication.getInstance().elideupdate); else
		 * if(MyApplication.getInstance().elideupdate>0)
		 * numView.setText(" "+MyApplication.getInstance().elideupdate);
		 */

	}
}
