package com.appdear.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdear.client.ContactOperateActivity.DialogListener;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.InstalledBackupControler;
import com.appdear.client.commctrls.PagerCallback;
import com.appdear.client.commctrls.PagerContoler;
import com.appdear.client.commctrls.PagerContolerVersion;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.TabCallBack;
import com.appdear.client.commctrls.TabbarCallback;
import com.appdear.client.commctrls.TopTabbar;
import com.appdear.client.download.MoreManagerDownloadActivity;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ContactUtil;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.SmsUtil;

/**
 * 管理--已安装
 * 
 * @author zqm
 *
 */
public class MoreManagerActivity extends BaseActivity implements
OnClickListener{

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.managernew);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		Button Button01 = (Button) findViewById(com.appdear.client.R.id.Button01);
		Button Button02 = (Button) findViewById(com.appdear.client.R.id.Button02);
		Button Button03 = (Button) findViewById(com.appdear.client.R.id.Button03);
		Button01.setOnClickListener(this);
		Button02.setOnClickListener(this);
		Button03.setOnClickListener(this);
//		Button btn_process = (Button) findViewById(R.id.btn_process);
		Button seting = (Button) findViewById(R.id.seting);
//		btn_process.setOnClickListener(this);
		seting.setOnClickListener(this);
	//	myRunnable = new GetRecommendListRunnble();
	//	new Thread(myRunnable).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.Button01:
			 Intent intent=new Intent(MoreManagerActivity.this, MoreManagerUpdateActivity.class)
             .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 this.startActivity(intent);
			break;
		case R.id.Button02:
			Intent intent1= new Intent(MoreManagerActivity.this, MoreManagerInstalledActivity.class)
              .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              this.startActivity(intent1);
			break;
		case R.id.Button03:
			Intent intent2= new Intent(MoreManagerActivity.this, MoreManagerDownloadActivity.class)
              .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              this.startActivity(intent2);
			break;
	/*	case R.id.darenUpdateButton:
			if (isLoading) {
				Toast.makeText(ContactOperateActivity.this, "正在加载数据请稍后",
						Toast.LENGTH_SHORT).show();
			} else {
				new Thread(myRunnable).start();
			}
			break;*/
		/*case R.id.btn_process:
			 break;*/
		case R.id.seting:
			Intent intent3 = new Intent();
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent3.setClass(this, MoreSettingsActivity.class);
			startActivity(intent3);
			break;
		default:
			break;
		}
	}

}
