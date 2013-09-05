package com.appdear.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.LineEditText;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.UesrInfoLayout;
import com.appdear.client.exception.ApiException;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiUserResult;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.VaildValue;
import com.appdear.client.R;

/**
 * 用户个人信息
 * @author jindan
 *
 */
public class UserInfoActivity extends BaseGroupActivity implements OnClickListener {
	private final static int USEREND = 1;
	/**
	 * 输入框
	 */
	private String[] array= {"保密","男","女"};
	private EditText mEtProfession = null;
	private EditText mEtArea= null;
	private EditText mEtEmail = null;
	private EditText username=null;
	private ProgressDialog progress; 
	/**
	 * 列表数据
	 */
	private ApiNormolResult result;
	private AutoCompleteTextView mEtGender = null;
	private ImageButton userprofile_genderType = null;
	private EditText mEtPhone = null;
	private EditText mEtQQ = null;
	private EditText mEtDesc = null;
	private boolean updateflag=false;
	private String text=null;
	
	/**
	 * button
	 */
	private RelativeLayout btnUpdate = null;
	private LinearLayout bestListLayout;
	private ApiUserResult userresult;
	
	public void onCreate(Bundle b){
		super.onCreate(b);  
		if (!ServiceUtils.checkLogin(this, true))
			return;
		setContentView(R.layout.user_profile);
	}
	
	@Override
	public void init() {
		username=(LineEditText)this.findViewById(R.id.userprofile_username);
		mEtProfession=(LineEditText)this.findViewById(R.id.userprofile_profession);
		mEtArea=(LineEditText)this.findViewById(R.id.userprofile_area);
		mEtEmail=(LineEditText)this.findViewById(R.id.userprofile_email);
		mEtGender=(AutoCompleteTextView)this.findViewById(R.id.userprofile_gender);
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,array);
		mEtGender.setAdapter(adapter);
		mEtPhone=(LineEditText)this.findViewById(R.id.userprofile_phone);
		mEtQQ=(LineEditText)this.findViewById(R.id.userprofile_qq);
		mEtDesc=(LineEditText)this.findViewById(R.id.userprofile_desc);
		btnUpdate=(RelativeLayout)this.findViewById(R.id.click_button_update);
		userprofile_genderType=(ImageButton)this.findViewById(R.id.userprofile_genderType);

		userprofile_genderType.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
	}
	
	private Handler handlerUserinfo=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case USEREND:
				userresult = (ApiUserResult) msg.obj;
				if (userresult != null) {
					mEtProfession.setText(userresult.userinfo.nickname);
					mEtArea.setText(userresult.userinfo.area);
					mEtEmail.setText(userresult.userinfo.email);
					mEtGender.setHint(handlerGenderI(userresult.userinfo.gender));
					text=handlerGenderI(userresult.userinfo.gender);
					mEtPhone.setText(userresult.userinfo.mobile);
					mEtQQ.setText(userresult.userinfo.qq);
					mEtDesc.setText(userresult.userinfo.desc);
				}
			default:return;
			}  
		}
		
	};
	
    @Override
	public void initData() {
    	String userid = SharedPreferencesControl.getInstance().getString("userid",Common.USERLOGIN_XML,this);
		String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML,this);
		
		try {
			userresult=ApiManager.userprofile(userid, token);
		} catch (ApiException e) {
			e.printStackTrace();
			showException(e);
		}
	
    	super.initData();
	}

	@Override
	public void updateUI() {
		String str=SharedPreferencesControl.getInstance().getString("gender", Common.USERLOGIN_XML, this);
		String name=SharedPreferencesControl.getInstance().getString("username",com.appdear.client.commctrls.Common.USERLOGIN_XML,this);
		username.setText(name);
		if(str.equals("1")){
			//男
			mEtGender.setHint("男");
		}else if(str.equals("0")){
			//女
			mEtGender.setHint("女");
		}else{
			//默认
			mEtGender.setHint("保密");
		}
		Message message=Message.obtain();
		message.what=USEREND;
		message.obj=userresult;	
		handlerUserinfo.sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.click_button_update:
				if (mEtProfession.getText().toString().trim().equals("")) {
					Toast.makeText(this, "昵称是必要的哦！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!VaildValue.VaildProfession(mEtProfession.getText().toString().trim())) {
			 		Toast.makeText(this, "昵称请输入1-20个字符", Toast.LENGTH_SHORT).show();
					return;
			 	}
				// 邮箱去空格
				if (!VaildValue.VaildEmail(mEtEmail.getText().toString().trim())) {
			 		Toast.makeText(this, "不符合邮箱格式,请重新输入", Toast.LENGTH_SHORT).show();
					return;
			 	}
				if (!VaildValue.VaildMobile(mEtPhone.getText().toString())) {
			 		Toast.makeText(this, "请输入正确格式的手机号码", Toast.LENGTH_SHORT).show();
					return;
			 	}
				if (!VaildValue.VaildQQ(mEtQQ.getText().toString())) {
			 		Toast.makeText(this, "qq请输入5-13位数字", Toast.LENGTH_SHORT).show();
					return;
			 	}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						if (isUpdateflag())
							return;
						setUpdateflag(true);
						handler1.sendEmptyMessage(0);
						String userid = SharedPreferencesControl.getInstance().getString("userid",Common.USERLOGIN_XML,UserInfoActivity.this);
						String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML,UserInfoActivity.this);
						try {
							if(!mEtGender.getText().toString().equals("")){
								text=handlerGender(mEtGender.getText().toString());
							}else if(text!=null){
								text=handlerGender(text);
							}
							result = ApiManager.updateprofile(token, userid, mEtProfession.getText().toString(), mEtArea.getText().toString(),
									text,"",mEtPhone.getText().toString(),mEtQQ.getText().toString(),mEtDesc.getText().toString(),
									mEtEmail.getText().toString());
							
							if (result != null && result.isok == 1) {
								SharedPreferencesControl.getInstance().putString("nickname",mEtProfession.getText().toString(),Common.USERLOGIN_XML,UserInfoActivity.this);
								SharedPreferencesControl.getInstance().putString("gender",text,Common.USERLOGIN_XML,UserInfoActivity.this);
								handler1.sendEmptyMessage(100);
								return;
							}
							handler1.sendEmptyMessage(-1);
						} catch (ApiException e) {
							handler1.sendEmptyMessage(-1);
						}
					}
				}).start();
				break;
			case R.id.userprofile_genderType:
				
				mEtGender.showDropDown();
				SharedPreferencesControl.getInstance().putInt("dds",1,com.appdear.client.commctrls.Common.USERLOGIN_XML,UserInfoActivity.this);
				break;
			case R.id.gender_click:
				mEtGender.showDropDown();
				break;
			default:return;
		}
	}
	
	private String handlerGender(String str) {
		if (str.equals("男")) {
			return "1";
		} else if (str.equals("女")) {
			return "0";
		} else {
			return "2";
		}
	}
	
	private String handlerGenderI(String str) {
		if (str.equals("1")) {
			return "男";
		} else if (str.equals("0")) {
			return "女";
		} else {
			return "保密";
		}
	}
	
	private Handler handler1=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				progress= ProgressDialog.show(UserInfoActivity.this, "数据加载中..", "请等待", true, false);
			} else if (msg.what == 100) {
				Toast.makeText(UserInfoActivity.this, "用户信息修改成功", Toast.LENGTH_SHORT).show();
				if (progress != null)
					progress.dismiss();
				setUpdateflag(false);
				UesrInfoLayout userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
				Message message = userlayout.handler.obtainMessage();
				message.what=2;
				userlayout.handler.sendMessage(message);
			} else if (msg.what == -1) {
				setUpdateflag(false);
				if (progress != null)
					progress.dismiss();
				Toast.makeText(UserInfoActivity.this, "用户信息修改失败，请重试!", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public synchronized boolean isUpdateflag() {
		return updateflag;
	}

	public synchronized void setUpdateflag(boolean updateflag) {
		this.updateflag = updateflag;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				initData();
			}
		}).start();
		if (bestListLayout != null)
			bestListLayout.removeAllViews();
		UesrInfoLayout userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
		Message message = userlayout.handler.obtainMessage();
		message.what=2;
		userlayout.handler.sendMessage(message);
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}
	
}