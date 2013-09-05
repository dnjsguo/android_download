package com.appdear.client;

import com.appdear.client.R;
import com.appdear.client.model.Updateinfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.update.CheckVersion;
import com.appdear.client.utility.ServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public  class QianmingDialog extends Activity implements OnClickListener {
	private String softname;
	private String appid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qianming_layout);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.update_layout_);
		DisplayMetrics wh = ServiceUtils.getMetrics(getWindowManager());
		layout.setLayoutParams(new FrameLayout.LayoutParams(wh.widthPixels*2/3,LayoutParams.WRAP_CONTENT));
		
		softname = (String) getIntent().getStringExtra("softname");
		appid = (String) getIntent().getStringExtra("appid");
		
		TextView btn_cancel = (TextView) findViewById(R.id.cannel);
		TextView uninstall=(TextView) findViewById(R.id.uninstall);
		uninstall.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
		//add items 
		LinearLayout listContainer = (LinearLayout) findViewById(R.id.updeinfo_list_container);
		listContainer.setOrientation(LinearLayout.VERTICAL);
		listContainer.removeAllViews();
		if(isStr(appid)){
			TextView tv = new TextView(this);
			tv.setTextColor(Color.parseColor("#4D4D4D"));
			tv.setTextSize(16);
			tv.setText(softname+"签名冲突,如果安装请先卸载该软件!");   
			tv.setPadding(5, 5, 5, 5);
			listContainer.addView(tv);
		}else{
			this.finish();
		}
	}
	public boolean isStr(String str){
		if(str==null||str.equals("")){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		 case R.id.uninstall:
			 if(isStr(appid)){
				 Uri packageURI = Uri.fromParts("package", (String) appid, null);
				Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
				startActivity(intent);
			 }
		     this.finish();
		 break;
		 case R.id.cannel:
			 this.finish();
		 break;
		}
	}
	
	
}