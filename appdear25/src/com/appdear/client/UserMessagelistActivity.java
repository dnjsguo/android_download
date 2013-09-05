package com.appdear.client;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.UesrInfoLayout;
import com.appdear.client.R;

public class UserMessagelistActivity extends BaseGroupActivity {

	private LocalActivityManager manager;
	
	private LinearLayout bestListLayout; 
	private final static int POINTLISTEND = 2;
	private boolean preregister = false;
	
	public void onCreate(Bundle b){
		super.onCreate(b); 
		Intent intent = this.getIntent();
		preregister=intent.getBooleanExtra("preregister",false);
		setContentView(R.layout.user_center_common_layout);
	}
	
	@Override
	public void init() {
		bestListLayout = (LinearLayout) this.findViewById(
				R.id.common_list_layout);
		manager = getLocalActivityManager();
	}
	
	private Handler handlerMessage=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case POINTLISTEND:
				View view=(View)msg.obj;
				LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				view.setLayoutParams(params);
				bestListLayout.addView((View)msg.obj);break;
			default:return;
			}
		}
		
	};
	
	@Override
	public void updateUI() {
		View pointlistview = null;
		if (pointlistview == null) {	
			Intent intent = new Intent(UserMessagelistActivity.this,MessageListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (preregister)
				intent.putExtra("preregister", true);   	
			pointlistview = manager.startActivity(
                "orderlist_in_layout",intent
                ).getDecorView();
		}
		Message message = Message.obtain();
		message.what = POINTLISTEND;
		message.obj = pointlistview;	
		handlerMessage.sendMessage(message);
		super.updateUI();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		UesrInfoLayout userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
		Message message = userlayout.handler.obtainMessage();
		message.what = 2;
		userlayout.handler.sendMessage(message);
		if (bestListLayout != null)
			bestListLayout.removeAllViews();
		handler.sendEmptyMessage(UPDATE);
		super.onNewIntent(intent);
	}
}
