/**
 * TheNewSoftListView.java
 * created at:2011-5-20下午03:08:11
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.appdear.client.Adapter.StorelistAdatper;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.ShopModel;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiShopListRequest;
import com.appdear.client.service.api.ApiShopListResult;
import com.appdear.client.service.api.ApiSoftListResult;
  
/** 
 * 积分
 * 
 * @author jdan
 */
public class StoreListActivity extends ListBaseActivity {
 

	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	ApiShopListResult shopListResult ;
	/**
	 *  详细信息列表
	 */
	private List<ShopModel> listData = new ArrayList<ShopModel>();
	
	private String area = "";
	private String chcode ="";
	private TextView wualert;
	private boolean first;
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isUpdate = true;
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
		area = this.getIntent().getStringExtra("area");
		chcode = this.getIntent().getStringExtra("androidchchode");
		System.out.println(area+"-----------------"+chcode);
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		first=this.getIntent().getBooleanExtra("first", false);
		wualert = (TextView) findViewById(R.id.alert);
		wualert.setText("请选择省市和手机品牌");
		wualert.setVisibility(View.INVISIBLE);
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}
	
	@Override
	public void initData() {
		try {
			if(area!=null&&!"".equals(area)){
				try {
					shopListResult = ApiShopListRequest.ShopListRequest(area,"1",chcode);
				} catch (ServerException e) {
					showException(e);
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) { 
					showException(e);
					e.printStackTrace();
				}
			
			if(shopListResult==null)return;
			listData = shopListResult.shopList;
			adapter = new StorelistAdatper(this, listData);
			adapter.notifyDataSetChanged();
			}
			
		} catch (ApiException e) {
			showException(e.getMessage());
			showRefreshButton();
		}finally{
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
		if(listData==null||listData.size()==0){
			wualert.setVisibility(View.VISIBLE);
			if(first==false){
				wualert.setText("抱歉,暂无数据!");
			}
		}else{
			wualert.setVisibility(View.INVISIBLE);
		}
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
	}
	
	@Override
	public void doRefreshData() {
//		if (page > PAGE_TOTAL_SIZE) {
//			listView.setEndTag(true);
//			return;
//		}
//		try {
//			shopListResult = null;
//			shopListResult = ApiShopListRequest.ShopListRequest(area, page+"", PAGE_SIZE+"");
//		} catch (ServerException e) {
//			listView.setErrTag(true);
//		} catch (ApiException e) {
//			listView.setErrTag(true);
//		} catch (UnsupportedEncodingException e) {
//			listView.setErrTag(true);
//		}
	}
}


 