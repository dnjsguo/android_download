/**
 * MainActivity.java
 * created at:2011-5-9 14:14:22
 * 
 * copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 * 
 */
package com.appdear.client;

import java.util.Date;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.PagerCallback;
import com.appdear.client.commctrls.PagerContoler;
import com.appdear.client.commctrls.PagerContolerVersion;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.TabCallBack;
import com.appdear.client.download.MoreManagerDownloadActivity;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;

/**
 * 主页
 * 
 * @author zqm
 */
public class HomePageMainActivity extends BaseGroupActivity implements 
	 PagerCallback{
 
	private LocalActivityManager manager;
	private View specialView;
	private View bestView;
	private View newlistView;
	public   PagerContoler pagerContoler ;
	public  static PagerContolerVersion pagerContolerVersion;
    private LayoutInflater  mLayoutInflater ;
    public static int topFlag=0;
   // private boolean isCallonStart=false;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_gallery_form_layout);
	}
    public String getModelName(String modelType,String company)
    {
    	company=company.toLowerCase();
    	//System.out.println("modelType=="+modelType+"   company="+company);
    	if(company.indexOf("moto")!=-1  )
    	{
    		MyApplication.getInstance().modelCompany="moto";
    		MyApplication.getInstance().modelCompanyPhone="400-810-5050";
    		return "MOTO专区";
    	}else if(company.indexOf("htc")!=-1)
    	{
    		MyApplication.getInstance().modelCompany="htc";
    		MyApplication.getInstance().modelCompanyPhone="400-821-8998";

    		return "HTC专区";
    	}else if(company.indexOf("samsung")!=-1)
    	{
    		MyApplication.getInstance().modelCompany="samsung";
    		MyApplication.getInstance().modelCompanyPhone="400-810-5858";

    		return "三星专区";
    	}else if(company.indexOf("huawei")!=-1)
    	{
    		MyApplication.getInstance().modelCompany="huawei";
    		MyApplication.getInstance().modelCompanyPhone="400-830-8300";

    		return "华为专区";
    		
    	}else if(company.indexOf("lenovo")!=-1)
    	{
    		MyApplication.getInstance().modelCompany="lenovo";
    		MyApplication.getInstance().modelCompanyPhone="400-818-8818";

    		return "联想专区";
    	}else{
    		MyApplication.getInstance().modelCompany="other";
    		MyApplication.getInstance().modelCompanyPhone="暂未收录";

    		return "热门下载";
    	}
    	//MB860  MOTO
    	//modelType==HTC EVO 3D X515m   company=htc_asia_wwe
         //modelType==GT-I8150   company=samsung
         //modelType==U8860   company=Huawei
    	//modelType==Lenovo A60   company=lenovo
    }
    
    @Override
    public void init() {    	 
    	manager = getLocalActivityManager(); 
    	String modelType=android.os.Build.MODEL;
    	String company=android.os.Build.BRAND;
    	
    	
    	String[]  strs=new String[]{"精品推荐", "分类推荐", getModelName(modelType,company) };		
		if (specialView == null) {
			SharedPreferencesControl.getInstance().putLong("starttime",new Date().getTime(),
					null,this);
			specialView = manager.startActivity(
                "speciallist",
                new Intent(HomePageMainActivity.this, HomePageBestListActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                .getDecorView();	 
		} 
		 
		mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
	    View view2= mLayoutInflater.inflate(R.layout.pagerview_init, null);
	    View view3= mLayoutInflater.inflate(R.layout.pagerview_init, null);
	    //	LinearLayout aipibiView = (LinearLayout) mLayoutInflater.inflate(R.layout.more_help_layout, null);
	   
	    if(MyApplication.getInstance().androidLevel>10)
		{
	    	pagerContolerVersion=new PagerContolerVersion(this);
	    	pagerContolerVersion.initImageView(this,0);
	    	pagerContolerVersion.setTabCallback(callback);
	    	pagerContolerVersion.initTextView(strs, this);
	    	pagerContolerVersion.initViewPager(this,specialView,view2,view3,false,true,true);
		}else
		{
		    pagerContoler = new PagerContoler(this);
		    pagerContoler.setTabCallback(callback);
		    pagerContoler.initImageView(this,0);
			pagerContoler.initTextView(strs, this);
		    pagerContoler.initViewPager(this,specialView,view2,view3,false,true,true);
		}
    }
    
	@Override
	public View viewSecend() {
		topFlag=1;
		MainActivity.topLogFlag=topFlag;
		if(newlistView == null) {
//			bestView = manager.startActivity(
//                    "bestView",
//                    new Intent(HomePageMainActivity.this, HomePageNewSoftListActivity.class)
//                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
//                    .getDecorView();
			
			// 调整成适用人群
			newlistView = manager.startActivity(
	                "newlist",
	                new Intent(HomePageMainActivity.this, MatchTargetUsersGridActivity.class)
	                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
	                .getDecorView();	
		}
		return newlistView;
	}
	
	@Override
	public View viewThird() {
		topFlag=2;
		MainActivity.topLogFlag=topFlag;
		if(bestView == null) {
			bestView = manager.startActivity(
                    "bestView",
                    new Intent(HomePageMainActivity.this, HomePageSpecialListActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    .getDecorView();
		}
		return bestView;
		
	}
 

	TabCallBack callback=new TabCallBack(){
		@Override
		public void callback(int position) {
			topFlag=position;
			MainActivity.topLogFlag=topFlag;
			/*if(position == 0)
			{
				createFirstView();

			}else if (position == 1) {
				newlistView = manager.startActivity(
	                    "newlist",
	                    new Intent(HomePageMainActivity.this, MatchTargetUsersGridActivity.class)
	                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
	                    .getDecorView();
		 	}else if(position == 2)
		 	{
		 		bestView = manager.startActivity(
	                    "bestView",
	                    new Intent(HomePageMainActivity.this, HomePageSpecialListActivity.class)
	                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
	                    .getDecorView();
		 	}*/
		}
	};

  /*public void   createFirstView()
  {
		long starttime=SharedPreferencesControl.getInstance().getLong("starttime",
				null,this);
		long t=0;
	 
			if((starttime+1800*1000)<(t=new Date().getTime())){
				if(ServiceUtils.isWiFiActive( this)){
					SharedPreferencesControl.getInstance().putLong("starttime",t,
							null,this);
 				 View view = manager.startActivity(
			                "speciallist",
			                new Intent(HomePageMainActivity.this, HomePageBestListActivity.class)
			                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			                .getDecorView();
				 
				    pagerContoler.listViews.remove(0);
				    pagerContoler.listViews.add(0, view);
				    pagerContoler.getPagerAdapterInstance().notifyDataSetChanged();
				 
				 return; 
				}
			} 
		specialView = manager.startActivity(
                "speciallist",
                new Intent(HomePageMainActivity.this, HomePageBestListActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                .getDecorView();
	 
  }*/
	@Override
	public View viewFirst() {
		topFlag=0;
		MainActivity.topLogFlag=topFlag;
		return null;
	}
	@Override
	protected void onResume() {
 
		super.onResume();
	}
}
