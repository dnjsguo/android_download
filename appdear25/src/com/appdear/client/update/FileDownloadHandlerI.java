package com.appdear.client.update;

import java.io.File;
/**
 * 处理下载通知接口
 * @author jindan
 *
 */
public interface FileDownloadHandlerI {
	/**
	 * 
	 * @param count 下载字节数
	 * @param total  总字节数
	 */
	public abstract void HandlerNocatifycation(long count,long total);
	/**
	 * 下载失败处理
	 */
	public abstract void HandlerNocatifycationFail();
}
