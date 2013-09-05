/**
 * Constants.java
 * created at:2011-5-9下午04:12:35
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.service;

/** 
 * 系统信息
 * 
 * @author zqm
 */
public interface Constants {

	/**
	 * andorid业务线ID
	 */
	public static final String BID = "100003";
	
	/**
	 * header--爱皮版本信息
	 */
	public static final String VERSION = "2.6";
	
	/**
	 * 爱皮的vesrioncode
	 */
	public static final int VERSIONCODE = 18;
	
	/**
	 * header--User agent
	 */
	public static final String USER_AGENT = "Aipi-apk";
	
	/**
	 * header--平台
	 */
	public static final String PLATFORM = "3";
	
	/**
	 * header--授权码
	 */
	public static final String AUTHID = "123456";
	
	/**
	 * header--设备操作系统
	 */
	public static final String OPERATION = android.os.Build.VERSION.RELEASE;
	
	/**
	 * header--手机型号
	 */
	public static final String DEVICEID = android.os.Build.MODEL;
	
	/**
	 * DB版本
	 */
	public static final int DB_VERSION = 21;
	
	/**
	 * DBname
	 */
	public static final String DB_FILENAME = "aipi.db";
	
	/**
	 * 广告数据表名称
	 */
	public static final String DB_TABLENAME_GALLERYAD = "galleryAd";
	
	/**
	 * 图片缓存目录(上级目录/sdcard/appdear)
	 */
	public static final String CACHE_IMAGE_DIR = "/appdear/img";
	
	/**
	 * 列表资源缓存目录(上级目录/sdcard/appdear)
	 */
	public static final String CACHE_SOURCE_DIR = "/appdear/source";
	
	/**
	 * APK存储位置
	 */
	public static final String APK_DATA = "/appdear/apk";
	
	/**
	 * 程序数据保存文件夹
	 */
	public static final String CACHE_DATA_DIR = "/appdear";
	
	/**
	 * 手机内存目录
	 */
	public static final String DATA_APK = "/data/data/com.appdear.client/files";
	
	/**
	 * 图片缓存数据库名称
	 */
	public static  final String CACHE_IMAGE_TABLE_NAME = "imageCache";
	
	/**
	 * 超时时间
	 */
	public static final int CONNECTION_TIME_OUT = 15000;
	
	/**
	 * API 口令
	 */
	public static final String SINA_CONSUMER_KEY = "1570233751";
	
	/**
	 * API 密钥
	 */
	public static String SINA_CONSUMER_SECRET = "09c09e350c54929d5e7ae17e729af074";
	
	/**
	 * android业务线--3//真数据时删掉
	 */
	public static String BUSINESSLINE = "3";
	
	/**
	 * DEBUG模式
	 */
	public static boolean DEBUG = true;
	
	/**
	 * 是否可以设置服务器地址
	 */
	public static boolean SETTING_HOST_DEBUG = false;
	
	/**
	 * 软件详情页面的请求url（Wap协议）
	 */
	/*public static final String SOFT_WAP_URL = AppContext.getInstance().wap_url
		+ "wap/wap/softinfo.jsp?softid=";*/
	public static final String SOFT_WAP_URL = "wap/s.jsp?sid=";
	
	/**
	 * 短信分享的内容模板
	 */
	//public static final String SMS_CONTENT_A = "我在爱皮应用下载里发现了一款好玩的软件";
	//public static final String SMS_CONTENT_B = "，推荐给你玩一下啊。应用详情页面链接:";
	public static final String SMS_CONTENT_A = "推荐你玩爱皮应用下载里的";
	
	/**
	 * 渠道号
	 */
	public static  String CANNEL_CODE = Channel.CANNEL_CODE;
	
	/**
	 * 详情页固定参数 
	 */
	public static final String SOFTPARAM = "sdetail";
	/**
	 * 升级页固定参数 
	 */
	public static final String UPDATEPARAM = "supdate";
	
	public static final int LABELVERSIONFORCLIENT = 1;
	
	//public static final String[] dynamic={"a","b","c","d","e","f"};
	/**
	 * 万花筒-职业（默认数据）
	 */
	public static final String[][] PROFESSION_ID_NAME_ARRAY = {
		{"学生", "100921"},     {"教师", "100922"},
		{"高级管理 ", "100917"},  {"市场拓展", "100925"},
		{"营销管理 ", "100926"},  {"IT技术",  "100923"}, {"工人", "100929"},
		{"企事业职员", "100918"}, {"科研人员", "100920"},
		{"警察 ", "100931"},     {"军人", "100933"},
		{"保安", "100932"},      {"文秘", "100927"},   {"农民", "100928"},
		{"法律", "100938"},      {"体育", "100939"},   {"金融", "100940"}, {"保险", "100941"},
		{"餐饮服务", "100936"},   {"新闻出版", "100942"},
		{"演员", "100934"},      {"歌手艺人", "100935"},
		{"作家", "100944"},      {"翻译", "100945"},
		{"旅游导购", "100930"},   {"运输服务", "100937"},
		{"工程技术", "100924"},   {"医疗", "100919"},    {"邮政快递", "100943"}};
	
	/**
	 * 万花筒-使用场景（默认数据）
	 */
	public static final String[][] USAGESCENARIO_ID_NAME_ARRAY = {
		{"学习", "100894"},        {"开会", "100895"},
		{"等待某人或某事 ", "100898"}, {"玩手机", "100913"}, 
		{"上厕所 ", "100905"},       {"开心",  "100900"},  {"伤心", "100901"},
		{"读书看报", "100908"},      {"看电视", "100907"},
		{"坐车 ", "100896"},         {"无聊", "100899"},
		{"看电影", "100906"},        {"在被窝", "100909"}, {"发短信", "100912"},
		{"开车", "100897"},         {" 吃饭", "100902"},  {"逛街", "100904"}, {"起床", "100910"},
		{"做家务", "100903"},        {"打电话", "100911"},
		{"在医院", "100915"},        {"摄影", "100914"},
		{"写文档", "100916"}};
	
	/**
	 * 万花筒-使用人群（默认数据）
	 * "时尚达人"用"时尚前卫"的id、"白领一族"用"白领"的id
	 */
	public static final String[][] TARGETUSERS_ID_NAME_ARRAY = {
		{"时尚达人", "100103", "http://content.appdear.com/picture/icon_label/20120228/appdear_74367_label.png"}, 
		{"白领一族", "100104", "http://content.appdear.com/picture/icon_label/20120228/appdear_76523_label.png"}, 
		{"宅男宅女 ", "100089", "http://content.appdear.com/picture/icon_label/20120228/appdear_41973_label.png"},
		{"商务人士", "100090", "http://content.appdear.com/picture/icon_label/20120228/appdear_75427_label.png"},
		
		{"爱阅读", "100101", "http://content.appdear.com/picture/icon_label/20120228/appdear_94723_label.png"},  
		{"爱摄影",  "100095", "http://content.appdear.com/picture/icon_label/20120228/appdear_79312_label.png"},  
		{"爱电影", "100096", "http://content.appdear.com/picture/icon_label/20120228/appdear_66018_label.png"}, 
		{"爱旅游", "100098", "http://content.appdear.com/picture/icon_label/20120228/appdear_31590_label.png"},
		
		{"在校学生 ", "100091", "http://content.appdear.com/picture/icon_label/20120228/appdear_23427_label.png"}, 
		{"游戏控 ", "100092", "http://content.appdear.com/picture/icon_label/20120228/appdear_12479_label.png"},  
		{"小资", "100094", "http://content.appdear.com/picture/icon_label/20120228/appdear_66342_label.png"},   
		{"体育", "100093", "http://content.appdear.com/picture/icon_label/20120228/appdear_90068_label.png"}, 
		
		{"汽车族", "100097", "http://content.appdear.com/picture/icon_label/20120228/appdear_94283_label.png"},  
		{"健康", "100099", "http://content.appdear.com/picture/icon_label/20120228/appdear_53308_label.png"},    
		{"学外语", "100100", "http://content.appdear.com/picture/icon_label/20120228/appdear_99382_label.png"},  
		{"其他", "100102", "http://content.appdear.com/picture/icon_label/20120228/appdear_98963_label.png"}};

	/**
	 * 进度随机提醒信息
	 */
	public static final String[] PROGRESS_MSG = {"点击软件图标，弹出快捷菜单",
		"左右滑动可快速切换界面",
		"软件升级提示可以忽略",
		"管理中可将应用在手机内存和SD卡中自由移动",
		"标签中可根据适用人群、使用场景和职业来查找应用",
		"首页软件列表动态变化，让您看到更多精彩的软件",
		"通讯录备份还原功能让您使用手机更安全，更放心"
    };

	/**
		 * 首页或者其他也日志标记 
		 */
	public static String[][] flagLog = {{"cid-","labelcatid-","modelarea-"},{"catalogid-","catalogid-","catalogid-"},{"search-1"},{"ad-1"},{"more-1"}};
	
	public static String[] PHONETAG={"手机型号：","IMEI：","设备序列号：","购机时间：","保修期：","保修范围：","你的手机保修信息：","客服电话:"};
	
}

 