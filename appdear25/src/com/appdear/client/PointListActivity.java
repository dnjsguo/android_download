/**
 * TheNewSoftListView.java
 * created at:2011-5-20下午03:08:11
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.Adapter.PointlistAdatper;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
  
/** 
 * 积分
 * 
 * @author jdan
 */
public class PointListActivity extends ListBaseActivity {

	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	
	/**
	 *  详细信息列表
	 */
	private List<SoftlistInfo> listData = new ArrayList<SoftlistInfo>();
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isUpdate = true;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_list_layout);
	}
	
	@Override
	public void init() {
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}
	
	@Override
	public void initData() {
		try {
			String userid = SharedPreferencesControl.getInstance().getString("userid",Common.USERLOGIN_XML,this);
			String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML,this);
			result = ApiManager.pointlist(userid, token, page + "", PAGE_SIZE+"");
			page ++;
			listData = result.softList;
			
			adapter = new PointlistAdatper(this, listData);
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			adapter.notifyDataSetChanged();
			if(PAGE_TOTAL_SIZE==1){
				listView.setRefreshTag(false);
			}
			
			if (page <= PAGE_TOTAL_SIZE)
				listView.setRefreshDataListener(this);
		} catch (ApiException e) {
			showException(e.getMessage());
			showRefreshButton();
		}
		super.initData();
	}
	
	/* (non-Javadoc)
	 * @see com.appdear.client.commctrls.BaseActivity#updateUI()
	 */
	@Override
	public void updateUI() {
		if(listView==null)return;
		listView.setAdapter(adapter);
	}
	
	/**
	 * 当前项操作
	 * @param position
	 */
	public void setSelectedValues(int position) {
		
	}
	
	@Override
	public void refreshDataUI() {
		if (result == null || result.softList == null)
			return;
		listData.addAll(result.softList);
		page ++;
	}
	
	@Override
	public void doRefreshData() {
		try {
			if (page > PAGE_TOTAL_SIZE) {
				listView.setEndTag(true);
				return;
			}
			result = null;
			String userid = SharedPreferencesControl.getInstance().getString("userid",Common.USERLOGIN_XML,this);
			String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML,this);
			result = ApiManager.pointlist(userid, token, page + "", PAGE_SIZE+"");
		} catch (ApiException e) {
			listView.setErrTag(true);
		}
	}
}

 