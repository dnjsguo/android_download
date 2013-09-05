/**
 * TheNewSoftListView.java
 * created at:2011-5-20下午03:08:11
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;
import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MessageHandler;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
  
/** 
 * 收藏
 * 
 * @author jdan
 */
public class FavoriteListActivity extends ListBaseActivity {

 

	/**
	 * 列表数据
	 */
	private ApiSoftListResult result;
	
	/**
	 *  详细信息列表
	 */
	private List<SoftlistInfo> listData = new ArrayList<SoftlistInfo>();
	
	/**
	 * 界面无数据提示
	 */
	private TextView wualert = null;
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isUpdate = true;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_list_layout);
	}
	
	@Override
	public void init() {
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		/*listView.setDivider(getResources().getDrawable(R.drawable.listspiner));
		listView.setDividerHeight(2);*/
		
		wualert = (TextView) findViewById(R.id.alert);
		wualert.setText("暂无收藏");
		wualert.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void initData() {
		try {
			String userid = SharedPreferencesControl.getInstance().getString("userid",Common.USERLOGIN_XML,this);
			String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML,this);
			String count = PAGE_SIZE + "";
			result = ApiManager.favoritelist(userid, token, page + "", count);
			page ++;
			listData = result.softList;
			adapter = new SoftwarelistAdatper(this, listData, listView);
			ServiceUtils.setSoftState(this,listData);
			PAGE_TOTAL_SIZE = result.totalcount%PAGE_SIZE==0?
					result.totalcount/PAGE_SIZE:result.totalcount/PAGE_SIZE+1;
			adapter.notifyDataSetChanged();
			if(listView==null)return;
			if (page <= PAGE_TOTAL_SIZE)
				listView.setRefreshDataListener(this);
			 
		} catch (ApiException e) {
			showRefreshButton();
		}
		super.initData();
	}
	
	/* (non-Javadoc)
	 * @see com.appdear.client.commctrls.BaseActivity#updateUI()
	 */
	@Override
	public void updateUI() {
		if(listView!=null)
		{
			if (listData.size() != 0)
				wualert.setVisibility(View.GONE);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new ListView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					setSelectedValues(arg2);
				}
			});
		}
		
	}
	
	/**
	 * 当前项操作
	 * @param position
	 */
	public void setSelectedValues(int position) {
		if(listData==null)return;
		final SoftlistInfo holder = listData.get(position);
		if(holder==null)return;
		int itemstate = 0;
		int menuItemIndex = R.array.select_favorite_items;
		 int i=0;//默认值显示下载
		 Log.i("info",MyApplication.getInstance().getSoftMap().values()+"");
		 if( MyApplication.getInstance().getSoftMap().get(holder.softid)!=null)
		{
				i=MyApplication.getInstance().getSoftMap().get(holder.softid);
		}
		if (i == 0) {
			//下载
			menuItemIndex = R.array.select_favorite_items_installed;
		} else if (i == 1) {
			//下载中
			menuItemIndex = R.array.select_favorite_items_installed;
		} else if (i==2&&ServiceUtils.isWhileFile(holder.downloadurl.substring(holder.downloadurl.lastIndexOf("/")+1))) {
			//有安装文件存在
			menuItemIndex = R.array.select_favorite_items_installed;
			itemstate = 2;
		} else if (i == 4) {
			//升级
			menuItemIndex = R.array.select_favorite_items_installed;
		} else {
			//已安装
			menuItemIndex = R.array.select_favorite_items_installed;
			itemstate = 1;
		}
		
		final int state = itemstate;
		ServiceUtils.getDialogForSelect(FavoriteListActivity.this.getParent(), holder.softname,
				menuItemIndex,new MessageHandler(){
			public void messageHandlerSelect(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (Constants.DEBUG)
				if (Constants.DEBUG) Log.i("jineefo", which+"");
			switch(which){
			case 0:
				//删除收藏
				try {
						//删除前弹出确认提示
						ServiceUtils.getAlertDialogForString(FavoriteListActivity.this, "提示", "确认要删除吗？", new MessageHandler() {
							@Override
							public void messageHandlerOk() {
								super.messageHandlerOk();
								ApiNormolResult result;
								try {
									result = ApiManager.removemessage(SharedPreferencesControl.getInstance().getString("userid",com.appdear.client.commctrls.Common.USERLOGIN_XML,FavoriteListActivity.this),holder.softid+"",SharedPreferencesControl.getInstance().getString("sessionid",com.appdear.client.commctrls.Common.USERLOGIN_XML,FavoriteListActivity.this));
									if(result.isok==1){
										listData.remove(holder);
										adapter.notifyDataSetChanged();     
									}else{
										Toast.makeText(MyApplication.getInstance().getBaseContext(),"删除收藏失败！",Toast.LENGTH_SHORT);
									}
								} catch (ApiException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void messageHandlerCannel() {
								super.messageHandlerCannel();
								Toast.makeText(FavoriteListActivity.this, "messageHandlerCannel()", Toast.LENGTH_SHORT);
							}
						}, 2);
						
				} catch (Exception e) {
						// TODO Auto-generated catch block
//						context.showException(e);
				}
				break;
			case 1:
				//查看信息
				if (Constants.DEBUG)
					Log.i("jineefo", holder.appid);
				if (!ServiceUtils.checkNetState(FavoriteListActivity.this)) {
					Toast.makeText(this,"网络错误，请检查网络状态",Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(FavoriteListActivity.this,SoftwareMainDetilActivity.class);
				//清除进程activity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("downloadurl",holder.downloadurl);
				intent.putExtra("softicon",  holder.softicon);
				intent.putExtra("softid", holder.softid);
				intent.putExtra("downloadcount",holder.download);
				FavoriteListActivity.this.startActivity(intent);
				break;
			case 2:
				if (state == 2) {
					//安装
					final String name = ServiceUtils.getApkname(holder.downloadurl);
					String path = ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null?
							"" : ServiceUtils.getSDCARDImg(Constants.APK_DATA).getPath() + "/" + name ;
					ServiceUtils.Install(this,path,holder.appid,holder.softid);
				} else {
					//下载
					download(holder,state);
					if(adapter!=null)adapter.notifyDataSetChanged();
				}
				break;
			}
		}
	  });
	}
	
	@Override
	public void refreshDataUI() {
		if (result == null || result.softList == null)
			return;
		listData.addAll(result.softList);
		page ++;
	}
	
	@Override
	public void doRefreshData() {
		try {
			if (page > PAGE_TOTAL_SIZE) {
				listView.setEndTag(true);
				return;
			}
			String userid = SharedPreferencesControl.getInstance().getString("userid",Common.USERLOGIN_XML,this);
			String token = SharedPreferencesControl.getInstance().getString("sessionid", Common.USERLOGIN_XML,this);
			String pageno = page + "";
			String count = PAGE_SIZE + "";
			result = null;
			result = ApiManager.favoritelist(userid, token, pageno, count);
			if(result!=null){
				ServiceUtils.setSoftState(this,result.softList);
			}
		} catch (ApiException e) {
			listView.setErrTag(true);
		}
	}
	
	@Override
	public void dataNotifySetChanged() {
		if(adapter==null)return;
		adapter.notifyDataSetChanged();
		super.dataNotifySetChanged();
	}
	private void download(SoftlistInfo holder,int state){
		if(holder!=null){
			String name = ServiceUtils.getApkname(holder.downloadurl);
			SiteInfoBean bean = new SiteInfoBean(holder.downloadurl,
					ServiceUtils.getSDCARDImg(Constants.APK_DATA)==null?"":ServiceUtils.getSDCARDImg(Constants.APK_DATA).getPath(),
					name, holder.softname, holder.softicon, holder.version,
					holder.softid, holder.appid, holder.softsize, 0, 1, null, null,
					BaseActivity.downloadUpdateHandler,state==4?Constants.UPDATEPARAM:"");
			String[] msg = DownloadUtils.download(bean, FavoriteListActivity.this);
			//任务已存在
			Toast mScreenHint = Toast.makeText(
				FavoriteListActivity.this, msg[0],Toast.LENGTH_SHORT);
			mScreenHint.show();
		}else{
			Toast mScreenHint = Toast.makeText(FavoriteListActivity.this, "下载失败,请尝试重新下载！",Toast.LENGTH_SHORT);
			mScreenHint.show();
		}
				
	}
}

 