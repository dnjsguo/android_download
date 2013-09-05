package com.appdear.client.update;

import java.io.File;
/**
 * 文件下载工具类
 * @author jindan
 *
 */
public  class FileDownloadUtil extends HandlerDownloadFile{

	private static FileDownloadUtil filedownutil=new FileDownloadUtil();
	private FileDownloadUtil(){
		
	}
	public FileDownloadUtil getInstance(){
		if(filedownutil==null){
			filedownutil=new FileDownloadUtil();
		}
		return filedownutil;
	}
	/** 
	 * 
	 * @param url 网络资源地址
	 * @param updatefile 本机更新文件
	 * @return connection请求
	 */
	public boolean downloadFileForConnect(String url,File updatefile){
		return downloadFile(url,updatefile,CONNECTURLTYPE,-1,null);
	}
	/** 
	 * 
	 * @param url 网络资源地址
	 * @param updatefile 本机更新文件
	 * @return connection请求
	 */
	public boolean downloadFileForConnectHandler(String url,File updatefile,FileDownloadHandlerI handler){
		return downloadFile(url,updatefile,CONNECTURLTYPE,-1,handler);
	}
	/** 
	 * 
	 * @param url         网络资源地址
	 * @param updatefile  本机更新文件
	 * @param timeout     连接超时时间
	 * @return connection 请求
	 */
	public boolean downloadFileForConnect(String url,File updatefile,int timeout){
		return downloadFile(url,updatefile,CONNECTURLTYPE,timeout,null);
	}
	/** 
	 * 
	 * @param url         网络资源地址
	 * @param updatefile  本机更新文件
	 * @param timeout     连接超时时间
	 * @return connection 请求
	 */
	public boolean downloadFileForConnectHandler(String url,File updatefile,int timeout,FileDownloadHandlerI handler){
		return downloadFile(url,updatefile,CONNECTURLTYPE,timeout,handler);
	}
	/**
	 * 
	 * @param url 网络加载地址
	 * @param updatefile 本机更新文件
	 * @return   httpget请求
	 */
	public boolean downloadFileForHttpgetHandler(String url,File updatefile,FileDownloadHandlerI handler){
		return downloadFile(url,updatefile,HTTPGETURLTYPE,-1,handler);
	}
	/**
	 * 
	 * @param url 网络加载地址
	 * @param updatefile 本机更新文件
	 * @return   httpget请求
	 */
	public boolean downloadFileForHttpget(String url,File updatefile){
		return downloadFile(url,updatefile,HTTPGETURLTYPE,-1,null);
	}
	
//	@Override
	public boolean downloadFileForConnect(String url, String updatefile) {
		return downloadFileForConnect(url,updatefile,-1,CONNECTURLTYPE);
	}
//	@Override
	public boolean downloadFileForHttpget(String url, String updatefile) {
		// TODO Auto-generated method stub
		return downloadFileForConnect(url,updatefile,-1,HTTPGETURLTYPE);
	}
//	@Override
	public boolean downloadFileForConnect(String url, String updatefile,
			int timeout) {
		// TODO Auto-generated method stub
		return downloadFileForConnect(url,updatefile,timeout,CONNECTURLTYPE);
	}
	
	private boolean downloadFileForConnect(String url, String updatefile,
			int timeout,int connecttype) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		File file=new File(updatefile);
		timeout=timeout<0?10000:timeout;
		if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){
			if(file.exists()==false){
				File file1=new File(updatefile.substring(0,updatefile.lastIndexOf("/")));
				if(file1.exists()==false){
					if(file1.mkdirs()){
						if(CONNECTURLTYPE==connecttype){
							return downloadFileForConnect(url,file,timeout);
						}else if(HTTPGETURLTYPE==connecttype){
							return downloadFileForHttpget(url,file);
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					if(CONNECTURLTYPE==connecttype){
						return downloadFileForConnect(url,file,timeout);
					}else if(HTTPGETURLTYPE==connecttype){
						return downloadFileForHttpget(url,file);
					}else{
						return false;
					}
				}
			}else{
				if(CONNECTURLTYPE==connecttype){
					return downloadFileForConnect(url,file,timeout);
				}else if(HTTPGETURLTYPE==connecttype){
					return downloadFileForHttpget(url,file);
				}else{
					return false;
				}
			}
		}else{
			return false;
		}
	}
	@Override
	public void HandlerNocatifycation(long count,long total) {
		// TODO Auto-generated method stub
		/**
		 * 默认处理方式
		 */
	}
	@Override
	public void HandlerNocatifycationFail() {
		// TODO Auto-generated method stub
		/**
		 * 默认处理方式
		 */
	}
}
