package com.appdear.client.service;

/**
 * 窗口的Tag
 * @author zqm
 *
 */
public interface SoftFormTags {

	/**
	 * flag name
	 */
	public static final String ACTIVITY_SWITCH_FLAG = "activityFlag";
	
	//窗口类型
	/**
	 * 主页的type
	 */
	public static final int MAIN_FORM = 1;
	
	/**
	 * 类别的type
	 */
	public static final int CATEGORY_FORM = 2;
	
	/**searchResultChildType
	 * 搜索的type
	 */
	public static final int SEARCH_FORM = 42;
	
	/**
	 * 更多主页
	 */
	public static final int MORE_ITEM = 518;
	
	//类别二级界面
	/**
	 * 搜索结果
	 */
	public static final int SEARCH_RESULT = 43;
	/**
	 * 软件类别-应用页面 视图标志
	 */
	public static  final int  CATEGORY_DETAIL = 201;

	/**
	 * 会员模块
	 */
	public static final int USER_MAIN_CENTER = 400;
	
	/**
	 * 会员主界面
	 */
	public static final int USER_LIST_CENTER = 401;

	/**
	 * 登录
	 */
	public static final int CHILD_USER_LOGIN = 405;
	
	/**
	 * 一键注册界面
	 */
	public static final int CHILD_USER_REGIST = 406;
	
	/**
	 * 管理界面
	 */
	public static final int CHILD_MORE_MANAGER = 501;
	
	
}
