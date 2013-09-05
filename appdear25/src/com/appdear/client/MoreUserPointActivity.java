package com.appdear.client;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.UesrInfoLayout;
import com.appdear.client.commctrls.BaseActivity.LoadThread;
import com.appdear.client.exception.ApiException;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiUserResult;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.R;

public class MoreUserPointActivity extends BaseGroupActivity{
	private LocalActivityManager manager;
	 
	private LinearLayout bestListLayout; 
	private final static int POINTLISTEND=2;
	private final static int USEREND=1;
	private ApiUserResult result;
	private UesrInfoLayout userinfo_top;
	public void onCreate(Bundle b){
		super.onCreate(b); 
		setContentView(R.layout.user_center_common_layout);
		userinfo_top=(UesrInfoLayout) this.findViewById(R.id.userinfo_top);
	}
	
	@Override
	public void init() {
		bestListLayout= (LinearLayout) this.findViewById(
				R.id.common_list_layout);
		manager=getLocalActivityManager();
	}
	
	private Handler handlerpoint=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case USEREND:
				result=(ApiUserResult)msg.obj;
				break;
			case POINTLISTEND:
				View view=(View)msg.obj;
				DisplayMetrics metrics=com.appdear.client.utility.ServiceUtils.getMetrics(MoreUserPointActivity.this.getWindowManager());
				LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
				view.setLayoutParams(params);
				bestListLayout.addView((View)msg.obj);break;
			default:return;
			}
		}
		
	};
    @Override
	public void initData() {
		// TODO Auto-generated method stub
    	try {
			result=ApiManager.userprofile(SharedPreferencesControl.getInstance().getString("userid",com.appdear.client.commctrls.Common.USERLOGIN_XML,this),SharedPreferencesControl.getInstance().getString("sessionid",com.appdear.client.commctrls.Common.USERLOGIN_XML,this));
			SharedPreferencesControl.getInstance().putString("account",String.valueOf(result.account),com.appdear.client.commctrls.Common.USERLOGIN_XML,this);
			SharedPreferencesControl.getInstance().putString("level",result.level.equals("")?"°®Æ¤Ð¡½«":result.level,com.appdear.client.commctrls.Common.USERLOGIN_XML,this);
			SharedPreferencesControl.getInstance().putString("point",String.valueOf(result.totalpoint),com.appdear.client.commctrls.Common.USERLOGIN_XML,this);
			if(userinfo_top!=null&&userinfo_top.useraccount_4!=null){
				Message msg=userinfo_top.handler.obtainMessage();
				msg.what=1;
				msg.obj=String.valueOf(result.totalpoint);
				userinfo_top.handler.sendMessage(msg);
			}
    	} catch (ApiException e) {
			showException(e);
		}
    	super.initData();
	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		View pointlistview=null;
		if(pointlistview==null){	  
			    	pointlistview=manager.startActivity( 
			                "pointlist_in_layout",
			                new Intent(MoreUserPointActivity.this, PointListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			               .getDecorView();
		}
		Message message=Message.obtain();
		message.what=POINTLISTEND;
		message.obj=pointlistview;	
		handlerpoint.sendMessage(message);
		super.updateUI();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		UesrInfoLayout userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
		Message message = userlayout.handler.obtainMessage();
		message.what=2;
		userlayout.handler.sendMessage(message);
		if (bestListLayout != null)
			bestListLayout.removeAllViews();
		handler.sendEmptyMessage(UPDATE);
		super.onNewIntent(intent);
	}
}
