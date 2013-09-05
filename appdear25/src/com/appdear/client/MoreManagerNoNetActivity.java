package com.appdear.client;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.PagerContolerTwoTab;
import com.appdear.client.download.MoreManagerDownloadActivity;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;

/**
 * 管理--已安装
 * 
 * @author zqm
 *
 */
public class MoreManagerNoNetActivity extends BaseGroupActivity implements PagerContolerTwoTab.PagerCallback  {

	private LocalActivityManager manager;
	private View installedView;
	public static View downloadView;
	public static final String ABOUT_T = "关于";
 
	
    private LayoutInflater  mLayoutInflater ;
	private LayoutParams params;
	public int resource;
	private PagerContolerTwoTab pagerContoler = new PagerContolerTwoTab(this);
	/**
	 * MENU ID--市场更新
	 */
	public static final int ABOUT_ID = Menu.FIRST+6;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ABOUT_ID, 0, "").setIcon(R.drawable.help);
		return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Constants.DEBUG)
			Log.i("onOptionsItemSelected", "event" + item.getItemId());
		switch (item.getItemId()) {
		case ABOUT_ID:
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(this, MoreHelpMainActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_main_layout_no_net);
 
	}
	@Override
	public void init() {
       manager = getLocalActivityManager();
    	
    	String[]  strs=new String[]{"可卸载", "下载管理" };
		
    	pagerContoler.initImageView(this,0);
		pagerContoler.initTextView(strs, this);
		installedView = manager.startActivity(
                "installed",
                new Intent(MoreManagerNoNetActivity.this, MoreManagerInstalledActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                .getDecorView();
		 mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		// View view2= mLayoutInflater.inflate(R.layout.pagerview_init, null);
		 downloadView = manager.startActivity(
	                "downlaod",
	                new Intent(MoreManagerNoNetActivity.this, MoreManagerDownloadActivity.class)
	                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
	                .getDecorView();
		 pagerContoler.initViewPager(this,installedView,downloadView, false,false );
	}
 
    
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				ServiceUtils.showExitDialog(MoreManagerNoNetActivity.this);
					return true;
			}
		}
		else if(event.getKeyCode()==67){
			return super.dispatchKeyEvent(event);
		}
		return false;
	}
	
	 
	@Override
	public View viewFirst() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public View viewSecend() {
		 
		
		return null;
	}
	 

	 
	 
 

	
}
