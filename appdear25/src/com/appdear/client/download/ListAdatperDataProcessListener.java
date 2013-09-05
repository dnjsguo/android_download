package com.appdear.client.download;

/**
 * list列表数据按键处理
 * 
 * @author zqm
 *
 */
public interface ListAdatperDataProcessListener {

	/**
	 * 按键事件处理
	 * @param object
	 * @param processTye
	 */
	public void keyPressProcess(Object object, int processTye);
	public void keyPressProcess(Object object, int processTye, int position);
}
