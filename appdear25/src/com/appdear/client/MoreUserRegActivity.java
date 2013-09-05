package com.appdear.client;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.service.MessageHandler;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiUserResult;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.VaildValue;
import com.appdear.client.R;
/**
 * 用户注册信息
 * @author jdan
 *
 */
public class MoreUserRegActivity extends BaseActivity implements OnClickListener {
	/**
	 * 输入框
	 */
	EditText mEtName = null;
	EditText mEtPwd = null;
	//EditText mEtEmail = null;
	EditText mEtRePwd = null;
	ProgressDialog progress; 
	//请求次数
	int count=0;
	/**
	 * 提示图标
	 */
	//ImageView mImgTipName = null;
	//ImageView mImgTipPwd = null;
	//ImageView mImgTipEmail = null;
	//ImageView mImgTipRePwd = null;
	/**
	 * 提示文字
	 */
	TextView mTvTipName = null;
	TextView mTvTipEmail = null;
	TextView mTvTipPwd = null;
	TextView mTvTipRePwd = null;
	/**
	 * button
	 */
	RelativeLayout reg_layout = null;
	Button btnReg = null;
	TextView readprotocol = null;
	CheckBox phonereg=null;
	CheckBox check=null;
	ApiUserResult result=null;
	LinearLayout mLinearLayout;
	public void onCreate(Bundle  b){
		super.onCreate(b);
		setContentView(R.layout.more_user_reg_layout);
	}
	
	@Override
	public void init() {
		initUi();
	}
	
	private void initUi(){
		mEtName = (EditText)findViewById(R.id.user_reg_edit_account);
	
		mEtPwd = (EditText)findViewById(R.id.user_reg_edit_pwd);
	
		mEtRePwd = (EditText)findViewById(R.id.user_reg_edit_rePwd);
	
		mTvTipName = (TextView)findViewById(R.id.userReg_tv_nameTip);

		mTvTipPwd = (TextView)findViewById(R.id.userReg_tv_pwdTip);
		mTvTipRePwd = (TextView)findViewById(R.id.userReg_tv_rePwdTip);
		
		readprotocol = (TextView) findViewById(R.id.readprotocol);
		btnReg = (Button)findViewById(R.id.user_reg_tv_login_text);
		reg_layout=(RelativeLayout)findViewById(R.id.reg_layout);
		
		btnReg.setOnClickListener(this);
		readprotocol.setOnClickListener(this);
		mLinearLayout=(LinearLayout)this.findViewById(R.id.LinearLayout);
		check=(CheckBox)this.findViewById(R.id.user_reg_check_protocol);
		phonereg=(CheckBox)this.findViewById(R.id.phone_reg);
		check.setOnClickListener(this);
		phonereg.setOnClickListener(this);
		reg_layout.setOnClickListener(this);
		mLinearLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
	
		 switch(v.getId()){
		 case R.id.user_reg_tv_login_text:
			 //
			 //注册类型，0--非手机号注册，1--手机号注册
			final String rtype = "0";
			final String userAccount = this.mEtName.getText().toString().trim();
			final String userPwd = this.mEtPwd.getText().toString().trim();
			final String reUesrPwd = this.mEtRePwd.getText().toString().trim();
				//
			 	if(!VaildValue.VaildUsername(userAccount)){
			 		mEtName.requestFocus();
			 		Toast.makeText(this, "用户名请填写6-25字符字母开头，字母或数字结尾", Toast.LENGTH_SHORT).show();
					return;
			 	}
				if(!VaildValue.VaildPasswd(userPwd)){
					this.mEtPwd.setFocusable(true);
					mEtPwd.requestFocus();
					 Toast.makeText(this, "密码请填写6-16字符或数字", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!userPwd.equals(reUesrPwd)){
					 mEtPwd.requestFocus();
					 Toast.makeText(this, "两次密码不一致！请重新输入", Toast.LENGTH_SHORT).show();
					 return;
				}
				if(!check.isChecked()){
					 Toast.makeText(this,"未同意爱皮应用下载服务条款!",Toast.LENGTH_SHORT).show();
					 break;
				}
				//handler.sendEmptyMessage(0);new Thread(new Runnable() {
				if(ServiceUtils.checkNetState(MoreUserRegActivity.this)){
					hideInputMode();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							register(rtype, userAccount,userPwd,reUesrPwd);
						}
					}).start();
				}else{
					Toast.makeText(this, "网络中断，请查看网络连接！", Toast.LENGTH_LONG).show();
				}
				//updateUIEnd();
			 break;
		  case R.id.reg_layout:
			  if(phonereg.isChecked()==true){
				  phonereg.setChecked(false);
				  this.mEtName.setText("");
				  this.mEtName.setEnabled(true);
			  }else if(phonereg.isChecked()==false){
				  phonereg.setChecked(true);
				  //TelephonyManager manager= (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
				  TelephonyManager manager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
				  String phone=manager.getLine1Number();
				  if(phone==null||phone.equals("")){
					  Toast.makeText(this, "需要系统认证,该功能暂时无法使用，请使用用户名注册！", Toast.LENGTH_LONG).show();
				  }else{
					  this.mEtName.setText(phone);
					  this.mEtName.setEnabled(false);
				  }
			  }
			  break;
		  case R.id.user_reg_check_protocol:
		   break;
		  case R.id.LinearLayout:
			 if(check.isChecked()==false){
				 check.setChecked(true);
			 }else
				 check.setChecked(false);
			  
			  break;
		  case R.id.readprotocol:
			  try {
				ServiceUtils.getAlertDialogForString(this,"爱皮应用下载服务条款",ServiceUtils.getXIEYI(this.getAssets().open("xieyi.txt")),new MessageHandler(){
					  public void messageHandlerCannel() {
						  check.setChecked(false);
						}

						@Override
						public void messageHandlerOk() {
							 check.setChecked(true);
						}		
				  }, -2); //-2表示”取消“在左”确认“在右
			} catch (IOException e) {
				e.printStackTrace();
			}
		 
			  break;
		  default:;
		}
	}
	
	private void register(String rtype, String userAccount,String userPwd,String userrepwd){
		if(userPwd.equals(userrepwd)){
			 handler1.sendEmptyMessage(0);
			 try {
				 ApiNormolResult result1=ApiManager.checkname(userAccount);
				 if(result1.isok==2){
					 handler1.sendEmptyMessage(10);
					 return;
				 }else if(result1.isok==1){
					 //请求之前关闭注册按钮
					 if(this.getCount()>0)return;
					 this.addCount();
					 result=ApiManager.register(rtype, userAccount, userPwd,""); 
					 if(result!=null){
							if(result.isok != 1){
								handler1.sendEmptyMessage(20);
								this.clearCount();
								return;
							}else{
								String saveXml = Common.USERLOGIN_XML;
								
								//保存临时存储信息
								SharedPreferencesControl.getInstance().putString("sessionid",result.token, saveXml,this);
								SharedPreferencesControl.getInstance().putString("userid",result.userid+"", saveXml,this);
								SharedPreferencesControl.getInstance().putString("nickname",result.nickname, saveXml,this);
								SharedPreferencesControl.getInstance().putString("username",mEtName.getText().toString(), saveXml,this);
								//判断是否是自动登录保存持久化用户信息
								boolean checked= SharedPreferencesControl.getInstance().getBoolean("issave",com.appdear.client.commctrls.Common.USERPASSWDXML,this);
								if (checked) {
									saveXml = Common.USERPASSWDXML;
									SharedPreferencesControl.getInstance().putString("sessionid",result.token, saveXml,this);
									SharedPreferencesControl.getInstance().putString("userid",result.userid+"", saveXml,this);
									SharedPreferencesControl.getInstance().putString("nickname",result.nickname, saveXml,this);
									SharedPreferencesControl.getInstance().putString("username",mEtName.getText().toString(), saveXml,this);
								}
								
								//添加到用户登录信息
								result=ApiManager.userprofile(SharedPreferencesControl.getInstance().getString("userid", saveXml,this),
										SharedPreferencesControl.getInstance().getString("sessionid",saveXml,this));
								SharedPreferencesControl.getInstance().putString("account",String.valueOf(result.account),Common.USERLOGIN_XML,this);
								SharedPreferencesControl.getInstance().putString("level",(result.level==null||result.level.equals(""))?"爱皮小将":result.level,Common.USERLOGIN_XML,this);
								SharedPreferencesControl.getInstance().putString("point",String.valueOf(result.totalpoint),Common.USERLOGIN_XML,this);
								SharedPreferencesControl.getInstance().putString("gender",String.valueOf(result.userinfo.gender),Common.USERLOGIN_XML,this);
								handler.sendEmptyMessage(END_TAG);
								handler1.sendEmptyMessage(30);
								
								//跳转到登录成功后的界面
								Intent userIntent = new Intent();
								userIntent.setClass(this, MoreUserCenterMainActivity.class);
								startActivity(userIntent);
								
								//注册成功关闭之前打开的登录界面
								setResult(2000);
								
								//注册恢复可用
								btnReg.setEnabled(true);
							//	count=0;
								finish();
							}
						}else{
							handler1.sendEmptyMessage(40);
						//	count=0;
							this.clearCount();
						}
				 }else{
					 handler1.sendEmptyMessage(50);
					 this.clearCount();
				 }
			} catch (ApiException e) {
				handler1.sendEmptyMessage(50);
			}
		 }else{
			 handler1.sendEmptyMessage(60);
			 this.clearCount();
		 }
	}
	private Handler handler1=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what==0){
				progress= ProgressDialog.show(MoreUserRegActivity.this, "数据加载中..", "请等待", true, false);
			}else if(msg.what==10){
				Toast.makeText(MoreUserRegActivity.this, "用户名已被注册，请换个用户名！", Toast.LENGTH_LONG).show();
				if(progress!=null)progress.dismiss();
			}else if(msg.what==20){
				Toast.makeText(MoreUserRegActivity.this, "用户注册失败,请重新注册！", Toast.LENGTH_SHORT).show();
				if(progress!=null)progress.dismiss();
			}else if(msg.what==30){
				Toast.makeText(MoreUserRegActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
				if(progress!=null)progress.dismiss();
			}else if(msg.what==40){
				Toast.makeText(MoreUserRegActivity.this, "服务器系统故障！", Toast.LENGTH_LONG).show();
				if(progress!=null)progress.dismiss();
			}else if(msg.what==50){
				Toast.makeText(MoreUserRegActivity.this, "系统出错,注册失败！", Toast.LENGTH_LONG).show();
				if(progress!=null)progress.dismiss();
			}else if(msg.what==60){
				Toast.makeText(MoreUserRegActivity.this, "两次密码不一致！请重新输入", Toast.LENGTH_SHORT).show();
				if(progress!=null)progress.dismiss();
			}
		}
		
	};
	//屏蔽
	public void updateUIStart() {}

	public synchronized int addCount() {
		 count++;
		 return count;
	}
	public synchronized int getCount() {
		return count;
	}
	public synchronized void clearCount() {
		this.count =0;
	};
	
	/**
	 * 隐藏软键盘
	 */
	private void hideInputMode() {
		View view = MoreUserRegActivity.this.getCurrentFocus();
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if(view != null) inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}