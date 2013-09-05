package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.appdear.client.Adapter.SoftCategoryListAdapter;
import com.appdear.client.Adapter.SoftSubjectCategoryListAdapter;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.api.ApiCatalogListResult;
import com.appdear.client.service.api.ApiManager;

/**
 * 类别首页--精彩专题
 * 
 * @author zhangxinyong
 *
 */
public class CategoryTopicActivity extends ListBaseActivity implements OnItemClickListener{
	 
	
	/**
	 * 
	 */
	private  List<CatalogListInfo> calList = new ArrayList<CatalogListInfo>();
	
	/**
	 * 适配器
	 */
//	private SoftCategoryListAdapter adapter =  null;
//	private result.catalogList adapter = null;
	
	/**
	 * 栏目ID
	 */
	private int id = 100007;
	
	/**
	 * 当前页
	 */
	private String pageno = "1";
	
	/**
	 * 当前页数量
	 */
	private String count = "20";
	
	/**
	 * 游戏类别列表数据
	 */
	private ApiCatalogListResult result = new ApiCatalogListResult();
	
	public void onCreate(Bundle b){
		super.onCreate(b);
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
		listView = (ListViewRefresh)findViewById(R.id.soft_list);
		listView.setOnItemClickListener(this);
	/*	listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}
	
	@Override
	public void updateUIErr(String msg) {
		super.updateUIErr(msg);
	}
	
	@Override
	public void updateUI() {
		if(listView!=null)
		{
			if(null!=adapter)
				this.listView.setAdapter(adapter);
				listView.setCacheColorHint(Color.TRANSPARENT);
				listView.setOnItemClickListener(this);
				super.updateUI();
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		if (result==null||result.catalogList == null)
			return;
		//标题
		String categoryTitle = result.catalogList.get(arg2).catalogname;
		//id
		String categoryId = result.catalogList.get(arg2).catalogid;
		//显示类别详情
		Intent categorydetail = new Intent(CategoryTopicActivity.this,
				CategorySubjectDetailsActivity.class);
		categorydetail.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		categorydetail.putExtra("category_title", categoryTitle);
		categorydetail.putExtra("category_id", categoryId);
		categorydetail.putExtra("category_navigation", categoryTitle);
		startActivity(categorydetail);
	}
	
	@Override
	public void initData() {
		try {
			id = SharedPreferencesControl.getInstance().getInt("203", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			result = ApiManager.cataloglist(id+"", pageno, count);
			if(null==result.catalogList||result.catalogList.size()<=0){
				showException("加载列表为空");
				showRefreshButton();
			}else{
				calList = result.catalogList;
//				adapter = new SoftCategoryListAdapter(this, result.catalogList, mListView);
				adapter = new SoftSubjectCategoryListAdapter(this, result.catalogList, listView);
			}
		} catch (Exception e) {
			showException(e);
			e.printStackTrace();
			showRefreshButton();
		}
		super.initData();
	}

	@Override
	public void refreshDataUI() {
	}

	@Override
	public void doRefreshData() {
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void dataNotifySetChanged() {
		if(adapter==null)return;
		adapter.notifyDataSetChanged();
		super.dataNotifySetChanged();
	}
}
