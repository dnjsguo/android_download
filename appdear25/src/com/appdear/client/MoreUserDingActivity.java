package com.appdear.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiCustomAppResult;
import com.appdear.client.service.api.ApiCustomappRequest;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.utility.ServiceUtils;

/**
 *  更多 - 应用定制！
 * @author jdan
 */
public class MoreUserDingActivity extends  BaseActivity{
	ApiCustomAppResult result = null;
	//private String TAG = MoreUserDingActivity.class.getName();
	/**
	 *数组
	 */
	String array  [] = {"社交聊天","音乐视频","浏览器","娱乐游戏","安全保护","输入法","金融理财","健康医疗","其他"};
	/**
	 * 下拉控件
	 */
	/**
	 * 输入内容
	 */
	EditText mEt_content = null;
	/**
	 * l类型
	 */
	private AutoCompleteTextView text;
	private ImageView button;
	TextView feedback_tv_typeText ;
	//private TextView more_feedback_submit;
	private Button more_feedback_submit;
	String selectType = "0";
	Spinner mSpinner = null;
	private RelativeLayout layout;
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.more_userding_layout);
		ScreenManager.getScreenManager().pushActivity(this);//进栈
		/**
		 * 设置不显示加载提示框文字
		 */
		isShowAlert = false;
	}
	
	@Override
	public void init() {
		selectType = "0";
		mEt_content = (EditText)findViewById(R.id.more_feedback_et_content);
		more_feedback_submit=(Button)findViewById(R.id.more_feedback_submit);
		
	        text=(AutoCompleteTextView)this.findViewById(R.id.auto_complete);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, array);
	        button=(ImageView)this.findViewById(R.id.button);
	        text.setAdapter(adapter);
	        text.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					MoreUserDingActivity.this.selectType = ""+position +1;
					
				}
	        });
	        
	        button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					text.showDropDown();
				}    	
	        });
	        //init
	        more_feedback_submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(ServiceUtils.checkNetState(MoreUserDingActivity.this)==false){
					Toast.makeText(MoreUserDingActivity.this,"网络不可用，请开启网络提交应用定制!",Toast.LENGTH_LONG).show();
					return;
				}
				String content  = mEt_content.getText().toString().trim();
				String type = selectType; 
				 
				//int resCode = -1;
				//content = "我来测试反馈！";
				
				String sessionid = SharedPreferencesControl.getInstance().getString("sessionid",com.appdear.client.commctrls.Common.USERLOGIN_XML,MoreUserDingActivity.this);
				String  userid = SharedPreferencesControl.getInstance().getString("userid",com.appdear.client.commctrls.Common.USERLOGIN_XML,MoreUserDingActivity.this);
				//if(sessionid!=""||userid!=""){
				if("".equals(content)){
					Toast.makeText(MoreUserDingActivity.this, "请填写内容", Toast.LENGTH_SHORT).show();
					return ;
					//TODO:验证
				}else if (!ServiceUtils.checkLogin(MoreUserDingActivity.this, true)) {
					return;
			    }else{
			    	new AddCustomappThread(type + "", userid,sessionid,  content).start();
			   }
			}
		});
	     layout = (RelativeLayout) findViewById(R.id.top);
	     layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	/**
	 * 创建应用定制提交数据线程
	 */
	private class AddCustomappThread extends Thread{
		String type;
		String userid;
		String sessionid;
		String content;
		
		@SuppressWarnings("unused")
		public AddCustomappThread(){
		
		}
		
		public AddCustomappThread(String type,String userid,String sessionid,String content){
			this.type = type;
			this.userid = userid;
			this.sessionid = sessionid;
			this.content = content;
		}
		
		@Override
		public void run() {
			try{
				showMessageInfo("正在提交,请稍候...");
				result = ApiCustomappRequest.addCustomapp(type + "", userid, sessionid, content);
				int resCode = -1;
				if(result!=null&&result.resultcode!=null&&result.resultcode!=""){
					resCode = Integer.parseInt(result.resultcode);
					switch(resCode){
					case 100000:
//						Toast.makeText(MoreUserDingActivity.this, "爱皮应用下载鉴权未通过", Toast.LENGTH_SHORT).show();
						showMessageInfo("爱皮应用下载鉴权未通过");
						break;
					case 200000:
//						Toast.makeText(MoreUserDingActivity.this, "请求参数格式错误", Toast.LENGTH_SHORT).show();
						showMessageInfo("请求参数格式错误");
						break;
					case 300000:
//						Toast.makeText(MoreUserDingActivity.this, "服务内部错误", Toast.LENGTH_SHORT).show();
						showMessageInfo("服务内部错误");
						break;
					case 400000:
//						Toast.makeText(MoreUserDingActivity.this, "网络超时，请重新再试。", Toast.LENGTH_SHORT).show();
						showMessageInfo("网络超时，请重新再试。");
						break;
					case 500000:
//						Toast.makeText(MoreUserDingActivity.this, "用户token已经失效。", Toast.LENGTH_SHORT).show();
						showMessageInfo("用户token已经失效。");
						break;
					case 600000:
//						Toast.makeText(MoreUserDingActivity.this, "请求JSON格式错误。", Toast.LENGTH_SHORT).show();
						showMessageInfo("请求JSON格式错误。");
						break;
					case 700000:
//						Toast.makeText(MoreUserDingActivity.this, "请求header头内容错误。", Toast.LENGTH_SHORT).show();
						showMessageInfo("请求header头内容错误。");
						break;
					case 000000:
//						Toast.makeText(MoreUserDingActivity.this, "应用定制已提交，谢谢参与！ !", Toast.LENGTH_SHORT).show();
						showMessageInfo("应用定制已提交，谢谢参与！ !");
//						more_feedback_submit.setClickable(false); //不支持重复提交评论
//						more_feedback_submit.setBackgroundColor(Color.parseColor("#9c9a9c")); //将按钮背景颜色变成灰色
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mEt_content.getWindowToken(), 0);
//						MoreUserDingActivity.this.finish();
						break;
					}
				}
			} catch (ApiException e) {
				showException(e);
				e.printStackTrace();
			} catch (ServerException e) {
				showException(e);
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateUI() {
		button.requestFocus();
	}

	@Override
	protected void onRestart() {
		if (Constants.DEBUG) Log.i("test","onRestart");
		button.setFocusable(true);
		super.onRestart();
	}

	@Override
	protected void onPause() {
		if (Constants.DEBUG) Log.i("test","onPause");
		button.setFocusable(true);
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (Constants.DEBUG) Log.i("test","onResume");
		button.setFocusable(true);
		super.onResume();
	}

	@Override
	protected void onStop() {
		if (Constants.DEBUG) Log.i("test","onStop");
		button.setFocusable(true);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		ScreenManager.getScreenManager().popActivity(this);//出栈
		super.onDestroy();
	}
}