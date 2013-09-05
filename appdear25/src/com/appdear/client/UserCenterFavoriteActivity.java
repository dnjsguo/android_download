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
/**
 * 个人收藏列表功能
 * 提供用户收藏软件功能
 * 包括删除收藏、下载、软件详情
 * @author jdan
 *
 */
public class UserCenterFavoriteActivity extends BaseGroupActivity{
	private LocalActivityManager manager;
	 
	private LinearLayout bestListLayout; 
	private final static int POINTLISTEND=2;
	private final static int USEREND=1;
	//private ApiUserResult result;
	public void onCreate(Bundle b){
		super.onCreate(b); 
		setContentView(R.layout.user_center_common_layout);
	}
	
	@Override
	public void init() {
		bestListLayout= (LinearLayout) this.findViewById(
				R.id.common_list_layout);
		manager=getLocalActivityManager();
	}
	
	/**
	 * 消息处理
	 */
	private Handler handlerFavorite=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case USEREND:
				//y
				//result=(ApiUserResult)msg.obj;
				break;
			case POINTLISTEND:
				//ui更新完添加收藏列表
				View view=(View)msg.obj;
				LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
				view.setLayoutParams(params);
				bestListLayout.addView((View)msg.obj);break;
			default:return;
			}
		}
		
	};
    @Override
	public void initData() {
    	super.initData();
	}

	@Override
	public void updateUI() {
		View pointlistview=null;
		if(pointlistview==null){	  
			    	pointlistview=manager.startActivity( 
			                "favritelist_in_layout",
			                new Intent(UserCenterFavoriteActivity.this, FavoriteListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			               .getDecorView();
				
		}
		Message message=Message.obtain();
		message.what=POINTLISTEND;
		message.obj=pointlistview;	
		handlerFavorite.sendMessage(message);
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
