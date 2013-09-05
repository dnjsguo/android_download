package com.appdear.client.download;

/**
 * 下载监听
 * @author zqm
 *
 */
public interface DownloadListener {
    
	/**
     * 下载进度条更新
     * @param index
     * @param threadID
     * @param downloadsize
     * @param filesize
     */
	public void updateProcess(Object object);
	
	/**
	 * 异常
	 * @param e
	 * @param msg
	 */
	public void updateProcess(Exception e, String msg, Object object);
	
	/**
	 * 下载完成
	 */
	public void updateFinish(Object object);
}
