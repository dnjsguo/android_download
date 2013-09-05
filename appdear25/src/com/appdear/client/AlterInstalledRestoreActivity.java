package com.appdear.client;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.appdear.client.Adapter.InstalledRestoreAdapter;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.InstalledBackupControler;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ServiceUtils;

public class AlterInstalledRestoreActivity extends ListBaseActivity implements  OnClickListener {

	 public   List<SoftlistInfo> listData = null ;
	 HashMap<Integer,Boolean> isSelected;
     public Button allSelectedButton;
     public  Button restoredButton;
      int taskNum=0;
  //   boolean isAllInstalled=true;
     private   final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
      {
         sWorkerThread.start();
     }
     private   final Handler sWorker = new Handler(sWorkerThread.getLooper());

  //  进度条对话框
 	ProgressDialog  pDialog;
    public Hashtable<String,PackageinstalledInfo> installlists = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.installed_restore_list_layout);
		 
	}
	
	@Override
	public void init() {
		listView = (ListViewRefresh) findViewById(R.id.installed_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setFadingEdgeLength(0);
		listView.setVerticalFadingEdgeEnabled(false);
		listView.setHorizontalFadingEdgeEnabled(false);
 		allSelectedButton=(Button)this.findViewById(R.id.allSelectedButton);
		restoredButton=(Button)this.findViewById(R.id.restoredButton);
		allSelectedButton.setOnClickListener(this);
		restoredButton.setOnClickListener(this);
 	}
	
	
	public void initData() {
		
		listData=	InstalledBackupControler.listData;
		//installlists= ServiceUtils.getInstalledApps(this, false,true);
		ServiceUtils.setSoftState(this, listData);
		Collections.sort(listData , new Comparator(){

			public int compare(final  Object o1, final  Object o2) {
				SoftlistInfo  info1=(SoftlistInfo)o1 ;
				SoftlistInfo  info2=(SoftlistInfo)o2 ;
				if(info1.type>info2.type)
				{
				return  1;
				}
				else if(info1.type<info2.type)
				{
					return  -1;
				}else
				{
					return 0;
				}
			 
			}
			
		});
		/*for(SoftlistInfo item:listData  )
		{
			if(installlists.get(item.appid)!=null)
			{
				item.type=1;// 1 已经安装  0没有安装 
			}else
			{
				isAllInstalled=false;
				item.type=0;
			}
		}*/
		isSelected= new HashMap<Integer,Boolean>();
		
    	for(int i = 0; i<listData.size(); i++){
    		SoftlistInfo item=(SoftlistInfo)listData.get(i);
    		if(item.type!=1)
    		{
    			isSelected.put(i, false);
    		}	
		}
    	
		adapter = new InstalledRestoreAdapter(this, listData, listView,isSelected);
		
		
		adapter.notifyDataSetChanged();
	}
	@Override
	public void updateUI(){
	 
		listView.setAdapter(adapter);
	}
	@Override
	public void refreshDataUI() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doRefreshData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.allSelectedButton )
		 {
			 /*if(isAllInstalled)
			 {
				 showMessageInfo("全部应用都已经安装");
				 return;
			 }*/
			
			 for(int i = 0; i<listData.size(); i++){
		    		SoftlistInfo item=(SoftlistInfo)listData.get(i);
		    		if(item.type!=1)
		    		{
		    			isSelected.put(i, true);
		    		}	
				}
			 adapter.notifyDataSetChanged();
		 }else if(v.getId()==R.id.restoredButton)
		 {
			/* if(isAllInstalled)
			 {
				 showMessageInfo("全部应用都已经安装");
				 return;
			 }*/
			 boolean isSele=false;
			 Collection<Boolean> values=isSelected.values();
			  for(Boolean item:values)
			  {
				  if(item)
				  {
					  isSele=true;
					  break;
				  }
			  }
			  if(!isSele)
			  {
				  showMessageInfo("请勾选需要还原的软件");
				  return;
			  }
			 sWorker.post(new Runnable() {
				
				@Override
				public void run() {
					 dialog();
				}
			});
			 new Thread() {
					public void run() {
						 for(int i = 0; i<listData.size(); i++){
							 if(isSelected.get(i)!=null&&isSelected.get(i))
							 {
								 taskNum++;
								 Message msg=new Message();
								 msg.arg1=i;
								 msg.what=0;
								 
								// myHandler.sendMessage(msg);
								 SoftlistInfo item=(SoftlistInfo)listData.get(i);
						    		
						    		if(item.type!=1)
						    		{	 
						    			Integer Status = MyApplication.getInstance().getSoftMap().get(item.softid);
						    			if (Status == null||Status==0) {
						    				 if (download(item,  item.downloadurl,0)) {
						    					 
						    				 } 
						    			} 
						    		}	
							 }
						}
						 try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
 						 sWorker.post(new Runnable() {
								
								@Override
								public void run() {
									 pDialog.cancel();
								     if(taskNum!=0)
								     Toast.makeText(AlterInstalledRestoreActivity.this, "有"+taskNum+"下载任务添加到下载管理中，请查看",Toast.LENGTH_LONG ).show();
									 AlterInstalledRestoreActivity.this.finish();

								}
							});
					};
				}.start();
 			
		 }
	}
/*	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case  0:  //  执行下载
				int i=msg.arg1;
				SoftlistInfo item=(SoftlistInfo)listData.get(i);
	    		 
	    		if(item.type!=1)
	    		{	 
	    			Integer Status = MyApplication.getInstance().getSoftMap().get(item.softid);
	    			if (Status == null||Status==0) {
	    				 if (download(item,  item.downloadurl,0)) {
	    					 
	    				 } 
	    			} 
	    		}
 	    		break;
			case  1:
 
			//	pDialog.cancel();
				if(taskNum!=0)
				Toast.makeText(AlterInstalledRestoreActivity.this, "有"+taskNum+"下载任务添加到下载管理中，请查看",Toast.LENGTH_LONG ).show();
				AlterInstalledRestoreActivity.this.finish();
			}
		}
	};*/
	private boolean download(SoftlistInfo info, String apkname,int state) {
		final SiteInfoBean downloadbean = new SiteInfoBean(info.downloadurl,
				ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null ? ""
						: ServiceUtils.getSDCARDImg(Constants.APK_DATA)
								.getPath(), ServiceUtils.getApkname(info.downloadurl), info.softname,
				info.softicon, info.version, info.softid, info.appid,
				info.softsize, 0, 1, null, null,
				BaseActivity.downloadUpdateHandler,state==4?Constants.UPDATEPARAM:"");
		String[] msg = DownloadUtils.download(downloadbean, this);
		// 任务已存在
		//Toast mScreenHint = Toast.makeText(this, msg[0],Toast.LENGTH_SHORT);
		//mScreenHint.show();
		if (msg[1] != null && msg[1].equals("0"))
			return true;
		return false;
	}
	public void dialog() {
		if (pDialog == null)
			pDialog = new ProgressDialog(this);
		// 设置进度条风格，风格为圆形，旋转的
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(false);
		pDialog.setMessage("正在处理中请稍后");
 
		pDialog.setIndeterminate(false);
		pDialog.show();
		// 设置ProgressDialog 是否可以按退回键取消
		// xh_pDialog.setCancelable(true);
		// 设置ProgressDialog 的一个Button
		// xh_pDialog.setButton("确定", new Bt1DialogListener());
	}
}
