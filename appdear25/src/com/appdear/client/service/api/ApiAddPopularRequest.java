package com.appdear.client.service.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum.ApiExceptionCode;
import com.appdear.client.exception.ExceptionEnum.ServerExceptionCode;
import com.appdear.client.exception.ServerException;
import com.appdear.client.utility.StringHashMap;

public class ApiAddPopularRequest {

	/**
	 * userid	用户id
	 * popcount	 被添加人气的用户ID
	 * 
	 * @return
	 * @throws ServerException 
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiAddpopularResult addpopular(String userid,String  tuserid) throws ServerException, ApiException{
		ApiAddpopularResult  result =  null;
		StringHashMap params = new StringHashMap();
		params.put("userid",userid);
		params.put("tuserid",tuserid);
		
		Log.i("userid ", "userid"+userid);
		Log.i("tuserid ", "tuserid"+tuserid);
		
		String responseStr = APIHelper.postURL(ApiUrl.addpopularaction,params, 1);
		Log.i("responseStr ", "responseStr :"+responseStr);
		
		if("".equals(responseStr)||responseStr==null){
			throw new ServerException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"服务器返回为空！");
		}else{
			try {
				result = new ApiAddpopularResult();
				JSONObject retJson;
				
				retJson = new JSONObject(responseStr);
				
				if(retJson.has("result")&&retJson.getJSONObject("result").has("resultcode")){
					result.resultcode = retJson.getJSONObject("result").getString("resultcode");
				}else{
					throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
				
				if(retJson.has("imei")){
					retJson.getString("imei");
					result.imei = retJson.getString("imei");
				}else{
					throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
				
				if(retJson.has("sv")){
					retJson.getString("sv");
					result.sv =  retJson.getString("sv");
				}else{
					throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
				
				if(retJson.has("isok")){
					retJson.getString("isok");
					result.isok =  retJson.getInt("isok");
				}else{
					throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
		}catch(ApiException e){
			throw new ApiException(ApiExceptionCode.API_OTHER_ERR.getValue(),"解析异常！");
		} catch (JSONException e) {
			throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
		}
		return result;
	}
	}
	
		
}
