/**
 * TheBestListView.java
 * created at:2011-5-20锟斤拷锟斤拷01:19:33
 * 
 * Copyright (c) 2011, 锟斤拷锟斤拷锟斤拷皮锟狡硷拷锟斤拷锟睫癸拷司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import test.WangUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.appdear.bdmap.Location;
import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.AdView;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.MpointView;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.HomeObj;
import com.appdear.client.model.InitModel;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.AppdearService;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.service.api.ApiUserResult;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.cache.ListviewSourceCache;
import com.appdear.client.utility.cache.SourceCommon;

/** 
 * 锟斤拷品锟狡硷拷list
 * 
 * @author zqm
 */
public class HomePageBestListActivity extends ListBaseActivity {

	public static boolean flag=true;
	private ApiUserResult result1=null;
	
	/**
	 * 锟叫憋拷锟斤拷锟�
	 */
	private ApiSoftListResult result;
	
	/**
	 *  List<SoftlistInfo>
	 */
	private List<SoftlistInfo> listData = null ;
	
	/**
	 * 锟斤拷目id
	 */
	private int id = 100002;
	
	/**
	 * 锟斤拷娌硷拷锟斤拷锟绞�
	 */
	private View mView;
	
	/**
	 * 锟斤拷锟截硷拷
	 */
	private AdView adView;
	
	/**
	 * 锟斤拷锟絬rl
	 */
	private List<SoftlistInfo> urls;
	
	/**
	 * 锟斤拷锟斤拷远锟斤拷锟揭�
	 */
	private ViewFlipper flipper;
	
	/**
	 * 锟斤拷锟斤拷锟捷凤拷锟斤拷
	 */
	private ApiSoftListResult adresult;
	
	 
	
	private String asids=null;
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_list_layout);
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
			loadingView=new MProgress(this,true);
		this.addContentView(loadingView, params);
		if (!AppContext.getInstance().isNetState) {
			handler1.sendEmptyMessage(LOADG);
			showRefreshButton();
			return;
		}
	}
	
	@Override
	public void init() {
		
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setFadingEdgeLength(0);
		listView.setVerticalFadingEdgeEnabled(false);
		listView.setHorizontalFadingEdgeEnabled(false);
//		listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
//		listView.setDividerHeight(2);
		
		mView = getLayoutInflater().inflate(R.layout.adver, null);
		DisplayMetrics  metrics = ServiceUtils.getMetrics(this.getWindowManager());
    	

		flipper = (ViewFlipper) mView.findViewById(R.id.mViewFlipper);
		
		//锟斤拷始锟斤拷锟斤拷页锟斤拷锟�
		MpointView pointView = (MpointView) mView.findViewById(R.id.mMpointView);
		int hegiht=metrics.widthPixels;
		if(hegiht==0){
			hegiht=(int)(100*metrics.density+0.5f);
			adView = new AdView(this,hegiht);
		}else{
			adView = new AdView(this,hegiht/3);
		}
		//adView.setPointView(pointView);
		adView.setFlipper(flipper);
	}
	
	@Override
	public void initData() {
		try {
			 
 			
			//锟叫憋拷锟斤拷莼锟饺�
			 //102
			id = SharedPreferencesControl.getInstance().getInt("102", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			int totalcount=0;
			//result = ApiManager.softlist(id+"", page + "", PAGE_SIZE + "");
			result=WangUtil.getSoftList();
			
			page ++;
			listData = result.softList;
			totalcount=result.totalcount;
		 
			ServiceUtils.setSoftState(this,listData);
			adapter = new SoftwarelistAdatper(this, listData, listView);
		
			PAGE_TOTAL_SIZE = totalcount%PAGE_SIZE==0?
					totalcount/PAGE_SIZE:totalcount/PAGE_SIZE+1;
			
			if (page <= PAGE_TOTAL_SIZE)
				if(listView != null) listView.setRefreshDataListener(this);
			
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			if (Constants.DEBUG)
				Log.e("net error:",e.getMessage(), e);
			showException(e);
			showRefreshButton();
		}finally{
		//	Log.i("info90","LOADG=HomePageBestListActivity");
			handler1.sendEmptyMessage(LOADG);
		}	
		super.initData();
	}

	/* (non-Javadoc)
	 * @see com.appdear.client.commctrls.BaseActivity#updateUI()
	 */
	@Override
	public void updateUI(){
		SharedPreferencesControl.getInstance().putLong("starttime",new Date().getTime(),
				null,this);
		if(listView==null)return;
		//锟斤拷锟斤拷前时锟斤拷娲�
		if (listView.getAdapter() == null) {
			//锟斤拷庸锟斤拷
			adView.setDefaultView();
			listView.removeHeaderview();
			listView.addHeaderView(mView);
		}
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg1.getId() == mView.getId()) {
					//锟斤拷锟斤拷锟斤拷
					if (adView != null) {
						if(urls==null)return;
						SoftlistInfo info = (SoftlistInfo) urls.get(adView.getnPost());
						if(info.adtype.equals("3")) {
							Intent intent = new Intent(HomePageBestListActivity.this, SoftwareMainDetilActivity.class);
			 				//锟斤拷锟斤拷锟絘ctivity
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("softid", info.softid);
							intent.putExtra("type", "softid");
							intent.putExtra("downloadcount",info.download);
							intent.putExtra("ad", true);
							startActivity(intent);
						} else if(info.adtype.equals("4")){
							Intent categorydetail = new Intent(HomePageBestListActivity.this,
									CategorySubjectDetailsActivity.class);
							categorydetail.putExtra("category_title", info.adtitle);
							categorydetail.putExtra("category_id", info.softid+"");
							categorydetail.putExtra("category_navigation", info.adtitle);
							startActivity(categorydetail);
						}
					}
				} else {
//					if (urls == null || urls.size() == 0)
//						setSelectedValues(arg2);
//					else
						setSelectedValues(arg2 - 1);
				}
			}
		});
		new BannerlistThread(id).start();
	}
	Handler bhandler=new Handler();
	class BannerlistThread extends Thread
	{
		private  int id1=0;
		public BannerlistThread(int id){
			this.id1=id;
		}
		@Override
		public void run() {
		
 			if (AppContext.getInstance().spreurl == null || AppContext.getInstance().spreurl.equals("")) {
    			AppContext.getInstance().spreurl = SharedPreferencesControl.getInstance().getString(
    					"spreurl", com.appdear.client.commctrls.Common.SETTINGS, HomePageBestListActivity.this);
    		}
    		if (AppContext.getInstance().dpreurl == null || AppContext.getInstance().dpreurl.equals("")) {
    			AppContext.getInstance().dpreurl = SharedPreferencesControl.getInstance().getString(
    					"dpreurl", com.appdear.client.commctrls.Common.SETTINGS, HomePageBestListActivity.this);
    		}
    		int id = SharedPreferencesControl.getInstance().getInt("104", com.appdear.client.commctrls.Common.SECTIONCODEXML, HomePageBestListActivity.this);
			try {
				//adresult = ApiManager.bannerlist(id + "", "0", "1", "20");
				adresult=WangUtil.getAd();
			} catch (Exception e) {
				 
				e.printStackTrace();
			}
			if (adresult != null && adresult.softList != null) {
				bhandler.post(new Runnable(){

					@Override
					public void run() {
 						if(listView==null)return;
							//锟斤拷庸锟斤拷
							if (adresult != null && adresult.softList != null) {
								urls = adresult.softList;
								List<String> urllist = new ArrayList<String>();
								for (SoftlistInfo info : adresult.softList) {
									System.out.println("ad imgurl="+info.imgurl);
									urllist.add(info.imgurl);
								}
								adView.setUrls(urllist, R.drawable.ad_src);
							}
							
							if (urls != null && urls.size() != 0) {
								//listView.removeHeaderview();
								//listView.addHeaderView(mView);
								//flipper.setFlipInterval(3000);
								flipper.startFlipping();
							}
					}
					
				});
			}
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟绞撅拷锟斤拷锟�
			boolean isflag=SharedPreferencesControl.getInstance().getBoolean(
					"softUpdateTip",com.appdear.client.commctrls.Common.SETTINGS, HomePageBestListActivity.this);	
			if(isflag){
				new Thread(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
//						timer=new Timer();
//						timer.schedule(new TimeTask(),15000);
						startService();
						//startRecycledImageService();
					}
					
				}.start();
			}
                // 锟介看锟角凤拷皮锟斤拷
			new Timer().schedule(new MyTimeTask(id),5000);
	 
			if(ServiceUtils.isWiFiActive(HomePageBestListActivity.this)){
				//Location.getWifiLocation(HomePageBestListActivity.this);
			}else{
				//Location.getLocations(HomePageBestListActivity.this);
			}
			
			try{
				boolean isNotice=false;
				String phonedate=SharedPreferencesControl.getInstance().getString("phonedate",null,HomePageBestListActivity.this);
				if(phonedate==null ||"".equals(phonedate))
				{
					if(MyApplication.getInstance().isLoadBuyTime==false)
			        {
						if(phonedate==null ||"".equals(phonedate))
						{
							try
							{
								ApiNormolResult  result=ApiManager.getwarranty() ;
								if(result!=null&&result.isok!=1&&result.buydate!=null&&!"".equals(result.buydate)){				
									phonedate=result.buydate;
									SharedPreferencesControl.getInstance().putString("phonedate",result.buydate,null,HomePageBestListActivity.this);						
									isNotice=true;
								}else
								{
									isNotice=false;
								}
							} catch (Exception e)
							{
								isNotice=false;
								e.printStackTrace();
							} 
						}
						MyApplication.getInstance().isLoadBuyTime=true;
			        }else
			        {
			        	isNotice=true;
			        }
				}else
				{
					isNotice=true;
				}
				if(isNotice&&phonedate!=null&&!"".equals(phonedate)&&!"null".equals(phonedate))
				{
					  Date currentTime = new Date();
					  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					  String nowDate = formatter.format(currentTime);
					  String date1 =MorePhoneStoreInfoActivity.getNextDay(phonedate,"6");
					  String date2 =MorePhoneStoreInfoActivity.getNextDay(phonedate,"14");
					  String temp =MorePhoneStoreInfoActivity.getNextMonth(phonedate,6);
					  String date3=MorePhoneStoreInfoActivity.getNextDay(temp, "-1");
					  temp =MorePhoneStoreInfoActivity.getNextYear(phonedate, 1);
					  String date4=MorePhoneStoreInfoActivity.getNextDay(temp, "-1");
					  System.out.println("nowdate="+nowDate+" date1="+date1+" date2="+date2+" date3="+date3+" date4="+date4);
					
					Notification notification = new Notification(com.appdear.client.R.drawable.nofication_logo,"",  
								System.currentTimeMillis());
						
					Intent notificationIntent = new Intent(HomePageBestListActivity.this, MorePhoneStoreInfoActivity.class);
					notificationIntent.putExtra("notificaiton", "true");
					notificationIntent.addFlags(Notification.FLAG_AUTO_CANCEL);
						
	 				 String	msgText="";
	 				 if(nowDate.equals(date1))
					  {
							msgText="7天包退还有一天到期";
						  showNotification(notification,notificationIntent,10,msgText,msgText+"请点击查看");
					  }else  if(nowDate.equals(date2))
					  {
						  msgText="15天包换还有一天到期";
						  showNotification(notification,notificationIntent,10,msgText,msgText+"请点击查看");
					  }else  if(nowDate.equals(date3))
					  {
						  msgText="配件半年保修还有一天到期";
						  showNotification(notification,notificationIntent,10,msgText,msgText+"请点击查看");
					  }else  if(nowDate.equals(date4))
					  {
						  msgText="1年免费维修还有一天到期";
						  showNotification(notification,notificationIntent,10,msgText,msgText+"请点击查看");
					  } 
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
			 //SharedPreferencesControl.getInstance().putString("phonedate",msg.obj.toString(),null,MorePhoneStoreInfoActivity.this);

		}
		 
	}
	 
	private void showNotification(Notification notification,Intent notificationIntent,int id,String title,String contextText){
		  NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//锟斤拷锟斤拷锟铰硷拷
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.contentIntent = contentIntent;
		//锟斤拷锟斤拷通知
		
		if(notification !=null){
			notification.flags |= Notification.FLAG_AUTO_CANCEL;  
		}
		notification.setLatestEventInfo(this, title, contextText, contentIntent);
		notificationManager.notify(id, notification);
	}
	public String getDynamic(String s){
		Random rand=new Random(new Date().getTime());
		List<String> strs=new ArrayList();
		for(String str:InitModel.dynamic){
			if(s.equals(str)){
				continue;
			}
			strs.add(str);
		}
		return strs.get(rand.nextInt(strs.size()-1));
	}
	/**
	 * 锟斤拷前锟斤拷锟斤拷锟�
	 * @param position
	 */
	public void setSelectedValues(int position) {
		ServiceUtils.setSelectedValues(this,listData, position,"ad");
	}
	
	@Override
	public void refreshDataUI() {
		if(listData!=null&&result!=null&&result.softList!=null){
			listData.addAll(result.softList);
			page ++;
		}
	}
	
	@Override
	public void doRefreshData() {
		if (Constants.DEBUG)
			Log.i("page....page....", page + "");
		if (page > PAGE_TOTAL_SIZE&&listView!=null) {
			listView.setEndTag(true);
			return;
		}
		try {
			result = ApiManager.softlist(id+"", page + "", PAGE_SIZE + "");
			ServiceUtils.setSoftState(this,result.softList); 
			 
		} catch (ApiException e) {
			if (Constants.DEBUG)
				Log.e("ApiException error:",e.getMessage(), e);
			showException(e);
			listView.setErrTag(true);
		}
	}
	
	@Override
	public void dataNotifySetChanged() {
		if(adapter==null)return;
		adapter.notifyDataSetChanged();
		super.dataNotifySetChanged();
	}
	private void startService(){
		Intent  backgroundService = new Intent(HomePageBestListActivity.this,AppdearService.class);
		backgroundService.putExtra("update",AppContext.getInstance().info);
		this.startService(backgroundService);
	}
	//锟斤拷锟斤拷锟绞硷拷锟斤拷涌锟斤拷锟较拷锟斤拷锟绞憋拷卸锟斤拷欠锟斤拷锟斤拷锟捷革拷锟铰伙拷锟斤拷锟斤拷息
	class MyTimeTask extends TimerTask{
		private int id=0;
		public MyTimeTask(int id){
			this.id=id;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				//	Log.i("info909","isFirstInit");
					ApiManager.initinfo(0+"");
					
				//	Log.i("info909",AppContext.getInstance().initModel.updateurl+"=");
				} catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	 
}

 