package com.appdear.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.appdear.client.R;
import com.appdear.client.model.Updateinfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.update.CheckVersion;
import com.appdear.client.update.Global;
import com.appdear.client.utility.ServiceUtils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public  class UpdateDialog extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_dialog_layout);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.update_layout_);
		DisplayMetrics wh = ServiceUtils.getMetrics(getWindowManager());
		layout.setLayoutParams(new FrameLayout.LayoutParams(wh.widthPixels*4/5, wh.heightPixels*4/5));
		
		Updateinfo info = (Updateinfo) getIntent().getSerializableExtra("info");
		if(info==null){
			info=AppContext.getInstance().info;
		}
		TextView btn_ok = (TextView) findViewById(R.id.ok);
		TextView btn_cancel = (TextView) findViewById(R.id.cancel);
		TextView updateyuan=(TextView) findViewById(R.id.updateyuan);
		RotateAnimation animation=new RotateAnimation(360,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animation.setDuration(2000);
		updateyuan.startAnimation(animation);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
		//add items 
		LinearLayout listContainer = (LinearLayout) findViewById(R.id.updeinfo_list_container);
		listContainer.setOrientation(LinearLayout.VERTICAL);
		listContainer.removeAllViews();
		if(info!=null){
			String updateInfo = info.softUpdateTip;
			String [] infoItems = updateInfo.split("\\s");
			if(infoItems.length>0){
				for(int i = 0;i<infoItems.length;i++){
					if(!"".equals(infoItems[i].trim())){
						TextView tv = new TextView(this);
						tv.setTextColor(Color.parseColor("#4D4D4D"));
						tv.setText(" "+infoItems[i]);   
						tv.setPadding(5, 5, 5, 5);
						listContainer.addView(tv);
					}
				}
			}
		
			TextView tv_version = (TextView) findViewById(R.id.versionname);
			tv_version.setText("最新版本："+info.softVersion);
			
			TextView tv_size = (TextView) findViewById(R.id.size);
			try{
				if(info!=null&&info.softSize!=null&&!info.softSize.equals(""))
				tv_size.setText("大小："+ServiceUtils.returnSpace(Integer.parseInt(info.softSize)));
			}catch (Exception e) {
				 
			}
		}else{
			this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		 case R.id.ok:
			 AppContext.getInstance().isUpdateStarted = true;
		 if (null==ServiceUtils.getSDCardUrl())
			Toast.makeText(this,"您的SD卡不存在，或者插入不正确！",Toast.LENGTH_LONG).show();
		 else {
			 File updateDir=null,updateFile=null;
			 if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){  
					updateDir = new File(Environment.getExternalStorageDirectory(),Global.downloadDir);  
					updateFile = new File(updateDir.getPath(),"appstore_"+(Constants.VERSIONCODE+1)+".apk");  
			  }  
			 if(updateFile!=null&&updateFile.exists()){
				 File sizefile=new File(updateDir.getPath(),"appstore_"+(Constants.VERSIONCODE+1));
				 if(sizefile.exists()){
					 FileReader reader=null;
					 try {
						 reader=new FileReader(sizefile);
						String size=new BufferedReader(reader).readLine();
						if(size!=null&&!size.equals("")){
							if(Integer.parseInt(size)==updateFile.length()){
								 AppContext.getInstance().isUpdateStarted = false;
								Uri uri=Uri.fromFile(updateFile);
								Intent install=new Intent(Intent.ACTION_VIEW);
								install.setDataAndType(uri,"application/vnd.android.package-archive");
								install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								((NotificationManager)this.getSystemService(NOTIFICATION_SERVICE)).cancel(UpdateAppService.notification_id);
							    this.startActivity(install);
							    finish();
							    return;
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if(reader!=null){
							try {
								reader.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					 CheckVersion.getInstance().checkVersion(this, AppContext.getInstance().info.updateurl, true);
					 Toast.makeText(this,"更新已开始！",Toast.LENGTH_LONG).show();
				 }else{
					 CheckVersion.getInstance().checkVersion(this, AppContext.getInstance().info.updateurl, true);
					 Toast.makeText(this,"更新已开始！",Toast.LENGTH_LONG).show();
				 }
				
			 }else{
				 CheckVersion.getInstance().checkVersion(this, AppContext.getInstance().info.updateurl, true);
				 Toast.makeText(this,"更新已开始！",Toast.LENGTH_LONG).show();
			 }
		 }
		 finish();
		 break;
		 case R.id.cancel:
			 finish();
		 break;
		}
	}
	
	
}