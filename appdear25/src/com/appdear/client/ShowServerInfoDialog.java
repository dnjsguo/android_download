package com.appdear.client;

import com.appdear.client.service.AppdearService;
import com.appdear.client.service.Constants;
import com.appdear.client.update.CheckVersion;
import com.appdear.client.utility.ServiceUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class ShowServerInfoDialog extends Activity{
	UpdateDialog updateDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//this.setContentView(R.layout.show_serverinfo_layout);
		/**
		 * 全屏主题
		 */
		this.setTheme(R.style.Dialog_Fullscreen);
	    this.setContentView(R.layout.update_dialog_layout);
		showUpdateDialog();
		super.onCreate(savedInstanceState);
	}
 
	protected void showUpdateDialog() {
		if(Constants.DEBUG)Log.i("update", "background  service    softUpdateTip 2333333322254545    "+AppdearService.softUpdateTip);
		// 提示
		if (Constants.DEBUG)
			Log.i("BaseActivity", "BaseActivity  BackgroundService.updateurl  :"
					+ AppdearService.updateurl);
			TextView btn_ok = (TextView) this.findViewById(R.id.ok);
			TextView btn_cancel = (TextView) this
					.findViewById(R.id.cancel);
			btn_ok.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					 if(null==ServiceUtils.getSDCardUrl())
							Toast.makeText(ShowServerInfoDialog.this,"您的SD卡不存在，或者插入不正确！",Toast.LENGTH_LONG).show();
						 else{
							 CheckVersion.getInstance().checkVersion(ShowServerInfoDialog.this, AppdearService.updateurl, true);
							 
							 Toast.makeText(ShowServerInfoDialog.this,"更新已开始！",Toast.LENGTH_LONG).show();
						 }
				}
			});
			btn_cancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					ShowServerInfoDialog.this.finish();
				}
			});
			//set scroll height .
			ScrollView scrolllist = (ScrollView)this.findViewById(R.id.contentlayout);
			Resources resources = getResources();//获得res资源对象
			DisplayMetrics dm = resources .getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
			int height = dm.heightPixels;
			int sheight = (int) (5/(float)9*height);
			scrolllist.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,sheight));
			scrolllist.setPadding(0, 25, 0, 10);
			
			//add items 
			LinearLayout listContainer = (LinearLayout)this.findViewById(R.id.updeinfo_list_container);
			listContainer.setOrientation(LinearLayout.VERTICAL);
			listContainer.removeAllViews();
			String updateInfo = AppdearService.softUpdateTip;
			String [] infoItems = updateInfo.split("\\s");
			if(infoItems.length>0){
				for(int i = 0;i<infoItems.length;i++){
					if(!"".equals(infoItems[i].trim())){
						TextView tv = new TextView(this);
						tv.setTextColor(Color.parseColor("#4D4D4D"));
						tv.setText(" "+infoItems[i]);
						tv.setPadding(5, 3, 2, 2);
						listContainer.addView(tv);
					}
				}
			}
			
			TextView tv_version = (TextView)this.findViewById(R.id.versionname);
			tv_version.setText("最新版本："+AppdearService.softVersion);
			
			TextView tv_size = (TextView)this.findViewById(R.id.size);
			tv_size.setText("大小："+AppdearService.softSize);

	}
}
