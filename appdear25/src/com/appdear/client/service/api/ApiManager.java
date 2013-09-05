package com.appdear.client.service.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.ApiCommentResult;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.PermissionListInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.Base64;
import com.appdear.client.utility.ContactUtil;
import com.appdear.client.utility.JsonUtil;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.SmsUtil;
import com.appdear.client.utility.StringHashMap;
import com.appdear.client.utility.factory.RequestResultFactory;

/**
 * 接口管理类
 * @author zqm
 *
 */
public class ApiManager {

	/**
	 * 3.5.1.	初始化服务接口（initinfo）
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult initinfo(String sectionversion) throws ApiException,ServerException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("bid", Constants.BID);
		params.put("versioncode", Constants.VERSIONCODE + "");
		Log.i("update", "update old version :"+Constants.VERSIONCODE);
		params.put("sectionversion", sectionversion);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.initinfo, params, -1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	/**
	 * 3.5.1.	初始化服务接口（initinfo）
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult updateclient(String sectionversion) throws ApiException,ServerException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("bid", Constants.BID);
		params.put("versioncode", Constants.VERSIONCODE + "");
		Log.i("update", "update old version :"+Constants.VERSIONCODE);
		params.put("sectionversion", sectionversion);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.updateclient, params, -1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	/**
	 * 3.5.2.	主页banner广告列表接口(bannerlist)
	 * @param cid
	 * @param order
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult bannerlist(String cid, String order, String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.bannerlist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.3.	二级频道软件内容列表接口(softlist)
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult softlist(String cid, String pageno, String count,String... strs) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		if(strs!=null&&strs.length>0){
			params.put("dynamic",strs[0]);
		}
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.softlist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.3.	二级频道软件内容列表接口(softlist)
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult dynamicsoftlist(String cid, String pageno, String count,String... strs) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		if(strs!=null&&strs.length>0){
			params.put("dynamic",strs[0]);
			if(strs.length>1){
				params.put("modelarea",MyApplication.getInstance().modelCompany);
			//	Log.i("info0000",strs[1]+"=");
			}
			if(strs.length>2){
				if(strs[2]!=null&&strs[2].length()>0&&!strs[2].equals("null"))
				params.put("sids",strs[2]);
			}
		}
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.dynamicsoftlist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	/**
	 * 3.5.3.	二级频道软件内容列表接口(softlist)
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult dynamicsoftlist2(String cid, String pageno, String count,String... strs) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		if(strs!=null&&strs.length>0){
			params.put("dynamic",strs[0]);
			if(strs.length>1){
				if(strs[1]!=null&&strs[1].length()>0&&!strs[1].equals("null"))
				params.put("sids",strs[1]);
			//	Log.i("info0000",strs[1]+"=");
			}
		}
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.dynamicsoftlist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	/**
	 * 3.5.4.	类别列表接口(cataloglist)
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiCatalogListResult cataloglist(String cid, String pageno, String count) throws ApiException {
		ApiCatalogListResult result = new ApiCatalogListResult();
		StringHashMap params = new StringHashMap();
		params.put("contenttype", "1");
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiCatalogListResult) sendparamsGet(result, ApiUrl.cataloglist, params, 1);
		} catch (org.json.JSONException e){
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), "解析异常："+e.getMessage());
		}
	}
	
	/**
	 * 3.5.5.	类别内容列表接口(catalogsoftlist)
	 * @param catalogid
	 * @param fee
	 * @param order
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult catalogsoftlist(String catalogid, String fee, String order, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("catalogid", catalogid);
		params.put("fee", fee);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.catalogsoftlist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.6.	榜单软件内容列表接口（boradsoftlist）
	 * @param catalogid
	 * @param fee
	 * @param order
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult boradsoftlist(String catalogid, String fee,  
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", catalogid);
		params.put("boardstageid", fee);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.boradsoftlist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.7.	搜索推荐关键字列表接口（keywordlist）
	 * @param cid
	 * @param order
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult keywordlist(String cid, String order, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.keywordlist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.8.	Autocomplete列表接口（autolist）
	 * @param keyword
	 * @param type
	 * @param order
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult autolist(String keyword, String type, String order,
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		try {
			params.put("keyword", URLEncoder.encode(keyword, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		params.put("type", type);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return(ApiSoftListResult) sendparamsGet(result, ApiUrl.autolist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.9.	搜索结果接口（resultsoftlist）
	 * @param keyword
	 * @param ordertype
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult resultsoftlist(String keyword, String ordertype, 
			String pageno, String count,String searchway) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		try {
			params.put("keyword", URLEncoder.encode(keyword, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if(searchway!=null&&!searchway.equals("")){
			params.put("searchway",searchway);
		}
		params.put("ordertype", ordertype);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.resultsoftlist, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}

	/**
	 * 3.5.10.	软件详情接口（softinfo）
	 * @param softid
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult resultsoftinfo(String softid, String... ad) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("softid", softid);
		params.put("count", "5");
		params.put("order", "0");
		if(ad!=null){
			if(ad.length>1){
				if (ad[0] != null&&ad[0].equals("ad")) {
					params.put("ad", "1");
				}
				if(ad[1]!=null&&!ad[1].equals("")){
					params.put("source", ad[1]);
				}
				if(ad[2]!=null&&!ad[2].equals("")){
					params.put("subsource",ad[2]);
				}
			}   
		}
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.softinfo, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.11.	软件评论列表(commentlist)
	 * @param softid
	 * @param ordertype
	 * @param contenttype 评论类型 0-全部1-软件2-音乐3-图书
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult commentlist(String softid, String ordertype, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("softid", softid);
		params.put("contenttype", "1");
		params.put("ordertype", ordertype);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.commentlist, params, 0);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.12.	文字评论提交接口（commentcommit）
	 * @param userid
	 * @param token
	 * @param username
	 * @param softid
	 * @param text
	 * @param grade
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult commentcommit(String userid, String token,
			String softid, String text,
			String grade) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("softid", softid);
		params.put("text", text);
		params.put("grade", grade);
		params.put("contenttype", "1");//评论类型-软件
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.commentcommit, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.13.	软件是否收藏列表(isfavoritelist)
	 * @param userid
	 * @param token
	 * @param softid
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult isfavoritelist(String userid, String token, 
			String softid) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("softid", softid);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.isfavoritelist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.14.	提交收藏接口(addfavorite)
	 * @param userid
	 * @param token
	 * @param softid
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult addfavorite(String userid, String token, 
			String softid) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("softid", softid);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.addfavorite, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}

	/**
	 * 3.5.15.	软件作者信息接口（softauthor）
	 * @param softid
	 * @param order
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult softauthor(String softid, String order, 
			String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("order", order);
		params.put("count", count);
		params.put("softid", softid);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.softauthor, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.18.	提交用户手机已安装软件列表(installlist)
	 * @param list
	 * @return
	 * @throws ApiException
	 * @throws JSONException 
	 */
	public static ApiNormolResult installlist(Hashtable<String,PackageinstalledInfo> list, String url)
		throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"count\":");
		json.append(list.size());
		json.append(",\"items\":");
		json.append("[");
		if (list != null && list.size() > 0) {
            for (java.util.Map.Entry<String, PackageinstalledInfo> obj : list.entrySet()) {
            	json.append("{");
            	json.append("\"appid\"");
                json.append(":");
                json.append("\"");
                json.append(obj.getValue().pname);
                json.append("\"");
                json.append(",");
                json.append("\"versioncode\"");
                json.append(":");
                json.append(obj.getValue().versionCode);
                json.append("},");
            }
            json.append("]");
		} else {
            json.append("]");
		}
		json.append("}");
		StringHashMap params = new StringHashMap();
		params.put("installlist", json.toString());
		try {
			if (url.equals(""))
				url = ApiUrl.installlist;
			return (ApiNormolResult) sendparamsPost(result, url, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.19.	用户获取软件可更新列表（updatelist）
	 * @param list
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiSoftListResult updatelist(List<PackageinstalledInfo> list,Context context)
		throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		PackageinstalledInfo soft=null;
		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"count\":");
		json.append(list.size());
		json.append(",\"items\":");
		json.append("[");
		if (list != null && list.size() > 0) {
            for (int i=0;i<list.size();i++) {
            	soft=list.get(i);
            	if(soft==null)continue;
            	json.append("{");
            	json.append("\"appid\"");
                json.append(":");
                json.append("\"");
                json.append(soft.pname);
                json.append("\"");
                json.append(",");
                json.append("\"versioncode\"");
                json.append(":");
                json.append(soft.versionCode);
                json.append("},");
            }
            json.append("]");
		} else {
            json.append("]");
		}
		json.append("}");
		StringHashMap params = new StringHashMap();
		params.put("installlist", json.toString());
		String temp = "";
		try {
			if("".equals(AppContext.getInstance().spreurl)||null==AppContext.getInstance().spreurl){
				temp = SharedPreferencesControl.getInstance().getString(
						"spreurl", com.appdear.client.commctrls.Common.SETTINGS, context);
				if (temp == null || temp.equals("")){
						return null;
				}
				
				return (ApiSoftListResult) sendparamsPost(result, temp + ApiUrl.updatelist, params, 0);
			}
			return (ApiSoftListResult) sendparamsPost(result, ApiUrl.updatelist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	/**
	 * 3.5.19.	用户获取软件可更新列表（updatelist）
	 * @param list
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiSoftListResult updatelist2(List<PackageinstalledInfo> preinstalllist,List<PackageinstalledInfo> installlist,Context context)
		throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		PackageinstalledInfo soft=null;
		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"preinstalllist\":");
		json.append("{");
		json.append("\"count\":");
		json.append(preinstalllist==null?0:preinstalllist.size());
		json.append(",\"items\":");
		json.append("[");
		if (preinstalllist != null && preinstalllist.size() > 0) {
            for (int i=0;i<preinstalllist.size();i++) {
            	soft=preinstalllist.get(i);
            	if(soft==null)continue;
            	json.append("{");
            	json.append("\"appid\"");
                json.append(":");
                json.append("\"");
                json.append(soft.pname);
                json.append("\"");
                json.append(",");
                json.append("\"versioncode\"");
                json.append(":");
                json.append(soft.versionCode);
                json.append("},");
            }
            json.append("]");
		} else {
            json.append("]");
		}
		json.append("}");
		json.append(",");
		
		json.append("\"installlist\":");
		json.append("{");
		json.append("\"count\":");
		json.append(installlist==null?0:installlist.size());
		json.append(",\"items\":");
		json.append("[");
		if (installlist != null && installlist.size() > 0) {
            for (int i=0;i<installlist.size();i++) {
            	soft=installlist.get(i);
            	if(soft==null)continue;
            	json.append("{");
            	json.append("\"appid\"");
                json.append(":");
                json.append("\"");
                json.append(soft.pname);
                json.append("\"");
                json.append(",");
                json.append("\"versioncode\"");
                json.append(":");
                json.append(soft.versionCode);
                json.append("},");
            }
            json.append("]");
		} else {
            json.append("]");
		}
		json.append("}");
		json.append("}");
		StringHashMap params = new StringHashMap();
		//Log.i("info000", "installlist="+json.toString());
		params.put("installlist", json.toString());
		String temp = "";
		try {
			if("".equals(AppContext.getInstance().spreurl)||null==AppContext.getInstance().spreurl){
				temp = SharedPreferencesControl.getInstance().getString(
						"spreurl", com.appdear.client.commctrls.Common.SETTINGS, context);
				if (temp == null || temp.equals("")){
						return null;
				}
				
				return (ApiSoftListResult) sendparamsPost(result, temp + ApiUrl.updatelist2, params, 0);
			}
			return (ApiSoftListResult) sendparamsPost(result, ApiUrl.updatelist2, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	/**
	 * 	3.5.20.	更新单个软件的最新版本(updatesoft)
	 * @param softid
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult updatesoft(String softid) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("softid", softid);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.updatesoft, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.21.	用户注册接口（register）
	 * @param rtype
	 * @param name
	 * @param passwd
	 * @param email
	 * @return
	 * @throws ApiException
	 */
	public static ApiUserResult register(String rtype, String name,
			String passwd, String email) throws ApiException {
		ApiUserResult result = new ApiUserResult();
		StringHashMap params = new StringHashMap();
		params.put("rtype", rtype);
		params.put("name", name);
		params.put("passwd", passwd);
		params.put("email", email);
		try {
			return (ApiUserResult) sendparamsPost(result, ApiUrl.register, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.22.	效验用户名是否可以使用接口（checkname）
	 * @param name
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult checkname(String name) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("name", name);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.checkname, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.23.	用户个人登录接口（userlogin）
	 * @param name
	 * @param passwd
	 * @return
	 * @throws ApiException
	 */
	public static ApiUserResult userlogin(String name, String passwd) throws ApiException {
		ApiUserResult result = new ApiUserResult();
		StringHashMap params = new StringHashMap();
		params.put("name", name);
		params.put("passwd", passwd);
		try {
			return (ApiUserResult) sendparamsPost(result, ApiUrl.userlogin, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.24.	用户个人信息接口（userprofile)
	 * @param userid
	 * @param token
	 * @return
	 * @throws ApiException
	 */
	public static ApiUserResult userprofile(String userid, String token) throws ApiException {
		ApiUserResult result = new ApiUserResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		try {
			return (ApiUserResult) sendparamsPost(result, ApiUrl.userprofile, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.25.	更新用户个人信息（updateprofile）
	 * @param token
	 * @param user
	 * @param userid
	 * @param nickname
	 * @param area
	 * @param gender
	 * @param profession
	 * @param mobile
	 * @param qq
	 * @param desc
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult updateprofile(String token, 
			String userid, String nickname, 
			String area, String gender, String profession,
			String mobile, String qq, String desc, String email) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("sessionid", token);
		params.put("userid", userid);
		params.put("nickname", nickname);
		params.put("area", area);
		params.put("gender", gender);
		params.put("profession", profession);
		params.put("mobile", mobile);
		params.put("qq", qq);
		params.put("desc", desc);
		params.put("email", email);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.updateprofile, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.26.	找回登录密码(findpasswd)
	 * @param mobile
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult findpass(String mobile) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("mobile", mobile);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.findpasswd, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.27.	更新个人密码(updatepasswd)
	 * @param userid
	 * @param oldpasswd
	 * @param newpasswd
	 * @param token
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult updatepasswd(String userid, String oldpasswd, 
			String newpasswd, String token) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("oldpasswd",oldpasswd);
		params.put("newpasswd",newpasswd);
		params.put("sessionid", token);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.updatepasswd, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.28.	消费记录列表接口（orderlist）
	 * @param userid
	 * @param token
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult orderlist(String userid, String token, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsPost(result, ApiUrl.orderlist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.29.	积分记录明细列表（pointlist）
	 * @param userid
	 * @param token
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult pointlist(String userid, String token, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsPost(result, ApiUrl.pointlist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.30.	收藏列表接口(favoritelist)
	 * @param userid
	 * @param token
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult favoritelist(String userid, String token, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsPost(result, ApiUrl.favoritelist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.31.	删除收藏接口(removemessage) 
	 * @param userid
	 * @param softid
	 * @param token
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult removemessage(String userid, String softid, String token) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("softid", softid);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.removefavorite, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.32.	消息列表接口(messagelist)
	 * @param userid
	 * @param token
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult messagelist(String userid, 
			String token, String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsPost(result, ApiUrl.messagelist, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.34.	软件访问资源权限列表接口(softpermission)
	 * @param softid
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftPermissionResult softpermission(String softid) throws ApiException {
		StringHashMap params = new StringHashMap();
		params.put("softid", softid);
 
		String jsonText = null;
		StringBuffer xmlsbf = new StringBuffer();
		xmlsbf.append(ApiUrl.softpermission);
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
			jsonText = APIHelper.getURL(xmlsbf.toString(), 1);
		} catch (ServerException e) {
			throw new ApiException(e.getCode(),
					e.getMessage());
		}
		ApiSoftPermissionResult result = new ApiSoftPermissionResult();
		List<PermissionListInfo> list = new ArrayList<PermissionListInfo>();
		//
		try {
		JSONObject resultJson = new JSONObject(jsonText);
		result.resultcode = resultJson.getJSONObject("result").getInt("resultcode");
		result.imei = resultJson.getString("imei");
		result.sv = resultJson.getString("sv");
		JSONArray array;
		array = resultJson.getJSONObject("permelist").getJSONArray("items");
		for(int i = 0;i<array.length();i++){
			JSONObject obj = array.getJSONObject(i);
			PermissionListInfo info = new PermissionListInfo();
			info.permcode=obj.getString("permcode");
			list.add(info);
		}
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.permissionList= list;
		return result;
	}
	
	/**
	 * 3.5.35.	用户确认已经读取某条消息(confirmmessage)
	 * @param userid
	 * @param token
	 * @param messid
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult confirmmessage(String userid, 
			String token, String messid) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", token);
		params.put("messid", messid);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.confirmmessage, params, 0);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.36.	推荐列表接口（recommentlist）
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult recommendlist(String softid, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("softid", softid);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.recommentlist, params, 1);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 投诉
	 * @param userid
	 * @param sessionid
	 * @param mobile
	 * @param type 1 费用分歧 2 产品质量
	 * @param text
	 * @return
	 * @throws ApiException
	 */
	public static ApiNormolResult complain(String userid, String sessionid, 
			String mobile, String type, String text) throws ApiException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("sessionid", sessionid);
		params.put("mobile", mobile);
		params.put("type", type);
		params.put("text", text);
		try {
			return (ApiNormolResult) sendparamsPost(result, ApiUrl.complain, params, 0);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.5.2.	获取客户端背景图列表(getbackgroundlist)
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult getbackgroundlist(String cid, String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.getbackgroundlist, params, 1);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.6.59.	我的评论列表(mycommentlist)
	 * @param userid 用户id
	 * @param contenttype 内容类型1 软件 2 音乐3 图书（默认选1）
	 * @param ordertype 排序类型 0 --按照服务端规则惊醒排序 1 -- 按时间排序
	 * @param pageno 页码
	 * @param count 每页记录数
	 * @return
	 * @throws ApiException
	 */
	public static ApiCommentResult getMyCommentList(String userid, String contenttype, String ordertype, 
			String pageno, String count) throws ApiException {
		ApiCommentResult result = new ApiCommentResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		params.put("contenttype", contenttype);
		params.put("ordertype", ordertype);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiCommentResult) sendparamsGet(result, ApiUrl.mycommentlist, params, 1);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 升级请求、完成次数的日志接口（appupgrade）
	 * @param imsi imsi
	 * @param upgadeversion 升级版本(versionname)
	 * @param status 升级状态
	 */
	public static void appupgrade(String imsi, String upgradeversion, String status) {
		StringHashMap params = new StringHashMap();
		params.put("imsi", imsi);
		params.put("currentversion", Constants.VERSION);
		params.put("upgradeversion", upgradeversion);
		params.put("status", status);
		try {
			sendparamsGet(null, ApiUrl.appupgrade, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.65.	升级提醒用户数量的日志接口(remindusercount)
	 * @param imsi
	 */
	public static void remindusercount(String imsi) {
		StringHashMap params = new StringHashMap();
		params.put("imsi", imsi);
		try {
			sendparamsGet(null, ApiUrl.remindusercount, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.66.	升级用户数量的日志接口(upgradeusercount)
	 * @param imsi
	 */
	public static void upgradeusercount(String imsi) {
		StringHashMap params = new StringHashMap();
		params.put("imsi", imsi);
		try {
			sendparamsGet(null, ApiUrl.upgradeusercount, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.67.	升级提醒应用数量的日志接口(upgraderemindappcount)
	 * @param imsi
	 * @param softids 可升级的软件id,以逗号分隔
	 * @param currentversion 当前版本号
	 */
	public static void upgraderemindappcount(String imsi, String softids) {
		StringHashMap params = new StringHashMap();
		params.put("imsi", imsi);
		params.put("softids", softids);
		params.put("currentversion", Constants.VERSION);
		try {
			sendparamsGet(null, ApiUrl.upgraderemindappcount, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.68.	广告位点击的日志接口(advertclick)
	 * @param requesturl 请求地址
	 */
	public static void advertclick(String requesturl) {
		StringHashMap params = new StringHashMap();
		params.put("imsi", requesturl);
		try {
			sendparamsGet(null, ApiUrl.advertclick, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.69.	下载地址的日志接口(download)
	 * @param downloadurl
	 * @param softsourceid
	 * @param isupgradesoft
	 * @param catalogids
	 * @param downloadway
	 * @param currentversion
	 */
	public static void download(String downloadurl, String softsourceid, String isupgradesoft, 
			String catalogids, String downloadway) {
		StringHashMap params = new StringHashMap();
		params.put("downloadurl", downloadurl);
		params.put("softsourceid", softsourceid);
		params.put("isupgradesoft", isupgradesoft);
		params.put("catalogids", catalogids);
		params.put("downloadway", downloadway);
		params.put("currentversion", Constants.VERSION);
		try {
			sendparamsGet(null, ApiUrl.download, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.60.	判断手机Imei和Imsi是否注册过用户 (checkregister)
	 * @param imsi
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiNormolResult checkregister(String imsi) throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		return (ApiNormolResult) sendparamsGet(result, ApiUrl.checkregister +"?imsi="+imsi, null, 1);
	}
	
	/**
	 * 3.6.61.	用户预注册 (preregister)
	 * 
	 * @param imsi
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiPreRegisterResult prePreRegister(String imsi) throws ApiException, JSONException {
		ApiPreRegisterResult result = new ApiPreRegisterResult();
		return (ApiPreRegisterResult) sendparamsGet(result, ApiUrl.preregister +"?imsi="+imsi, null, 1);
	}
	
	/**
	 * 3.6.62.	重置用户密码 (resetpasswd)
	 * 
	 * @param imsi
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult resetpasswd(String imsi) throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		return (ApiNormolResult) sendparamsGet(result, ApiUrl.resetpasswd +"?imsi="+imsi, null, 1);
	}
	
	/**
	 * 获取imsi
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiNormolResult getimsi() throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		return (ApiNormolResult) sendparamsGet(result, ApiUrl.getimsi +"?phonetype=3", null, 1);
	}
	
	
	/**
	 * 万花筒标签列表接口(labellist)
	 * @param ordertype 排序 0按照优先级倒叙排列 1 按照优先级排列
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiCatalogListResult labellist(String cid, String pageno, String count, String ordertype) throws ApiException, JSONException {
		ApiCatalogListResult result = new ApiCatalogListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		params.put("ordertype", ordertype);
		try {
			return (ApiCatalogListResult)  sendparamsGet(result, ApiUrl.labellist, params, 1);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
		
	}
	
	/**
	 * 万花筒标签软件列表接口(labelsoftlist)
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 * label=10001&fee=0&order=1&pageno=1&count=10
	 */
	public static ApiSoftListResult Labelsoftlist(String labelid, String fee, String order, 
			String pageno, String count) throws ApiException, JSONException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("labelid", labelid);
		params.put("fee", fee);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult)  sendparamsGet(result, ApiUrl.labelsoftlist, params, 1);
		}catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 3.6.67.	软件下载完成的日志接口(downloadcomplete)
	 * @param downloadurl
	 * @param type 1 标识软件下载完成的日志 2 安装完成的日志
	 */
	public static void downloadcomplete(String downloadurl, String type,String... detail) {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		downloadurl = ServiceUtils.getImeiTimeUrl(downloadurl,detail);
		params.put("downloadurl", downloadurl);
		params.put("type", type);
		try {
			sendparamsPost(result, ApiUrl.downloadcomplete, params, 1);
		} catch (ApiException e) {
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 3.6.65.	通过标签分类的子分类ID获取标签列表接口(labellistbycatid)
	 * @param catid 
	 * @param pageno
	 * @param count
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiSoftListResult labellistbycatid(String catid, String pageno, 
			String count, String ordertype)
	throws ApiException, JSONException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("catid", catid);
		params.put("pageno", pageno);
		params.put("count", count);
		params.put("ordertype", ordertype);
		return (ApiSoftListResult) sendparamsGet(result, ApiUrl.labellistbycatid, params, 1);
	}
	
	/**
	 * 3.6.68.	根据标签分类Id获取软件列表(softlistbylabelcatid)
	 * @param labelcatid 标签分类ID 
	 * @param fee 计费类型 0 全部 1 免费 2 收费 
	 * @param order 排序类型
	 * @param pageno 页码 
	 * @param count 每页记录数
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiSoftListResult softlistbylabelcatid(String labelcatid, 
			String fee, String order, String pageno, String count,String... type) throws ApiException, JSONException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("labelcatid", labelcatid);
		params.put("fee", fee);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		if(type!=null&&type.length==1){
			params.put("type",type[0]);
		}
		return (ApiSoftListResult) sendparamsGet(result, ApiUrl.softlistbylabelcatid, params, 1);
	}
	
	/**
	 * 3.6.69.	链路响应的接口协议(linkresponse)
	 * @param url 终端测试的url地址
	 * @param time 网络连接开始到结束的时间 
	 * @param bytes 传输的字节数
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static void linkresponse(String url, String time, String bytes) throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("url", url);
		params.put("time", time);
		params.put("bytes", bytes);
		sendparamsGet(result, ApiUrl.linkresponse, params, 1);
	}
	
	/**
	 * 3.6.72.	达人响应的接口协议(linkresponse)
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static void daren() throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		sendparamsGet(result, ApiUrl.darenresponse, null, 1);
	}
	
	/**
	 * 3.6.58.	根据栏目ID获取栏目下文本列表接口(ccontentlist)
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiSoftListResult ccontentlist(String cid, String pageno, String count) throws ApiException, JSONException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("cid", cid);
		params.put("pageno", pageno);
		params.put("count", count);
		return (ApiSoftListResult) sendparamsGet(result, ApiUrl.ccontentlist, params, 1);
	}
	
	/**
	 * 备份手机联系人
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult backupcontact(String userid, Context context)
			throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		String jsonstr = ContactUtil.handlerContact(context, 0);
		String count = ContactUtil.handlerContact(context, -1);
//		WriteSettings(context, jsonstr);
		try{
//			jsonstr = JsonUtil.compress(jsonstr);
		}catch(Exception e){
			e.printStackTrace();
		}
		params.put("userid", userid);
		if(jsonstr !=null) params.put("contact", jsonstr);
		else return null;
		result = (ApiNormolResult) sendparamsPost(result, ApiUrl.backupcontact,params, 0);
		result.contactcount = (String) ((count!=null && !"".equals(count.trim()))?count:0);
		return result;
	}

	/**
	 * 还原手机联系人
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult recovercontact(String userid)
			throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("userid", userid);
		return (ApiNormolResult) sendparamsPost(result, ApiUrl.recovercontact,
				params, 0);
	}
	/**
	 * 备份短信
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult backupsms(String uid,String imsi,Context context)
			throws ApiException, JSONException {
		System.out.println(">>>>>ApiNormolResult>>>>>>>>>");
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		//TODO:
		String jsonstr = SmsUtil.getSmsInPhone(context, 1, 0, 1000, 1);
		System.out.println(jsonstr);
		String count = SmsUtil.getSmsInPhone(context, 1, 0, 1000, -1);
		System.out.println(count);
//		String jsonstr = ContactUtil.handlerContact(context, 0);
//		String count = ContactUtil.handlerContact(context, -1);
//		WriteSettings(context, jsonstr);
//		try{
////			jsonstr = JsonUtil.compress(jsonstr);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		params.put("uid", uid);
		params.put("imsi", imsi);
		//params.put("imei", imei);
		if(jsonstr !=null) params.put("sms", jsonstr);
		else return null;
		result = (ApiNormolResult) sendparamsPost(result, ApiUrl.backupsms,params, 0);
		result.contactcount = (String) ((count!=null && !"".equals(count.trim()))?count:0);
		System.out.println(">>>>>>>backupsms>>>>>>>>>>");
		return result;
	}

	/**
	 * 还原短信
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult recoversms(String uid,String imsi)
			throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("uid", uid);
		params.put("imsi", imsi);
		return (ApiNormolResult) sendparamsPost(result, ApiUrl.recoversms,
				params, 0);
	}

	/**
	 * 安装列表备份
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult installedBackup(String userid,String imsi,String install)
			throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("uid", userid);
		params.put("imsi", imsi);
		params.put("install", install);
		return (ApiNormolResult) sendparamsPost(result, ApiUrl.backupinstall,
				params, 0);
	}
	
	/**
	 *  3.6.81  根据appid获取同类软件列表（softlistbyappid）
	 * @param catalogid
	 * @param fee
	 * @param order
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 */
	public static ApiSoftListResult softlistbyappid(String appid, String fee, String order, 
			String pageno, String count) throws ApiException {
		ApiSoftListResult result = new ApiSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("appid", appid);
		params.put("fee", fee);
		params.put("order", order);
		params.put("pageno", pageno);
		params.put("count", count);
		try {
			return (ApiSoftListResult) sendparamsGet(result, ApiUrl.softlistbyappid, params, 1);
		} catch (org.json.JSONException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}
	
	/**
	 * 用户注册手机保修信息
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult registerwt(String date)
			throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
		params.put("buydate",date);
		return (ApiNormolResult) sendparamsPost(result, ApiUrl.registerwt,
				params, 0);
	}
	/**
	 * 查询用户手机保修信息
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiNormolResult getwarranty()
			throws ApiException, JSONException {
		ApiNormolResult result = new ApiNormolResult();
		StringHashMap params = new StringHashMap();
	 
		return (ApiNormolResult) sendparamsPost(result, ApiUrl.getwarranty,
				params, 0);
	}
	
	/**
	 * 安装列表还原
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws ApiException
	 * @throws JSONException
	 */
	public static ApiInstalledRecoverSoftListResult installedRecover(String userid,String imsi)
			throws ApiException, JSONException {
		ApiInstalledRecoverSoftListResult result = new ApiInstalledRecoverSoftListResult();
		StringHashMap params = new StringHashMap();
		params.put("uid", userid);
		params.put("imsi", imsi);
		
		return (ApiInstalledRecoverSoftListResult) sendparamsPost(result, ApiUrl.recoveriinstall,
				params, 0);
	}
	/**
	 * POST
	 * 
	 * @param url
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 * @throws org.json.JSONException
	 */
	public static <T> ApiResult<T> sendparamsPost(T result, String url,
			StringHashMap params, int urlType) throws ApiException,
			org.json.JSONException {
		return new ApiRequest(result).sendParamsPost(url, params, urlType);
	}

	/**
	 * GET
	 * 
	 * @param url
	 * @param cid
	 * @param pageno
	 * @param count
	 * @return
	 * @throws ApiException
	 * @throws org.json.JSONException
	 */
	public static <T> ApiResult<T> sendparamsGet(T result, String url,
			StringHashMap params, int urlType) throws ApiException,
			org.json.JSONException {
		RequestResultFactory.getInstance().getObjectSingle(result.getClass());
		return new ApiRequest(result).sendParamsGet(url, params, urlType);
	}

	public String ReadSettings(Context context) {
		FileInputStream fIn = null;
		InputStreamReader isr = null;

		char[] inputBuffer = new char[255];
		String data = null;

		try {
			fIn = context.openFileInput("settings.dat");
			isr = new InputStreamReader(fIn);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
			Toast.makeText(context, "Settings read", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Settings not read", Toast.LENGTH_SHORT)
					.show();
		} finally {
			try {
				isr.close();
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public static void WriteSettings(Context context, String data) {
		File path = new File("/mnt/sdcard/test");
		File file = new File("/mnt/sdcard/test/test.txt");
		if (!path.exists()) {
			// 路径不存在? Just 创建
			path.mkdirs();
		}
		if (!file.exists()) {
			// 文件不存在、 Just创建
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Log.i("gefy", "写入");
			osw.write(data);
			osw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
