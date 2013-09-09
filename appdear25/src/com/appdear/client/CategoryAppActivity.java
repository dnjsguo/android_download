package com.appdear.client;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.appdear.client.Adapter.SoftCategoryListAdapter;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiCatalogListResult;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;

/**
 * 类别首页--应用列表
 * @author zhangxinyong
 *
 */
public class CategoryAppActivity extends ListBaseActivity implements OnItemClickListener,Constants {
	/**
	 * 加载Bar
	 */
	ProgressDialog  progressDialog = null;
	CatalogListInfo mSoftCategory = null;
	List<CatalogListInfo> mSoftCategoryList = null;
	List<CatalogListInfo> temp = null;
	SoftCategoryListAdapter adapter = null;
	/**
	 * 类别列表数据   
	 */ 
	ApiCatalogListResult result = new ApiCatalogListResult();
	
	/**
	 * 栏目ID
	 */
	private int id = 100005;
	
	/**
	 * 当前页
	 */
	
	private String pageno = "1";
	
	/**
	 * 当前页数量
	 */
	private String count = "20";
	
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
		listView.requestFocus();
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		if (result==null||result.catalogList == null)
			return;
		 
		if(!ServiceUtils.checkNetState(CategoryAppActivity.this)){
			Toast.makeText(CategoryAppActivity.this,"无网络状态！",Toast.LENGTH_LONG).show();
			return;
		}
		//标题
		String categoryTitle = result.catalogList.get(arg2).catalogname;
		//id
		String categoryId = result.catalogList.get(arg2).catalogid;
		//显示类别详情
		/*Intent categorydetail = new Intent(CategoryAppActivity.this,
				CategoryDetailsActivity.class);
		categorydetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		categorydetail.putExtra("category_title", categoryTitle);
		categorydetail.putExtra("category_id", categoryId);
		categorydetail.putExtra("category_navigation", "应用-"+categoryTitle);
		startActivity(categorydetail);*/
		Intent categorydetail = new Intent(CategoryAppActivity.this,
				CategoryDetailListActivity.class);
		categorydetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		categorydetail.putExtra("category_title", categoryTitle);
		categorydetail.putExtra("category_id", categoryId);
		categorydetail.putExtra("category_navigation", "应用-"+categoryTitle);
		startActivity(categorydetail);
	}
	
	@Override
	public void initData() {
		try {
			//113671
			id = SharedPreferencesControl.getInstance().getInt("201", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			result = ApiManager.cataloglist(id+"", pageno, count);
			if(null==result.catalogList){
				showException("加载列表为空");
				showRefreshButton();
			}else{
				mSoftCategoryList = result.catalogList;
				adapter = new SoftCategoryListAdapter(this,mSoftCategoryList, listView);
			}
			
		} catch (Exception e) {
			showException(e);
			e.printStackTrace();
			showRefreshButton();
		}finally{
			handler1.sendEmptyMessage(LOADG);
		}
		super.initData();
	}
	
	@Override
	public void updateUI() {
		if(listView!=null){
			if(null!=adapter)
				this.listView.setAdapter(adapter);
				listView.setOnItemClickListener(this);
				listView.setCacheColorHint(Color.TRANSPARENT);
				super.updateUI();
		}
		
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
