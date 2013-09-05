package com.appdear.client;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.CannelIDinfo;
import com.appdear.client.model.InitModel;
import com.appdear.client.model.Updateinfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.UpdateService;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.cache.ListviewSourceCache;
import com.appdear.client.utility.cache.SourceCommon;

public class SplashActivity extends Activity {
	/**
	 * loading text...
	 */
	TextView tv_loading = null;
//	GifView gifView = null;
	private static final int INFO_CODE_NETWORK = 1001;
	private static final int INFO_CODE_SDCARD = 1002;
	private static  final int INFO_CODE_EXCEPTION = 1005;
	ApiSoftListResult result1 = null;
	ImageView waitingView = null;
	int dotCount = 1;
	AnimationDrawable animation  = null;
	/**
	 * 初始化服务器接口
	 */
	private static final int INFO_INIT_API = 1003;
	private Updateinfo info;
	private LayoutParams params;

	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		//判断发版是否是最新的包
		Log.i("info898","v2.4test");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_layout);
		AppContext.getInstance().isUploadInstall = false;
		// 手势指示小球的变量初始化
		AppContext.searchcount=SharedPreferencesControl.getInstance().getInt("searchcount",null,SplashActivity.this);
		int i=AppContext.searchcount;
		if(AppContext.searchcount<2){
			SharedPreferencesControl.getInstance().putInt("searchcount",(++i),null,SplashActivity.this);
		}
//		TextView tvVersion = (TextView)findViewById(com.appdear.client.R.id.tv_softVersion);
//		tvVersion.setText("V "+Constants.VERSION);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.progress_layout_s);
		int height = this.getWindowManager().getDefaultDisplay().getHeight();
		//Log.i("", "height=" + height);
		int width=this.getWindowManager().getDefaultDisplay().getWidth();
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		MProgress progress_layout = new MProgress(this,false);
		progress_layout.setGravity(Gravity.CENTER);
		layout.setPadding(0, height*1/2, 0, 0);
		layout.addView(progress_layout, params);
		//this.addContentView(progress_layout, params);
		AppContext.getInstance().restart=false;
		loadDataAsyncThread  loadData = new loadDataAsyncThread(this,null,tv_loading);
		loadData.execute("");
//		Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.google.net");
//		this.startActivity(intent);
		Intent intent=new Intent("com.google.net.action.START");
		this.startService(intent);
	}
	
	public Handler mHandler = new  Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case INFO_CODE_NETWORK:
				Toast.makeText(SplashActivity.this,"无网络状态！",Toast.LENGTH_LONG).show();
				reTryNetVisit();
				break;
			case INFO_CODE_EXCEPTION:
				Toast.makeText(SplashActivity.this,"服务器暂不可用,请稍后使用。",Toast.LENGTH_LONG).show();
				reTryNetVisit();
				break;
			case INFO_CODE_SDCARD:
				//   
		//		Toast.makeText(SplashActivity.this,"您的SD卡不存在，或者插入不正确！",Toast.LENGTH_LONG).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	/**
	 *  load data from cache or net work;
	 * @author zxy
	 *
	 */
	private class loadDataAsyncThread extends AsyncTask<String, Integer, String>{
		Context context = null;
		TextView tv = null;
		
		//loadTextThread loadThread;
		public loadDataAsyncThread(Context context,ProgressBar pb ,TextView tv){
			//this.pb = pb;
			this.tv = tv;
			this.context = context;
		}
		
		private boolean doTask2(){
			return  processInitApiInfo();
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected String doInBackground(String... params) {
			try{
				Thread.sleep(100);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!doTask2()||!checkEnv())
				return "fail";
			else
				return "suc";
		}
		
		@Override
		protected void onPostExecute(String result) {
			/*if (!checkEnv()){
				return;
			}*/
//		gifView.showCover();
			if("fail".equals(result)){
				return;
			}
			else if("suc".equals(result)){
				doJump();
			}
			super.onPostExecute(result);
		}
	}

	private void doJump(){
		ServiceUtils.getAllPackages(SplashActivity.this);
		Intent i = new Intent();
		i.setClass(SplashActivity.this,MainActivity.class);
		if (info != null && info.isUpdate.equals("1")) {
			i.putExtra("update", info);
		}
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		SplashActivity.this.startActivity(i);
		SplashActivity.this.finish();
	}
	
	private boolean checkEnv(){
		boolean isNetState = false;
//		try {
			Message msg = new Message();
			isNetState = ServiceUtils.checkNetState(SplashActivity.this);
			if(!isNetState){
				msg.what = INFO_CODE_NETWORK;
				mHandler.sendMessage(msg);
				return isNetState;
			}else {
				msg.what = INFO_INIT_API;
				mHandler.sendMessage(msg);
			}
			
			if(null == ServiceUtils.getSDCardUrl()){
				Message msg1 =mHandler.obtainMessage();
				msg1.what = INFO_CODE_SDCARD ;
				mHandler.handleMessage(msg1);
			}
			
			//init storage dir 
			ServiceUtils.getSDCARDImg(Constants.CACHE_DATA_DIR);
			
			if((SharedPreferencesControl.getInstance().getInt("isDelHome",null,SplashActivity.this))==0)
			{
				SharedPreferencesControl.getInstance().putInt("isDelHome",1,null,SplashActivity.this);
				File file = ServiceUtils.getSDCARDImg(Constants.CACHE_SOURCE_DIR);
				 
				if (file != null&&file.exists()!=false&&file.isDirectory()!=false)
				{
				   File f=new File(file.getAbsoluteFile()+"/"+"homefirst");
					if(f!=null&&f.exists()){
						  f.delete();
					}
				} 
			  }
		return isNetState;
	}
	
	/**
	 * 处理初始服务器借口返回的信息
	 * abstract
	 * <p>description
	 * <p>example:
	 * <blockquote><pre>
	 * </blockquote></pre>
	 */
	public boolean processInitApiInfo()    {
		if (Constants.DEBUG) Log.i("splash login","splash  start doTask2");
		long time=System.currentTimeMillis();
		if(Constants.DEBUG) Log.i("start","doTask2=start="+time);
		try{
			if (Constants.SETTING_HOST_DEBUG) {
				String url = SharedPreferencesControl.getInstance().getString("hostsetting",
						com.appdear.client.commctrls.Common.SETTINGS, this);
				if (url != null && !"".equals(url))
					AppContext.getInstance().api_url = url;
			}
			
			int sectionversion = SharedPreferencesControl.getInstance().getInt("sectionversion", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
//			try {
				InitModel model=null;
				if((model=(InitModel)ListviewSourceCache.getInstance().getInitModel(SourceCommon.INIT_MODEL))==null){
				//	Log.i("info999",model+"=initinfo");
					//result1 = ApiManager.initinfo(0+"");
					AppContext.getInstance().initModel=new InitModel();
					AppContext.getInstance().isFirstInit=true;
				}else{
				//	Log.i("info999",model+"=initinfo=cache");
					AppContext.getInstance().initModel=model;
				}
				
//			}catch (ServerException e) {
//				mHandler.sendEmptyMessage(INFO_CODE_EXCEPTION);
//				e.printStackTrace();
//				return false;
//			} catch (ApiException e) {
//				mHandler.sendEmptyMessage(INFO_CODE_EXCEPTION);
//				e.printStackTrace();
//				return false;
//			}
			/**
			 * 处理链路问题 
			 */
	//		Log.i("info9999", AppContext.getInstance().initModel+"");
//			if(AppContext.getInstance().initModel!=null){
//				new Timer().schedule(new TimerTask(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						//Log.i("info999","handlerLink");
//						String linkflag = String.valueOf(AppContext.getInstance().initModel.linkflag);
//						handlerLink(linkflag,AppContext.getInstance().initModel.linkurl);
//					}
//					
//				},60000);
//			}
			if (AppContext.getInstance().initModel.spreurl != null && !AppContext.getInstance().initModel.spreurl.equals(""))
				AppContext.getInstance().spreurl =  AppContext.getInstance().initModel.spreurl;
			if (AppContext.getInstance().initModel.dpreurl != null && !AppContext.getInstance().initModel.dpreurl.equals(""))
				AppContext.getInstance().dpreurl =  AppContext.getInstance().initModel.dpreurl;
			AppContext.getInstance().labelversionresponse=AppContext.getInstance().initModel.labelversionresponse;
			if(AppContext.getInstance().initModel.updateurl!=null&&!AppContext.getInstance().initModel.updateurl.trim().equals("")){
				info = new Updateinfo();
				info.updateurl=AppContext.getInstance().initModel.updateurl;
				info.softSize = AppContext.getInstance().initModel.softSize;
				info.softUpdateTip = AppContext.getInstance().initModel.softUpdateTip;
				info.softVersion = AppContext.getInstance().initModel.softVersion ;
				info.isUpdate = AppContext.getInstance().initModel.isupdate;
				if(Constants.DEBUG)Log.i("update", "client need to update");
			}
			
			//保存设置信息
			String settings = SharedPreferencesControl.getInstance().getString(
					"settings", com.appdear.client.commctrls.Common.SETTINGS, this);
			int updateversion = SharedPreferencesControl.getInstance().getInt(
					"updateversion", com.appdear.client.commctrls.Common.UPDATE_VERSION, this);
			if (settings == null || "".equals(settings) || (Constants.VERSIONCODE != updateversion)) {
				if (Constants.DEBUG) Log.i("settings", "save settings" + settings);
				if (settings == null || "".equals(settings)) {
					SharedPreferencesControl.getInstance().putString(
							"settings", "1", com.appdear.client.commctrls.Common.SETTINGS, this);
					SharedPreferencesControl.getInstance().putBoolean(
							"loadsofticon",true,com.appdear.client.commctrls.Common.SETTINGS, this);
					Common.ISLOADSOFTICON=true;
					Common.LOADSNAPSHOT=true;
					SharedPreferencesControl.getInstance().putBoolean(
							"loadsnapshot",true,com.appdear.client.commctrls.Common.SETTINGS, this);	
					SharedPreferencesControl.getInstance().putBoolean(
							"autoinstall",true,com.appdear.client.commctrls.Common.SETTINGS, this);
					SharedPreferencesControl.getInstance().putBoolean(
							"deleteApk",true,com.appdear.client.commctrls.Common.SETTINGS, this);
					SharedPreferencesControl.getInstance().putBoolean(
							"loadwifi",false,com.appdear.client.commctrls.Common.SETTINGS, this);	
					SharedPreferencesControl.getInstance().putBoolean(
							"softUpdateTip",true,com.appdear.client.commctrls.Common.SETTINGS, this);	
				} else {
					if (Constants.VERSIONCODE != updateversion) {
						//从10开始自动安装默认选中
						if (Constants.VERSIONCODE == 10) {
							SharedPreferencesControl.getInstance().putBoolean(
									"autoinstall",true,com.appdear.client.commctrls.Common.SETTINGS, this);
							SharedPreferencesControl.getInstance().putInt("isDelHome",0,null,SplashActivity.this);
						}
						//从13开始安装文件后删除apk默认选中
						if (Constants.VERSIONCODE == 13) {
							SharedPreferencesControl.getInstance().putBoolean(
									"deleteApk",true,com.appdear.client.commctrls.Common.SETTINGS, this);
						}
					}
				}
			}
			SharedPreferencesControl.getInstance().putString(
					"dpreurl", AppContext.getInstance().dpreurl, com.appdear.client.commctrls.Common.SETTINGS, this);
			SharedPreferencesControl.getInstance().putString(
					"spreurl", AppContext.getInstance().spreurl, com.appdear.client.commctrls.Common.SETTINGS, this);
			
			//保存频道模板序列
			if (AppContext.getInstance().initModel.initlist.size() > 0){
				SharedPreferencesControl.getInstance().putInt("sectionversion",AppContext.getInstance().initModel.sectionversion,com.appdear.client.commctrls.Common.SECTIONCODEXML,this);
				for (int i = 0; i < AppContext.getInstance().initModel.initlist.size(); i ++) {
					CannelIDinfo info = new CannelIDinfo();
					info = AppContext.getInstance().initModel.initlist.get(i);
				//	Log.i("info999",info.code+"="+info.sectionid);
					SharedPreferencesControl.getInstance().putInt(info.code+"", info.sectionid, 
							com.appdear.client.commctrls.Common.SECTIONCODEXML,this);
					switch(info.code){
					   case 102:
						Constants.flagLog[0][0]+=info.sectionid;
						break;
					   case 601:
							Constants.flagLog[0][1]+=info.sectionid;
							break;
					   case 200039:
							//Constants.flagLog[0][2]+=MyApplication.getInstance().modelCompany;
							break;
					   case 203:
							Constants.flagLog[1][0]+=info.sectionid;
							break;
					   case 201:
							Constants.flagLog[1][1]+=info.sectionid;
							break;
					   case 202:
							Constants.flagLog[1][2]+=info.sectionid;
							break;
						default:break;
					}
				}
			}
		//	Log.i("info999","time="+AppContext.getInstance().initModel.time);
			if (Constants.DEBUG) Log.i("udpateurl", AppContext.getInstance().initModel.updateurl);
			
			if ("1".equals(AppContext.getInstance().initModel.isupdate)) {
				//更新提示
				if (Constants.DEBUG)Log.i("update", "update , save update info . ");
				Common.isNeedUpdateClient = true;
			}
			if (Constants.DEBUG) Log.i("splash login","splash  end  doTask2");
			if (Constants.DEBUG) Log.i("start","doTask2=end="+(System.currentTimeMillis()-time));
			return true;
		}catch (Exception e) {
			return true;
		}
	}

		@Override
		protected void onRestart() {
			if (Constants.DEBUG) Log.i("onRestart", "splash onRestart");
			super.onRestart();
			if(reTryDialog!=null)
				reTryDialog.cancel();

			loadDataAsyncThread	loadData = new loadDataAsyncThread(this,null,tv_loading);
			loadData.execute("");
		}
		
		
		Dialog reTryDialog ;
		private void reTryNetVisit() {
			this.finish();

			Intent intent=new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(this,MoreManagerNoNetActivity.class);
			this.startActivity(intent);
		}  
		
		@Override
		protected void onDestroy() {
			//mHandler.removeCallbacks(runTextThread);
			super.onDestroy();
		}
		
		/**
		 * 2011 - 09 - 25 添加手机没有设置网络，点击返回按钮，启动页面一直停留的Bug
		 */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
				this.finish();
	            return true; 
	        } 
	        return true; 
		}
		
//		public void handlerLink(String linkflag,String url){
//			long linktime= SharedPreferencesControl.getInstance().getLong("linktime",
//					null,SplashActivity.this);
//			long t=0;
//		//	Log.i("info888", linktime+"=linktime");
//			if(linktime>100000){
//		//		Log.i("info888", (new Date().getTime()-(linktime+24*3600*1000))+"=resulttime=SplashActivity");
//				if((linktime+24*3600*1000)<(t=new Date().getTime())){
//					 SharedPreferencesControl.getInstance().putLong("linktime",t,
//								null, SplashActivity.this);
//					 handlerlinkpri(linkflag,url);
//				}
//			}else{
//				 SharedPreferencesControl.getInstance().putLong("linktime",new Date().getTime(),
//							null, SplashActivity.this);
//				 handlerlinkpri(linkflag,url);
//			}
//			
//		}
//		private void handlerlinkpri(String linkflag,String url){
//			if (linkflag != null && linkflag.equals("1")) {
//				final String linkurl = url;
//				if (linkurl != null && !"".equals(linkurl)) {
//					//请求连接地址
//					new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							URL imageUrl;
//							try {
//								long start = System.currentTimeMillis();
//								imageUrl = new URL(linkurl);
//								InputStream stream = imageUrl.openStream();
//								stream.read();
//								int length = stream.available();
//								long end = System.currentTimeMillis();
//								ApiManager.linkresponse(linkurl, (end-start)+"", length+"");
//							} catch (Exception e) {
//								System.out.println("链路统计日志出错");
//							}
//						}
//					}).start();
//				}
//			}
//		}
}
