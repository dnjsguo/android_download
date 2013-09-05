package com.appdear.client.service.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum.ApiExceptionCode;
import com.appdear.client.exception.ExceptionEnum.ServerExceptionCode;
import com.appdear.client.exception.ServerException;
import com.appdear.client.utility.StringHashMap;

public class ApiCustomappRequest {
	/**
	 * 
	 * @param userid
	 * @param sessionid
	 * @param text
	 * @return
	 * @throws ServerException
	 * @throws ApiException
	 */
	public static ApiCustomAppResult addCustomapp(String apptype,String userid,String sessionid,String text) throws ServerException, 
		ApiException{
	
		ApiCustomAppResult  result =  null;
		StringHashMap params = new StringHashMap();
		params.put("userid",userid);
		params.put("sessionid",sessionid);
		params.put("text",text);
		params.put("apptype",apptype);

		String responseStr = APIHelper.postURL(ApiUrl.customappaction,params, 1);
		if("".equals(responseStr)||responseStr==null){
			throw new ServerException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"服务器返回为空！");
		}else{
			try {
				result = new ApiCustomAppResult();
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
		}
		return result;
	}
}
