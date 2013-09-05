package com.appdear.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.Adapter.UpdateListAdapter;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.ElideUpdateView;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.model.UpdatelistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.AppdearService;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;

/**
 * 更新
 * 
 * @author zqm
 * 
 */
public class MoreManagerUpdateActivity extends ListBaseActivity {

 	private boolean ismElideUpdateViewOnShow = false;
	private ElideUpdateView mElideUpdateView;
	private TextView availableText;
	private String text;
	private int intalledsize;
	private int intalledsizesys;
	private Button click_button_update, userprofile_update_elide;
			//hulue_all_button;// 升级所有 忽略
	private LinearLayout update_all_button, elide_all_button;// 升级所有，忽略所有
	private boolean flag = false;
	ImageButton btn_return;
//	ImageView iv_fengexian;

	public static boolean allupdateflag = true;

	/**
	 * 线程池
	 * 
	 * @return
	 */
	// protected ExecutorService pool = Executors.newFixedThreadPool(10);

	public synchronized boolean getFalg() {
		return flag;
	}

	public synchronized void setFalg(boolean flag) {
		this.flag = flag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_soft_list_layout);
		TextView text=(TextView)findViewById(R.id.title_font);
		text.setText("软件管理--可升级");
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		loadingView = new MProgress(this, false);
		this.addContentView(loadingView, params);
		if (!AppContext.getInstance().isNetState) {
			handler1.sendEmptyMessage(LOADG);
			showRefreshButton();
			return;
		}
	}

	@Override
	public void init() {
		if (getIntent().getStringExtra("notificaiton") != null
				&& getIntent().getStringExtra("notificaiton").equals("true")) {
			RelativeLayout title_manager = (RelativeLayout) findViewById(R.id.title_manager);
			title_manager.setVisibility(View.VISIBLE);
			btn_return = (ImageButton) findViewById(R.id.btn_return);
			btn_return.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			title_manager.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			TextView title = (TextView) title_manager
					.findViewById(R.id.title_font);
			title.setText("可升级");
		}else{
			RelativeLayout title_manager = (RelativeLayout) findViewById(R.id.title_manager);
			title_manager.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			//返回键
			ImageButton btn_return = (ImageButton) findViewById(R.id.btn_return);
			btn_return.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/

		mElideUpdateView = (ElideUpdateView) findViewById(R.id.elideupdateview);
		mElideUpdateView.setDivider(getResources().getDrawable(
				R.drawable.listspiner));
		mElideUpdateView.setDividerHeight(2);
		mElideUpdateView.setVisibility(View.GONE);
		ismElideUpdateViewOnShow = false;

		availableText = (TextView) findViewById(R.id.available);
		// tab切换,查看升级,查看忽略
		click_button_update = (Button) findViewById(R.id.userprofile_update);
		userprofile_update_elide = (Button) findViewById(R.id.userprofile_update_elide);

		text = "您当前没有需要升级的应用";
		availableText.setVisibility(View.GONE);

		// 全部升级，全部取消按钮
		update_all_button = (LinearLayout) findViewById(R.id.update_all_button);
		elide_all_button = (LinearLayout) findViewById(R.id.elide_all_button);
//		hulue_all_button = (Button) findViewById(R.id.hulue_all_button);
//		iv_fengexian = (ImageView) findViewById(R.id.iv_fengexian);
		update_all_button.setVisibility(View.VISIBLE);
//		hulue_all_button.setVisibility(View.VISIBLE);
//		iv_fengexian.setVisibility(View.VISIBLE);

		// 全部升级
		update_all_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (allupdateflag == true) {
					allupdateflag = false;
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							handlerAllview();
							/*if(adapter!=null)
							adapter.notifyDataSetChanged();*/
						}
					});
 					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
						 
							if(adapter!=null)
							adapter.notifyDataSetChanged();
						}
					},1000);
				}
				// canalAllUpdate();
			}
		});

		// 全部取消
		elide_all_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				canalAll();   //忽略  全部取消
			}
		});

		/*hulue_all_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				canalAllUpdate();  //升级  全部取消
			}
		});*/

		userprofile_update_elide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//点击查看忽略按钮
				// 替换两个焦点
				userprofile_update_elide
						.setBackgroundResource(R.drawable.view_ignore_click);
				click_button_update.setBackgroundColor(Color.parseColor("#00ffffff"));
				listView.setVisibility(View.GONE);
				availableText.setVisibility(View.GONE);
				click_button_update.setTextColor(Color.parseColor("#525252"));
				//userprofile_update_elide.setTextColor(Color.WHITE);

				// 隐藏全部升级按钮，显示全部忽略按钮
				update_all_button.setVisibility(View.GONE);
//				hulue_all_button.setVisibility(View.GONE);
//				iv_fengexian.setVisibility(View.GONE);
				elide_all_button.setVisibility(View.VISIBLE);
				// 显示忽略列表
				mElideUpdateView.setVisibility(View.VISIBLE);
				ismElideUpdateViewOnShow = true;
			}
		});
		click_button_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {//点击查看升级按钮
				click_button_update
						.setBackgroundResource(R.drawable.view_ignore_click);
				//click_button_update.setText("aaaa");
				userprofile_update_elide.setBackgroundColor(Color.parseColor("#00ffffff"));
				elide_all_button.setVisibility(View.GONE);
				update_all_button.setVisibility(View.VISIBLE);
//				hulue_all_button.setVisibility(View.VISIBLE);
//				iv_fengexian.setVisibility(View.VISIBLE);
				//click_button_update.setTextColor(Color.WHITE);
				userprofile_update_elide.setTextColor(Color
						.parseColor("#525252"));
				if (ismElideUpdateViewOnShow) {
					mElideUpdateView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					if (adapter != null) {
						adapter.notifyDataSetChanged();
					}
					ismElideUpdateViewOnShow = false;
					return;
				}
			}
		});
	}

	@Override
	public void initData() {
		text = "您当前没有需要升级的应用";
		ServiceUtils.getAllPackages(this);
//		if (AppContext.getInstance().installlists.size() == 0)
//			AppContext.getInstance().installlists = ServiceUtils
//					.getInstalledApps(this, false,false);
//
//		if (AppContext.getInstance().installlistssys.size() == 0)
//			AppContext.getInstance().installlistssys = ServiceUtils
//					.getInstalledAppsSys(this, false);
		// Log.i("size",AppContext.getInstance().installlistssys.size()+"="+AppContext.getInstance().installlists.size());
		ApiSoftListResult result = AppdearService.updateSoftResult;

		if (null == result) {
			try {
				List<PackageinstalledInfo> list = new ArrayList();
				
				AppContext.getInstance().installlists = ServiceUtils
							.getInstalledApps(this, false,true);

				if (AppContext.getInstance().installlistssys == null
						|| AppContext.getInstance().installlistssys.size() == 0) {
					AppContext.getInstance().installlistssys = ServiceUtils
							.getInstalledAppsSys(this, false);
				}
//				list.addAll(ServiceUtils
//						.getInstalledAppsForServer(this, false).values());
//				list.addAll(ServiceUtils
//						.getInstalledAppsSysForServer(this, false).values());
//				// Log.i("info909",list.size()+"="+AppContext.getInstance().installlistssys.size());
//				result = ApiManager.updatelist(ServiceUtils.getUpdatelist(list), this);
				
			//	List list= new ArrayList();
				List list1= new ArrayList();
				list.addAll(AppContext.getInstance().installlistssys.values());
				list1.addAll(AppContext.getInstance().installlists.values());
				result = ApiManager.updatelist2(ServiceUtils.getUpdatelist(list),list1,MoreManagerUpdateActivity.this);
				AppdearService.updateSoftResult = result;
				list.clear();
				list = null;
				// Log.i("info",result.updatelist+"");
			} catch (ApiException e) {
				// Log.i("info909", e.getMessage());
				showRefreshButton();
				text = e.getMessage();
			}
		}
		// Log.i("info9",AppContext.getInstance().updatelist+"=>start");
		if (AppContext.getInstance().updatelist != null
				&& AppContext.getInstance().updatelist.size() > 0) {

		} else if (result != null && result.updatelist.size() > 0) {
			// Log.i("info99",result.updatelist+"");
			AppContext.getInstance().updatelist.clear();
			// for (java.util.Map.Entry<String, PackageinstalledInfo>
			// pack:AppContext.getInstance().installlists.entrySet()) {
			// if(pack.getValue()==null)continue;
			// String appname =pack.getValue().pname;
			// Log.i("info",appname);
			java.util.Map<String, PackageinstalledInfo> map = AppContext
					.getInstance().installlists;
			java.util.Map<String, PackageinstalledInfo> map1 = AppContext
					.getInstance().installlistssys;

			// Log.i("info090", result.updatelist+"=>");
			for (UpdatelistInfo uinfo : result.updatelist) {

				String updateappid = uinfo.appid;
				PackageinstalledInfo info = null;
				if (map.containsKey(updateappid)) {
					info = map.get(updateappid);
				} else if (map1.containsKey(updateappid)) {
					info = map1.get(updateappid);
				}
				if (info == null)
					continue;
				info.updateVesrionName = uinfo.versionname;
				info.softID = uinfo.softid;
				info.downloadUrl = uinfo.downloadurl;
				info.softsize = uinfo.softsize;
				info.updatedesc=uinfo.updatedesc;
				info.udlinenum=uinfo.udlinenum;
				if (AppContext.getInstance().elideupdatelistpackages
						.contains(info.pname)) {
					if (AppContext.getInstance().elideupdatelist.contains(info) == false)
						AppContext.getInstance().elideupdatelist.add(info);

				} else {
					AppContext.getInstance().updatelist.add(info);
				}
			}
		}
		intalledsize = AppContext.getInstance().installlists.size();
		intalledsizesys = AppContext.getInstance().installlistssys.size();
		adapter = new UpdateListAdapter(this, handler,listView);
		handler1.sendEmptyMessage(CALL_STATUS);
		super.initData();
	}

	@Override
	public void updateUI() {
		if(listView==null)return;
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});
		if (mElideUpdateView.getVisibility() == View.VISIBLE) {
			availableText.setVisibility(View.GONE);
		} else {
			if (AppContext.getInstance().updatelist == null
					|| AppContext.getInstance().updatelist.size() == 0) {

				availableText.setText(text);
//				availableText.setVisibility(View.VISIBLE);
			} else {
				availableText.setVisibility(View.GONE);
			}
		}
		super.updateUI();
	}

	Handler handler = new Handler();

	@Override
	public void refreshDataUI() {
		if (AppContext.getInstance().updatelist.size() <= 0)
			availableText.setText(text);
		setFalg(true);
		updateUIEnd();
	}

	@Override
	public void doRefreshData() {
	}

	/**
	 * 检查安装列表变化
	 */
	private void changeInstalled() {
		if (intalledsize != AppContext.getInstance().installlists.size()) {
			intalledsize = AppContext.getInstance().installlists.size();
			// initSoftNum();
			handler.sendEmptyMessage(UPDATE);
		} else if (intalledsizesys != AppContext.getInstance().installlistssys
				.size()) {
			intalledsizesys = AppContext.getInstance().installlistssys.size();
			// initSoftNum();
			handler.sendEmptyMessage(UPDATE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (AppContext.getInstance().updatelist != null
				&& AppContext.getInstance().updatelist.size() > 0) {
			availableText.setVisibility(View.GONE);
			click_button_update.setVisibility(View.VISIBLE);
		}
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		changeInstalled();
		// click_button_update.setEnabled(true);
		super.onNewIntent(intent);
	}

	@Override
	protected void updateUIEnd() {
		// TODO Auto-generated method stub
		if (this.getFalg() == true) {
			super.updateUIEnd();
		}
	}

	private synchronized void canalAll() {
		if (AppContext.getInstance().elideupdatelist != null
				&& AppContext.getInstance().elideupdatelist.size() > 0) {
			for (PackageinstalledInfo info : AppContext.getInstance().elideupdatelist) {
				AppContext.getInstance().updatelist.add(0, info);
				ServiceUtils.removeOneElidePackages(this, info.pname);
			}
			MyApplication.getInstance().elideupdate=MyApplication.getInstance().elideupdate+AppContext.getInstance().elideupdatelist.size();
			//更新主界面主菜单显示更新数量
			((MainActivity)MyApplication.getInstance().mainActivity).updateNumView();
			AppContext.getInstance().elideupdatelist.removeAllElements();
			if (mElideUpdateView != null
					&& mElideUpdateView.getAdapter() != null) {
				mElideUpdateView.getAdapter().notifyDataSetChanged();
			}
		}
	}

	private synchronized void canalAllUpdate() {
		if (AppContext.getInstance().updatelist != null
				&& AppContext.getInstance().updatelist.size() > 0) {
			
			for (int i = AppContext.getInstance().updatelist.size() - 1; i >= 0; i--) {
				PackageinstalledInfo mPackageinstalledInfo = AppContext
						.getInstance().updatelist.remove(i);
				AppContext.getInstance().elideupdatelist
						.add(mPackageinstalledInfo);
				ServiceUtils.putOneElidePackages(this,
						mPackageinstalledInfo.pname);
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			//更新主界面主菜单显示更新数量
			MyApplication.getInstance().elideupdate=0;
			((MainActivity)MyApplication.getInstance().mainActivity).updateNumView();
		}
	}

	public void handlerAllview() {
		Integer i = null;
		for (PackageinstalledInfo info : AppContext.getInstance().updatelist) {
			i = MyApplication.getInstance().getSoftMap().get(info.softID);
			if (i != null) {
				if (i == 4 || i > 10) {
					handlerUpdate(info);
				}
			} else {
				handlerUpdate(info);
			}
		}
		MoreManagerUpdateActivity.allupdateflag = true;
	}

	private void handlerUpdate(PackageinstalledInfo info) {

		String filename = info.downloadUrl.substring(info.downloadUrl
				.lastIndexOf("/") + 1);
		if (filename.lastIndexOf("?") > 0)
			filename = filename.substring(0, filename.lastIndexOf("?"));
		SoftlistInfo item = new SoftlistInfo();
		item.appid = (info.pname);
		item.softid = (info.softID);
		item.softname = (info.appname);
		item.icon = info.icon;
		item.version = info.versionName;
		item.softsize = info.softsize;
		item.downloadurl = info.downloadUrl;
		if (download(item, filename, info)) {
		}
	}

	private boolean download(SoftlistInfo info, String apkname,
			PackageinstalledInfo item) {
		final SiteInfoBean downloadbean = new SiteInfoBean(info.downloadurl,
				ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null ? ""
						: ServiceUtils.getSDCARDImg(Constants.APK_DATA)
								.getPath(),
				ServiceUtils.getApkname(info.downloadurl), info.softname,
				info.softicon, info.version, info.softid, info.appid,
				info.softsize, 0, 1, null, null,
				BaseActivity.downloadUpdateHandler, Constants.UPDATEPARAM);
		item.downloadUrl = downloadbean.sSiteURL;
 		downloadbean.dicon = info.icon;

		// 提示下载信息
		String[] msg = DownloadUtils.download(downloadbean, this);
//		Toast mScreenHint = Toast.makeText(this, msg[0],Toast.LENGTH_SHORT);
//		mScreenHint.show();
		if (msg[1] != null && msg[1].equals("0"))
			return true;
		return false;
	}
	
//	private void setState(int softid,String url){
//		SiteInfoBean bean = ServiceUtils.checkTaskState(softid);
//		//下载中判断是否完成下载
//		
//		if (bean != null && bean.state == 2) {
//			//apk文件完整
//			//item.state = 2;
//			MyApplication.getInstance().getSoftMap().put(softid, 2);
//			return;
//		} else {
//			if (bean != null) {		
//				//item.state = 1;	
//				MyApplication.getInstance().getSoftMap().put(softid, 1);
//				return;
//			}
//		}
//		
//			if (!url.equals("")&&bean!=null) {
//				String apkname=bean.sFileName;
//				if (ServiceUtils.isWhileFile(apkname)) {
//					//item.state = 2;
//					MyApplication.getInstance().getSoftMap().put(softid, 2);
//					return;
//				}
//			}
//	}
}
