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

import com.appdear.client.Adapter.MessageAdatper;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.R;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
  
/** 
 * 消息
 * 
 * @author jdan
 */
public class MessageListActivity extends ListBaseActivity {

	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	
	/**
	 *  详细信息列表
	 */
	private List<SoftlistInfo> listData = null;
	
	private boolean preregister=false;
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isUpdate = true;
		preregister=this.getIntent().getBooleanExtra("preregister",false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_list_layout);
		
		params = new LayoutParams(width,
				height-70);
		loadingView=new MProgress(this,false);
		this.addContentView(loadingView, params);
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
			String count = PAGE_SIZE+"";
			result = ApiManager.messagelist(userid, token, page + "", count);
			listData = result.softList;
			page ++;
			if((listData!=null&&listData.size()<1)&&preregister==true){
				SoftlistInfo info=new SoftlistInfo();
				String passwd=SharedPreferencesControl.getInstance().getString("passwd",com.appdear.client.commctrls.Common.USERPASSWDXML,this);
				info.text="恭喜您已成功注册为爱皮会员，您的初始密码为"+passwd+"，请及时修改为便于您记忆的密码。";
				listData.add(info);
			}
			adapter = new MessageAdatper(this, listData);
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			adapter.notifyDataSetChanged();
			
			if (page <= PAGE_TOTAL_SIZE)
				listView.setRefreshDataListener(this);
		} catch (ApiException e) {
			if (Constants.DEBUG)Log.e("net error:",e.getMessage(), e);
			showException(e);
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
			String count = PAGE_SIZE+"";
			listData = result.softList;
			result = ApiManager.messagelist(userid, token, page+"", count);
		} catch (ApiException e) {
			listView.setErrTag(true);
		}
	}
}

 