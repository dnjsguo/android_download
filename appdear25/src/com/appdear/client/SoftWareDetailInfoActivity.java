/**
 * SoftWareInfoActivity.java
 * created at:2011-5-20下午04:32:37
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable.Callback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.Adapter.ViewPicAdapter;
import com.appdear.client.commctrls.AsynLoadDetailImageView;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.MyScollView;
import com.appdear.client.commctrls.ScrollChangedCallBack;
import com.appdear.client.commctrls.ViewPicLayout;
import com.appdear.client.commctrls.ViewShotControl;
import com.appdear.client.download.ListAdatperDataProcessListener;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;
 
/** 
 * 软件详情界面--软件介绍
 * 
 * @author zqm
 */
public class SoftWareDetailInfoActivity extends BaseActivity 
	implements OnClickListener, ListAdatperDataProcessListener,ScrollChangedCallBack {
	
	public static  boolean flag=false;
	/**
	 * 软件介绍列表"下载次数"
	 */
	private TextView software_content_list_download_count;
	
	/**
	 * 软件介绍列表“上传日期”
	 */
	private TextView software_content_list_upload_date;
	
	 /**
	  * 图片展示控件 布局
	  */
	 private View mView;
	 
	 /**
	  * PopupWindow 
	  */
	 private ViewShotControl popWindow ;
	 
	 /**
	  * 软件详情
	  */
	 private ApiSoftListResult result;
	 
	 /**
	  * 屏幕宽度
	  */
	 private int width = 0;
	 
	 /**
	  * 屏幕高度
	  */
	 private int height = 0;
	 
	 /**
	  * 图片浏览
	  */
	 private ViewPicLayout viewpicLayout;
	 
	 /**
	  * 图片浏览适配器
	  */
	 private ViewPicAdapter adapter;
	 
	 /**
	  * 滑动控件
	  */
	  
	 private MyScollView scrollView;
//	 private ImageView leftImageView;
//	 private ImageView rightImageView;
	 
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	 private Integer downloadcount=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.softwareinfo);
		flag=false;
	}
	
	@Override
	public void init(){
		downloadcount = getIntent().getIntExtra("downloadcount", -1);
		DisplayMetrics  metrics = ServiceUtils.getMetrics(this.getWindowManager());
    	height = metrics.heightPixels;
    	width = metrics.widthPixels;

		scrollView = (MyScollView) findViewById(R.id.picscroll);
		android.widget.RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(width, height/7*3);
		layout.setMargins(0, 0, 0, 20);
		
		scrollView.setLayoutParams(layout);
		scrollView.setScrollChangedListener(this);
//		leftImageView=(ImageView)this.findViewById(R.id.leftImageView);
//		rightImageView=(ImageView)this.findViewById(R.id.rightImageView);
		
		software_content_list_download_count = (TextView) findViewById(R.id.software_content_list_download_count);
		
		software_content_list_upload_date = (TextView) findViewById(R.id.software_content_list_upload_date);
	
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("");
		viewpicLayout = (ViewPicLayout) findViewById(R.id.viewpiclayout);
    	adapter = new ViewPicAdapter(this, list, this);
    	 
    	viewpicLayout.setAdapter(adapter, scrollView, width, height,this );
		mView = LayoutInflater.from(this).inflate(R.layout.soft_shot_dialog_layout, null);
		//根据分表率来适配
		popWindow = new ViewShotControl(this,mView,width,height,true);
		//下载按钮
		mView = LayoutInflater.from(this).inflate(R.layout.softwaredetail, null);	
		
	}

	@Override
	public void initData() {
		result = (ApiSoftListResult) getIntent().getSerializableExtra("info");
		if(result==null||result.detailinfo==null)return;
		SoftwareMainDetilActivity.appname=result.detailinfo.appid;
  		super.initData();
	}
	
	/* (non-Javadoc)
	 * @see com.appdear.client.commctrls.BaseActivity#updateUI()
	 */
	@Override
	public void updateUI() {
		//Log.i("info","updateUI");
		if (result == null || result.detailinfo == null)
			return;
		//修改为下载、更新共占一行，版本占一行
	//	Log.i("info909",downloadcount+"=downloadcount");
		if(downloadcount==null||downloadcount==-1)downloadcount= result.detailinfo.download ;
		software_content_list_download_count.setText("下载：" + downloadcount + "次"
				+ "\n" + "版本：" + result.detailinfo.version);
		software_content_list_upload_date.setText("更新：" + result.detailinfo.publishtime
				+ "\n" + "语言：" + result.detailinfo.language);
		
		TextView summy = (TextView) findViewById(R.id.software_summy);
		//software_content.setText(result.detailinfo.summary.trim());
		summy.setText(result.detailinfo.detail);
		List<String> imgurl = result.imgurl;
 		if (Common.LOADSNAPSHOT && imgurl != null && imgurl.size() != 0) {
			//recycledImg(result.imgurl,true);
			adapter = new ViewPicAdapter(this, imgurl, this);
	    	viewpicLayout.setAdapter(adapter, scrollView, width, height,this);
	    	scrollView.setCount(result.imgurl.size());
//	    	if(result.imgurl.size()>2)
//	    	{
//	    		 leftImageView.setVisibility(View.VISIBLE);
//	    	}else
//	    	{
//	    		leftImageView.setVisibility(View.GONE);
//	    	}
		}
	}
	
	public void setCallBack(Callback callback) {
	}
	
	@Override
	public void onClick(View v) {
		//点击的位置
		flag=true;
		int vid = v.getId();
		if (android.view.View.NO_ID == vid)
			vid = 0;
		if(result != null && result.detailinfo!=null)
		{
			popWindow.setSoftid(String.valueOf(result.detailinfo.softid));
		}else
		{
			return;
		}
		if (Common.LOADSNAPSHOT &&result!=null&& result.imgurl != null && result.imgurl.size() != 0) {
			popWindow.setSourceList(result.imgurl);
			popWindow.setUrls(result.imgurl);
		}
		if (Common.LOADSNAPSHOT&&popWindow!=null) {
			if (result!=null && result.imgurl != null && result.imgurl.size() > 0) {
				popWindow.setCurrentView(vid);
				popWindow.showAtLocation(SoftWareDetailInfoActivity.this.findViewById(R.id.softwareinfo_container), 
						Gravity.CENTER, 0, 0);
			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void keyPressProcess(Object object, int processTye) {
		AsynLoadDetailImageView imageview = (AsynLoadDetailImageView) object;
		imageview.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
	//	Log.i("info808", popWindow+"="+"onDestroy");
		result=null;
		flag=true;
		if(popWindow!=null){
			popWindow.dismiss();
		}
		super.onDestroy();
	}
	
	@Override
	public void scrollChangedCallback(int direction) {
		// 0  向右指   1  向右指消失  2 向左指  3 左指消失
//		 switch (direction) 
//		 {
//		  case 0:
//			  leftImageView.setVisibility(View.VISIBLE);
//			break;
//           case 1:
//        	   leftImageView.setVisibility(View.GONE);            	 
//			break;
//           case 2:         	  
//        	   rightImageView.setVisibility(View.VISIBLE);
//			break;
//           case 3:
//        	   rightImageView.setVisibility(View.GONE);
//			break;				
//		}		
		
	}

	@Override
	public void keyPressProcess(Object object, int processTye, int position) {
		
	}
}
 