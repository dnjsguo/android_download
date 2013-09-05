/**
 * RefreshListData.java
 * created at:2011-5-11下午02:08:36
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client;
  
/** 
 * 异步刷新数据
 * 
 * @author zqm
 */
public interface RefreshDataListener {

	/**
	 * 设置刷新状态
	 * @param state
	 */
	public void refreshState(int state);
	
	/**
	 * 刷新数据
	 */
	public void refreshData();
	
	/**
	 * 刷新界面（为了显示图片）
	 */
	public void refreshUI(int position) ;
	
	/**
	 * 刷新界面（为了显示图片）
	 */
	public void refreshUI(int first,int last) ;
	
	/**
	 * 弹出最后一页的提示
	 */
	public void showendalert();
}

 