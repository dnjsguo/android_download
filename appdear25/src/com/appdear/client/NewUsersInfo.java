package com.appdear.client;

import java.io.IOException;

import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class NewUsersInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_dialog_layout);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.update_layout_);
		DisplayMetrics wh = ServiceUtils.getMetrics(getWindowManager());
		layout.setLayoutParams(new FrameLayout.LayoutParams(wh.widthPixels*4/5, wh.heightPixels*4/5));
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("新功能体验");
		
		TextView btn_ok = (TextView) findViewById(R.id.ok);
		TextView btn_cancel = (TextView) findViewById(R.id.cancel);
		btn_ok.setVisibility(View.GONE);
		btn_cancel.setVisibility(View.GONE);
		TextView updateyuan=(TextView) findViewById(R.id.updateyuan);
		updateyuan.setVisibility(View.GONE);
		TextView btn_confirm = (TextView) findViewById(R.id.confirm);
		btn_confirm.setVisibility(View.VISIBLE);
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("roll_cancel", "true");
				intent.setClass(NewUsersInfo.this, NoviceGuideActivity.class);
				NewUsersInfo.this.startActivity(intent);
				finish();
			}
		});
	
		//add items 
		LinearLayout listContainer = (LinearLayout) findViewById(R.id.updeinfo_list_container);
		listContainer.setOrientation(LinearLayout.VERTICAL);
		listContainer.removeAllViews();
		
		String updateInfo = "";
		try {
			updateInfo = ServiceUtils.getXIEYI(this.getAssets().open("updateinfo.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		tv_version.setText("版本："+ Constants.VERSION);
		
		TextView tv_size = (TextView) findViewById(R.id.size);
		tv_size.setText("大小：1.58M");
	
	}
}
