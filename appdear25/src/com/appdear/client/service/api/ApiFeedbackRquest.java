package com.appdear.client.service.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.StringHashMap;

/**
 * 
 * @author zxy
 *
 */
public class ApiFeedbackRquest {
	/**
	 * 
	 * @param uid
	 * @param sessionid
	 * @param type
	 * @param msg
	 * @return
	 * @throws ServerException 
	 * @throws ApiException 
	 * @throws JSONException 
	 */
	public static ApifeedBackResult publishMsg(String uid,String sessionid,String type,String msg) throws ApiException, ServerException, JSONException{
		ApifeedBackResult  result =  null;
		StringHashMap params = new StringHashMap();
		params.put("userid",uid);
		params.put("sessionid",sessionid);
		params.put("type",type);
		params.put("text",msg);
		
		String rerurnStr = APIHelper.postURL(ApiUrl.feedback, params, 0);
		JSONObject obj = new JSONObject(rerurnStr);
		result = new ApifeedBackResult();
		result.sv = obj.getString("sv");
		result.imei= obj.getString("imei");
		result.isok= obj.getString("isok");
		result.resultcode= obj.getJSONObject("result").getString("resultcode");
		return result;
	}
}
