package com.appdear.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.api.APIHelper;
import com.appdear.client.utility.StringHashMap;

public class Test extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		post();
	}
	
	private void post(){
		StringHashMap map = new  StringHashMap();
		map.put("bid", "x1");
		map.put("versioncode", "x2");
		map.put("sectionversion", "x3");
	    //http://192.168.100.103:8090/ap/android/initinfo.jsp?bid=100003&versioncode=2&sectionversion=1000
		try {
			APIHelper.postURL("htt://192.168.100.103:8090/ap/android/initinfo.jsp", map, 0);
		}catch (ApiException e) {
			
			Toast.makeText(this, "ApiException "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ServerException e) {
			
			Toast.makeText(this, "ServerException :"+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
}
