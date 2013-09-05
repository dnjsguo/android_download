/**
 * ApiHelper.java
 * created at:2011-5-10下午01:53:08
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.service.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.os.Build;
import android.util.Log;

import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.BaseReqeust;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;

/** 
 * 网络连接
 * 
 * @author zqm
 */
public class APIHelper {

	public static String postURL(String url, Map<String,String> paramsMaps, int urlType)throws ApiException, ServerException {
//		if (!AppContext.getInstance().isNetState)
//			throw new ApiException(ApiException.API_OTHER_ERR, "请重新设置网络！");
	//	AndroidHttpClient client = AndroidHttpClient.newInstance("POST_URL");
		if (!url.startsWith("http")) {
			if (urlType == 0){
				url = AppContext.getInstance().spreurl + url;
			} else if (urlType == 1) {
				url = AppContext.getInstance().dpreurl + url;
			} else { 
				url = AppContext.getInstance().api_url + url;
			}
		}
		if (!url.startsWith("http")) {
			throw new ApiException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "Unkonwn Host Exception。");
		}
		HttpPost httpRequest = new HttpPost(url);
		if (Constants.DEBUG)
			Log.i("tag","get url :"+url);
		List<BasicNameValuePair> list = new ArrayList();
		if (Constants.DEBUG)
			System.out.println("url="+url);
		for(Map.Entry<String,String> param:paramsMaps.entrySet()){
			if (Constants.DEBUG)
				System.out.println(param.getKey()+"="+param.getValue());
			list.add(new BasicNameValuePair(param.getKey(),param.getValue()));
		}
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			httpRequest.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,  Constants.CONNECTION_TIME_OUT);  
			httpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  Constants.CONNECTION_TIME_OUT);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		if (Constants.VERSION != null && !Constants.VERSION.equals(""))
			httpRequest.setHeader("Version", Constants.VERSION);
		if (Constants.USER_AGENT != null && !Constants.USER_AGENT.equals(""))
			httpRequest.setHeader("User-Agent", Constants.USER_AGENT);
		if (AppContext.getInstance().IMEI != null && !AppContext.getInstance().IMEI.equals(""))
			httpRequest.setHeader("Imei", AppContext.getInstance().IMEI);
		if (AppContext.getInstance().MAC != null && !AppContext.getInstance().MAC.equals(""))
			httpRequest.setHeader("Mac", AppContext.getInstance().MAC);
		if (Constants.PLATFORM != null && !Constants.PLATFORM.equals(""))
			httpRequest.setHeader("Platform", Constants.PLATFORM);
		if (Constants.OPERATION != null && !Constants.OPERATION.equals(""))
			httpRequest.setHeader("Operation", Constants.OPERATION);
		if (Constants.DEVICEID != null && !Constants.DEVICEID.equals(""))
			httpRequest.setHeader("Deviceid", Constants.DEVICEID+"<>" +Build.BRAND);
		if (Constants.AUTHID != null && !Constants.AUTHID.equals(""))
			httpRequest.setHeader("Authid", Constants.AUTHID);
		if (!Constants.CANNEL_CODE.equals("") 
				&& Constants.CANNEL_CODE != null)
			httpRequest.setHeader("Channelcode", Constants.CANNEL_CODE);
		httpRequest.setHeader("Timestamp",System.currentTimeMillis()+"");
		httpRequest.setHeader("Network",AppContext.getInstance().network+"");
		httpRequest.setHeader("Nettype", AppContext.getInstance().nettype+"");
		httpRequest.setHeader("Opname", AppContext.getInstance().simname+"");
		DefaultHttpClient client=new DefaultHttpClient();
		try {
			HttpResponse httpResponse =client.execute(httpRequest);
			StatusLine status = httpResponse.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_INVALID_RESPONSE.getValue(),
						"无效的响应，响应码: " + status.toString());
			}
			
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			httpResponse.getEntity().writeTo(content);
			
			String ret = new String(content.toByteArray()).trim();
			if (Constants.DEBUG) {
				Log.d(APIHelper.class.getName(), "request to [" + url
						+ "] result\r\n" + ret);
			}
			if(ret.indexOf(String.valueOf(ExceptionEnum.ApiExceptionCode.API_TOKEN_FAILURE.getValue()))>=0){
				SharedPreferencesControl.getInstance().clear(Common.USERLOGIN_XML,MyApplication.getInstance().getApplicationContext());
			}
			content.close();
			//client.getConnectionManager().shutdown();
			return (ret);
		}catch(org.apache.http.conn.ConnectTimeoutException e){
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "网络连接超时。", e);
		}catch(java.net.SocketTimeoutException e){
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e); 
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "网络连接错误。", e);
		} catch (UnsupportedEncodingException e) {
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "不支持的编码格式。", e);
		} catch (ClientProtocolException e) {
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "客户端协议异常。", e);
		} catch (IOException e) {
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "联网错误,请检查网络状态。", e);
		}
	} 
	
	public static String getURL(String url, int urlType) throws ServerException{
		if (urlType == 0)
			url = AppContext.getInstance().spreurl + url;
		else if (urlType == 1)
			url = AppContext.getInstance().dpreurl + url;
		else 
			url = AppContext.getInstance().api_url + url;
		
		if (!url.startsWith("http")) {
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "Unkonwn Host Exception。");
		}
		//AndroidHttpClient client = AndroidHttpClient.newInstance("GET_URL");
		if (Constants.DEBUG)
			Log.i(APIHelper.class.getName(),"get url :"+url);
		HttpClient client = new DefaultHttpClient();
		if (Constants.DEBUG)
			Log.i("tag","post url :"+url);
		HttpGet request = new HttpGet(url);
		
		//设置连接超时时间
		request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,  Constants.CONNECTION_TIME_OUT);  
		request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  Constants.CONNECTION_TIME_OUT);
//		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params, Constants.CONNECTION_TIME_OUT);
//		//设置读取数据超时时间
//		HttpConnectionParams.setSoTimeout(params, Constants.CONNECTION_TIME_OUT);
//		request.setParams(params);
		if (Constants.VERSION != null && !Constants.VERSION.equals(""))
			request.setHeader("Version", Constants.VERSION);
		if (Constants.USER_AGENT != null && !Constants.USER_AGENT.equals(""))
			request.setHeader("User-Agent", Constants.USER_AGENT);
		if (AppContext.getInstance().IMEI != null && !AppContext.getInstance().IMEI.equals(""))
			request.setHeader("Imei", AppContext.getInstance().IMEI);
		if (AppContext.getInstance().MAC != null && !AppContext.getInstance().MAC.equals(""))
			request.setHeader("Mac", AppContext.getInstance().MAC);
		if (Constants.PLATFORM != null && !Constants.PLATFORM.equals(""))
			request.setHeader("Platform", Constants.PLATFORM);
		if (Constants.OPERATION != null && !Constants.OPERATION.equals(""))
			request.setHeader("Operation", Constants.OPERATION);
		if (Constants.DEVICEID != null && !Constants.DEVICEID.equals(""))
			request.setHeader("Deviceid", Constants.DEVICEID+"<>" +Build.BRAND);
		if (Constants.AUTHID != null && !Constants.AUTHID.equals(""))
			request.setHeader("Authid", Constants.AUTHID);
		
		if (!Constants.CANNEL_CODE.equals("") 
				&& Constants.CANNEL_CODE != null)
			request.setHeader("Channelcode", Constants.CANNEL_CODE);
		request.setHeader("Timestamp",System.currentTimeMillis()+"");
		request.setHeader("Network",AppContext.getInstance().network+"");
		request.setHeader("Nettype", AppContext.getInstance().nettype+"");
		request.setHeader("Opname", AppContext.getInstance().simname+"");
		try {
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			if (Constants.DEBUG)
				Log.d("response code", status.getStatusCode() + "");
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_INVALID_RESPONSE.getValue(),
						"无效的响应，响应码: " + status.toString());
			}
			
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			response.getEntity().writeTo(content);
			
			String ret = new String(content.toByteArray()).trim();
			if(ret.indexOf(String.valueOf(ExceptionEnum.ApiExceptionCode.API_TOKEN_FAILURE.getValue()))>=0){
				SharedPreferencesControl.getInstance().clear(Common.USERLOGIN_XML,MyApplication.getInstance().getApplicationContext());
			}
			if (Constants.DEBUG) {
				Log.d(APIHelper.class.getName(), "request to [" + url
						+ "] result\r\n" + ret);
			}
			content.close(); 
			//client.close();
			//client.getConnectionManager().shutdown();
			return (ret);
		}catch(org.apache.http.conn.HttpHostConnectException ee){
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "网络连接错误。", ee);
		} catch(org.apache.http.conn.ConnectTimeoutException e){
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e); 
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "网络连接超时。", e);
		}catch(java.net.SocketTimeoutException e){
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e); 
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "网络连接错误。", e);
		}catch (UnsupportedEncodingException e) {
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "不支持的编码格式。", e); 
		} catch (ClientProtocolException e){
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "客户端协议异常。", e);
		} catch (IOException e) {
			Log.d(BaseReqeust.class.getName(), "request to [" + url
					+ "] error ", e);
			throw new ServerException(ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue(), "联网错误，请检查网络状态。", e);
		}
		finally{
			//client.close();
		}
	}
}

 