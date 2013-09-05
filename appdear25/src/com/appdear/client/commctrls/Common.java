package com.appdear.client.commctrls;

import com.appdear.client.service.AppContext;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * 公用信息
 * @author jdan
 *
 */
public class Common {
	/**
	 * 点击更新爱皮应用下载
	 */
	public final static String UPDATE_APPDEAR_LOG = "update_appdear_log";
	
	//资源列表数量存储
	public final static String LISTVIEWSOURCE_XML="listview";
	//当前用户登录信息
	public final static String USERLOGIN_XML="userlogin";
	//当前163用户登录信息
	public final static String USERLOGIN_163_XML="userloginweibo";
	public final static String SEARCH_XML = "searchxml";
	
	//本机用户密码存储信息
	public final static String USERPASSWDXML="userpasswd";
	
	public final static String TYPES_XML="types";
	//功能模板序列
	public final static String SECTIONCODEXML = "sectioncode";
	
	//设置信息
	public final static String SETTINGS = "settings";
	public final static String UPDATE_VERSION = "updateversion";
	
	//参数显示需要传递的list信息
	public final static String SHOWTYPE="showtype";
	
	//point积分列表
	public final static int  POINT=1;
	
	//参数显示需要传递的list信息
	public final static int  ORDER=2;
	
	//参数显示需要传递的list信息
	public final static int  MESSAGE=3;
	//电话充值枚举
	public static enum PHONEACCOUNT{
		PHONE10(10),PHONE20(20),PHONE30(30),PHONE50(50),
		PHONE100(100),PHONE200(200),PHONE300(300),PHONE500(500);
		private final int value;
		private PHONEACCOUNT(int value){
			this.value=value;
		}
		public int getValue(){
			return value;
		}
	}
	
	/*
	 * 爱皮币支付方式
	 */
	public final static int APPAY=11;
	/*
	 * 余额支付方式
	 */
	public final static int ACCOUNTPAY=12;
	/*
	 * 手机支付方式
	 */
	public final static  int SMSPAY=13;
	/*
	 * 储值卡支付方式
	 */
	public final static  int CARDPAY=14;
	/*
	 * 储值卡支付方式
	 */
	public final static  int CARDPAYSECOND=15;
	/*
	 * 储值卡余额不足的状态
	 */
	public final static  int CARDPAYSECOND_ACCOUNT_NOTENOUGHFLAG=16;
	
	/*
	 * 储值卡余额充足的状态
	 */
	public final static  int CARDPAYSECOND_ACCOUNT_ENOUGHFLAG=7;

	/*
	 * 支付状态
	 */
	public final static String PAYFLAG="pay_flag";
	
	/*
	 * 支付状态
	 */
	public final static String PAYSOFT="pay_soft";
	/**
	 * 是否推出界面
	 */
	public final static int OFFVIEW=-1;
	/**
	 * 登录状态
	 */
	public final static String USERLOGINFLAG="userloginflag";
	
	/**
	 * 游戏卡类型
	 */
	public final static int GAMESHENGDA=1;
	/**
	 * 游戏卡类型
	 */
	public final static int GAMEZHENGTU=2;
	/**
	 * 游戏卡类型
	 */
	public final static int GAMEWANGYI=3;
	/**
	 * 游戏卡类型
	 */
	public final static int GAMEJUNWANG=4;
	/**
	 * 当前图片的类型
	 */
	public final static String CURRENTBG="currentpic";
	/**
	 * 设置背景结束状态
	 */
	public final static int ENDCURRENTBG=20;
	/**
	 * 主界面发送广播
	 */
	public final static String BACKGROUNDBFLAG="bgflag";
	/**
	 * 更新地址
	 */
	public final static String UPDATEURL="updateurl";
	
	/**
	 * 更新地址状态
	 */
	public final static int UPDATEURLFLAG=30;
	
//	public static String updateurl="";
	public static boolean isNeedUpdateClient = false;
	
	public static boolean autosearch=true;
	
	public static Drawable BGDRAWABLE;
	public static Bitmap bgbitmap;

	public static String DOWNLOAD_NOTIFY = "DOWNLOAD_NOTIFY";
	
	public static boolean ISLOADSOFTICON=SharedPreferencesControl.getInstance().getBoolean(
			"loadsofticon",com.appdear.client.commctrls.Common.SETTINGS, AppContext.getInstance().appContext);
	
    public static boolean LOADSNAPSHOT=SharedPreferencesControl.getInstance().getBoolean(
			"loadsnapshot",com.appdear.client.commctrls.Common.SETTINGS,AppContext.getInstance().appContext);	
    /*
	 * 下载精度间隔
	 */
	public final static int DOWNPROCESS=1;
	/*
	 * 下载精度间隔
	 */
	public final static int DOWNPROCESS1=2;
	/*
	 * 下载精度间隔
	 */
	public final static int DOWNPROCESS2=4;
	/*
	 * 下载精度间隔
	 */
	public final static int DOWNPROCESS3=6;
	
	/*
	 * 下载精度间隔
	 */
	public final static int DOWNPROCESS4=15;
}

