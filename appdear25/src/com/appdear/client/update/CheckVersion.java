package com.appdear.client.update;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.appdear.client.UpdateAppService;
import com.appdear.client.R;
import com.appdear.client.service.Constants;

/**
 *  �汾���
 * @author jdan
 *
 */
public class CheckVersion{
	private static CheckVersion checkversion=null;
	private CheckVersion(){
		
	}
	public static  CheckVersion getInstance(){
		if(checkversion==null){
			checkversion=new CheckVersion();
		}
		return checkversion;
	}
	 /**  
  		* �?测版本是否更�? 
  	 */ 
	public void checkVersion(final Context context,String url,boolean flag){  
		if (Constants.DEBUG)Log.i("update", "client need to update action checkVersion");
		 if(flag){  
			 //如果有新的版本，提示用户更新  
			 
			 Intent updateIntent =new Intent(context,UpdateAppService.class);
			 	Bundle bundle=new Bundle();
			 	bundle.putInt("titleId", R.string.app_name);
			 	//或从服务器取得下载地�?
			 	//updateIntent.putExtra("url",Global.downloadserverurl);
			 	bundle.putString("appurl",url);
			 	
			 	updateIntent.putExtra("bundle",bundle);
			 	context.startService(updateIntent);  
		 	}else{  
		 		//清理缓存区app 
		 		File updateDir = new File(Environment.getExternalStorageDirectory(),Global.downloadDir);  
				File updateFile = new File(updateDir.getPath(),context.getResources().getString(R.string.app_name)+".apk");  
				if(updateFile.exists()){
					updateFile.delete();
				}
		 	}  
	 } 
	
}
