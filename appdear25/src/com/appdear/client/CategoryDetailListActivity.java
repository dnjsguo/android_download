package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.BackgroundInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.utility.BitmapTemp;
import com.appdear.client.utility.ImageCache;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;

/**
 * �������б�--����б�
 * 
 * @author zhangxinyong
 *
 */
public class CategoryDetailListActivity extends ListBaseActivity implements  OnItemClickListener  {
 
	//change
	private LinearLayout ll_navigation;
	TextView tv_navigation;
	ImageButton btn_return;
	RelativeLayout tab_ll_linear;
	String categoryId = null;
	String categoryTitle = null;
	
	/**
	 * ����
	 */
	private ApiSoftListResult result = null;
	
 
	
	/**
	 * �б�����
	 */
	private List<SoftlistInfo> listData = new ArrayList<SoftlistInfo>();
	
	/**
	 * ��Ŀid
	 */
	private String id = "1";
	
	/**
	 * �Ʒ�����
	 */
	private String fee = "0";
	
	/**
	 * ����ʽ
	 */
	private String orderType = "4";//4 ��������������
	
	private Intent intent = null;
	/**
	 *  ������ʱ��Ŀid
	 */
	private String  tempId = "-1";
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
 		setContentView(R.layout.soft_list_layout);
 		params = new LayoutParams(width,
				height*3/4);
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
		//change
		ll_navigation = (LinearLayout) findViewById(R.id.ll_navigation);
		ll_navigation.setVisibility(View.VISIBLE);
		tv_navigation = (TextView) findViewById(R.id.tv_navigation);
		tab_ll_linear = (RelativeLayout) findViewById(R.id.layout);
		tab_ll_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_return = (ImageButton) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		categoryTitle = this.getIntent().getStringExtra("category_title");
		categoryId = this.getIntent().getStringExtra("category_id");
		tv_navigation.setText(this.getIntent().getStringExtra("category_navigation"));
		listView = (ListViewRefresh)findViewById(R.id.soft_list);
		this.getWindow().getDecorView().requestFocus();
		listView.setOnItemClickListener(this);
		
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		//init parameter .
		this.intent = this.getIntent();
		int tabIndex = getIntent().getIntExtra("tabIndex", 1);
		initParameter(tabIndex);
		this.setAdapter(adapter);
		id = getIntent().getStringExtra("category_id");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.setClass(this, SoftwareMainDetilActivity.class);
		intent.putExtra("downloadurl",listData.get(arg2).downloadurl);
		intent.putExtra("softicon",  listData.get(arg2).softicon);
		intent.putExtra("softid", listData.get(arg2).softid);
		intent.putExtra("cataid", "cataid-"+id);
		intent.putExtra("downloadcount",listData.get(arg2).download);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void refreshDataUI() {
		if(listData==null){
			listData = new ArrayList<SoftlistInfo>();
		}
		if(result!=null){
			listData.addAll(result.softList);
			page ++;
		}
	}
	
	@Override
	public void doRefreshData() {
		if (page > PAGE_TOTAL_SIZE) {
			listView.setEndTag(true);
			return;
		}
		try {
			result = null;
			result = ApiManager.catalogsoftlist(id, 
					fee, orderType, page + "", PAGE_SIZE + "");
			ServiceUtils.setSoftState(this,result.softList);
		}catch (ApiException e) {
			showException(e);
			listView.setErrTag(true);
		}
	}
	
	@Override
	public void updateUI() {
		if(listView!=null){
			if(adapter!=null)
			listView.setAdapter(adapter);
			listView.setCacheColorHint(Color.TRANSPARENT);
			listView.setOnItemClickListener(this);
		}
		super.updateUI();
	}
	
	@Override
	public void initData() {
		try {
			id = getIntent().getStringExtra("category_id");

			if(!id.equals(tempId)){
				listData.clear();
				page =1;
				//�������ID
				tempId = id;
				listView.setEndTag(false);
				listView.setRefreshTag(true);
			}
			initResult();
			if (page <= PAGE_TOTAL_SIZE)
				listView.setRefreshDataListener(this);
		} catch (Exception e) {
			showException(e);
			e.printStackTrace();
			showRefreshButton();
		}
		super.initData();
	}

	@Override
	protected void onNewIntent(final Intent intent) {
		if ("refresh".equals(intent.getStringExtra("state"))) {
			new Thread(new Runnable() {
				@Override 
				public void run() {
					CategoryDetailListActivity.this.intent = intent;
					id = intent.getStringExtra("category_id");
					//init parameter .
					int tabIndex = getIntent().getIntExtra("tabIndex", 1);
					initParameter(tabIndex);
					if(!id.equals(tempId)){
						listData.clear();
						page = 1;
						//�������ID
						tempId = id;
						listView.setEndTag(false);
						listView.setRefreshTag(true);
					}
					initResult();
				    handler.sendEmptyMessage(1);
				}
			}).start();
		}
		super.onNewIntent(intent);
	}

	/**
	 * ˢ�½���
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if(list!=null)
					listView.setAdapter(adapter);
				/*if(adapter!=null)
					adapter.notifyDataSetChanged();*/
				dataNotifySetChanged();
				break;
			case 2:
				/*if(adapter!=null)
					adapter.notifyDataSetChanged();*/
				dataNotifySetChanged();
				break;
			}
		}
	};
	
	
	/**
	 * ��ʼ������
	 */
	private void initParameter(int tabIndex){
		switch(tabIndex){
		case 1:
			
			//��������
			fee = "0";
			orderType = "7";
			id = intent.getStringExtra("category_id");
			break;
		case 2:
			//�������
			fee = "0";
			orderType = "4";
			id = intent.getStringExtra("category_id");
			break;
		case 3:
			//�����ϼ�
			fee = "0";
			orderType = "6";
			id = intent.getStringExtra("category_id");
			break;
		}
	}
	
	/**
	 * result ��ֵ
	 */
	private void initResult(){
		try {
			result = ApiManager.catalogsoftlist(id, fee, orderType, page + "", PAGE_SIZE + "");
			if (result == null)
				return;
			listData = result.softList;
			ServiceUtils.setSoftState(this,listData);
			adapter = new SoftwarelistAdatper(CategoryDetailListActivity.this, listData, listView);
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			page++;
			handler.sendEmptyMessage(2);
		}catch (Exception e) {
			showException(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void dataNotifySetChanged() {
		if(adapter==null)return;
		actionLayoutNotShow();
		adapter.notifyDataSetChanged();
		super.dataNotifySetChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		listData.clear();
		listData=null;
//		adapter=null;
		super.onDestroy();
	}
}
