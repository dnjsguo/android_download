/**
 * created at:2012-2-28下午02:38:14
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;
  
/** 
 * 万花筒-软件列表
 */
public class MatchSoftListActivity extends ListBaseActivity {

	/**
	 * 头部标志
	 */
	TextView mTv_categoryDetailTitle = null;
	TextView detail_info =  null;
	String categoryId = null;
	String categoryTitle = null;
	Intent onNewIntent;
	String tempCategoryId;
	
	TextView tv_navigation;
	ImageButton btn_return;
	 
	
	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	
	/**
	 *  List<SoftlistInfo>
	 */
	private List<SoftlistInfo> listData = null ;
	
	RelativeLayout tab_ll_linear=null;
	
	/**
	 * 栏目id
	 */
	private int id = 100001;
	
	private String catdesc="";
	
	private String ctype = "0";
	
	private String type=null;
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category_app_subject_detail_layout);
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
		tv_navigation = (TextView) findViewById(R.id.tv_navigation);
		//detail_info = (TextView) findViewById(R.id.detail_info);
		detail_info=new TextView(this);
		detail_info.setPadding(20,20,20,20);
		detail_info.setTextColor(Color.parseColor("#838282"));
		detail_info.setBackgroundColor(Color.parseColor("#ededed"));
		detail_info.setGravity(Gravity.CENTER);
		detail_info.setTextSize(18);
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.requestFocus();
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		
		//设置标题文字
		categoryTitle = this.getIntent().getStringExtra("category_title");
		categoryId = this.getIntent().getStringExtra("category_id");
//		System.out.println("--------categoryId--------------");
//		System.out.println("MatchSoftListActivity-categoryId: " + categoryId);
		tv_navigation.setText(this.getIntent().getStringExtra("category_navigation"));
		ctype = getIntent().getStringExtra("ctype");
		type=getIntent().getStringExtra("type");
	//	detail_info.setText(this.getIntent().getStringExtra("category_navigation")+"撒旦法发生地方撒旦法撒地方撒地方撒旦法撒");
		this.tempCategoryId = categoryId;
		tab_ll_linear = (RelativeLayout) findViewById(R.id.layout);
		tab_ll_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//返回键
		btn_return = (ImageButton) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	@Override
	public void initData() {
		try {
//			id = SharedPreferencesControl.getInstance().getInt("103", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			
			if (ctype.equals("0")) {
				//labelsoftlist
			//	System.out.println("----ctype.equals(0)----labelsoftlist");
				result = ApiManager.Labelsoftlist(categoryId+"", "0", "0", page+"", PAGE_SIZE+"");
			} else if (ctype.equals("1")) {
			//	System.out.println("----ctype.equals(1)----labellistbycatid");
				//labellistbycatid
				result = ApiManager.labellistbycatid(categoryId+"", page+"", PAGE_SIZE+"", "0");	
			} else if (ctype.equals("3")) {
				if(type!=null){
					result = ApiManager.softlistbylabelcatid(categoryId+"", "0", "0", page+"", PAGE_SIZE+"",type);
				}else{
					result = ApiManager.softlistbylabelcatid(categoryId+"", "0", "0", page+"", PAGE_SIZE+"");
				}
			
			}
			
			if (result == null)
				return;
			page ++;
			listData = result.softList;
			catdesc=result.catdesc;
			adapter = new SoftwarelistAdatper(this, listData, listView);
			ServiceUtils.setSoftState(this,listData);
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			adapter.notifyDataSetChanged();
			
			if (page <= PAGE_TOTAL_SIZE){
				if(listView!=null)
				listView.setRefreshDataListener(this);
			}
		}catch (ApiException e) {
			Log.e("net error:",e.getMessage(), e);
			showException(e);
			showRefreshButton();
		} catch (OutOfMemoryError error) {
			showRefreshButton();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.initData();
	}

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
		ServiceUtils.setSelectedValues(this, listData, position,"labelid-"+categoryId);
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
		if(listView==null)return;
		if (Constants.DEBUG)
			Log.i("page....page....", page + "");
		if (page > PAGE_TOTAL_SIZE) {
			listView.setEndTag(true);
			return;
		}
		try {
			result = null;
			if (ctype.equals("0")) {
				//labelsoftlist
			//	System.out.println("----ctype.equals(0)----labelsoftlist");
				result = ApiManager.Labelsoftlist(categoryId+"", "0", "0", page+"", PAGE_SIZE+"");
			} else if (ctype.equals("1")) {
			//	System.out.println("----ctype.equals(1)----labellistbycatid");
				//labellistbycatid
				result = ApiManager.labellistbycatid(categoryId+"", page+"", PAGE_SIZE+"", "0");	
			} else if (ctype.equals("3")) {
				if(type!=null){
					result = ApiManager.softlistbylabelcatid(categoryId+"", "0", "0", page+"", PAGE_SIZE+"",type);
				}else{
					result = ApiManager.softlistbylabelcatid(categoryId+"", "0", "0", page+"", PAGE_SIZE+"");
				}
			}
			ServiceUtils.setSoftState(this,result.softList); 
		} catch (ApiException e) {
			Log.e("net error:",e.getMessage(), e);
			showException(e);
			listView.setErrTag(true);
		} catch (JSONException e) {
			showException(e);
			listView.setErrTag(true);
			e.printStackTrace();
		}
	}
	
	@Override
	public void dataNotifySetChanged() {
		if(adapter==null)return;
		adapter.notifyDataSetChanged();
		super.dataNotifySetChanged();
	}
	@Override
	public void finish() {
 		super.finish();
	}
 
}