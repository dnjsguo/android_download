/**
 * MoreActivity.java
 * created at:2011-01-07 下午01:57:29
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */
package com.appdear.client;

import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.appdear.client.Adapter.MatchTargetUsersGridAdatper;
import com.appdear.client.Adapter.SoftwarelistAdatper.ItemViewHolder;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.db.LabellistDB;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.BackgroundInfo;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiCatalogListResult;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.Utils;
import com.appdear.client.R;

/**
 * 万花筒-适用人群
 */
public class MatchTargetUsersGridActivity extends BaseActivity {
	private static final int MAX_VIEW_COUNT = 16;
	private String[][] tvDataArray = new String[MAX_VIEW_COUNT][3];

	public static List<BackgroundInfo> backgroundlist = null;
	public static int totalcountbackground;
	
	/**
	 * 类别列表数据   
	 */ 
	ApiCatalogListResult result = new ApiCatalogListResult();
	
	/**
	 * 栏目ID
	 */
	private int id = 104475;
	
	/**
	 * 栏目Key
	 */
	private final String CAT_KEY = "601";
	
	/**
	 * 数据库表的的type（根据type的不同缓存入不同的表）
	 */
	private final String DB_TYPE = "0";
	
	/**
	 * 当前页
	 */
	
	private String pageno = "1";
	
	/**
	 * 当前页数量
	 */
	private String count = "16";
	
	private boolean flag=false;
	

	/**
	 * 类别列表返回信息
	 */
	private ApiCatalogListResult catList = new ApiCatalogListResult();
	
	private String[] iconUrls = new String[16];
	private String[] names = new String[16];

	private GridView gridView;
	
	private String ctype = "1";
	MProgress loadingView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isUpdate=true;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		isShowAlert = false;
		setContentView(R.layout.match_targetusers_grid_layout);
		int nowServerVersion = AppContext.getInstance().labelversionresponse;
		if (nowServerVersion != Constants.LABELVERSIONFORCLIENT&&AppContext.getInstance().isNetState) {
			loadingView=new MProgress(this,true);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			this.addContentView(loadingView, params);
			if (!AppContext.getInstance().isNetState) {
				handler1.sendEmptyMessage(ListBaseActivity.LOADG);
				showRefreshButton();
				return;
			}
		}
	}
	public Handler handler1 = new Handler() {

		ItemViewHolder holder;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case ListBaseActivity.LOADV:
					if (loadingView != null)
						loadingView.setVisibility(View.VISIBLE);
					break;
				case ListBaseActivity.LOADG:
					if (loadingView != null)
						loadingView.setVisibility(View.GONE);
					break;
			}
		}
	};
	@Override
	public void init() {
		gridView = (GridView) findViewById(R.id.targetusers_list_grid);
	}

	@Override
	public void initData() {
		try {
			// 当前服务器版本、上次保存的服务器版本（第一次运行时savedLabelVersion因为取不到值等于0）
			int nowServerVersion = AppContext.getInstance().labelversionresponse;
			//int savedLabelVersion = SharedPreferencesControl.getInstance().getInt("labelversion", com.appdear.client.commctrls.Common.SETTINGS, this);
			
			if (nowServerVersion == Constants.LABELVERSIONFORCLIENT) {
				// 1，如果 服务器版本号=预置版本号，则从预置数据取
				tvDataArray = Constants.TARGETUSERS_ID_NAME_ARRAY;
			//	Utils.print("-----服务器版本号=预置版本号-->取内置数据-----");
			} else {
				// 2，否则从数据库或者服务器取
				if(!AppContext.getInstance().isNetState){
					tvDataArray = Constants.TARGETUSERS_ID_NAME_ARRAY;
				}else{
					catList = Utils.getCatListFromServerOrDB(this, pageno, count, 
							id, CAT_KEY, DB_TYPE, nowServerVersion, MAX_VIEW_COUNT);
					
					int dumpCount = catList.catalogList.size() > MAX_VIEW_COUNT ? MAX_VIEW_COUNT : catList.catalogList.size();
					if (dumpCount > 0) {
						ctype = catList.catalogList.get(0).ctype;
						for (int i = 0; i < dumpCount; i++) {
							CatalogListInfo info = catList.catalogList.get(i);
							tvDataArray[i][0] = info.catalogname;
							tvDataArray[i][1] = info.catalogid;
							tvDataArray[i][2] = info.catalogicon;
							/*if (i == dumpCount - 1)
								ctype = info.ctype;*/
						}
					}
					flag=true;
				}
			}

			// 获取icon与title数据
			Utils.getNameArray(tvDataArray, names);
			getUrlArray(tvDataArray, iconUrls);
		} catch (Exception e) {
			showRefreshButton();
			e.printStackTrace();
		}finally{
		//	Log.i("info90","LOADG=HomePageBestListActivity");
			handler1.sendEmptyMessage(ListBaseActivity.LOADG);
		}

		super.initData();
	}
	
	@Override
	public void updateUI() {
		gridView.setNumColumns(4);
		gridView.setGravity(Gravity.CENTER);
		gridView.setAdapter(new MatchTargetUsersGridAdatper(this, iconUrls, names));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String str = names[position];
				if (catList==null||catList.catalogList == null)
					return;
				//标题
				String categoryTitle = "适用人群-" + str;
				
				//显示类别详情
				Intent categorydetail = new Intent(MatchTargetUsersGridActivity.this,
						MatchSoftListActivity.class);
				categorydetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				categorydetail.putExtra("category_title", categoryTitle);
				categorydetail.putExtra("category_id", Utils.getIdByName(str, tvDataArray));
//				Utils.print("-----------------------");
//				Utils.print("适用人群-category_id: " + Utils.getIdByName(str, tvDataArray));
				categorydetail.putExtra("category_navigation", categoryTitle);
				categorydetail.putExtra("ctype", "3");
				categorydetail.putExtra("type",flag==true?"1":"0");
				startActivity(categorydetail);
			}
		});
		super.updateUI();
	}


	/**
	 * 从二维数组dataArray中取出name并填充至一位数组names中
	 * @param dataArray  二维数组（源数据）
	 * @param names  一位数组（目标数据）
	 * @return -1失败，0成功
	 */
	private int getUrlArray(String[][] dataArray, String[] iconUrls) {
		if (dataArray == null || iconUrls == null) {
			return -1;
		}
		try {
			for (int i = 0; i < MAX_VIEW_COUNT; i++) {
				iconUrls[i] = tvDataArray[i][2]; //取出每一个iconUrl填充至一位数组url中
			}
			return 0;
		} catch (Exception e) {
			return -1;
		}
	}
	@Override
	protected void updateUIStart() {
		if(loadingView!=null)
		loadingView.setVisibility(View.VISIBLE);
		// do nothing ,just override super class .
	}
}