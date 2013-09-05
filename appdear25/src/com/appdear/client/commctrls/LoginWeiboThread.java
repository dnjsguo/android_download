package com.appdear.client.commctrls;

import org.json.JSONException;
import org.json.JSONObject;

import t4j.TBlog;
import t4j.TBlogException;
import t4j.http.AccessToken;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.JsonUtil;

public class LoginWeiboThread extends Thread
{
	private String username;
	private String password;
	//  1 保存 0 不保存  -1  直接登录
	private int  isSave;
	private Handler handerl;
	private String content;
	public LoginWeiboThread(String username,String password ,int isSave,Handler handerl,String content)
	{
		this.username=username;
		this.password=password;
		this.handerl=handerl;
		this.content=content;
		this.isSave=isSave;
	}
    public void run()
    {		
		AccessToken access;
		Handler handler1=handerl;;
		try {
			System.setProperty("tblog4j.oauth.consumerKey", "wmTbXJdsc8Vl4clv");
	    	System.setProperty("tblog4j.oauth.consumerSecret", "UkiVdoyyjJqzwYPV14BEdsZpWJ2yXJdo");		    	
	    	// 暂时把debug关了。减少干扰信息
	    	System.setProperty("tblog4j.debug", "true");		    	
			TBlog tblog = new TBlog();
 			access = tblog.getXAuthAccessToken(username,password);
 			tblog.updateStatus(content);		
		} catch (TBlogException e) {
			e.printStackTrace();				
			Message msg = handler1.obtainMessage();
			Bundle data=new Bundle();
			String  message= e.getMessage();
			msg.what=BaseActivity.SHARE_WEIBO_FAILURE;
			if(message.indexOf("不能发布重复的内容")!=-1)
			{	
				data.putString("message","不能发布重复的内容");
				msg.setData(data);
				handler1.sendMessage(msg);
				return;
			}
			MyApplication.getInstance().username163=null;
			MyApplication.getInstance().password163=null;
			if(message.indexOf("{")==-1 || message.indexOf("}")==-1)
			{
				data.putString("message","登录错误，用户名密码不正确");
				msg.setData(data);
				handler1.sendMessage(msg);				
				return ;
			}
			message=(message.substring(message.indexOf("{"),message.indexOf("}")+1) );
			JSONObject jsonobject;			
			try {
				jsonobject = new JSONObject(message);
				message=JsonUtil.getString(jsonobject, "error", "22222");
				data.putString("message",message.substring(0, message.indexOf(":")));
				msg.setData(data);
				handler1.sendMessage(msg);
				MyApplication.getInstance().username163=null;
				MyApplication.getInstance().password163=null;
			} catch (Exception e1) {
				 
				e1.printStackTrace();
				MyApplication.getInstance().username163=null;
				MyApplication.getInstance().password163=null;
				data.putString("message","登录异常请检查");
				msg.setData(data);
				handler1.sendMessage(msg);
				return;
			}
			return;
		}
	 MyApplication.getInstance().username163=username;
	 MyApplication.getInstance().password163=password;
	  if(isSave==1)
	  {
		  SharedPreferencesControl.getInstance().putString("password", password,com.appdear.client.commctrls.Common.USERLOGIN_163_XML,MyApplication.getInstance());
		  SharedPreferencesControl.getInstance().putString("username",username ,com.appdear.client.commctrls.Common.USERLOGIN_163_XML,MyApplication.getInstance());	
	  } 
	  
	 handler1.sendEmptyMessage(BaseActivity.SHARE_WEIBO_SUCESS);
		 
    }
}