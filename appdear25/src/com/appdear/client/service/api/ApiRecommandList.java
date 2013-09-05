package com.appdear.client.service.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.model.Recommentlist;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.StringHashMap;

public class ApiRecommandList {
	/**
	 * 返回数据
	 * {
    "result": {
        "resultcode": "000000"
    },
    "imei": "000000000000000",
    "sv": "1.0",
    "recommentlist": {
        "count": 6,
        "items": [
            {
                "recomname": "金山手机卫士",
                "recomid": 118006,
                "appid": "com.jxphone.mosecurity",
                "recomprice": 0,
                "recomicon": "/picture/icon/20110629/hiapk_868_icon.png",
                "recomgrade": 50
            },
            {
                "recomname": "我查查",
                "recomid": 113025,
                "appid": "com.wochacha",
                "recomprice": 0,
                "recomicon": "/picture/icon/20110629/hiapk_134583_icon.png",
                "recomgrade": 50
            },
            {
                "recomname": "水果忍者",
                "recomid": 103936,
                "appid": "com.ninja.studio.game.Kaka.pro",
                "recomprice": 0,
                "recomicon": "/picture/icon/20110629/hiapk_108294_icon.jpg",
                "recomgrade": 50
            },
            {
                "recomname": "德州扑克",
                "recomid": 100864,
                "appid": "com.gemego.playtexasfree",
                "recomprice": 0,
                "recomicon": "/picture/icon/20110629/hiapk_141199_icon.png",
                "recomgrade": 50
            },
            {
                "recomname": "全国影讯",
                "recomid": 118936,
                "appid": "com.mtime",
                "recomprice": 0,
                "recomicon": "/picture/icon/20110629/hiapk_133050_icon.png",
                "recomgrade": 50
            },
            {
                "recomname": "QQ微信",
                "recomid": 110544,
                "appid": "com.tencent.mm",
                "recomprice": 0,
                "recomicon": "/picture/icon/20110629/hiapk_139388_icon.png",
                "recomgrade": 50
            }
        ]
    }
}
	 */
	
	/**
	 * 
	 */
	public static ApiSoftRecommandListResult getRecommandList(String softid,String count){
		ApiSoftRecommandListResult  result =  null;
			String rerurnStr = null;
			try{
				result = new  ApiSoftRecommandListResult();
				rerurnStr = APIHelper.getURL(ApiUrl.recommentlist+"?softid="+softid+"&count="+count, 1);
				if (Constants.DEBUG) {
					Log.i("softList  u :", "rerurnStr:"+ApiUrl.recommentlist+"?softid="+softid+"&count="+count);
					Log.i("softList  rerurnStr :", "rerurnStr:"+rerurnStr);
				}
				JSONObject jsonobject = null;
				try {
					jsonobject = new JSONObject(rerurnStr);
				} catch (JSONException e6) {
					// TODO Auto-generated catch block
					e6.printStackTrace();
				}
				//JSONObject recommentjson = jsonobject.has("recommentlist")?jsonobject.get("recommentlist")!=null?jsonobject.getJSONObject("recommentlist"):null:null;
				if (jsonobject != null) {
					try {
						result.resultcode = jsonobject.getJSONObject("result").has("resultcode")?jsonobject.getJSONObject("result").getString("resultcode")!=null?jsonobject.getJSONObject("result").getString("resultcode"):"0":"0";
					} catch (JSONException e5) {
						e5.printStackTrace();
					}
					try {
						result.imei = jsonobject.getString("imei")==null? "":jsonobject.getString("imei");
					} catch (JSONException e4) {
						e4.printStackTrace();
					}
					try {
						result.sv = jsonobject.getString("sv")==null? "":jsonobject.getString("sv");
					} catch (JSONException e3) {
						e3.printStackTrace();
					}
					try {
						result.count = jsonobject.getJSONObject("recommentlist").getString("count");
					} catch (JSONException e2) {
						e2.printStackTrace();
					}
					JSONArray recommentitems = null;
					try {
						recommentitems = jsonobject.getJSONObject("recommentlist").getJSONArray("items");
								
					} catch (JSONException e2) {
						e2.printStackTrace();
					}
					
					ArrayList<Recommentlist> remcommentlist = new ArrayList<Recommentlist>();
					if (recommentitems != null) {
						for (int i = 0; i < recommentitems.length(); i ++){
							Recommentlist info = new Recommentlist();
							JSONObject object = null;
							
							try {
								object = recommentitems.getJSONObject(i);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							try {
								info.recomname = object.has("recomname")?object.get("recomname")!=null?object.getString("recomname"):"":"";
							} catch (JSONException e){
								e.printStackTrace();
							}
							
							try {
								info.recomid = object.has("recomid")?object.get("recomid")!=null?object.getString("recomid"):"0":"0";
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							try {
								info.recomicon = object.has("recomicon")?object.get("recomicon")!=null?object.getString("recomicon"):"":"";
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							try {
								info.recomappid = object.has("recomappid")?object.get("recomappid")!=null?object.getString("recomappid"):"":"";
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							
							try {
								info.recomprice= object.has("recomprice")?object.get("recomprice")!=null?object.getString("recomprice"):"0":"0";
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							try {
								info.recomgrade = object.has("recomgrade")?object.get("recomgrade")!=null?object.getInt("recomgrade"):0:0;
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							try {
							info.recomdesc = object.has("recomdesc")?object.get("recomdesc")!=null?object.getString("recomdesc"):"0":"0";
							} catch (JSONException e) {
							e.printStackTrace();
							}
							
							remcommentlist.add(info);
						}
					}
					result.recommentlist = remcommentlist;
					if (Constants.DEBUG)
						Log.i("ApiSoftRecommandListResult", "ApiSoftRecommandListResult size :"+result.recommentlist.size());
				}
		} catch (ServerException e) {
			e.printStackTrace();
		}
		if (Constants.DEBUG)
			Log.i("softList  rerurnStr :", "rerurnStr  recommentlist size :"+result.recommentlist);
		return result;
	} 
}
