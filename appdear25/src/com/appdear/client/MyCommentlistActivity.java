package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.Adapter.CommentListAdapter;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.ApiCommentResult;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ScreenManager;

public class MyCommentlistActivity extends ListBaseActivity {

 	private ApiCommentResult result;
	private List<SoftlistInfo> listdata = new ArrayList<SoftlistInfo>();
	private TextView wu;
	private RelativeLayout user_info_control;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_reviews_main_layout);
		ScreenManager.getScreenManager().pushActivity(this);//进栈
		
		params = new LayoutParams(width,
				height-58);
		loadingView=new MProgress(this,false);
		this.addContentView(loadingView, params);
	} 
	
	@Override
	public void init() {
		wu = (TextView) findViewById(R.id.wu);
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		user_info_control = (RelativeLayout) findViewById(R.id.user_info_control);
		user_info_control.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void initData() {
		try {
			result = ApiManager.getMyCommentList("0", "1", "1", page+"", PAGE_SIZE+"");
			if (result == null)
				return;
			page ++;
			
			listdata = result.list;
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			adapter = new CommentListAdapter(this, listdata, true);
			adapter.notifyDataSetChanged();
			if (page <= PAGE_TOTAL_SIZE)
				listView.setRefreshDataListener(this);
		} catch (ApiException e) {
			showMessageInfo("获取数据错误！");
			showRefreshButton();
		}
		super.initData();
	}
	
	@Override
	public void updateUI() {
		if(listView==null)return;
		if (listdata.size() != 0)
			wu.setVisibility(View.GONE);
		listView.setAdapter(adapter);
		super.updateUI();
	}

	@Override
	public void refreshDataUI() {
		if (result == null || result.list == null)
			return;
		listdata.addAll(result.list);
		page ++;
	}

	@Override
	public void doRefreshData() {
		if (page > PAGE_TOTAL_SIZE) {
			listView.setEndTag(true);
			return;
		}
		try {
			result = null;
			result = ApiManager.getMyCommentList("0", "1", "0", page+"", PAGE_SIZE+"");
		} catch (ApiException e) {
			showMessageInfo("获取数据错误");
			listView.setErrTag(true);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0&&AppContext.userreg==true) {
			AppContext.userreg=false;
			Intent userIntent = new Intent();
			//跳转到MainActivity ，在那个页面根据Flag加载想要的视图
			userIntent.setClass(this, MainActivity.class);
			userIntent.putExtra(SoftFormTags.ACTIVITY_SWITCH_FLAG, SoftFormTags.USER_LIST_CENTER);
			startActivity(userIntent);
			Log.i("number", "onKeyDown");
            return true;
        }
      

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		listView.removeAllViews();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				listdata = new ArrayList<SoftlistInfo>();
				initData();
				handler.sendEmptyMessage(UPDATE);
			}
		}).start();
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onDestroy() {
		ScreenManager.getScreenManager().pushActivity(this);//出栈
		super.onDestroy();
	}
}
