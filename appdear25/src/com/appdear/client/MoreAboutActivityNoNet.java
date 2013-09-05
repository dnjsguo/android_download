package com.appdear.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.appdear.client.service.Constants;
import com.appdear.client.R;

public class MoreAboutActivityNoNet extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_about);
		TextView about = (TextView) findViewById(R.id.about_version);
		about.setText("°æ±¾£ºV" + Constants.VERSION);
	}
}
