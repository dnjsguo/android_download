package com.appdear.client;

import com.appdear.client.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Õ¯¬Á…Ë÷√Ã· æ
 * @author zqm
 *
 */
public class AlertDialogView extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alertdialog_layout);
		
		if (getIntent().getStringExtra("type").equals("close")) {
			finish();
		}
		
		Button settings = (Button) findViewById(R.id.yes);
		settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(
						new Intent(
								Settings.ACTION_WIRELESS_SETTINGS), 0);
			}
		});
		
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getStringExtra("type").equals("close")) {
			finish();
		}
	}
}
