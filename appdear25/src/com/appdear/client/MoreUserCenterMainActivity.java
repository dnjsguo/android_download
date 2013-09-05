package com.appdear.client;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.appdear.client.Adapter.UserCenterAdapter;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.UesrInfoLayout;
import com.appdear.client.model.GridInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.R;

public class MoreUserCenterMainActivity extends BaseActivity{
	ArrayList<GridInfo> list = new ArrayList<GridInfo>();
	public Toast toast=null;
	private UesrInfoLayout userlayout;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		toast=new Toast(MoreUserCenterMainActivity.this);
		setContentView(R.layout.more_user_center_grid_layout);
		userChildType = -1;
		ScreenManager.getScreenManager().pushActivity(this);//进栈
	}

	@Override
	public void init() {
		if (AppContext.getInstance().spreurl == null || AppContext.getInstance().spreurl.equals("")) {
			AppContext.getInstance().spreurl = SharedPreferencesControl.getInstance().getString(
					"spreurl", com.appdear.client.commctrls.Common.SETTINGS, this);
		}
		if (AppContext.getInstance().dpreurl == null || AppContext.getInstance().dpreurl.equals("")) {
			AppContext.getInstance().dpreurl = SharedPreferencesControl.getInstance().getString(
					"dpreurl", com.appdear.client.commctrls.Common.SETTINGS, this);
		}
		ListViewRefresh gv = (ListViewRefresh)this.findViewById(R.id.soft_list);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.requestFocus();
		gv.setDivider(getResources().getDrawable(R.drawable.listspiner));
		gv.setDividerHeight(2);
		initJiuGongGe();
		UserCenterAdapter adapter =  new UserCenterAdapter(this, list);
		gv.setAdapter(adapter);
	}

	private void initJiuGongGe(){
		GridInfo jiu1 = new GridInfo();
		jiu1.imageId = R.drawable.usercenter_33;
		jiu1.text = "个人中心";
		jiu1.arrowImage = R.drawable.right_button_show;
		list.add(jiu1);

		GridInfo jiu4 = new GridInfo();
		jiu4.imageId = R.drawable.usercenter_36;
		jiu4.text = "我的积分";
		jiu4.arrowImage = R.drawable.right_button_show;
		list.add(jiu4);
		
		GridInfo jiu5 = new GridInfo();
		jiu5.imageId = R.drawable.usercenter_37;
		jiu5.text = "我的收藏";
		jiu5.arrowImage = R.drawable.right_button_show;
		list.add(jiu5);
		
		GridInfo jiu6 = new GridInfo();
		jiu6.imageId = R.drawable.usercenter_38;
		jiu6.text = "消息盒子";
		jiu6.arrowImage = R.drawable.right_button_show;
		list.add(jiu6);
		
		GridInfo jiu8 = new GridInfo();
		jiu8.imageId = R.drawable.alter_pwd;
		jiu8.text = "修改密码";
		jiu8.arrowImage = R.drawable.right_button_show;
		list.add(jiu8);
		
		GridInfo jiu9 = new GridInfo();
		jiu9.imageId = R.drawable.ico_wdpl_normal;
		jiu9.text = "我的评论";
		jiu9.arrowImage = R.drawable.right_button_show;
		list.add(jiu9);
		
		GridInfo jiu10 = new GridInfo();
		jiu10.imageId = R.drawable.ico_yydz_normal;
		jiu10.text = "应用定制";
		jiu10.arrowImage = R.drawable.right_button_show;
		list.add(jiu10);
		
		GridInfo jiu7 = new GridInfo();
		jiu7.imageId = R.drawable.userlogout;
		jiu7.text = "注销";
		jiu7.arrowImage = R.drawable.right_button_show;
		list.add(jiu7);
	}
	
	@Override
	protected void onResume() {
		userChildType = -1;
		if (userlayout == null)
			userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
		Message message = userlayout.handler.obtainMessage();
		message.what=2;
		userlayout.handler.sendMessage(message);
		super.onResume();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		if (userlayout == null)
			userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
		Message message = userlayout.handler.obtainMessage();
		message.what=2;
		userlayout.handler.sendMessage(message);
		super.onNewIntent(intent);
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getScreenManager().popActivity(this);//出栈
		super.onDestroy();
	}
}
