package com.appdear.client;

import java.util.Hashtable;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appdear.client.Adapter.UninstalledRecommendedAdapter;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;

public class AlterUninstalledRecommendActivity extends ListBaseActivity implements  OnClickListener {

	 public   List<SoftlistInfo> listData = null ;
	// HashMap<Integer,Boolean> isSelected;
	 private  Button updateButton;
	 private  Button allDownloadButton;
     private  Button  closedButton;
     private  TextView titleView;
     private  String appid;
     private  String appname;
     int page=1;
     int pageCount=1;
     boolean isClickUpdateButton=false;

  //  进度条对话框
 	ProgressDialog  pDialog;
    public Hashtable<String,PackageinstalledInfo> installlists = null;
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Intent intent=this.getIntent();
		appid=intent.getExtras().getString("appid");
		appname=intent.getExtras().getString("appname");
		pageCount=intent.getExtras().getInt("pageCount");
		setContentView(R.layout.uninstalled_recomment_list_layout);	
	}
	
	@Override
	public void init() {
		listView = (ListViewRefresh) findViewById(R.id.installed_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setFadingEdgeLength(0);
		listView.setVerticalFadingEdgeEnabled(false);
		listView.setHorizontalFadingEdgeEnabled(false);
		updateButton=(Button)this.findViewById(R.id.updateButton);
 		allDownloadButton=(Button)this.findViewById(R.id.allDownloadButton);
 		titleView=(TextView)this.findViewById(R.id.titleView);
 		closedButton=(Button)this.findViewById(R.id.closedButton);
 		allDownloadButton.setClickable(true);
 		updateButton.setClickable(true);
 		updateButton.setOnClickListener(this);
		allDownloadButton.setOnClickListener(this);
        closedButton.setOnClickListener(this);
        titleView.setText("你刚刚卸载了"+appname+",爱皮为您推荐了同类型的软件。");
	}
	
	
	public void initData() {
		
		listData=	MoreManagerInstalledActivity.listData;
        System.out.println("--initData()----listData=="+listData.size());
		adapter = new UninstalledRecommendedAdapter(this, listData, listView);
		
		adapter.notifyDataSetChanged();
	}
	@Override
	public void updateUI(){
	 
		listView.setAdapter(adapter);
	}
	@Override
	public void refreshDataUI() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doRefreshData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.updateButton )
		 {
			 if(isClickUpdateButton)
			 {
				 this.showMessageInfo("正在加载中请稍后");
				 System.out.println("-----正在加载中请稍后------------");
				 return;
			 }
			 isClickUpdateButton=true;
			 new Thread() {
					public void run() {
						try
						{
							System.out.println("page==="+page+"   pagecount=="+pageCount);
							if(page>pageCount)
							{
								page=1;
							}
					   		ApiSoftListResult	result = ApiManager.softlistbyappid(appid, 0+"", 0+"", (++page)+"", 5+"");
 
					   		//ApiSoftListResult	result = ApiManager.catalogsoftlist("100025", 0+"", (page++)+"", page+"", 5+"");
							if (result == null) 
							{
							//	System.out.println("-----run()-----------");
								page--;
								isClickUpdateButton=false;
								return;
							}
							listData=result.softList;
							Hashtable<String,PackageinstalledInfo>  installlists= ServiceUtils.getInstalledApps(AlterUninstalledRecommendActivity.this, false,true);

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
							page--;
							isClickUpdateButton=false;
							return;
						}
					 
						adapter = new UninstalledRecommendedAdapter(AlterUninstalledRecommendActivity.this, listData, listView);
						myHandler.sendEmptyMessage(0);
						System.out.println("--------sendEmptyMessage()--------------");
					}
				}.start();
	 
		 }else if(v.getId()==R.id.allDownloadButton)
		 {
 			 
			 new Thread() {
					public void run() {
						int sucessCount=0;
						for(int i = 0; i<listData.size(); i++){
							  
							 SoftlistInfo item=(SoftlistInfo)listData.get(i);
 					    		if(item.type==0)
					    		{	 
					    			Integer Status = MyApplication.getInstance().getSoftMap().get(item.softid);
					    			if (Status == null||Status==0) {
					    				 if (download(item,  item.downloadurl,0)) {
					    					 sucessCount++;
					    				 } 
					    			} 

					             }
					 }
 						if(sucessCount>0)
 						{
 							AlterUninstalledRecommendActivity.this.showMessageInfo("有"+sucessCount+"个应用添加到下载管理中，请查看");
 						} 
					} 
				}.start();
			 this.finish();
		 }else if(v.getId()==R.id.closedButton)
		 {
			 this.finish();
		 }
	}
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case  0:  //  执行下载
				System.out.println("--------handleMessage()-------------");
				listView.setAdapter(adapter);
				isClickUpdateButton=false;
 	    		break;
			}
		}
	};
	private boolean download(SoftlistInfo info, String apkname,int state) {
		final SiteInfoBean downloadbean = new SiteInfoBean(info.downloadurl,
				ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null ? ""
						: ServiceUtils.getSDCARDImg(Constants.APK_DATA)
								.getPath(), ServiceUtils.getApkname(info.downloadurl), info.softname,
				info.softicon, info.version, info.softid, info.appid,
				info.softsize, 0, 1, null, null,
				BaseActivity.downloadUpdateHandler,state==4?Constants.UPDATEPARAM:"");
		String[] msg = DownloadUtils.download(downloadbean, this);
		// 任务已存在
		//Toast mScreenHint = Toast.makeText(this, msg[0],Toast.LENGTH_SHORT);
		//mScreenHint.show();
		if (msg[1] != null && msg[1].equals("0"))
			return true;
		return false;
	}
 
}
