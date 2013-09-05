package com.appdear.client;

import org.json.JSONException;
import org.json.JSONObject;

import t4j.TBlog;
import t4j.TBlogException;
 
import t4j.http.AccessToken;

import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.LoginWeiboThread;
 
import com.appdear.client.service.AppContext;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiUserResult;
import com.appdear.client.utility.JsonUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlterUserShare163Activity extends BaseActivity implements View.OnClickListener{

	private EditText usernameView;//◊¢≤· ‰»Î±‡º≠øÚ
    private EditText passwordView;
    private String username;
    private String password;
	private TextView loginTextView;
	private TextView logoutTextView;
	private CheckBox  rememberBox;
	private int  isSave=0;  // 0≤ª±£¥Ê 1 ±£¥Ê
	private ApiUserResult result=null;
    private boolean isClick=false;
	
	private int counts=0;
	private long start;
	private String content; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_user_share_login);
	}
	
	@Override
	public void init() {
		//µ«¬º±‡º≠øÚ
		usernameView=(EditText) findViewById(R.id.username);
		passwordView=(EditText) findViewById(R.id.password);
		loginTextView=(TextView) findViewById(R.id.login);
		logoutTextView=(TextView)this.findViewById(R.id.logout);
		rememberBox=(CheckBox)this.findViewById(R.id.login_in_checkbox);
		loginTextView.setOnClickListener(this);
		logoutTextView.setOnClickListener(this);
		initUi();
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.login)
		{
			if(isClick)
			{
				Toast.makeText(AlterUserShare163Activity.this, "≤ªƒ‹÷ÿ∏¥µ«¬º,«Î…‘∫Û", Toast.LENGTH_SHORT).show();
				return;	
			}
			username=usernameView.getText().toString().trim();
			password=passwordView.getText().toString().trim();
			if(rememberBox.isChecked())
			{
				isSave=1;
			}
			if(username==null || "".equals(username))
			{
		        Toast.makeText(AlterUserShare163Activity.this, "«Î ‰»Î”√ªß√˚", Toast.LENGTH_SHORT).show();
				return; 
			}
			if(password==null || "".equals(password))
			{
		        Toast.makeText(AlterUserShare163Activity.this, "«Î ‰»Î√‹¬Î", Toast.LENGTH_SHORT).show();
				return; 
			}
			 isClick=true;
			 content=this.getIntent().getStringExtra("content");
			 if (!AppContext.getInstance().isNetState) {
					Toast.makeText(this, "Õ¯¬Á¥ÌŒÛ£¨«ÎºÏ≤ÈÕ¯¬Á◊¥Ã¨", Toast.LENGTH_SHORT).show();
					return;
				}
	 		  new LoginWeiboThread(username, password, isSave, handler1,content).start();
		}else if(v.getId()==R.id.logout)
		{
			this.finish();
		}
			
	}	
	 
	/** 
	 * øÿ÷∆ ‰»Î ‰≥ˆ»Ìº¸≈Ã
	 */
	private void hideInputMode(){
		InputMethodManager imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
		View view = AlterUserShare163Activity.this.getCurrentFocus();   
		    if (view != null){   
		    	//  imm.showSoftInput(view, 0); //œ‘ æ»Ìº¸≈Ã   
		        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//“˛≤ÿ»Ìº¸≈Ã   
		    }  
	}
	
	private Handler handler1=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			 
			
			isClick=false;
			switch (msg.what) 
			{
				case SHARE_WEIBO_SUCESS: // ∑÷œÌŒ¢≤©≥…π¶
					Toast.makeText(AlterUserShare163Activity.this, "Œ¢≤©∑¢ÀÕ≥…π¶", Toast.LENGTH_SHORT).show();
					AlterUserShare163Activity.this.finish();
					break;
				case SHARE_WEIBO_FAILURE:  //∑÷œÌŒ¢≤© ß∞‹
					Bundle b = msg.getData();
					String message = b.getString("message");
					Toast.makeText(AlterUserShare163Activity.this,message, Toast.LENGTH_SHORT).show();				 
				break;	
			}
		}
	};

	private void initUi() {
		if (MyApplication.getInstance().username163!=null&&!(MyApplication.getInstance().username163).equals("")) {
			usernameView.setText(MyApplication.getInstance().username163);
		}
	}

		
}
	

