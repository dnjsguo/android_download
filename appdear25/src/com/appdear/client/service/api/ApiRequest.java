package com.appdear.client.service.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.ApiCommentResult;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.StringHashMap;

/**
 * 接口协议请求
 * @author zqm
 *
 */
public class ApiRequest<T> {
	private T obj;
	public ApiRequest(T obj){
		this.obj=obj;
	}
	
	public <T> ApiResult<T> sendParamsPost(String url, Map<String, String> params, int urlType) throws ApiException, JSONException{
		if (!AppContext.getInstance().isNetState)
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_NETWORK_NOAVALIBLE.getValue(), "网络不可用");
		String xmlText = null;
		StringBuffer xmlsbf = new StringBuffer();
		xmlsbf.append(url);
		try {
			xmlText = APIHelper.postURL(url,params, urlType);
		} catch (ServerException e) {
			throw new ApiException(e.getCode(),
					e.getMessage());
		}
		return (ApiResult<T>) paserXML(xmlText, url);
	}
	
	public <T> ApiResult<T> sendParamsGet(String url, StringHashMap params, int urlType) throws ApiException, JSONException{
		String xmlText = null;
		StringBuffer xmlsbf = new StringBuffer();
		xmlsbf.append(url);
		if (params  != null) {
			int num = 0;
			for (Iterator<String> i = params.keySet().iterator(); i.hasNext();) {
			   String key = i.next();
			   String value = params.get(key);
			   if (num == 0)
				   xmlsbf.append("?");
			   else
				   xmlsbf.append("&");
			   xmlsbf.append(key);
			   xmlsbf.append("=");
			   xmlsbf.append(value);
			   num ++;
			}
		}
		try {
			xmlText = APIHelper.getURL(xmlsbf.toString(), urlType);
		} catch (ServerException e) {
			throw new ApiException(e.getCode(),
					e.getMessage());
		} catch (OutOfMemoryError e) {
		}
		return (ApiResult<T>) paserXML(xmlText, url);
	}
	
	public ApiResult<T> paserXML(String xmlText, String url) throws JSONException, ApiException {
		ApiResult<T> result = null;
		if (obj instanceof ApiSoftListResult) {
			result = ApiParseResult.parseSoftList(xmlText, url);
		} else if (obj instanceof ApiUserResult) {
			result = ApiParseResult.parseRegisterLogin(xmlText, url);
		} else if (obj instanceof ApiNormolResult) {
			result = ApiParseResult.parseNormol(xmlText, url);
		}else if (obj instanceof ApiCatalogListResult) {
			result = ApiParseResult.parseCatalogList(xmlText, url);
		}else if (url.equals(ApiUrl.softpermission)){
			if (Constants.DEBUG)
				Log.i("parse permission", "parse permission json data");
			result = ApiParseResult.parsePermissionList(xmlText, url);
		} else if (obj instanceof ApiOrderInfoResult) {
			//result = ApiParseResult.parserOrderInfo(xmlText, url);
		} else if (obj instanceof ApiCommentResult) {
			result = ApiParseResult.parseMyCommentlist(xmlText, url);
		} else if (obj instanceof ApiPreRegisterResult) {
			result = ApiParseResult.parsePreRegister(xmlText, url);
		} else if(obj instanceof ApiInstalledRecoverSoftListResult)
		{
			result = ApiParseResult.parseInstalledRecoverSoftList(xmlText, url);
		}
		return result;
	}
}
