package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.appdear.client.Adapter.SoftCategoryListAdapter;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.api.ApiCatalogListResult;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.R;

/**
 * 类别首页--游戏列表
 * @author zhangxinyong
 *
 */
public class CategoryGameActivity extends ListBaseActivity implements OnItemClickListener{
 
	SoftlistInfo mSoftCategory = null;
	ArrayList<CatalogListInfo> mSoftCategoryList = null;
	List<CatalogListInfo> dataList =  new ArrayList<CatalogListInfo>() ;
	SoftCategoryListAdapter adapter = null;
	
	/**
	 * 栏目ID
	 */
	private int id = 100006;
	
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
	ApiCatalogListResult result = new ApiCatalogListResult();
	
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
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setOnItemClickListener(this);
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}

	@Override
	public void updateUI() {
		 if(listView!=null)
		 {
			 if(null!=adapter)
					this.listView.setAdapter(adapter);
					listView.setCacheColorHint(Color.TRANSPARENT);
					super.updateUI();   
		 }
		   
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		//标题
		String categoryTitle = result.catalogList.get(arg2).catalogname;
		//id
		String categoryId = result.catalogList.get(arg2).catalogid;
		//显示类别详情
		Intent categorydetail = new Intent(CategoryGameActivity.this,
				CategoryDetailListActivity.class);//change
		categorydetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		categorydetail.putExtra("category_title", categoryTitle);
		categorydetail.putExtra("category_id", categoryId);
		categorydetail.putExtra("category_navigation", "游戏-"+categoryTitle);
		startActivity(categorydetail);
	}
	
	@Override
	public void initData() {
		try {
			id = SharedPreferencesControl.getInstance().getInt("202", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			result = ApiManager.cataloglist(id+"", pageno, count);
			if(null==result.catalogList||result.catalogList.size()<=0){
				showException("加载列表为空");
				Log.d("TAG", "TAG:show  exception ");
				showRefreshButton();
			}else{
				dataList.addAll(result.catalogList);
				adapter = new SoftCategoryListAdapter(this, dataList, listView);
			}
			
		}catch (Exception e){
			showException(e);
			e.printStackTrace();
			showRefreshButton();
		}finally{
			handler1.sendEmptyMessage(LOADG);
		}
		super.initData();
	}

	@Override
	public void refreshDataUI() {
	}

	@Override
	public void doRefreshData() {
	}
	
	@Override
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
