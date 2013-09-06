package com.appdear.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiResult;

public class InitModel  implements ApiResult, Serializable{
//	/**
//	 * 服务地址前缀1
//	 */
	public String spreurl = "http://a.appdear.com/ap/android/";
//	public String spreurl = "http://nta.appdear.com/ap/android/";
//	public String spreurl = "http://172.16.16.82:10010/ap/android/";
	
	/**
	 * 服务地址前缀0
	 */
	public String dpreurl = "http://sa.appdear.com/ap/android/";
//	public String dpreurl = "http://ntsa.appdear.com/ap/android/";
//	public String dpreurl = "http://172.16.16.82:10010/ap/android/";
	
	/**
	 * wapurl
	 */
	public String wapurl = "";
	
	/**
	 * 版块模板版本号
	 */
	public int sectionversion = 1;
	
	/**
	 * 是否需要升级爱皮市场客户端
	 * 0--否 1--是
	 */
	public String isupdate = "0";
	
	/**
	 * 爱皮市场升级地址
	 */
	public String updateurl = "";
	/**
	 * 
	 */
	public String softVersion;
	
	/**
	 * 测试链路url
	 */
	public String linkurl="http://content.appdear.com";
	
	/**
	 * 测试链路开关 1开启 0关闭
	 */
	public int linkflag=1;
	
	public int labelversionresponse=2;
	

	/**
	 * 升级信息
	 */
	public String softSize;

	public String softUpdateTip;
	
	public Long time;
	
	public static String[] dynamic={"a","b","c","d","e","f"};
	
	public int versioncode=Constants.VERSIONCODE;
	/**
	 * 频道ID
	 */
	public static List<CannelIDinfo> initlist = null;
	
	static{
		initlist=new ArrayList<CannelIDinfo>();
//		initlist.add(getInfo(106620,601));initlist.add(getInfo(106621,602));
//		initlist.add(getInfo(106622,603));initlist.add(getInfo(106616,402));
//		initlist.add(getInfo(106617,403));initlist.add(getInfo(106618,501));
//		initlist.add(getInfo(106619,0));initlist.add(getInfo(106612,203));
//		initlist.add(getInfo(106613,0));initlist.add(getInfo(106614,301));
//		initlist.add(getInfo(106615,401));initlist.add(getInfo(106608,107));
//		initlist.add(getInfo(106609,0));initlist.add(getInfo(106610,201));
//		initlist.add(getInfo(106611,202));initlist.add(getInfo(106605,104));
//		initlist.add(getInfo(106604,103));initlist.add(getInfo(106607,106));
//		initlist.add(getInfo(106606,105));initlist.add(getInfo(106601,0));
//		initlist.add(getInfo(106603,102));initlist.add(getInfo(106602,101));
		/**
		 * 线上初始化接口数据2.3
		 */
//		initlist.add(getInfo(106997,0));initlist.add(getInfo(106998,101));
//		initlist.add(getInfo(106999,102));initlist.add(getInfo(107000,103));
//		initlist.add(getInfo(107001,104));initlist.add(getInfo(107002,105));
//		initlist.add(getInfo(107003,106));initlist.add(getInfo(107004,107));
//		initlist.add(getInfo(107005,0));initlist.add(getInfo(107006,201));
//		initlist.add(getInfo(107007,202));initlist.add(getInfo(107008,203));
//		initlist.add(getInfo(107009,0));initlist.add(getInfo(107010,301));
//		initlist.add(getInfo(107011,401));initlist.add(getInfo(107012,402));
//		initlist.add(getInfo(107013,403));initlist.add(getInfo(107014,501));
//		initlist.add(getInfo(107015,0));initlist.add(getInfo(107016,601));
//		initlist.add(getInfo(107017,602));initlist.add(getInfo(107018,603));
//		initlist.add(getInfo(107041,200039));
//		initlist.add(getInfo(107342,101));
//		initlist.add(getInfo(107343,102));initlist.add(getInfo(107366,103));
//		initlist.add(getInfo(107367,104));initlist.add(getInfo(107368,105));
//		initlist.add(getInfo(107369,106));initlist.add(getInfo(107370,107));
//		initlist.add(getInfo(107372,201));
//		initlist.add(getInfo(107373,202));initlist.add(getInfo(107374,203));
//		initlist.add(getInfo(107376,301));
//		initlist.add(getInfo(107377,401));initlist.add(getInfo(107378,402));
//		initlist.add(getInfo(107379,403));initlist.add(getInfo(107380,501));
//		initlist.add(getInfo(107382,601));
//		initlist.add(getInfo(107383,602));initlist.add(getInfo(107384,603));
//		initlist.add(getInfo(107335,200039));
		
		/**
		 * 2.5
		 */
//		initlist.add(getInfo(108297,101));
//		initlist.add(getInfo(108298,102));initlist.add(getInfo(108321,103));
//		initlist.add(getInfo(108322,104));initlist.add(getInfo(108323,105));
//		initlist.add(getInfo(108324,106));initlist.add(getInfo(108325,107));
//		initlist.add(getInfo(108327,201));
//		initlist.add(getInfo(108328,202));initlist.add(getInfo(108329,203));
//		initlist.add(getInfo(108331,301));
//		initlist.add(getInfo(108332,401));initlist.add(getInfo(108333,402));
//		initlist.add(getInfo(108334,403));initlist.add(getInfo(108335,501));
//		initlist.add(getInfo(108337,601));
//		initlist.add(getInfo(108338,602));initlist.add(getInfo(108339,603));
//		initlist.add(getInfo(108290,200039));
		/**
		 * 2.6
		 */
		initlist.add(getInfo(113640,102));
		initlist.add(getInfo(113695,104));
		initlist.add(getInfo(113671,201));
		initlist.add(getInfo(113672,202));
		initlist.add(getInfo(113673,203));
		initlist.add(getInfo(113681,601));
		initlist.add(getInfo(113632,200039));
	}
	
	public static CannelIDinfo getInfo(int sectionid,int code){
		CannelIDinfo info=new CannelIDinfo();
		info.sectionid=sectionid;
		info.code=code;
		return info;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return spreurl+"||"+dpreurl+"||"+wapurl+"||"+sectionversion
		+"||"+isupdate+"||"+updateurl+"||"+softVersion+"||"+linkurl
		+"||"+linkflag+"||"+labelversionresponse+"||"+softSize
		+"||"+softUpdateTip+"||"+time+"||"+initlist;
	}
	
}
