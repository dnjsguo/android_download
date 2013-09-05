/**
 * TheBestListView.java
 * created at:2011-5-20下午01:19:33
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiShareActionRequest;
import com.appdear.client.service.api.ApiShareSoftResult;
import com.appdear.client.utility.ServiceUtils;

/** 
 * 我的分享
 * 
 * @author zqm
 */
public class MyShareListActivity extends ListBaseActivity {
 
	
	/**
	 * 列表数据
	 */
	private ApiShareSoftResult result;
	
	/**
	 *  List<SoftlistInfo>
	 */
	private List<SoftlistInfo> listData = null ;
	
	/**
	 * 栏目id
	 */
	private int id = 100002;
	
	/**
	 * userid
	 */
	private String userid = "";
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isUpdate = true;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_list_layout);
		
		params = new LayoutParams(width,
				height-45);
		loadingView=new MProgress(this,false);
		this.addContentView(loadingView, params);
	}
	
	@Override
	public void init() {
		userid = getIntent().getStringExtra("userid");
		listView = (ListViewRefresh) this.findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDivider(this.getResources().getDrawable(R.drawable.spinner_bg));
		listView.setDividerHeight(1);
		listView.setSelector(R.drawable.soft_list_bg);
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}
	
	@Override
	public void initData() {
		try {
			//id = SharedPreferencesControl.getInstance().getInt("102", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			try {
				result = ApiShareActionRequest.requestMyShareSoftList(userid, page+"", PAGE_SIZE + "");
			} catch (ServerException e) {
				showException("读取数据错误");
			}
			page ++;
			if (result != null) {
				listData = result.myshareList;
				ServiceUtils.setSoftState(this,listData);
				adapter = new SoftwarelistAdatper(this, listData, listView);				
				PAGE_TOTAL_SIZE = result.popcount % PAGE_SIZE == 0 ?
						result.popcount/PAGE_SIZE:result.popcount/PAGE_SIZE + 1;
				adapter.notifyDataSetChanged();
			}
		} catch (ApiException e) {
			if (Constants.DEBUG)
				Log.e("net error:",e.getMessage(), e);
			showException(e);
			showRefreshButton();
		}
		super.initData();
	}

	/* (non-Javadoc)
	 * @see com.appdear.client.commctrls.BaseActivity#updateUI()
	 */
	@Override
	public void updateUI(){
		if(listView==null)return;
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				setSelectedValues(arg2);
			}
		});
	}

	/**
	 * 当前项操作
	 * @param position
	 */
	public void setSelectedValues(int position) {
		ServiceUtils.setSelectedValues(this, listData, position);
	}
	
	@Override
	public void refreshDataUI() {
		if (result == null || result.myshareList == null)
			return;
		listData.addAll(result.myshareList);
		page ++;
	}
	
	@Override
	public void doRefreshData() {
		if (page > PAGE_TOTAL_SIZE) {
			listView.setEndTag(true);
			return;
		}
		try {
			try {
				result = null;
				result = ApiShareActionRequest.requestMyShareSoftList("", page+"", PAGE_SIZE + "");
				ServiceUtils.setSoftState(this,result.myshareList); 
			} catch (ServerException e) {
				if (Constants.DEBUG)
					Log.e("ApiException error:",e.getMessage(), e);
				showException(e);
				//e.printStackTrace();
			}
		} catch (ApiException e) {
			if (Constants.DEBUG)
				Log.e("ApiException error:",e.getMessage(), e);
			showException(e);
			listView.setErrTag(true);
		}
	}
}

 