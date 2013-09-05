package com.appdear.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.UesrInfoLayout;
import com.appdear.client.exception.ApiException;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.utility.VaildValue;

/**
 * 修改密码
 * @author zqm
 *
 */
public class UserAlterPwd extends BaseActivity {

	private EditText oldpwd = null;
	private EditText newpwd = null;
	private EditText confirmpwd = null;
	private RelativeLayout alterpwdButton = null;
	private boolean isOpenKeyBoard = false;
	private int num = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alter_pwd);
		ScreenManager.getScreenManager().pushActivity(this);//进栈
	}
	
	
	@Override
	public void init() {
		oldpwd = (EditText) findViewById(R.id.oldpwd);
		oldpwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					num ++;
					if (num == 2)
						isOpenKeyBoard = true;
				}
			}
		});
		newpwd = (EditText) findViewById(R.id.newpwd);
		newpwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					isOpenKeyBoard = true;
				}
			}
		});
		confirmpwd = (EditText) findViewById(R.id.confirmpwd);
		confirmpwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					isOpenKeyBoard = true;
				}
			}
		});
		alterpwdButton = (RelativeLayout) findViewById(R.id.click_button_update);
		alterpwdButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!AppContext.getInstance().isNetState) {
					Toast.makeText(UserAlterPwd.this, "网络错误，请检查网络状态", Toast.LENGTH_SHORT).show();
					return;
				}
				showMessageInfo("正在验证...");
				if (oldpwd.getText().toString().equals("")) {
					showMessageInfo("请输入旧密码!");
					return;
				}
				
				if (!VaildValue.VaildPasswd(oldpwd.getText().toString())) {
					showMessageInfo("旧密码请输入6-16个字符或字母!");
					return;
				}
				
				if (newpwd.getText().toString().equals("")) {
					showMessageInfo("请输入新密码!");
					return;
				}
				if (confirmpwd.getText().toString().equals("")) {
					showMessageInfo("请输入确认密码!");
					return;
				}
				
				if (!confirmpwd.getText().toString().equals(newpwd.getText().toString())) {
					showMessageInfo("新密码与确认密码输入不一致!");
					return;
				}
				
				if (!VaildValue.VaildPasswd(confirmpwd.getText().toString())) {
					showMessageInfo("新密码请输入6-16个字符或字母!!");
					return;
				}
				
				showMessageInfo("正在修改...");
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String oldpwdtext = oldpwd.getText().toString();
						String newpwdtext = newpwd.getText().toString();
						String userid = SharedPreferencesControl.getInstance().getString("userid",com.appdear.client.commctrls.Common.USERLOGIN_XML, UserAlterPwd.this);
						String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML, UserAlterPwd.this);
						try {
							ApiNormolResult result = ApiManager.updatepasswd(userid, oldpwdtext, newpwdtext, token);
							if (result.isok == 1) {
								showMessageInfo("密码修改成功，下次登录生效！");
								return;
							} else {
								showMessageInfo("修改密码失败，旧密码错误！");
								return;
							}
						} catch (ApiException e) {
							showMessageInfo("修改密码失败，旧密码错误！");
						}
					}
				}).start();
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		UesrInfoLayout userlayout = (UesrInfoLayout) findViewById(R.id.userinfo_top);
		Message message = userlayout.handler.obtainMessage();
		message.what=2;
		userlayout.handler.sendMessage(message);
		handler.sendEmptyMessage(UPDATE);
		if (oldpwd != null)
			oldpwd.setText("");
		if (newpwd != null)
			newpwd.setText("");
		if (confirmpwd != null)
			confirmpwd.setText("");
		super.onNewIntent(intent);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isOpenKeyBoard
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
			View view = getCurrentFocus();
		    if (view != null) {
		    	imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//隐藏软键盘  
		    }
		    isOpenKeyBoard = false;
		    return true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		ScreenManager.getScreenManager().popActivity(this);//出栈
		super.onDestroy();
	}
}
