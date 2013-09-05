/**
 * CommentActivity.java
 * created at:2011-5-20下午04:56:49
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.appdear.client.Adapter.CommentListAdapter;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.R;

/** 
 * 软件详情界面--评论
 * 
 * @author zqm
 */
public class SoftwareDetailCommentActivity extends ListBaseActivity {
	
 
	
	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	
	/**
	 * 提交评论
	 */
	private Button button_submit;
	
	/**
	 * 编辑评论
	 */
	private EditText editText;
	
	/**
	 * 评分
	 */
	private RatingBar bar;
	/**
	 * userid
	 */
	
	private String userid = "0";
	// 加载数据失败
	private boolean isNetProblem =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		flag=false;
		setContentView(R.layout.software_detail_comment_info);
	}
	private void initGif(){
		if(height==0&&width==0){
			height = this.getWindowManager().getDefaultDisplay().getHeight();
			width=this.getWindowManager().getDefaultDisplay().getWidth();
		}
		LayoutParams params = new LayoutParams(width,
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
		initGif();
		listView = (ListViewRefresh) findViewById(R.id.comment_list);
		listView.setFadingEdgeLength(0);//取消渐变色显示
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		button_submit = (Button) findViewById(R.id.button_submit);
		editText = (EditText) findViewById(R.id.input_comment);
		bar = (RatingBar) findViewById(R.id.star_level);
		button_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View view = SoftwareDetailCommentActivity.this.getCurrentFocus();
				if (view != null) {
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					inputMethodManager
							.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				if(!AppContext.getInstance().isNetState){
					showMessageInfo("网络不可用，请开启网络提交评论!");
					return;
				}
				if (result != null && result.softList.size() > 0) {
					for (SoftlistInfo info : result.softList) {
						if (info.commentimei.equals(AppContext.getInstance().IMEI)) {
							showMessageInfo("您已经评论过，不能重复评论!");
							return;
						}
					}
				}
				if("".equals(editText.getText().toString().trim())){
					showMessageInfo("评论内容不可为空!");
					return;
				}
				if(editText.getText().toString().trim().length()>70){
					showMessageInfo("评论字数不可超过70字!");
					return;
				}if(bar.getRating() <= 0){
					showMessageInfo("请对软件进行评分！");
					return;
				}
				button_submit.setClickable(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							 
							showMessageInfo("正在提交,请稍候...");
							String token = "";
							userid = SharedPreferencesControl.getInstance().getString("userid", com.appdear.client.commctrls.Common.USERLOGIN_XML,SoftwareDetailCommentActivity.this)+"";
							token = SharedPreferencesControl.getInstance().getString("sessionid", com.appdear.client.commctrls.Common.USERLOGIN_XML,SoftwareDetailCommentActivity.this);
							//真实数据token未登录为null,userid未登录为“0”
							ApiNormolResult result = ApiManager.commentcommit((userid.equals("") || userid == null)?"0":userid, 
									(token.equals("") || token == null)?"0":token,
									getIntent().getStringExtra("softid"), editText.getText().toString(),
									bar.getProgress()*10/2+"");
							String alertinfo = "";
							if (result.isok == 1) {
								alertinfo = "提交成功";
								refreshUI("commitcomment", "1");
							} else if (result.isok == 2) {
								alertinfo = "有敏感词，您的评论已被屏蔽！";
							} else if (result.isok == 3) {
								alertinfo = "您已经评论过，不能重复评论！";
							} else if (result.isok == 4) {
								alertinfo = "请下载后再评论！";
							} else {
								alertinfo = "评论失败！";
							}
							showMessageInfo(alertinfo);		
							refreshUI("commitcomment", "fail");
						} catch (ApiException e) {
							refreshUI("commitcomment", "fail");
							if (Constants.DEBUG)Log.e("net error:",e.getMessage(), e);
							showException(e);
						}catch (OutOfMemoryError e) {
							 AsyLoadImageService.getInstance().getImageCache().clear();
							System.gc();
							e.printStackTrace();
							Log.e("load image", "内存溢出啦");
						}
					}
				}).start();
			}
		});	
	}

	@Override
	public void refreshUI(Bundle budle) {
		if (budle.getString("commitcomment").equals("1")) {
			//将提交成功的评论添加到评论列表中
			SoftlistInfo info = new SoftlistInfo();
			info.commentid = "0";
			//获取系统时间
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			info.text = editText.getText().toString();
			info.time = format.format(new Date());
			String name = SharedPreferencesControl.getInstance().getString("nickname", com.appdear.client.commctrls.Common.USERLOGIN_XML,SoftwareDetailCommentActivity.this)+"";
			info.username = (name.equals("") || name == null)? "爱皮网友" : name;
			info.userid = (userid.equals("") || userid == null)?"0":userid;
			info.softgrade = bar.getProgress()*10/2;
			if (result == null)
				result = new ApiSoftListResult();
			if (result.softList.size() == 0)
				result.softList.add(info);
			else
				result.softList.add(0, info);
			//清界面
			editText.setText("");
			bar.setProgress(0);
			button_submit.setClickable(true);
			listView.setAdapter(adapter); // 若无此语句，则在空评论情况下进行评论不会刷新界面
		}else if (budle.getString("commitcomment").equals("fail")) {
			button_submit.setClickable(true);
		}
		super.refreshUI(budle);
	}
	
	@Override
	public void initData() {
		try {
 			result = ApiManager.commentlist(getIntent().getStringExtra("softid"), "1", "1", "6");
			if (result == null) 
				return;
			adapter = new CommentListAdapter(this, result.softList, false);
			adapter = (CommentListAdapter) adapter;
			adapter.notifyDataSetChanged();
		} catch (ApiException e) {
			if (Constants.DEBUG)Log.e("net error:",e.getMessage(), e);
			isNetProblem=true;
			showException(e);
		}finally{
			handler1.sendEmptyMessage(LOADG);
		}
		super.initData();
	}
	
	@Override
	public void updateUI() {
		if(listView==null)return;
		listView.setAdapter(adapter);

		//如果适配器为空，即没有评论，则显示提示信息
		String content;
		if(isNetProblem )
		{
			  content = "网络问题，加载数据失败！";
			  isNetProblem=false;
		}else
		{
		       content = "目前还没有人评论，你可以抢个沙发哦~";
		}
		listView.setEmptyView(inflateEmptyView(this, content)); // 设置空Adapter情况下的view
	}

	/**
	 * 填充一个内容为content的TextView
	 * @param context
	 * @param content  TextView的显示内容
	 * @return
	 */
	private View inflateEmptyView(Context context, String content) {
		TextView emptyView = new TextView(context);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		emptyView.setText(content);
		emptyView.setVisibility(View.GONE);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL);
		
		ViewGroup group = (ViewGroup) listView.getParent();
		group.setBackgroundColor(Color.TRANSPARENT); //设置背景透明
		group.addView(emptyView);
		
		return emptyView;
	}

	@Override
	public void refreshDataUI() {
		
	}

	@Override
	public void doRefreshData() {
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		result=null;
		super.onDestroy();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if(false){
			super.onSaveInstanceState(outState);
		}
	}
	
}

 