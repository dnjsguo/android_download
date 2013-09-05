/**
 * CateGoryActivity.java
 * created at:2011-5-16閿熸枻鎷烽敓鏂ゆ嫹01:37:39
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
import com.appdear.client.commctrls.TabCallBack;
import com.appdear.client.commctrls.TabbarCallback;
import com.appdear.client.commctrls.TopTabbar;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.R;

/**
 * 类别首页 (应用--游戏--精彩专题)
 * 
 * @author zhangxinyong
 *
 */
public class CateGoryMainActivity extends BaseGroupActivity implements  PagerCallback {

	private LocalActivityManager manager;
	
	private View specialView;
	private View bestView;
	private View newlistView;
	private PagerContoler pagerContoler ;
	public  static PagerContolerVersion pagerContolerVersion;
	 private boolean isCallonStart=false;
    private LayoutInflater  mLayoutInflater ;
    
    public static int topFlag=0;
 	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_main_layout);
		ScreenManager.getScreenManager().pushActivity(this);//进栈
	}
	
	@Override
	public void init() {
       manager = getLocalActivityManager();
    	
    	String[]  strs=new String[]{"专题","应用", "游戏"};
		
    	
		if (specialView == null) {
			specialView = manager.startActivity(
                "speciallist",
                new Intent(CateGoryMainActivity.this, CategoryTopicActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                .getDecorView();	 
		}
		 mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 View view2= mLayoutInflater.inflate(R.layout.pagerview_init, null);
		 View view3= mLayoutInflater.inflate(R.layout.pagerview_init, null);
		 
		/* pagerContoler.initImageView(this,0);
		 pagerContoler.initTextView(strs, this);
		 pagerContoler.initViewPager(this,specialView,view2,view3,false,true,true);*/
		  if(MyApplication.getInstance().androidLevel>10)
			{
		    	pagerContolerVersion=new PagerContolerVersion(this);
		    	pagerContolerVersion.setTabCallback(callback);
		    	pagerContolerVersion.initImageView(this,0);
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
	
 
	TabCallBack callback=new TabCallBack(){
		@Override
		public void callback(int position) {
			topFlag=position;
			MainActivity.topLogFlag=topFlag;
		}
	};
	
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
		// TODO Auto-generated method stub
		topFlag=0;
		MainActivity.topLogFlag=topFlag;
		return null;
	}

	@Override
	public View viewSecend() {
		topFlag=1;
		MainActivity.topLogFlag=topFlag;
		if(newlistView == null) {
			newlistView = manager.startActivity(
                    "newlist",
                    new Intent(CateGoryMainActivity.this, CategoryAppActivity.class)
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
                    new Intent(CateGoryMainActivity.this, CategoryGameActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    .getDecorView();
		}
		return bestView;
		 
	}
	/*@Override
	protected void onStart() {
		if(MyApplication.getInstance().androidLevel>10)
		{
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

 