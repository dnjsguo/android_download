/**
 * CateGoryActivity.java
 * created at:2012-02-06閿熸枻鎷烽敓鏂ゆ嫹01:37:39
 * 
 * Copyright (c) 2011, 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风毊閿熺嫛纭锋嫹閿熸枻鎷烽敓鐫櫢鎷峰徃
 *
 * All right reserved
 */ 
package com.appdear.client;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.PagerCallback;
import com.appdear.client.commctrls.PagerContoler;
import com.appdear.client.commctrls.PagerContolerVersion;
import com.appdear.client.commctrls.TabbarCallback;
import com.appdear.client.commctrls.TopTabbar;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.R;

/**
 * 匹配（万花筒）首页  (适用人群--使用场景--职业)
 */
public class MatchMainActivity extends BaseGroupActivity implements  PagerCallback  {
	private LocalActivityManager manager;
	
	private View specialView;
	private View bestView;
	private View newlistView;
 	 private LayoutInflater  mLayoutInflater ;
 	private PagerContoler pagerContoler  ;
	public  static PagerContolerVersion pagerContolerVersion;
	// private boolean isCallonStart=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_main_layout);
		ScreenManager.getScreenManager().pushActivity(this);//进栈
		 
	}
	
	@Override
	public void init() {
		manager = getLocalActivityManager();
        String[]  strs=new String[]{"职业", "适用人群", "使用场景"};
		
	
		if (bestView == null) {
			bestView = manager.startActivity(
                "best",
                new Intent(MatchMainActivity.this, MatchTargetUsersGridActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                .getDecorView();	 
		}
		mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 View view1= mLayoutInflater.inflate(R.layout.pagerview_init, null);
		 View view3= mLayoutInflater.inflate(R.layout.pagerview_init, null);
			/*pagerContoler.initImageView(this,1);
			pagerContoler.initTextView(strs, this);
		 pagerContoler.initViewPager(this,view1,bestView,view3,true,false,true); */
		 
		 
		 if(MyApplication.getInstance().androidLevel>10)
			{
		    	pagerContolerVersion=new PagerContolerVersion(this);
		    	pagerContolerVersion.initImageView(this,1);
		    	pagerContolerVersion.initTextView(strs, this);
		    	pagerContolerVersion.initViewPager(this,view1,bestView,view3,true,false,true); 
			}else
			{
			    pagerContoler = new PagerContoler(this);
			    pagerContoler.initImageView(this,1);
				pagerContoler.initTextView(strs, this);
			    pagerContoler.initViewPager(this,view1,bestView,view3,true,false,true); 
			}
	}
	
 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}
	
	@Override
	protected void onDestroy() {
		ScreenManager.getScreenManager().popActivity(this);//出栈
		super.onDestroy();
	}

	@Override
	public View viewFirst() {
		if(specialView == null) {
			specialView = manager.startActivity(
                    "special",
                    new Intent(MatchMainActivity.this, MatchProfessionActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    .getDecorView();
		}
		return specialView;
	}

	@Override
	public View viewSecend() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View viewThird() {
		if(newlistView == null) {
			newlistView = manager.startActivity(
                    "newlist",
                    new Intent(MatchMainActivity.this, MatchUsageScenarioActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    .getDecorView();
		}
		return newlistView;
	}
	
	/*@Override
	protected void onStart() {
		if(MyApplication.getInstance().androidLevel>10)
		{
			System.out.println("-----MatchMainActivity---onStart()--");
			isCallonStart=true;
		}
		super.onStart();
	}
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().androidLevel>10)
		{
			if(isCallonStart)
			{
				System.out.println("-----MatchMainActivity---onResume()--");
				isCallonStart=false;
			}else
			{
			    if(pagerContolerVersion!=null)
			    {
			    	 int i=	 pagerContolerVersion.currIndex;
				     pagerContolerVersion.initImageView_version(i);
			    }
				
			}
		
		}
		super.onResume();
	}*/
}