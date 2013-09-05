/**
 * TheNewSoftListView.java
 * created at:2011-5-20下午03:08:11
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

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
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;

/** 
 * 装机必备
 * 修改记录
 * 2011 - 09 - 13 最新上架更改为 新鲜玩意儿
 * 
 * @author zqm
 */
public class HomePageNewSoftListActivity extends ListBaseActivity {

 
	
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
	private int id = 100001;
	
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
		listView.requestFocus();
	/*	listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}
	
	@Override
	public void initData() {
		try {
			id = SharedPreferencesControl.getInstance().getInt("107", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			result = ApiManager.softlist(id+"", page + "", PAGE_SIZE + "");

			if (result == null)
				return;
			page ++;
			listData = result.softList;
			adapter = new SoftwarelistAdatper(this, listData, listView);
			ServiceUtils.setSoftState(this,listData);
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			adapter.notifyDataSetChanged();
			if (page <= PAGE_TOTAL_SIZE)
				listView.setRefreshDataListener(this);
		}catch (ApiException e) {
			Log.e("net error:",e.getMessage(), e);
			showException(e);
			showRefreshButton();
		} catch (OutOfMemoryError error) {
			showRefreshButton();
		}finally{
			Log.i("info90","LOADG=HomePageNewSoftListActivity");
			handler1.sendEmptyMessage(LOADG);
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
		ServiceUtils.setSelectedValues(this,listData, position);
	}
	
	@Override
	public void refreshDataUI() {
		if (result == null)
			return;
		listData.addAll(result.softList);
		page ++;
	}
	
	@Override
	public void doRefreshData() {
		if (Constants.DEBUG)
			Log.i("page....page....", page + "");
		if (page > PAGE_TOTAL_SIZE) {
			listView.setEndTag(true);
			return;
		}
		try {
			result = null;
			result = ApiManager.softlist(id+"", page + "", PAGE_SIZE + "");
			ServiceUtils.setSoftState(this,result.softList); 
		} catch (ApiException e) {
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
}

 