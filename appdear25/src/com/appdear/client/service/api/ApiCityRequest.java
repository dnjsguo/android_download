package com.appdear.client.service.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum.ApiExceptionCode;
import com.appdear.client.exception.ExceptionEnum.ServerExceptionCode;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.Constants;
/**
 * 返回类似数据 {"citys":["北京","上海"],"result":{"resultcode":"000000"},"imei":"SDF434TTTTTTTTT","sv":"1.0"}
 * @author zxy
 *
 */
public class ApiCityRequest {
//	{
//	    "citys": [
//	        "北京",
//	        "上海"
//	    ],
//	    "result": {
//	        "resultcode": "000000"
//	    },
//	    "imei": "SDF434TTTTTTTTT",
//	    "sv": "1.0"
//	}
	/**
	 * 
	 * @param province
	 * @return
	 * @throws ServerException
	 * @throws ApiException
	 */
	public static ApiCitylistRequest citylistRequest(String province) throws ServerException, ApiException{
		ApiCitylistRequest  result =  null;
		//String responseStr = APIHelper.getURL(ApiUrl.citylist+"?province="+province,1);
		String responseStr = APIHelper.getURL(ApiUrl.citylist+"?province="+province,1);
		if(Constants.DEBUG)Log.i("responseStr", "responseStr,"+responseStr);
		if("".equals(responseStr)||responseStr==null){
			throw new ServerException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"服务器返回为空！");
		}else{
			try {
				result = new ApiCitylistRequest();
				JSONObject retJson;
				
				retJson = new JSONObject(responseStr);
				
				if(retJson.has("result")&&retJson.getJSONObject("result").has("resultcode")){
					result.resultcode = retJson.getJSONObject("result").getString("resultcode");
				}else{
					//throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
				
				if(retJson.has("imei")){
					result.resultcode = retJson.getString("imei");
				}else{
					//throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}

				if(retJson.has("sv")){
					result.resultcode = retJson.getString("sv");
				}else{
					//throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
				
				if(retJson.has("citys")){
					JSONArray cityArray = retJson.getJSONArray("citys");
					String retCitys = "";
					for(int i = 0;i<cityArray.length();i++){
						retCitys+= cityArray.getString(i)+",";
					}
					result.citys = retCitys;
				}else{
					//throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
				}
				
				
		}catch (JSONException e) {
			throw new ApiException(ServerExceptionCode.SERVER_OTHER_ERR.getValue(),"解析异常！");
		} 
		return result;
	}
	}
}
