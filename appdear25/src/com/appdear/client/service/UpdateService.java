package com.appdear.client.service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.appdear.client.SplashActivity;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.InitModel;
import com.appdear.client.model.Updateinfo;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.cache.ListviewSourceCache;
import com.appdear.client.utility.cache.SourceCommon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//import android.util.Log;
import android.util.Log;


public class UpdateService  extends Service {
	public static  Timer timer;
	//最后一个退出客户端的时间
	public static long UPDATE_SERVICE_STARTTIME = 0;
	public static long updatetime=60000*60*24*3;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		//获取配置文件中保存的最后一次退出爱皮的时间
		UPDATE_SERVICE_STARTTIME = SharedPreferencesControl.getInstance().getLong("updatetime",
				null, UpdateService.this);
		//如果等于0设置当前时间
		if (UPDATE_SERVICE_STARTTIME == 0) {
			UPDATE_SERVICE_STARTTIME = System.currentTimeMillis();
			 SharedPreferencesControl.getInstance().putLong("updatetime", System.currentTimeMillis(),
						null, UpdateService.this);
		}
		//如果当前时间比最后一次退出时间多7天
		if ((System.currentTimeMillis() - UPDATE_SERVICE_STARTTIME) >= updatetime) {
		//	new TimeTask().run();
			new UpdateInfo().start();
		}
		long t=0;
		long linktime= SharedPreferencesControl.getInstance().getLong("linktime1",
				null,this);
		if(linktime>100000){
			if((linktime+24*3600*1000)<(t=new Date().getTime())){
				 SharedPreferencesControl.getInstance().putLong("linktime1",t,
							null,this);
				 
				 handlerlinkpri("1","1");
			}
		}else{
			 
			 SharedPreferencesControl.getInstance().putLong("linktime1",new Date().getTime(),
						null,this);
			 handlerlinkpri("1","1");
		}
		
		super.onStart(intent, startId);
	}
	
//	class TimeTask extends TimerTask{
//		@Override
//		public void run() {
//			ApiSoftListResult result1=null;
//			InitModel model=null;
//			try {
//				if((model=(InitModel)ListviewSourceCache.getInstance().getInitModel(SourceCommon.INIT_MODEL))==null){
//					result1 = ApiManager.initinfo(0+"");
//					AppContext.getInstance().isFirstInit=true;
//				}else{
//					AppContext.getInstance().initModel=model;
//				}
//			}catch (ServerException e) {
//				e.printStackTrace();
//			} catch (ApiException e) {
//				e.printStackTrace();
//			}
//			if(AppContext.getInstance().initModel!=null&&AppContext.getInstance().initModel.updateurl!=null&&!AppContext.getInstance().initModel.updateurl.trim().equals("")){
//				Updateinfo	info = new Updateinfo();
//				info.updateurl=AppContext.getInstance().initModel.updateurl;
//				info.softSize = AppContext.getInstance().initModel.softSize;
//				info.softUpdateTip = AppContext.getInstance().initModel.softUpdateTip;
//				info.softVersion = AppContext.getInstance().initModel.softVersion ;
//				info.isUpdate = AppContext.getInstance().initModel.isupdate;
//				SharedPreferencesControl.getInstance().putString(
//						"linkflag",String.valueOf(AppContext.getInstance().initModel.linkflag), com.appdear.client.commctrls.Common.SETTINGS, UpdateService.this);
//				SharedPreferencesControl.getInstance().putString(
//						"linkurl",AppContext.getInstance().initModel.linkurl, com.appdear.client.commctrls.Common.SETTINGS, UpdateService.this);
//				AppContext.getInstance().info=info;
//			}
//			
////			String linkflag = String.valueOf(AppContext.getInstance().initModel.linkflag);
////			if (linkflag != null && linkflag.equals("1")) {
////				final String linkurl = AppContext.getInstance().initModel.linkurl;
////				if (linkurl != null && !"".equals(linkurl)) {
////					//请求连接地址
////					new Thread(new Runnable() {
////						
////						@Override
////						public void run() {
////							URL imageUrl;
////							try {
////								long start = System.currentTimeMillis();
////								imageUrl = new URL(linkurl);
////								InputStream stream = imageUrl.openStream();
////								stream.read();
////								int length = stream.available();
////								long end = System.currentTimeMillis();
////								ApiManager.linkresponse(linkurl, (end-start)+"", length+"");
////							} catch (Exception e) {
////								e.printStackTrace();
////								System.out.println("链路统计日志出错");
////							}
////						}
////					}).start();
////				}
////			}
//		}
//		
//	}
	
	class UpdateInfo extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ApiSoftListResult result1=null;
			InitModel model=null;
			try {
					result1=ApiManager.updateclient(0+"");
					if(result1!=null&&result1.updateurl!=null&&!result1.updateurl.trim().equals("")){
						Updateinfo	info = new Updateinfo();
						info.updateurl=result1.updateurl;
						info.softSize = result1.softSize;
						info.softUpdateTip = result1.softUpdateTip;
						info.softVersion = result1.softVersion ;
						info.isUpdate = result1.isupdate;
						AppContext.getInstance().info=info;
					}
					startService();
			} catch (ApiException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("info1111",  e.getMessage());
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("info1111",  e.getMessage());
			}									
		}
		private void startService(){
			Intent  backgroundService = new Intent(UpdateService.this,AppdearService.class);
			UpdateService.this.startService(backgroundService);
			//每个3天提示一次
			SharedPreferencesControl.getInstance().putLong("updatetime",new java.util.Date().getTime(),
						null, UpdateService.this);
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		super.onDestroy();
	}
	
	private void handlerlinkpri(String linkflag,String url){
	//	Log.i("info888","linkflag="+linkflag+"=");
		if (linkflag != null && linkflag.equals("1")) {
			final String linkurl = url;
			String u=null;
			if(!(u=SharedPreferencesControl.getInstance().getString(
						"dpreurl", com.appdear.client.commctrls.Common.SETTINGS, this)).equals("")){
						 AppContext.getInstance().dpreurl=u;
			}
	//		Log.i("info888","linkurl="+linkurl+"=");
			if (linkurl != null && !"".equals(linkurl)) {
				//请求连接地址
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						URL imageUrl;
						try {
//							long start = System.currentTimeMillis();
//							imageUrl = new URL(linkurl);
//							InputStream stream = imageUrl.openStream();
//							stream.read();
//							int length = stream.available();
//							long end = System.currentTimeMillis();
//							Log.i("info888","linkresponse");
		//					Log.i("info999","linkresponse");
							if(AppContext.getInstance().dpreurl==null||"".equals(AppContext.getInstance().dpreurl)){
								InitModel model=null;
								ApiSoftListResult result1=null;
								//try {
									if((model=(InitModel)ListviewSourceCache.getInstance().getInitModel(SourceCommon.INIT_MODEL))==null){
										//result1 = ApiManager.initinfo(0+"");
										AppContext.getInstance().initModel=new InitModel();
										AppContext.getInstance().isFirstInit=true;
									}else{
										AppContext.getInstance().initModel=model;
									}
//								}catch (ServerException e) {
//									e.printStackTrace();
//								} catch (ApiException e) {
//									e.printStackTrace();
//								}
								AppContext.getInstance().dpreurl=AppContext.getInstance().initModel.dpreurl;
							}
							ApiManager.linkresponse(linkurl, 0+"", 0+"");
						} catch (Exception e) {
							System.out.println("链路统计日志出错");
						}
					}
				}).start();
			}
		}
	}
}
