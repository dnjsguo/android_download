package com.appdear.client.service.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ExceptionEnum;
import com.appdear.client.model.ApiCommentResult;
import com.appdear.client.model.BackgroundInfo;
import com.appdear.client.model.CannelIDinfo;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.model.InitModel;
import com.appdear.client.model.OrderlistInfo;
import com.appdear.client.model.PermissionListInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.model.UpdatelistInfo;
import com.appdear.client.model.UserInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.JsonUtil;
import com.appdear.client.utility.cache.ListviewSourceCache;
import com.appdear.client.utility.cache.SourceCommon;

/**
 * 协议解析
 * @author zqm
 *
 */
public class ApiParseResult {
	
	/**
	 * 3.5.12.	文字评论提交接口
	 * 3.5.13.	软件是否收藏列表
	 * 3.5.14.	提交收藏接口
	 * 3.5.16.	爱皮账户与积分是否可以进行支付接口
	 * 3.5.18.	提交用户手机已安装软件列表
	 * 3.5.22.	效验用户名是否可以使用接口
	 * 3.5.25.	更新用户个人信息
	 * 3.5.26.	找回登录密码
	 * 3.5.27.	更新个人密码
	 * 3.5.31.	删除收藏接口
	 * 3.5.35.	用户确认已经读取某条消息
	 *
	 * @param xmlText
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiNormolResult parseNormol(String xmlText, String url) throws JSONException, ApiException {
		JSONObject jsonobject = new JSONObject(xmlText);
		ApiNormolResult result = new ApiNormolResult();
		JSONObject resultJson = JsonUtil.getJSONObject(jsonobject, "result", null);
		if (resultJson == null)
			return result;
		result.resultcode = JsonUtil.getString(resultJson, "resultcode", "0");
		if (!result.resultcode.equals("000000")) {
			result.resultinfo = JsonUtil.getString(resultJson, "resultinfo", "出错了！");
			try {
				throw new ApiException(Integer.parseInt(result.resultcode), result.resultinfo);
			} catch (NumberFormatException e) {
				throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
			}
		}
		result.sv = JsonUtil.getString(jsonobject, "sv", Constants.VERSION);
		result.imei = JsonUtil.getString(jsonobject, "imei", "3333333333333");
		
		if (!url.contains(ApiUrl.isfavoritelist))
			result.isok = JsonUtil.getInt(jsonobject, "isok", 0);
		else
			result.isfavorited = JsonUtil.getInt(jsonobject, "isfavorited", 0);
		
		if (url.contains(ApiUrl.getimsi)) {
			result.imsi = JsonUtil.getString(jsonobject, "imsi", "");
		}
		if (url.contains(ApiUrl.checkregister)) {
			result.username = JsonUtil.getString(jsonobject, "username", "");
		}
		if(url.contains(ApiUrl.backupinstall))
		{
			result.failidx=JsonUtil.getString(jsonobject, "failidx", "");
		}
		if (url.contains(ApiUrl.backupcontact) || url.contains(ApiUrl.recovercontact)) {
			result.contact = JsonUtil.getString(jsonobject, "contact", "");
		}
		if (url.contains(ApiUrl.backupsms) || url.contains(ApiUrl.recoversms)) {
			result.sms = JsonUtil.getString(jsonobject, "sms", "");
		}
		if(url.contains(ApiUrl.getwarranty)){
			result.buydate=JsonUtil.getString(jsonobject, "buydate", "");
		}
		return result;
	}
	
	/**
	 * 3.5.21.	用户注册接口
	 * 3.5.23.	用户个人登录接口
	 * 3.5.24.	用户个人信息接口
	 * @param xmlText
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	public static ApiUserResult parseRegisterLogin(String xmlText, String url) throws JSONException, ApiException {
		JSONObject jsonobject = new JSONObject(xmlText);
		ApiUserResult result = new ApiUserResult();
		JSONObject resultJson = JsonUtil.getJSONObject(jsonobject, "result", null);
		if (resultJson == null)
			return result;
		result.resultcode = JsonUtil.getString(resultJson, "resultcode", "0");
		if (!result.resultcode.equals("000000")) {
			result.resultinfo = JsonUtil.getString(resultJson, "resultinfo", "出错了");
			try {
				throw new ApiException(Integer.parseInt(result.resultcode), result.resultinfo);
			} catch (NumberFormatException e) {
				throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
			}
		}
		//用户注册
		result.sv = JsonUtil.getString(jsonobject, "sv", Constants.VERSION);
		result.imei = JsonUtil.getString(jsonobject, "imei", "333333333333333");
		result.token = JsonUtil.getString(jsonobject, "sessionid", "");
		if (url.contains(ApiUrl.register) || url.contains(ApiUrl.userlogin)) {
			result.isok = JsonUtil.getInt(jsonobject, "isok", 0);
			result.nickname = JsonUtil.getString(jsonobject, "nickname", "");
			result.userid = JsonUtil.getString(jsonobject, "userid", "0");
		}
		
		//用户个人信息接口
		if (url.contains(ApiUrl.userprofile)) {
			result.account = JsonUtil.getInt(jsonobject, "acount", 0);
			result.level = JsonUtil.getString(jsonobject, "level", "");
			result.totalpoint = JsonUtil.getInt(jsonobject, "totalpoint", 0);
			JSONObject userJson = JsonUtil.getJSONObject(jsonobject, "user", null);
			if (userJson != null) {
				UserInfo userinfo = new UserInfo();
				userinfo.desc = JsonUtil.getString(userJson, "desc", "");
				userinfo.area = JsonUtil.getString(userJson, "area", "");
				userinfo.email = JsonUtil.getString(userJson, "email", "");
				userinfo.nickname = JsonUtil.getString(userJson, "nickname", "");
				userinfo.profession = JsonUtil.getString(userJson, "profession", "");
				userinfo.userid = JsonUtil.getString(userJson, "userid", "0");
				userinfo.gender = JsonUtil.getString(userJson, "gender", "");
				userinfo.qq = JsonUtil.getString(userJson, "qq", "");
				userinfo.mobile = JsonUtil.getString(userJson, "mobile", "");
				result.userinfo = userinfo;
			}
		}
		return result;
	}
	

	/**
	 * 3.5.15.	软件作者信息接口（softauthor）
	 * 3.5.29.	积分记录明细列表
	 * 3.5.30.	收藏列表接口
	 * 3.5.19.	用户获取软件可更新列表
	 * 3.5.20.	更新单个软件的最新版本
	 * 3.5.28.	消费记录列表接口
	 * 3.5.34.	软件访问资源权限列表接口
	 * 3.5.36.	推荐列表接口（recommentlist）
	 * 3.5.11.	软件评论列表
	 * 3.5.8.	Autocomplete列表接口
	 * 3.5.2.	主页banner广告列表接口
	 * 3.5.3.	二级频道软件内容列表接口
	 * 3.5.5.	类别内容列表接口
	 * 3.5.6.	榜单软件内容列表接口
	 * 3.5.9.	搜索结果接口
	 * 3.5.32.	消息列表接口
	 * 3.5.1.	初始化服务接口
	 * 3.5.10.	软件详情接口
	 * @param xmlText
	 * @return
	 * @throws JSONException 
	 * @throws ApiException 
	 */
	
	private static void initModelFun(ApiSoftListResult result,int lable){
		AppContext.getInstance().initModel=new InitModel();
		AppContext.getInstance().initModel.dpreurl=result.dpreurl;
		AppContext.getInstance().initModel.spreurl=result.spreurl;
		AppContext.getInstance().initModel.sectionversion=result.sectionversion;
		AppContext.getInstance().initModel.isupdate=result.isupdate;
		AppContext.getInstance().initModel.updateurl=result.updateurl;
		AppContext.getInstance().initModel.softVersion=result.softVersion;
		AppContext.getInstance().initModel.linkurl=result.linkurl;
		AppContext.getInstance().initModel.linkflag=result.linkflag;
		AppContext.getInstance().initModel.labelversionresponse=lable;
		AppContext.getInstance().initModel.softSize=result.softSize;
		AppContext.getInstance().initModel.softUpdateTip=result.softUpdateTip;
		AppContext.getInstance().initModel.time=new Date().getTime();
		if(result.dynamics!=null&&!result.dynamics.equals("")){
			String[] str=result.dynamics.split(",");
			if(str!=null&&str.length>0){
				InitModel.dynamic=str;
			}
		}
		if(result.initlist!=null&&result.initlist.size()>0){
			AppContext.getInstance().initModel.initlist=result.initlist;
		}
		ListviewSourceCache.getInstance().addInitModel(SourceCommon.INIT_MODEL, AppContext.getInstance().initModel);
	}
	public static ApiSoftListResult parseSoftList(String xmlText, String url) throws JSONException, ApiException {
		if (Constants.DEBUG) {
			Log.i("api url", url);
		}
		if (xmlText == null)
			return null;
		
		try {
			ApiSoftListResult result = new ApiSoftListResult();
			JSONObject jsonobject = new JSONObject(xmlText);
			JSONObject resultJson = JsonUtil.getJSONObject(jsonobject, "result", null);
			if (resultJson == null)
				return result;
			result.def=JsonUtil.getString(jsonobject, "default", "");
			result.timpstamp=Long.parseLong(JsonUtil.getString(jsonobject, "chtime", "0"));
			result.resultcode = JsonUtil.getString(resultJson, "resultcode", "0");
			if (!result.resultcode.equals("000000")) {
				result.resultinfo = JsonUtil.getString(resultJson, "resultinfo", "出错了");
				try {
					throw new ApiException(Integer.parseInt(result.resultcode), result.resultinfo);
				} catch (NumberFormatException e) {
					throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
				}
			}
			//如果是类别软件列表加副标题
			if (url.contains(ApiUrl.catalogsoftlist)) {
				result.catdesc = JsonUtil.getString(jsonobject, "catdesc", "");
			}
			if (url.contains(ApiUrl.initinfo)||url.contains(ApiUrl.updateclient)) {
				
				result.dpreurl = JsonUtil.getString(jsonobject, "spreurl", "");
				result.spreurl = JsonUtil.getString(jsonobject, "dpreurl", "");
				result.wapurl = JsonUtil.getString(jsonobject, "wap2url", "");
				AppContext.getInstance().wap_url = result.wapurl;
				result.sectionversion = JsonUtil.getInt(jsonobject, "sectionversion", 0);
				result.isupdate = JsonUtil.getString(jsonobject, "isupdate", "");
				result.updateurl = JsonUtil.getString(jsonobject, "updateurl", "");
				result.softVersion = JsonUtil.getString(jsonobject, "version", "");
				result.linkurl = JsonUtil.getString(jsonobject, "linkurl", "");
				result.dynamics = JsonUtil.getString(jsonobject, "dynamics", "");
				result.linkflag = JsonUtil.getInt(jsonobject, "linkflag", 0);
				AppContext.getInstance().labelversionresponse = 
					Integer.parseInt(JsonUtil.getString(jsonobject, "labelversion", "1"));
				
				//升级信息，后来添加
				result.softSize = JsonUtil.getString(jsonobject, "versionsize", "");
				result.softUpdateTip = JsonUtil.getString(jsonobject, "versiondesc", "");
				if (result.sectionversion > 0) {
					JSONObject initlist = JsonUtil.getJSONObject(jsonobject, "list", null);
					if(initlist != null) {
						result.totalcount = JsonUtil.getInt(initlist, "count", 0);
						JSONArray items = initlist.has("items")?(initlist.get("items")!=null?initlist.getJSONArray("items"):null):null;
						List<CannelIDinfo> list = new ArrayList<CannelIDinfo>();
						if (items != null) {
							for (int i = 0; i < items.length(); i ++) {
								CannelIDinfo info = new CannelIDinfo();
								JSONObject object = items.getJSONObject(i);
								info.sectionid = JsonUtil.getInt(object, "sectionid", 0);
								info.code = JsonUtil.getInt(object, "code", 0);
								list.add(info);
							}
							result.initlist = list;
						}
					}
				}
				if(url.contains(ApiUrl.initinfo))
					initModelFun(result,AppContext.getInstance().labelversionresponse);
			}
			
			// 3.5.15.	软件作者信息接口
			if (url.contains(ApiUrl.softauthor)) {
				result.author = JsonUtil.getString(jsonobject, "author", "");
				result.email = JsonUtil.getString(jsonobject, "email", "");
				result.homeurl = JsonUtil.getString(jsonobject, "homeurl", "");
				result.authorid = JsonUtil.getString(jsonobject, "authorid", "0");
				JSONObject softlistJosn = JsonUtil.getJSONObject(jsonobject, "softlist", null);
				if (softlistJosn != null) {
					result.totalcount = JsonUtil.getInt(softlistJosn, "count", 0);
					List<SoftlistInfo> list = new ArrayList<SoftlistInfo>();
					JSONArray items = softlistJosn.has("items")?softlistJosn.get("items")!=null?softlistJosn.getJSONArray("items"):null:null;
					if (items != null) {
						for (int i = 0; i < items.length(); i ++) {
							JSONObject object = items.getJSONObject(i);
							SoftlistInfo info = new SoftlistInfo();
							info.softdesc = JsonUtil.getString(object, "softdesc", "");
							info.appid = JsonUtil.getString(object, "appid", "");
							int price = JsonUtil.getInt(object, "softprice", 0);
							if (price != 0)
								info.softprice = price/100;
							info.softgrade = JsonUtil.getInt(object, "softgrade", 0);
							info.softpoints = JsonUtil.getInt(object, "softpoints", 0);
							info.softname = JsonUtil.getString(object, "softname", "");
							info.softicon = JsonUtil.getString(object, "softicon", "");
							info.softid = JsonUtil.getInt(object, "softid", 0);
							info.versioncode = JsonUtil.getInt(object, "versioncode", 0);
							list.add(info);
						}
						result.softList = list;
					}
				}
			}
			
			//3.5.10.	软件详情接口softinfo
			if (url.contains(ApiUrl.softinfo)) {
				SoftlistInfo detailinfo = new SoftlistInfo();
				detailinfo.downloadurl = JsonUtil.getString(jsonobject, "downloadurl", "");
				detailinfo.detail = JsonUtil.getString(jsonobject, "detail", "");
				detailinfo.softsize = JsonUtil.getInt(jsonobject, "softsize", 0);
				detailinfo.appid = JsonUtil.getString(jsonobject, "appid", "");
				detailinfo.download = JsonUtil.getInt(jsonobject, "download", 0);
				detailinfo.softname = JsonUtil.getString(jsonobject, "softname", "");
				detailinfo.softid = JsonUtil.getInt(jsonobject, "softid", 0);
				detailinfo.version = JsonUtil.getString(jsonobject, "version", "");
				detailinfo.publishtime = JsonUtil.getString(jsonobject, "publishtime", "");
				detailinfo.language = JsonUtil.getString(jsonobject, "language", "中文");
				int price = JsonUtil.getInt(jsonobject, "softprice", 0);
				if (price != 0) {
					detailinfo.softprice = price/100;
				}
				detailinfo.softgrade = JsonUtil.getInt(jsonobject, "softgrade", 0);
				detailinfo.comment = JsonUtil.getInt(jsonobject, "comment", 0);
				detailinfo.softicon = JsonUtil.getString(jsonobject, "softicon", "");
				detailinfo.catid = JsonUtil.getInt(jsonobject, "catid", 0);
				result.detailinfo = detailinfo;
				//imageurl
				JSONObject snapshotlist = JsonUtil.getJSONObject(jsonobject, "snapshotlsit", null);
				if (snapshotlist != null) {
					result.totalcount = JsonUtil.getInt(snapshotlist, "count", 0);
					JSONArray snapshotItems = snapshotlist.has("items")?snapshotlist.get("items")!=null?snapshotlist.getJSONArray("items"):null:null;
					List<String> imgurlList = new ArrayList<String>();
					if (snapshotItems != null) {
						for (int i = 0; i < snapshotItems.length(); i ++) {
							JSONObject object = snapshotItems.getJSONObject(i);
							String imgurl = JsonUtil.getString(object, "imgurl", "");
							if (!imgurl.equals(""))
								imgurlList.add(imgurl);
						}
						result.imgurl = imgurlList;
					}
				}
			}
			
			
			if (url.contains(ApiUrl.keywordlist) 
					|| url.contains(ApiUrl.bannerlist)
					|| url.contains(ApiUrl.softlist) 
					|| url.contains(ApiUrl.dynamicsoftlist) 
					|| url.contains(ApiUrl.catalogsoftlist)
					|| url.contains(ApiUrl.softlistbyappid)
					|| url.contains(ApiUrl.boradsoftlist)
					|| url.contains(ApiUrl.favoritelist)
					|| url.contains(ApiUrl.resultsoftlist)
					|| url.contains(ApiUrl.commentlist)
					|| url.contains(ApiUrl.pointlist)
					|| url.contains(ApiUrl.messagelist)
					|| url.contains(ApiUrl.getbackgroundlist)
					||url.contains(ApiUrl.labelsoftlist)
					|| url.contains(ApiUrl.labellistbycatid)
					|| url.contains(ApiUrl.softlistbylabelcatid)
					|| url.contains(ApiUrl.ccontentlist)) {
				
				if (url.contains(ApiUrl.dynamicsoftlist)){
					String asids = JsonUtil.getString(jsonobject, "asids", "");
					result.asids=asids;
				}
				
				JSONObject pagejson = JsonUtil.getJSONObject(jsonobject, "page", null);
				if (pagejson != null) {
					result.totalcount = JsonUtil.getInt(pagejson, "totalcount", 0);
					result.pagenum = JsonUtil.getInt(pagejson, "pagenum", 0);
					result.pageno = JsonUtil.getInt(pagejson, "pageno", 0);
					JSONArray items = pagejson.has("items")?pagejson.get("items")!=null?pagejson.getJSONArray("items"):null:null;
					List<SoftlistInfo> list = new ArrayList<SoftlistInfo>();
					if (items != null) {
						//处理labellistbycatid协议返回数据
						if (url.contains(ApiUrl.labellistbycatid)) {
							for (int i = 0; i < items.length(); i ++) {
								SoftlistInfo info = new SoftlistInfo();
								JSONObject object = items.getJSONObject(i);
								info.softid = JsonUtil.getInt(object, "labelid", 0);
								info.softicon = JsonUtil.getString(object, "icon", "");
								info.softname = JsonUtil.getString(object, "labelname", "");
								list.add(info);
							}
							result.softList = list;
							return result;
						}
						if (url.contains(ApiUrl.ccontentlist)) {
							List<SoftlistInfo> contentlist = new ArrayList<SoftlistInfo>();
							for (int i = 0; i < items.length(); i ++) {
								SoftlistInfo info = new SoftlistInfo();
								JSONObject object = items.getJSONObject(i);
								info.softname = JsonUtil.getString(object, "name", "");
								info.softdesc = JsonUtil.getString(object, "desc", "");
								info.softicon = JsonUtil.getString(object, "icon", "");
								contentlist.add(info);
							}
							result.softList = contentlist;
							return result;
						}
						//处理getbackgroundlist协议返回数据
						if (url.contains(ApiUrl.getbackgroundlist)) {
							List<BackgroundInfo> backgroundlist = new ArrayList<BackgroundInfo>();
							for (int i = 0; i < items.length(); i ++) {
								BackgroundInfo backinfo = new BackgroundInfo();
								JSONObject object = items.getJSONObject(i);
								backinfo.id = JsonUtil.getInt(object, "backid", 0);
								backinfo.icon = JsonUtil.getString(object, "icon", "");
								backinfo.desc = JsonUtil.getString(object, "desc", "");
								backinfo.backurl = JsonUtil.getString(object, "backurl", "");
								backgroundlist.add(backinfo);
							}
							result.backgroundlist = backgroundlist;
						} else {
							for (int i = 0; i < items.length(); i ++) {
								SoftlistInfo info = new SoftlistInfo();
								JSONObject object = items.getJSONObject(i);
								if (url.contains(ApiUrl.keywordlist)) {
									result.keywordList.add(JsonUtil.getString(object, "keyword", "")+":"+JsonUtil.getString(object, "trend", ""));
								}
								if (url.contains(ApiUrl.bannerlist)) {
									info.adid = JsonUtil.getInt(object, "adid", 0);
									String adurl = JsonUtil.getString(object, "adurl", "");
									if (!adurl.equals(""))
										info.adurl = adurl;
									info.adtitle = JsonUtil.getString(object, "adtitle", "");
									info.adtype = JsonUtil.getString(object, "adtype", "0");
									info.imgurl = JsonUtil.getString(object, "imgurl", "");
									info.softid = JsonUtil.getInt(object, "cid", 0);
								}
								if (url.contains(ApiUrl.softlist) 
										|| url.contains(ApiUrl.dynamicsoftlist) 
										|| url.contains(ApiUrl.catalogsoftlist)
										|| url.contains(ApiUrl.softlistbyappid)
										|| url.contains(ApiUrl.boradsoftlist) 
										|| url.contains(ApiUrl.favoritelist)
										|| url.contains(ApiUrl.resultsoftlist)
										|| url.contains(ApiUrl.softlistbylabelcatid)) {
									info.appid = JsonUtil.getString(object, "appid", "");
									info.softdesc = JsonUtil.getString(object, "softdesc", "");
									int price = JsonUtil.getInt(object, "softprice", 0);
									if (price != 0) {
										info.softprice = price/100;
									}
									info.softname = JsonUtil.getString(object, "softname", "");
									info.softgrade = JsonUtil.getInt(object, "softgrade", 0);
									info.downloadurl = JsonUtil.getString(object, "downloadurl", "");
									info.softicon = JsonUtil.getString(object, "softicon", "");
									info.softid = JsonUtil.getInt(object, "softid", 0);
									if (!url.contains(ApiUrl.resultsoftlist)) {
										info.softpoints = JsonUtil.getInt(object, "softpoints", 0);
										info.versioncode = JsonUtil.getInt(object, "versioncode", 0);
									}
									info.version = JsonUtil.getString(object, "version", "");
									info.softsize = JsonUtil.getInt(object, "softsize", 0);
									info.isfirst=JsonUtil.getString(object, "isfirst","");
									info.isexclusive=JsonUtil.getString(object, "isexclusive","");
									info.istop=JsonUtil.getString(object, "istop","");
									info.download = JsonUtil.getInt(object, "download", 0);
								}
								if (url.contains(ApiUrl.commentlist)) {
									info.commentid = JsonUtil.getString(object, "commentid", "0");
									info.text = JsonUtil.getString(object, "text", "");
									info.time = JsonUtil.getString(object, "time", "");
									info.username = JsonUtil.getString(object, "username", "");
									info.userid = JsonUtil.getString(object, "userid", "0");
									info.softgrade = JsonUtil.getInt(object, "grade", 0);
									info.commentimei = JsonUtil.getString(object, "commentimei", "");
								}
								if(url.contains(ApiUrl.pointlist)) {
									info.point = JsonUtil.getInt(object, "point", 0);
									info.time = JsonUtil.getString(object, "time", "");
									info.pointinfo = JsonUtil.getString(object, "pointinfo", "");
								}
				
								if (url.contains(ApiUrl.messagelist)) {
									info.text = JsonUtil.getString(object, "text", "");
									info.time = JsonUtil.getString(object, "time", "");
									info.type = JsonUtil.getInt(object, "type", 0);
								}
								
								if (url.contains(ApiUrl.cataloglist)) {
									info.catalogicon = JsonUtil.getString(object, "catalogicon", "");
									info.catalogdesc = JsonUtil.getString(object, "catalogdesc", "");
									info.catalogname = JsonUtil.getString(object, "catalogname", "");
									info.catalognum = JsonUtil.getString(object, "catalognum", "");
									info.catalogid = JsonUtil.getString(object, "catalogid", "");
								}
								
								list.add(info);
							}
							result.softList = list;
							result.searchlist = list;	
						}
					}
				}
			}
			if (url.contains(ApiUrl.updatesoft)) {
				JSONObject jsonitem = JsonUtil.getJSONObject(jsonobject, "softitem", null);
				if(jsonitem != null){
					UpdatelistInfo info = new UpdatelistInfo();
					info.downloadurl = JsonUtil.getString(jsonitem, "downloadurl", "");
					info.versionname = JsonUtil.getString(jsonitem, "versionname", "");
					info.appid = JsonUtil.getString(jsonitem, "appid", "");
					info.softname = JsonUtil.getString(jsonitem, "softname", "");
					info.softid = JsonUtil.getInt(jsonitem, "softid", 0);
					info.softsize = JsonUtil.getInt(jsonitem, "size", 0);
				}
			}
			
			//3.5.28.	消费记录列表接口
			if (url.contains(ApiUrl.orderlist)) {
				JSONObject orderlistObject = JsonUtil.getJSONObject(jsonobject, "orderlist", null);
				if (orderlistObject != null) {
					result.totalcount = JsonUtil.getInt(orderlistObject, "count", 0);
					JSONArray items = orderlistObject.has("items")?orderlistObject.get("items")!=null?orderlistObject.getJSONArray("items"):null:null;
					List<OrderlistInfo> list = new ArrayList<OrderlistInfo>();
					if (items != null) {
						for (int i = 0; i < items.length(); i ++) {
							JSONObject iemsObject = items.getJSONObject(i);
							OrderlistInfo info = new OrderlistInfo();
							info.icon = JsonUtil.getString(iemsObject, "icon", "");
							info.tranid = JsonUtil.getString(iemsObject, "tranid", "0");
							info.price = JsonUtil.getInt(iemsObject, "price", 0);
							info.desc = JsonUtil.getString(iemsObject, "desc", "");
							info.softname = JsonUtil.getString(iemsObject, "sotname", "");
							info.pay = JsonUtil.getInt(iemsObject, "pay", 0);
							info.grade = JsonUtil.getInt(iemsObject, "grade", 0);
							info.pytype = JsonUtil.getInt(iemsObject, "pytype", 0);
							info.orderid = JsonUtil.getInt(iemsObject, "orderid", 0);
							info.discount = JsonUtil.getInt(iemsObject, "discount", 0);
							list.add(info);
						}
						result.orderList = list;	
					}
				}
			}
			
			//3.5.34.	软件访问资源权限列表接口
			if (url.contains(ApiUrl.softpermission)) {
				JSONObject permlist = JsonUtil.getJSONObject(jsonobject, "permlist", null);
				if (permlist != null) {
					result.totalcount = JsonUtil.getInt(permlist, "count", 0);
					JSONArray items = permlist.has("items")?permlist.get("items")!=null?permlist.getJSONArray("items"):null:null;
					List<PermissionListInfo> list = new ArrayList<PermissionListInfo>();
					if (items != null) {
						for (int i = 0; i < items.length(); i ++) {
							PermissionListInfo info = new PermissionListInfo();
							JSONObject object = items.getJSONObject(i);
							info.permdescen = JsonUtil.getString(object, "permdescen", "");
							info.permcode = JsonUtil.getString(object, "permcode", "");
							info.permdescch = JsonUtil.getString(object, "permdescch", "");
							info.permtype = JsonUtil.getInt(object, "permtype", 0);
							list.add(info);
						}
						result.permissionlist = list;	
					}
				}
			}
			
			//3.5.8.	Autocomplete列表接口
			if (url.contains(ApiUrl.autolist)) {
				JSONObject listObject = JsonUtil.getJSONObject(jsonobject, "autolist", null);
				if (listObject != null) {
					result.totalcount = JsonUtil.getInt(listObject, "count", 0);
					JSONArray items = listObject.has("items")?listObject.get("items")!=null?listObject.getJSONArray("items"):null:null;
					List<String> list = new ArrayList<String>();
					if (items != null) {
						for (int i = 0; i < items.length(); i ++) {
							JSONObject object = items.getJSONObject(i);
							list.add(JsonUtil.getString(object, "contentname", ""));
						}	
					}
					result.autolist = list;
				}
				
			}
			
			//intalllist
			if(url.contains(ApiUrl.updatelist)) {
				JSONObject installlist = JsonUtil.getJSONObject(jsonobject, "updatelist", null);
				if (installlist != null) {
					result.totalcount = JsonUtil.getInt(installlist, "count", 0);
					JSONArray snapshotItems = installlist.has("items")?installlist.get("items")!=null?installlist.getJSONArray("items"):null:null;
					List<UpdatelistInfo> updatelist = new ArrayList<UpdatelistInfo>();
					if (snapshotItems != null) {
						for (int i = 0; i < snapshotItems.length(); i ++) {
							JSONObject object = snapshotItems.getJSONObject(i);
							UpdatelistInfo info = new UpdatelistInfo();
							info.downloadurl = JsonUtil.getString(object, "downloadurl", "");
							info.versionname = JsonUtil.getString(object, "versionname", "");
							info.appid = JsonUtil.getString(object, "appid", "");
							info.softid = JsonUtil.getInt(object, "softid", 0);
							info.softsize=JsonUtil.getInt(object, "size", 0);
							updatelist.add(info);
						}
						result.updatelist = updatelist;	
					}
				}
			}
			//intalllist
			if(url.contains(ApiUrl.updatelist2)) {
				JSONObject installlist = JsonUtil.getJSONObject(jsonobject, "updatelist", null);
				if (installlist != null) {
					result.totalcount = JsonUtil.getInt(installlist, "count", 0);
					JSONArray snapshotItems = installlist.has("items")?installlist.get("items")!=null?installlist.getJSONArray("items"):null:null;
					List<UpdatelistInfo> updatelist = new ArrayList<UpdatelistInfo>();
					if (snapshotItems != null) {
						for (int i = 0; i < snapshotItems.length(); i ++) {
							JSONObject object = snapshotItems.getJSONObject(i);
							UpdatelistInfo info = new UpdatelistInfo();
							info.downloadurl = JsonUtil.getString(object, "downloadurl", "");
							info.versionname = JsonUtil.getString(object, "versionname", "");
							info.appid = JsonUtil.getString(object, "appid", "");
							info.softid = JsonUtil.getInt(object, "softid", 0);
							info.softsize=JsonUtil.getInt(object, "size", 0);
							info.updatedesc=JsonUtil.getString(object, "updatedesc","");
							info.udlinenum = JsonUtil.getInt(object, "udlinenum", 0);
							updatelist.add(info);
						}
						result.updatelist = updatelist;	
					}
				}
			}
			return result;
		} catch (NumberFormatException e) {
			throw new ApiException(ExceptionEnum.ApiExceptionCode.API_OTHER_ERR.getValue(), e.getMessage());
		}
	}

	public static ApiCatalogListResult parseCatalogList(String jsonText, String url) throws JSONException {
		JSONObject resultJson = new JSONObject(jsonText); 
		ApiCatalogListResult result= new ApiCatalogListResult();
		//该节点匹配  软件类别
		if(url.contains(ApiUrl.cataloglist)){
			result.resultcode = resultJson.getJSONObject("result").getInt("resultcode");
			if(resultJson.has("page")){
			result.totalcount = resultJson.getJSONObject("page").getInt("totalcount");
			result.imei  = resultJson.getString("imei");
			result.pageno = resultJson.getJSONObject("page").getInt("pageno");
			result.pagenum = resultJson.getJSONObject("page").getInt("pagenum");
			result.sv = resultJson.getString("sv");
			
			JSONArray array = resultJson.getJSONObject("page").getJSONArray("items");
			ArrayList<CatalogListInfo> list = new ArrayList<CatalogListInfo>();
			CatalogListInfo info = null;
			for(int i = 0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				if(object != null) {
					info = new CatalogListInfo();
					info.catalogdesc = JsonUtil.getString(object, "catalogdesc", "");
					info.catalogicon = JsonUtil.getString(object, "catalogicon", "");
					info.catalogname = JsonUtil.getString(object, "catalogname", "");
					info.catalognum = JsonUtil.getString(object, "catalognum", "");
					info.catalogid = JsonUtil.getString(object, "catalogid", "0");
					info.appCount = JsonUtil.getString(object, "catalognum", "0");
					//精品专题  类别的更新时间    c c
					info.catalogTime = JsonUtil.getString(object, "catalogtime", ""); 
					list.add(info);
				}
			}
			result.catalogList = list;
		 }else{
			result.catalogList = null;
		 }
				
		}
		
		//labellist
		if(url.contains(ApiUrl.labellist)) {
			String ctype = JsonUtil.getString(resultJson, "ctype", "0");
			JSONObject labellist = JsonUtil.getJSONObject(resultJson, "page", null);
			if (labellist != null) {
				result.totalcount = JsonUtil.getInt(labellist, "totalcount", 0);
				JSONArray snapshotItems = labellist.has("items")?labellist.get("items")!=null?labellist.getJSONArray("items"):null:null;
				List<CatalogListInfo> list = new ArrayList<CatalogListInfo>();
				if (snapshotItems != null) {
					for (int i = 0; i < snapshotItems.length(); i ++) {
						JSONObject object = snapshotItems.getJSONObject(i);
						CatalogListInfo info = new CatalogListInfo();
						if ("0".equals(ctype)) {
							info.catalogicon = JsonUtil.getString(object, "icon", "");
							info.catalogname = JsonUtil.getString(object, "labelname", "");;
							info.catalogid = JsonUtil.getInt(object, "labelid", 0) + "";
							info.ctype = ctype;
						} else if ("1".equals(ctype)) {
							info.catalogicon = JsonUtil.getString(object, "caticon", "");
							info.catalogname = JsonUtil.getString(object, "catname", "");;
							info.catalogid = JsonUtil.getInt(object, "catid", 0) + "";
							info.ctype = ctype;
						}
						list.add(info);
					}
					result.catalogList = list;	
				}
			}
		}
		return result;
	}
	
	public static ApiSoftPermissionResult parsePermissionList(String jsonText, String url) throws JSONException {
		JSONObject resultJson = new JSONObject(jsonText); 
		ApiSoftPermissionResult result = new ApiSoftPermissionResult();
		List<PermissionListInfo> list = new ArrayList<PermissionListInfo>();
		if(url.contains(ApiUrl.softpermission)){
			result.resultcode = resultJson.getJSONObject("result").getInt("resultcode");
			result.imei = resultJson.getString("imei");
			result.sv = resultJson.getString("sv");
			JSONArray array = resultJson.getJSONObject("permlist").getJSONArray("items");
			for(int i = 0;i<array.length();i++){
				JSONObject obj = array.getJSONObject(i);
				PermissionListInfo info = new PermissionListInfo();
				info.permcode=obj.getString("permcode");
				list.add(info);
			}
			result.permissionList= list;
		}
		return result;
	}
	
	/**
	 * 解析我的评论列表
	 * @param jsonText
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	public static ApiCommentResult parseMyCommentlist(String jsonText, String url) throws JSONException {
		JSONObject resultJson = new JSONObject(jsonText); 
		ApiCommentResult result = new ApiCommentResult();
		result.resultcode = resultJson.getJSONObject("result").getInt("resultcode");
		result.imei = JsonUtil.getString(resultJson, "imei", "");
		result.sv = JsonUtil.getString(resultJson, "sv", "");
		if (resultJson.has("page")) {
			JSONArray pageObj = resultJson.getJSONObject("page").getJSONArray("items");
			if (pageObj == null)
				return null;
			
			List<SoftlistInfo> list = new ArrayList<SoftlistInfo>();
			for (int i = 0; i < pageObj.length(); i ++) {
				JSONObject object = pageObj.getJSONObject(i);
				SoftlistInfo info = new SoftlistInfo();
				info.commentid = JsonUtil.getString(object, "commentid", "0");
				info.username = JsonUtil.getString(object, "softname", "");
				info.text = JsonUtil.getString(object, "text", "");
				info.time = JsonUtil.getString(object, "time", "");
				info.softgrade = JsonUtil.getInt(object, "grade", 0);
				list.add(info);
			}
			result.list = list;
		}
		return result;
	}
	
	/**
	 * 用户预注册 preregister
	 * @param jsonText
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	public static ApiPreRegisterResult parsePreRegister(String jsonText, String url) throws JSONException {
		ApiPreRegisterResult result = new ApiPreRegisterResult();
		JSONObject resultJson = new JSONObject(jsonText);
		result.resultcode = JsonUtil.getString(resultJson.getJSONObject("result"), "resultcode", "0");
		result.isok = JsonUtil.getInt(resultJson, "isok", 0);
		result.imei = JsonUtil.getString(resultJson, "imei", "");
		result.sessionid = JsonUtil.getString(resultJson, "sessionid", "0");
		result.userid = JsonUtil.getString(resultJson, "userid", "0");
		result.username = JsonUtil.getString(resultJson, "username", "");
		result.passwd = JsonUtil.getString(resultJson, "passwd", "");
		result.nickname = JsonUtil.getString(resultJson, "nickname", "");
		return result;
	}

	public static ApiInstalledRecoverSoftListResult parseInstalledRecoverSoftList(String xmlText,
			String url) throws JSONException {
		JSONObject resultJson = new JSONObject(xmlText); 
		ApiInstalledRecoverSoftListResult result = new ApiInstalledRecoverSoftListResult();
		result.resultcode = resultJson.getJSONObject("result").getString("resultcode");
		result.imei = JsonUtil.getString(resultJson, "imei", "");
		result.sv = JsonUtil.getString(resultJson, "sv", "");
		result.isok=JsonUtil.getInt(resultJson, "isok", 0);
		if (resultJson.has("softlist")) {
			JSONArray pageObj = resultJson.getJSONObject("softlist").getJSONArray("items");
			if (pageObj == null)
				return null;
			
			List<SoftlistInfo> list = new ArrayList<SoftlistInfo>();
			for (int i = 0; i < pageObj.length(); i ++) {
				JSONObject object = pageObj.getJSONObject(i);
				SoftlistInfo info = new SoftlistInfo();
				info.appid = JsonUtil.getString(object, "appid", "");
				info.softdesc = JsonUtil.getString(object, "softdesc", "");
				int price = JsonUtil.getInt(object, "softprice", 0);
				if (price != 0) {
					info.softprice = price/100;
				}
				info.softname = JsonUtil.getString(object, "softname", "");
				info.softgrade = JsonUtil.getInt(object, "softgrade", 0);
				info.downloadurl = JsonUtil.getString(object, "downloadurl", "");
				info.softicon = JsonUtil.getString(object, "softicon", "");
				info.softid = JsonUtil.getInt(object, "softid", 0);
				if (!url.contains(ApiUrl.resultsoftlist)) {
					info.softpoints = JsonUtil.getInt(object, "softpoints", 0);
					info.versioncode = JsonUtil.getInt(object, "versioncode", 0);
				}
				info.version = JsonUtil.getString(object, "version", "");
				info.softsize = JsonUtil.getInt(object, "softsize", 0);
				info.isfirst=JsonUtil.getString(object, "isfirst","");
				info.isexclusive=JsonUtil.getString(object, "isexclusive","");
				info.istop=JsonUtil.getString(object, "istop","");
				info.download = JsonUtil.getInt(object, "download", 0);
				list.add(info);
			}
			 result.softList = list;
		}
		return result;
	}
}
