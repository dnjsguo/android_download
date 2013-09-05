package com.appdear.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.update.Global;
import com.appdear.client.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import android.widget.RemoteViews;

/**
 * �г����²���
 * @author jdan
 *
 */
public class UpdateAppService extends Service{
	private final static int DOWNLOAD_COMPLETE=1;
	private final static int DOWNLOAD_FAIL=2;
	public final static int notification_id=15674;
	//private int titleId = 0;  
	private String url;
	public Intent update;
	//鏂囦欢�?�樺�?  
	private File updateDir = null;  
	private File updateFile = null;
	RemoteViews view=null;
	//閫氱煡鏍�? 
	private NotificationManager updateNotificationManager = null;  
	private Notification updateNotification = null;  
	//閫氱煡鏍忚烦杞琁ntent  
	private Intent updateIntent = null;  
	private PendingIntent updatePendingIntent = null; 
	private Handler updateHandler = new  Handler(){  
			@Override 
			public void handleMessage(Message msg) {  
				//
				switch(msg.what){
					case DOWNLOAD_COMPLETE:
							Uri uri=Uri.fromFile(updateFile);
							Intent install=new Intent(Intent.ACTION_VIEW);
							install.setDataAndType(uri,"application/vnd.android.package-archive");
							install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							updateNotificationManager.cancel(notification_id);
							UpdateAppService.this.startActivity(install);
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									ApiManager.downloadcomplete(url+"?softid=-1", "1");
									ApiManager.downloadcomplete(url+"?softid=-1", "2");
								}
							}).start();
							stopService(updateIntent);
							break;
					case DOWNLOAD_FAIL:
						//閲嶆柊涓嬭浇璧勬�?
						update=new Intent(UpdateAppService.this,UpdateAppService.class);
						Bundle bundle=new Bundle();
						bundle.putInt("titleId", R.string.app_name);
					 	//鎴栦粠鏈嶅姟鍣ㄥ彇寰椾笅杞藉湴鍧�?
					 	//updateIntent.putExtra("url",Global.downloadserverurl);
						if (AppContext.getInstance().info == null)
							break;
					 	bundle.putString("appurl",AppContext.getInstance().info.updateurl);
					 	update.putExtra("bundle", bundle);
					 	updatePendingIntent=PendingIntent.getService(UpdateAppService.this,0,update,0);
						
						updateNotification.setLatestEventInfo(UpdateAppService.this,getResources().getString(R.string.app_name),"����ʧ�ܣ�������?",updatePendingIntent);
						if(updateNotification !=null){
							updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;  
						}
						updateNotificationManager.notify(notification_id, updateNotification);
						stopService(updateIntent);
						break;
					default:
						stopService(updateIntent);
				}
			}  
	}; 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	//鍔犺浇淇℃伅
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent == null)
			return -1;
		Bundle bundle=intent.getBundleExtra("bundle");
		if (bundle == null)
			return -1;
		//titleId = bundle.getInt("titleId",0);  
		url=bundle.getString("appurl");
		if(!url.contains("http://"))
			url = "http://content.appdear.com/"+url;
		if (Constants.DEBUG)Log.i("update", "client need to update action onStartCommand ,client url :"+url);
		//鍒涘缓鏂囦欢  
		if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){  
				updateDir = new File(Environment.getExternalStorageDirectory(),Global.downloadDir);  
				updateFile = new File(updateDir.getPath(),"appstore_"+(Constants.VERSIONCODE+1)+".apk");  
		}  
		
		this.updateNotificationManager=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
		updateNotification=new Notification(R.drawable.logo,this.getString(R.string.app_name),System.currentTimeMillis());
		
		
		//閫氱煡鏄剧ず鍐呭�?
		view=new RemoteViews(this.getPackageName(),R.layout.update);
		updateNotification.contentView=view;
		updateNotification.contentView.setTextViewText(R.id.app_name,this.getString(R.string.app_name));
		updateNotification.contentView.setImageViewBitmap(R.id.app_icon,BitmapFactory.decodeResource(this.getResources(),R.drawable.logo));
		updateNotification.contentView.setTextViewText(R.id.update_progresstext,"0%");
		updateNotification.contentView.setProgressBar(R.id.update_progressbar, 100, 0, false);
		
		
		//杩斿洖service鐣岄�?
		updateIntent=new Intent(this,UpdateAppService.class);
		updateIntent.putExtra("bundle", bundle);
		updatePendingIntent=PendingIntent.getActivity(this,0,updateIntent,0);
		
		updateNotification.contentIntent=updatePendingIntent;
		
		updateNotificationManager.notify(notification_id,updateNotification);
		new Thread(new UpdateRunnable(updateHandler.obtainMessage())).start();
		if (Constants.DEBUG)Log.i("update", "client need to update action download thread started");
		return super.onStartCommand(intent, flags, startId);
	}
	//浜掕仈缃戜笅杞絘pp鐨勭嚎绋�?
	class UpdateRunnable implements Runnable{
		//寰楀埌涓�釜娑堟�?
		
		Message message; 
		public UpdateRunnable(Message message){
			this.message=message;
		}
		@Override
		public void run() {
			//�洢���°�ƤӦ��������־
			SharedPreferencesControl.getInstance().putString("appdear", url, 
					com.appdear.client.commctrls.Common.UPDATE_APPDEAR_LOG, UpdateAppService.this);
			
			//��Ƥ���°��������
			message.what=DOWNLOAD_COMPLETE;
				if(updateDir==null)return;
				if(!updateDir.exists()){  
					updateDir.mkdirs();  
                } 
				if(!updateFile.exists()){  
					try {
						updateFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				}  
				//long size=downloadFileForUpdate(url,updateFile);
				//鍙︿竴绉嶆柟寮忎笅杞�?
				boolean flag=downloadFileForUpdateOther(url,updateFile);
				//��ɸ���
				AppContext.getInstance().isUpdateStarted = false;
				if(flag==true){
					if(updateHandler.hasMessages(message.what)==false)
					updateHandler.sendMessage(message);
				}
			 
		}
		public boolean downloadFileForUpdateOther(String url,File updatefile){
			URL urlpath=null;
			InputStream is = null;
			FileOutputStream fos =null;
			long totalsize=0;
			//int currentSize = 0;   
			int updateTotalSize = 0; 
			int countUpdate=0;
			boolean flag=false;
			
		  try {
				urlpath = new URL(url);
				HttpURLConnection httpconnection =(HttpURLConnection)urlpath.openConnection();
				if (Constants.VERSION != null && !Constants.VERSION.equals(""))
					httpconnection.addRequestProperty("Version", Constants.VERSION);
				if (Constants.USER_AGENT != null && !Constants.USER_AGENT.equals(""))
					httpconnection.addRequestProperty("User-Agent", Constants.USER_AGENT);
				if (AppContext.getInstance().IMEI != null && !AppContext.getInstance().IMEI.equals(""))
					httpconnection.addRequestProperty("Imei", AppContext.getInstance().IMEI);
				if (AppContext.getInstance().MAC != null && !AppContext.getInstance().MAC.equals(""))
					httpconnection.setRequestProperty("Mac", AppContext.getInstance().MAC);
				if (Constants.PLATFORM != null && !Constants.PLATFORM.equals(""))
					httpconnection.addRequestProperty("Platform", Constants.PLATFORM);
				if (Constants.OPERATION != null && !Constants.OPERATION.equals(""))
					httpconnection.addRequestProperty("Operation", Constants.OPERATION);
				if (Constants.DEVICEID != null && !Constants.DEVICEID.equals(""))
					httpconnection.addRequestProperty("Deviceid", Constants.DEVICEID);
				if (Constants.AUTHID != null && !Constants.AUTHID.equals(""))
					httpconnection.addRequestProperty("Authid", Constants.AUTHID);
				if (!Constants.CANNEL_CODE.equals("") 
						&& Constants.CANNEL_CODE != null)
					httpconnection.addRequestProperty("Channelcode", Constants.CANNEL_CODE);
				httpconnection.addRequestProperty("Timestamp",System.currentTimeMillis()+"");
				httpconnection.addRequestProperty("Network",AppContext.getInstance().network+"");
			   
				httpconnection.setConnectTimeout(10000);
			    httpconnection.setReadTimeout(10000);
			    if(httpconnection.getResponseCode()==HttpURLConnection.HTTP_OK){
				int count=0;
			    is = httpconnection.getInputStream();
			    totalsize=httpconnection.getContentLength();
			    fos = new FileOutputStream(updatefile);
			    byte[] buffer = new byte[1024];
			    while ((count = is.read(buffer)) != -1)
			    {
			     fos.write(buffer, 0, count);
			     updateTotalSize+=count;
			     if(countUpdate==0||(int)(updateTotalSize*100/totalsize)-1>countUpdate){
			    	 countUpdate+=1;
			    	 updateNotification.contentView.setProgressBar(R.id.update_progressbar,100,(int)(updateTotalSize*100/totalsize), false);
			 		 updateNotification.contentView.setTextViewText(R.id.update_progresstext,(int)(updateTotalSize*100/totalsize)+"%");
			    	
			    	 updateNotificationManager.notify(notification_id,updateNotification);
			     }
			    }
			    if(updateTotalSize==totalsize){
			    	 flag=true;
			    }else{
			    	 message.what=DOWNLOAD_FAIL;
					  updateHandler.sendMessage(message);
					  flag=false;
			    }
			   
			  }else{
				  message.what=DOWNLOAD_FAIL;
				  updateHandler.sendMessage(message);
				  flag=false;
			  }
		  	 }
		  	  catch (Exception e)
			  {
				 // e.printStackTrace();
				  message.what=DOWNLOAD_FAIL;
				  AppContext.getInstance().isUpdateStarted = false;
				  updateHandler.sendMessage(message);
				  flag=false;
			  }finally{
				 
				  if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				  }
				  if(is!=null){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				  }
				  if(flag==false){
					  updatefile.delete();
				  }else{
					  BufferedWriter bw=null;
					  try {
						  bw= new BufferedWriter(new FileWriter(new File(updateDir.getPath(),"appstore_"+(Constants.VERSIONCODE+1))));
						  bw.write(String.valueOf(totalsize));
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}finally{
						if(bw!=null){
							try {
								bw.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				  }
				  AppContext.getInstance().isUpdateStarted = false;
			  }
			  return flag;
		}
		public long downloadFileForUpdate(String url,File updatefile){
			HttpGet httpGet = new HttpGet(url);
			long totalsize=0;
			//int currentSize = 0;   
			int updateTotalSize = 0; 
			int countUpdate=0;
			 InputStream is = null;
			FileOutputStream fos =null;
		  try
		  {
		   HttpResponse httpResponse = new DefaultHttpClient()
		     .execute(httpGet);
		   if (httpResponse.getStatusLine().getStatusCode() == 200)
		   {
			int count=0;
		    is = httpResponse.getEntity().getContent();
		    totalsize=httpResponse.getEntity().getContentLength();
		    fos = new FileOutputStream(updatefile);
		    byte[] buffer = new byte[8192];
		    while ((count = is.read(buffer)) != -1)
		    {
		     fos.write(buffer, 0, count);
		     updateTotalSize+=count;
		     if(countUpdate==0||(int)(updateTotalSize*100/totalsize)-2>countUpdate){
		    	 countUpdate+=2;
		    	 updateNotification.contentView.setProgressBar(R.id.update_progressbar,100,(int)(updateTotalSize*100/totalsize), false);
		 		 updateNotification.contentView.setTextViewText(R.id.update_progresstext,(int)(updateTotalSize*100/totalsize)+"%");
		    	 updateNotificationManager.notify(notification_id,updateNotification);
		     }
		    }
		    updateNotification.contentView.setProgressBar(R.id.update_progressbar,100,100, false);
	 		updateNotification.contentView.setTextViewText(R.id.update_progresstext,100+"%");
	    	updateNotificationManager.notify(notification_id,updateNotification);
		   }else{
			   message.what=DOWNLOAD_FAIL;
			   updateHandler.sendMessage(message);
		   }
		  }
		  catch (Exception e)
		  {
			  e.printStackTrace();
			  message.what=DOWNLOAD_FAIL;
			  updateHandler.sendMessage(message);
		  }finally{
			  if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
			  if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
		  }
		  return updateTotalSize;
		}
	}
}
