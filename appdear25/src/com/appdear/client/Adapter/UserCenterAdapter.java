package com.appdear.client.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.MainActivity;
import com.appdear.client.MoreUserCenterMainActivity;
import com.appdear.client.MoreUserDingActivity;
import com.appdear.client.MoreUserLoginInActivity;
import com.appdear.client.MoreUserPointActivity;
import com.appdear.client.MyCommentlistActivity;
import com.appdear.client.R;
import com.appdear.client.UserAlterPwd;
import com.appdear.client.UserCenterFavoriteActivity;
import com.appdear.client.UserInfoActivity;
import com.appdear.client.UserMessagelistActivity;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.model.GridInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.MessageHandler;
import com.appdear.client.utility.ServiceUtils;

/**
 * 会员中心
 * @author jindan
 *
 */
public class UserCenterAdapter extends BaseAdapter {
	private Context context ;
	private ArrayList<GridInfo> listData = null;
	private LayoutInflater inflater = null;
	public UserCenterAdapter(Context context ,ArrayList<GridInfo> data){
		this.context = context ;
		this.listData = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return this.listData.size();
	}

	@Override
	public Object getItem(int position) {
		return this.listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		v = inflater.inflate(R.layout.more_usercenter_main, null);
		TextView text = (TextView)v.findViewById(R.id.userCenter_gv_item_text);
		ImageView img = (ImageView)v.findViewById(R.id.userCenter_gv_item_image);
		ImageView arrowImg = (ImageView)v.findViewById(R.id.userCenter_gv_item_arrowimage);
		
		img.setImageResource(this.listData.get(position).imageId);
		arrowImg.setImageResource(this.listData.get(position).arrowImage);
		text.setText(this.listData.get(position).text);

		v.setOnClickListener(new OnClickListener(){
			 
			@Override 
			public void onClick(View v) {
				if (!ServiceUtils.checkLogin(context, true))
					return;
				MoreUserCenterMainActivity activity = null;
				if (context instanceof MoreUserCenterMainActivity){
					 activity = (MoreUserCenterMainActivity) context;
				}
				if (position==0) {
					//个人中心
					Intent userIntent = new Intent();
					userIntent.setClass(context, UserInfoActivity.class);
					context.startActivity(userIntent);
				} else if(position == 1){ 
					//我的积分
					Intent userIntent = new Intent();
					userIntent.setClass(context, MoreUserPointActivity.class);
					context.startActivity(userIntent);
				} else if(position == 2) { 
					//我的收藏
					Intent userIntent = new Intent();
					userIntent.setClass(context, UserCenterFavoriteActivity.class);
					context.startActivity(userIntent);
				} else if(position == 3) { 
					//消息盒子
					Intent userIntent = new Intent();
					userIntent.setClass(context, UserMessagelistActivity.class);
					context.startActivity(userIntent);
				} else if (position == 4) {
					//修改密码
					Intent userIntent = new Intent();
					userIntent.setClass(context, UserAlterPwd.class);
					context.startActivity(userIntent);
				} else if (position == 5) {  //用户登录后，新增我的评论
					BaseActivity pActivity= ((BaseActivity)context);
					if(!AppContext.getInstance().isNetState) {
						pActivity.showMessageInfo("网络错误，不能进行该操作");
						return;
					}
					Intent shareIntent2 = new Intent();
					shareIntent2.setClass(context, MyCommentlistActivity.class);
					context.startActivity(shareIntent2);
				} else if (position == 6) { //用户登录后，新增个性应用定制
					BaseActivity pActivity= ((BaseActivity)context);
					if(!AppContext.getInstance().isNetState) {
						pActivity.showMessageInfo("网络错误，不能进行该操作");
						return;
					}
					Intent shareIntent2 = new Intent();
					shareIntent2.setClass(context, MoreUserDingActivity.class);
					shareIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(shareIntent2);
				} else if (position == 7) {
					if (activity != null) {
						ServiceUtils.getAlertDialogForString(context,"注销用户","是否退出登录？",new MessageHandler(){

							@Override
							public void messageHandlerCannel() {
								return;
							}

							@Override
							public void messageHandlerOk() {
								if(ServiceUtils.checkLogin(context,false)==true&&context instanceof MoreUserCenterMainActivity){	
									
							    	SharedPreferencesControl.getInstance().clear(Common.USERLOGIN_XML,((MoreUserCenterMainActivity)context));
							    	SharedPreferencesControl.getInstance().putString("passwd","",com.appdear.client.commctrls.Common.USERPASSWDXML,((MoreUserCenterMainActivity)context));
							    	((MoreUserCenterMainActivity)context).handler.sendEmptyMessage(((MoreUserCenterMainActivity)context).MNUEHANDLERLONGOUT);
							    	((MoreUserCenterMainActivity)context).toast.makeText(context, "已退出登录", Toast.LENGTH_LONG).show();
							    	
							    	int num = SharedPreferencesControl.getInstance().getInt("usernamecount",null, context);
							    	//跳转到登录界面
									Intent userIntent = new Intent();
									userIntent.setClass(context, MoreUserLoginInActivity.class);
									userIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									context.startActivity(userIntent);
									((Activity)context).finish();
							    } else {
							    	((MoreUserCenterMainActivity)context).toast.makeText(((MoreUserCenterMainActivity)context), "您已退出登录", Toast.LENGTH_LONG).show();  
							    }
							}
								
						});
						
					}
				}
			
			}
		});
		return v;
	}
 
}
