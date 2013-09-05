/**
 * AuthorInfoActivity.java
 * created at:2011-5-20下午04:55:59
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;
  
/** 
 * 软件详情界面--同类推荐
 * 
 * @author 
 */
public class SoftwareDetailAuthorInfoActivity extends ListBaseActivity {

 
	private ApiSoftListResult result;
	private String catid = "0";
	private int softid = 0;
	LinearLayout smileface = null;
	RelativeLayout container = null;
	ProgressDialog progress;
	private List<SoftlistInfo> listdata = new ArrayList<SoftlistInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_list_layout);
		
		container = (RelativeLayout) this.findViewById(R.id.layout);
		
		smileface = (LinearLayout) getLayoutInflater().inflate(R.layout.smile_face, null);
		smileface.setVisibility(View.GONE);
		smileface.setGravity(Gravity.CENTER);
		TextView face_txt = (TextView) smileface.findViewById(R.id.face_txt);
		face_txt.setText("暂时没有相同类别的应用推荐给您");
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		smileface.setLayoutParams(params);

		 /**
		  * find container .
		  * 
		  */
		 container.addView(smileface);
		 initGif();
	}
	private void initGif(){
		if(height==0&&width==0){
			height = this.getWindowManager().getDefaultDisplay().getHeight();
			width=this.getWindowManager().getDefaultDisplay().getWidth();
		}
		LayoutParams params = new LayoutParams(width,
				height*3/4);
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
		
		catid = getIntent().getStringExtra("category_id");
		softid = getIntent().getIntExtra("softid", 0);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
		//layout.setPadding(0, 0, 0, 60);
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setFadingEdgeLength(0);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.requestFocus();
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		
	}
	public void  compareInit(List<Integer> detailList,List<SoftlistInfo> softList)
    {
    	List<SoftlistInfo>  compareList=new ArrayList<SoftlistInfo>();
      	for(int i=0;i<softList.size();i++)
      	{
      		
      		SoftlistInfo softInfo = softList.get(i);
      	//	System.out.println("old   "+softInfo.appid);
      		for(int  softId:detailList)
      		{
      			if (softId==softInfo.softid) 
      			{
      				//System.out.println("-----filter---"+softInfo.softid);
      				compareList.add(softInfo);
					break;
				}
      		}
      	}
      	softList.removeAll(compareList);
     /* 	for(int i=0;i<softList.size();i++)
      	{ 		
      		SoftlistInfo softInfo = softList.get(i);
      	//	System.out.println("new   "+softInfo.appid);
      	}*/
    }
	@Override
	public void initData() {
		try {
			if (catid.equals("0")) {
				return;
			}
 			result = ApiManager.catalogsoftlist(catid, 0+"", 1+"", page+"", PAGE_SIZE+"");
			//屏蔽所有正在显示的应用详情对应的应用
			compareInit(MyApplication.getInstance().detailSoftidList,result.softList);
			/*for (int i = 0; i < result.softList.size(); i ++) {
				SoftlistInfo bean = (SoftlistInfo) result.softList.get(i);
				if (bean.softid == softid) {
					result.softList.remove(bean);
					break;
				}
			}*/
			listdata = result.softList;
			ServiceUtils.setSoftState(this,listdata);
			adapter = new SoftwarelistAdatper(this, listdata, listView);
			adapter = (SoftwarelistAdatper) adapter;
			page ++;
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			
			adapter.notifyDataSetChanged();
			
			if (page <= PAGE_TOTAL_SIZE&&listView!=null)
				listView.setRefreshDataListener(this);
		} catch (ApiException e) {
			showRefreshButton();
			showException(e.getMessage());
		}
		finally{
			handler1.sendEmptyMessage(LOADG);
		}
		super.initData();
	}
	
	@Override
	public void updateUI() {
		if(listView==null)return;
		if (result != null && result.softList.size() != 0) {
			if(smileface!=null)
				smileface.setVisibility(View.GONE);
			
			if(listView==null)return;
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new ListView.OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					setSelectedValues(arg2);
				}
			});
		} else {
			//showMessageInfo("没有相关信息");tvNoSoft
			smileface.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 当前项操作
	 * @param position
	 */
	public void setSelectedValues(int position) {
		ServiceUtils.setSelectedValues(this,listdata, position,"cataid-"+catid);
	}
	
	@Override
	public void refreshDataUI() {
		if(result==null||result.softList==null||listdata==null)return;
		listdata.addAll(result.softList);
		page ++;
	}

	@Override
	public void doRefreshData() {
		if (page > PAGE_TOTAL_SIZE) {
			listView.setEndTag(true);
			return;
		}
		try {
			result = null;
			result = ApiManager.catalogsoftlist(catid, 0+"", 1+"", page+"", PAGE_SIZE+"");
		/*	//屏蔽当然软件
			for (int i = 0; i < result.softList.size(); i ++) {
				SoftlistInfo bean = (SoftlistInfo) result.softList.get(i);
				if (bean.softid == softid) {
					result.softList.remove(bean);
					break;
				}
			}*/
			compareInit(MyApplication.getInstance().detailSoftidList,result.softList);
			ServiceUtils.setSoftState(this,result.softList); 
		} catch (ApiException e) {
			if (Constants.DEBUG)
				Log.e("net error:",e.getMessage(), e);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		result=null;
		listdata=null;
		super.onDestroy();
	}
}

 