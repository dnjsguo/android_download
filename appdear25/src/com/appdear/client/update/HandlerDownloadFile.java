package com.appdear.client.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
/**
 * 文件下载处理
 * @author jindan
 *
 */
public abstract class HandlerDownloadFile implements FileDownloadHandlerI{
	protected static int CONNECTURLTYPE=1;
	protected static int HTTPGETURLTYPE=2;
	
	/**
	 * 
	 * @param url 网络加载地址
	 * @param updatefile 网络更新本机文件
	 * @return
	 */
	protected boolean downloadFile(String url,File updatefile,int updatetype,int timeout,FileDownloadHandlerI handler){
		HttpGet httpGet = null;
		URL urlpath=null;
		//文件下载成功状态
		boolean endflag=false;
		//int currentSize = 0;   
		 InputStream is = null;
		FileOutputStream fos =null;
	    try
	    {
		    int count=0;
		    long totalsize=0;
		    if(updatetype==CONNECTURLTYPE){
		    	urlpath = new URL(url);
				HttpURLConnection httpconnection =(HttpURLConnection)urlpath.openConnection();
				if (Constants.VERSION != null && !Constants.VERSION.equals(""))
					httpconnection.addRequestProperty("Version", Constants.VERSION);
				if (Constants.USER_AGENT != null && !Constants.USER_AGENT.equals(""))
					httpconnection.addRequestProperty("User-Agent", Constants.USER_AGENT);
				if (AppContext.getInstance().IMEI != null && !AppContext.getInstance().IMEI.equals(""))
					httpconnection.addRequestProperty("Imei", AppContext.getInstance().IMEI);
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
				
			    httpconnection.setConnectTimeout(timeout<0?10000:timeout);
			    if(httpconnection.getResponseCode()==HttpURLConnection.HTTP_OK){
			    	is = httpconnection.getInputStream();
			    	totalsize=httpconnection.getContentLength();
			    }
		    }else if(updatetype==HTTPGETURLTYPE){
		    	httpGet=new HttpGet(url);
		    	HttpResponse httpResponse = new DefaultHttpClient()
			     .execute(httpGet);
		    	if (httpResponse.getStatusLine().getStatusCode() == 200){
				    is = httpResponse.getEntity().getContent();
				    totalsize=httpResponse.getEntity().getContentLength();
		    	}
		    }else{
		    	return endflag;
		    }
		    if(is!=null){
			    fos = new FileOutputStream(updatefile);
			    byte[] buffer = new byte[8192];
			    int updateTotalSize=0;
			    while ((count = is.read(buffer)) != -1)
			    {
			      fos.write(buffer, 0, count);
			      updateTotalSize+=count;
			      if(handler!=null)handler.HandlerNocatifycation(updateTotalSize,totalsize);
			    }
			    endflag=true;
		    }else{
			   endflag=false;
			   if(handler!=null) handler.HandlerNocatifycationFail();
		    }
	   }
	  catch (Exception e)
	  {
		  e.printStackTrace();
		  endflag=false;
		  if(handler!=null) handler.HandlerNocatifycationFail();
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
	  return endflag;
	}
}
