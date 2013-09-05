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
public class ApiTousuRquest {
	/**
	 * 
	 * @param uid
	 * @param sessionid
	 * @param mobile
	 * @param type
	 * @param msg
	 * @return
	 * @throws ServerException 
	 * @throws ApiException 
	 * @throws JSONException 
	 */
	public static ApiTousuResult publishMsg(String uid,String sessionid,String mobile,String type,String msg) throws ApiException, ServerException, JSONException{
		ApiTousuResult  result =  null;
		StringHashMap params = new StringHashMap();
		params.put("uid",uid);
		params.put("sessionid",sessionid);
		params.put("mobile",mobile);
		params.put("type",type);
		params.put("text",msg);
		String rerurnStr = APIHelper.postURL(ApiUrl.complain, params, 0);
			 
		JSONObject obj = new JSONObject(rerurnStr);
		result = new ApiTousuResult();
		result.sv = obj.getString("sv");
		result.imei= obj.getString("imei");
		result.isok= obj.getString("isok");
		result.resultcode= obj.getJSONObject("result").getString("resultcode");
			 
		return result;
	}
}
