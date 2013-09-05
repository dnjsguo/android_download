/**
 * TheBestListView.java
 * created at:2011-5-20下午01:19:33
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
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
 * 精品推荐list
 * 
 * @author zqm
 */
public class HomePageBestListActivity extends ListBaseActivity {

	public static boolean flag=true;
	private ApiUserResult result1=null;
	
	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	
	/**
	 *  List<SoftlistInfo>
	 */
	private List<SoftlistInfo> listData = null ;
	
	/**
	 * 栏目id
	 */
	private int id = 100002;
	
	/**
	 * 广告布局显示
	 */
	private View mView;
	
	/**
	 * 广告控件
	 */
	private AdView adView;
	
	/**
	 * 广告url
	 */
	private List<SoftlistInfo> urls;
	
	/**
	 * 广告自动翻页
	 */
	private ViewFlipper flipper;
	
	/**
	 * 广告数据返回
	 */
	private ApiSoftListResult adresult;
	
	private boolean iscachesoftlist=true;
	
	HomeObj homeobj=null;
	
	private boolean defflag=false;
	
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
		
		//初始化首页广告
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
			//广告数据获取
//			if (AppContext.getInstance().spreurl == null || AppContext.getInstance().spreurl.equals("")) {
//    			AppContext.getInstance().spreurl = SharedPreferencesControl.getInstance().getString(
//    					"spreurl", com.appdear.client.commctrls.Common.SETTINGS, this);
//    		}
//    		if (AppContext.getInstance().dpreurl == null || AppContext.getInstance().dpreurl.equals("")) {
//    			AppContext.getInstance().dpreurl = SharedPreferencesControl.getInstance().getString(
//    					"dpreurl", com.appdear.client.commctrls.Common.SETTINGS, this);
//    		}
//    		id = SharedPreferencesControl.getInstance().getInt("104", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
//			adresult = ApiManager.bannerlist(id + "", "0", "1", "20");
//			
			//列表数据获取
			id = SharedPreferencesControl.getInstance().getInt("102", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			int totalcount=0;
			if((homeobj=(HomeObj)ServiceUtils.getListview(SourceCommon.HOME_FIRST_NEW,this))==null){
				homeobj=new HomeObj();
				result = ApiManager.dynamicsoftlist2(id+"", page + "", PAGE_SIZE + "",homeobj.getDynamic());
				page ++;
				listData = result.softList;
				totalcount=result.totalcount;
//				Log.i("infodef", result.def+"=default=initData");
//				if(result.def!=null&&!result.def.equals("1")){
//					homeobj.setListData(listData);
//					totalcount=result.totalcount;
//					ServiceUtils.addListview(SourceCommon.HOME_FIRST_NEW, homeobj, this, result.totalcount);	
//				}
				asids=result.asids;
				if(result.def!=null&&result.def.equals("1")){
					iscachesoftlist=false;
					defflag=true;
				}
			}else{
				listData=homeobj.getListData();
				asids=homeobj.getAsids();
				totalcount=	SharedPreferencesControl.getInstance().getInt(SourceCommon.HOME_FIRST_NEW, com.appdear.client.commctrls.Common.LISTVIEWSOURCE_XML, this);
				page ++;
			}
			ServiceUtils.setSoftState(this,listData);
			adapter = new SoftwarelistAdatper(this, listData, listView);
		
			PAGE_TOTAL_SIZE = totalcount%PAGE_SIZE==0?
					totalcount/PAGE_SIZE:totalcount/PAGE_SIZE+1;
			
			if (page <= PAGE_TOTAL_SIZE)
				if(listView != null) listView.setRefreshDataListener(this);
			
			adapter.notifyDataSetChanged();
		} catch (ApiException e) {
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
		//将当前时间存储 
		if (listView.getAdapter() == null) {
			//添加广告
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
					//点击广告
					if (adView != null) {
						if(urls==null)return;
						SoftlistInfo info = (SoftlistInfo) urls.get(adView.getnPost());
						if(info.adtype.equals("3")) {
							Intent intent = new Intent(HomePageBestListActivity.this, SoftwareMainDetilActivity.class);
			 				//清除进程activity
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
				adresult = ApiManager.bannerlist(id + "", "0", "1", "20");
			} catch (ApiException e) {
				 
				e.printStackTrace();
			}
			if (adresult != null && adresult.softList != null) {
				bhandler.post(new Runnable(){

					@Override
					public void run() {
 						if(listView==null)return;
							//添加广告
							if (adresult != null && adresult.softList != null) {
								urls = adresult.softList;
								List<String> urllist = new ArrayList<String>();
								for (SoftlistInfo info : adresult.softList) {
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
			//第一次加载进入缓存不需要再次加载数据进入缓存
			if(iscachesoftlist==true){
				ApiSoftListResult result=null;
				try {
					HomeObj homeobj1=new HomeObj();
					String dynamic=getDynamic(homeobj!=null?homeobj.getDynamic():InitModel.dynamic[0]);
					result = ApiManager.dynamicsoftlist2(id1+"", 1 + "", PAGE_SIZE + "",dynamic);
					List<SoftlistInfo> listData = result.softList;
					int totalcount=result.totalcount;
					AppContext.getInstance().dynamicnew=dynamic;
					if(result.def!=null&&!result.def.equals("1")){
				//		homeobj1.setTimestamp(new Date().getTime()+result.timpstamp);
						homeobj1.setTimestamp(new Date().getTime()+result.timpstamp);
						homeobj1.setDynamic(dynamic);
						homeobj1.setListData(listData);
						homeobj1.setAsids(result.asids);
						ServiceUtils.addListview(SourceCommon.HOME_FIRST_NEW, homeobj1, HomePageBestListActivity.this, result.totalcount);
					}
					if(result.def!=null&&result.def.equals("1")){
						ListviewSourceCache.getInstance().removeInitModel(SourceCommon.HOME_FIRST_NEW);
					}
				} catch (ApiException e) {
					// TODO Auto-generated catch block
				}
			}
			//之后走缓存信息后，需要再次加载下次的数据
			iscachesoftlist=true;
			//if(AppContext.getInstance().isFirstInit==false){
				new Timer().schedule(new MyTimeTask(id),5000);
			//}
			
			if(flag==true){
				flag=false;
				if(SharedPreferencesControl.getInstance().getBoolean("issave",com.appdear.client.commctrls.Common.USERPASSWDXML,HomePageBestListActivity.this)){
					String username=SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,HomePageBestListActivity.this);
					String passwd=SharedPreferencesControl.getInstance().getString("passwd",com.appdear.client.commctrls.Common.USERPASSWDXML,HomePageBestListActivity.this);
					if(username!=null&&passwd!=null&&!username.equals("")&&!passwd.equals("")){	
							try {
								result1=ApiManager.userlogin(SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,HomePageBestListActivity.this),SharedPreferencesControl.getInstance().getString("passwd",com.appdear.client.commctrls.Common.USERPASSWDXML,HomePageBestListActivity.this));
							} catch (ApiException e) {
								e.printStackTrace();
								return;
							}
							try{
							SharedPreferencesControl.getInstance().putString("sessionid",result1.token,com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							SharedPreferencesControl.getInstance().putString("userid",result1.userid+"",com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							SharedPreferencesControl.getInstance().putString("nickname",result1.nickname,com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							SharedPreferencesControl.getInstance().putString("username",SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERPASSWDXML,HomePageBestListActivity.this),com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							}catch(Exception e){
								return;
							}
							try {
								result1=ApiManager.userprofile(SharedPreferencesControl.getInstance().getString("userid",com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this),SharedPreferencesControl.getInstance().getString("sessionid",com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this));
							} catch (ApiException e) {
								e.printStackTrace();
								return;
							}
							SharedPreferencesControl.getInstance().putString("account",String.valueOf(result1.account),com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							SharedPreferencesControl.getInstance().putString("level",result1.level.equals("")?"爱皮小将":result1.level,com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							SharedPreferencesControl.getInstance().putString("point",String.valueOf(result1.totalpoint),com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
							SharedPreferencesControl.getInstance().putString("gender",String.valueOf(result1.userinfo.gender),com.appdear.client.commctrls.Common.USERLOGIN_XML,HomePageBestListActivity.this);
						    return;
						   }
				   }
			}
			if(ServiceUtils.isWiFiActive(HomePageBestListActivity.this)){
				Location.getWifiLocation(HomePageBestListActivity.this);
			}else{
				Location.getLocations(HomePageBestListActivity.this);
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
	/**
	 * 发出通知
	 * @param notification 通知
	 * @param notificationIntent 点击通知的跳转intent
	 */
	private void showNotification(Notification notification,Intent notificationIntent,int id,String title,String contextText){
		  NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//定义事件
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.contentIntent = contentIntent;
		//发出通知
		
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
	 * 当前项操作
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
			result = null;
			String dyn="";
			if(defflag==false){
				dyn=homeobj!=null?homeobj.getDynamic():InitModel.dynamic[0];
			}
			result = ApiManager.dynamicsoftlist2(id+"", page + "", PAGE_SIZE + "",dyn,getAsids(page));
			if(result!=null){
				ServiceUtils.setSoftState(this,result.softList); 
			} 
			if(result.def!=null&&result.def.equals("1")){
				ListviewSourceCache.getInstance().removeInitModel(SourceCommon.HOME_FIRST_NEW);
			}
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
	//缓存初始化接口信息，适时判断是否有数据更新或升级信息
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
	
	private String getAsids(int page){
		StringBuffer sb=null;
		if(asids!=null&&!asids.equals("")){
			sb=new StringBuffer();
			String[] s=asids.split(",");
			int offset=(page-1)*PAGE_SIZE;
			if(offset>=s.length)return null;
			int end=page*PAGE_SIZE-1;
			end=s.length-1>=end?end:s.length-1;
			for(int i=offset;i<=end;i++){
				sb.append(s[i]);
				if(i<end){
					sb.append(",");
				}
			}
		}
		if(sb!=null)return sb.toString();
		else return null;
	}
}

 