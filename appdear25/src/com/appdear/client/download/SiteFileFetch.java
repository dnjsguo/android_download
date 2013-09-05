/**
 * SiteFileFetch.java
 * created at:2011-5-10下午02:18:51
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.download;

import java.io.*;
import java.net.*;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.appdear.client.commctrls.Common;
import com.appdear.client.exception.ApiException;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.Utils;

import dalvik.system.TemporaryDirectory;

/** 
 * 整个文件的管理和线程的控制
 * 
 * @author zqm
 */
public class SiteFileFetch extends Thread {
	private static final int aglintime=400;
	/**
	 * 文件信息
	 */
	private SiteInfoBean siteInfoBean = null;
	
	/**
	 * 文件长度
	 */
	private long nFileLength = -1;
	
	/**
	 * 已下载的文件长度
	 */
	private long downloadFileLength;
	
	/**
	 * 是否第一次取文件
	 */
	private boolean bFirst = true;
	
	/**
	 * 文件下载的临时信息
	 */
	private File tmpFile;
	
	/**
	 * 文件已下载的大小
	 */
	private File sizeFile;
	
	/**
	 * 输出到文件的输出流
	 */
	private DataOutputStream output;
	
	/**
	 * 文件是否下载完成
	 */
	public boolean breakWhile = false;
	
	/**
	 * Stop identical
	 */
	public boolean bStop = false;
	
	public boolean fileEquesStop = false;
	/**
	 * File Access interface
	 */
	private FileAccess fileAccess = null;
	
	private final Handler handler = new Handler();
	

	private final int maxcount=3;
	private int count = 0;
	
	private int downprocess=Common.DOWNPROCESS;
	
	/**
	 * 文件管理
	 * @param bean
	 * @throws IOException
	 * @throws ApiException 
	 */
	public SiteFileFetch(SiteInfoBean bean) {
		siteInfoBean = bean;
		siteInfoBean.siteFileFecth = this;
		
		//内存中创建文件的方式
		if (bean.place == 1) {
			try {
				MyApplication.getInstance().openFileOutput(
						bean.sFileName,
						Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE);
				MyApplication.getInstance().openFileOutput(
						bean.sFileName + ".size", 
						Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		tmpFile = new File(bean.sFilePath + File.separator + bean.sFileName);
		sizeFile = new File(bean.sFilePath + File.separator + bean.sFileName + ".size");
		
		if (tmpFile.exists()) {
			 
			bFirst = false;
			downloadFileLength = tmpFile.length();
			siteInfoBean.downloadLength = (int) downloadFileLength;
			String path=bean.sFilePath + File.separator + bean.sFileName;
			try {
 			//	Log.i("info888", tmpFile.length()+"="+ServiceUtils.read_size_download(bean.sFilePath + File.separator + bean.sFileName));
				int fiszie=ServiceUtils.read_size_download(bean.sFilePath + File.separator + bean.sFileName);
				if(fiszie>0&&tmpFile.length()>=fiszie)
				{
					bean.state=2;
					MyApplication.getInstance().getSoftMap().put(bean.softID,2);
					AppContext.getInstance().downloader.downDb.save(bean);
					AppContext.getInstance().taskList.put(bean.softID,bean);
					if(!AppContext.getInstance().taskSoftList.contains(String.valueOf(bean.softID))){
						AppContext.getInstance().taskSoftList.add(String.valueOf(bean.softID));
					}
					Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
					intent.putExtra("softid", bean.softID);
					intent.putExtra("downloadfinsh",1);
					AppContext.getInstance().appContext.sendBroadcast(intent);
					fileEquesStop=true;
					return;
				}
			} catch (ApiException e) {
				e.printStackTrace();
			}
		} else {
			try {
				tmpFile.createNewFile();
				sizeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			downloadFileLength = 0;
			siteInfoBean.downloadLength = (int) downloadFileLength;
		}
	}
	
	/**
	 * download文件处理
	 */
	public void run() {
		//获得文件长度
		//分割文件
		//实例FileSplitterFetch
		//启动FileSplitterFetch线程
		//等待子线程返回
		if (bStop) {
			try {
				this.stop();
			} catch (Throwable e){
			} finally {
				return;	
			}
		}
		while(count<maxcount){
			InputStream input=null;
	//		long totaltime=0;
	//		long time=0;
			try {
				if (Constants.DEBUG)
					Log.d("", "downloadurl=" + siteInfoBean.sSiteURL);
				if (bFirst) {
					nFileLength = getFileSize();
					bFirst=!write_size_download();
				 
				} else {
					//文件大小
					if (nFileLength == -1 || nFileLength == 0) {
						nFileLength = getFileSize();
					}
					if (downloadFileLength == nFileLength) {
						append(0, siteInfoBean.softID,true);
						return;
					}
				}
				if (nFileLength != -1 && nFileLength != 0 && nFileLength != -2){
					siteInfoBean.fileSize = (int)nFileLength;
				}
				else{
					errPrint(null, "下载失败,点击查看");
					return;
				}
				
				if(siteInfoBean.state==1)
				{
					return;
				}
				//刷新界面
				//设置下载状态为开始下载
				downprocess=handlerpro(siteInfoBean.fileSize);
				siteInfoBean.state = 0;
				if (siteInfoBean.listener == null) {
					siteInfoBean.listener = AppContext.getInstance().downloadlistener;
				}
				/*if(siteInfoBean.downloadLength>=siteInfoBean.fileSize){
					siteInfoBean.state = 2;
					this.stop();
					return;
				}*/
				if(siteInfoBean.downloadLength>10){
					siteInfoBean.downloadLength=siteInfoBean.downloadLength-10;
				}
				fileAccess = new FileAccess(siteInfoBean.sFilePath
						+ File.separator + siteInfoBean.sFileName, siteInfoBean.downloadLength);
				URL url = new URL(siteInfoBean.sSiteURL);
				HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
				httpConnection.setRequestProperty("User-Agent", "NetFox");
				
				//爱皮应用下载的header
				if (Constants.VERSION != null && !Constants.VERSION.equals(""))
					httpConnection.setRequestProperty("Version", Constants.VERSION);
				if (Constants.USER_AGENT != null && !Constants.USER_AGENT.equals(""))
					httpConnection.setRequestProperty("User-Agent", Constants.USER_AGENT);
				if (AppContext.getInstance().IMEI != null && !AppContext.getInstance().IMEI.equals(""))
					httpConnection.setRequestProperty("Imei", AppContext.getInstance().IMEI);
				if (AppContext.getInstance().MAC != null && !AppContext.getInstance().MAC.equals(""))
					httpConnection.setRequestProperty("Mac", AppContext.getInstance().MAC);
				if (Constants.PLATFORM != null && !Constants.PLATFORM.equals(""))
					httpConnection.setRequestProperty("Platform", Constants.PLATFORM);
				if (Constants.OPERATION != null && !Constants.OPERATION.equals(""))
					httpConnection.setRequestProperty("Operation", Constants.OPERATION);
				if (Constants.DEVICEID != null && !Constants.DEVICEID.equals(""))
					httpConnection.setRequestProperty("Deviceid", Constants.DEVICEID);
				if (Constants.AUTHID != null && !Constants.AUTHID.equals(""))
					httpConnection.setRequestProperty("Authid", Constants.AUTHID);
				httpConnection.setRequestProperty("Timestamp",System.currentTimeMillis()+"");
				httpConnection.setRequestProperty("Network",AppContext.getInstance().network+"");
				httpConnection.setRequestProperty("Nettype", AppContext.getInstance().nettype+"");
				httpConnection.setRequestProperty("Opname", AppContext.getInstance().simname+"");
		//		httpConnection.setRequestProperty("modelarea",MyApplication.getInstance().modelCompany);
				httpConnection.setRequestProperty("Downloadstate", siteInfoBean.downloadstateHeader+"");
				if (!Constants.CANNEL_CODE.equals("") 
						&& Constants.CANNEL_CODE != null)
					httpConnection.addRequestProperty("Channelcode", Constants.CANNEL_CODE);
			
				String sProperty = "bytes=" + siteInfoBean.downloadLength + "-" + siteInfoBean.fileSize;
				httpConnection.setRequestProperty("RANGE", sProperty);
				httpConnection.setConnectTimeout(20 * 1000);
				httpConnection.setReadTimeout(20*1000);
				Utils.log(sProperty);
				input = httpConnection.getInputStream();     
				siteInfoBean.setFlagProcess(1);
				byte[] b = new byte[1024];
				int nRead;
	//			time=System.currentTimeMillis();
				count=0;
				Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
				long time=System.currentTimeMillis();
				while ((nRead = input.read(b, 0, 1024)) > 0) {
					if (bStop) {
						try {
							if(input!=null)input.close();
							this.stop();
						} catch (Throwable e){
						} finally {
							return;	
						}
					}
					 
					fileAccess.write(b, 0, nRead); 
					long t=System.currentTimeMillis();
					if(t-time>aglintime){
						append(nRead, siteInfoBean.softID,true);
						time=t;
					}else {
						append(nRead, siteInfoBean.softID,false);
					}
				}
				if(input!=null)input.close();
				if(siteInfoBean.fileSize>siteInfoBean.downloadLength){
					count++;
					if(count>=maxcount){
						errPrint(null, "下载失败,点击查看");
					}else{
						continue;
					}
				}
					siteInfoBean.state = 2;
					siteInfoBean.siteFileFecth = null;
					AppContext.getInstance().downloader.downDb.update(siteInfoBean);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
							intent.putExtra("softid", siteInfoBean.softID);
							intent.putExtra("dprocess",100f);
					//		Log.i("infodown", siteInfoBean.softName+"="+siteInfoBean.getProgress()+"="+siteInfoBean.flagprocess);
							MyApplication.getInstance().sendBroadcast(intent);
							if (siteInfoBean != null && siteInfoBean.listener != null)
								siteInfoBean.listener.updateFinish(siteInfoBean);
							if (siteInfoBean!= null && siteInfoBean.notification != null)
								siteInfoBean.notification.updateFinish(siteInfoBean);
						}
					});
				break;
			}catch (IOException e) {
				
				if(tmpFile==null||tmpFile.exists()==false){
					errPrint(e, "下载失败,点击查看");
					return;
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				count++;
				
	//			time=System.currentTimeMillis()-time;
	//			Log.i("timeinfo","IOException=time="+time+"=e="+e.getMessage());
				if(input!=null)
					try {
						input.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		//		e.printStackTrace();
				if(count>=maxcount)
					errPrint(e, checkSpace());
			} catch(Exception e) {
	//			time=System.currentTimeMillis()-time;
	//			Log.i("timeinfo","Exception=time="+time+"=e="+e.getMessage());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				count++;
				if(input!=null)
					try {
						input.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			//	e.printStackTrace();
				if(count>=maxcount)
					errPrint(e, "下载失败,点击查看");
			}finally{
	//			Log.i("timeinfo","totaltime="+totaltime);
			}
		}
	}
	
	
	/**
	 * 保存已下载文件大小
	 * @throws ApiException 
	 */
	private boolean write_size_download() {
		try {
			output = new DataOutputStream(new FileOutputStream(sizeFile));
			output.writeInt((int)nFileLength);
			output.close();
			return true;
		} catch (FileNotFoundException e) {
			 
			e.printStackTrace();
			if(count>=maxcount)
				errPrint(e, "下载失败,点击查看");
			
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			if(count>=maxcount)
				errPrint(e, checkSpace());
			
			return false;
		}
	}
	
	/**
	 * 获得文件长度
	 * @return
	 * @throws ApiException 
	 */
	public synchronized long getFileSize() {
		if (Constants.DEBUG)
			Log.i("下载", "getFileSize");
		int nFileLength = -1;
		try {
			URL url = new URL(siteInfoBean.sSiteURL);
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestProperty("User-Agent","NetFox");
			httpConnection.setConnectTimeout(20 * 1000);
			int responseCode=httpConnection.getResponseCode();
			if (responseCode >= 400) {
				processErrorCode(responseCode);
				return -2;//-2 represent access is error
			}
			String sHeader;
			for ( int i = 1; ; i ++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.toLowerCase().equals("content-length")) {
						nFileLength = Integer.parseInt(httpConnection.getHeaderField(sHeader));
						break;
					}
				}
				else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(count>=maxcount)
				errPrint(e, "下载失败,点击查看");
		} catch (Exception e) {
			e.printStackTrace();
			if(count>=maxcount)
				errPrint(e, "下载失败,点击查看");
		}
		Utils.log(nFileLength);
		return nFileLength;
	}
	
	/**
	 * 错误信息打印
	 * @param nErrorCode
	 */
	private void processErrorCode(int nErrorCode) {
		System.err.println("Error Code : " + nErrorCode);
	}
	
	/**
	 * 停止文件下载
	 */
	public void siteStop() {
		splitterStop();
	}
	
	/**
	 * 是否存在下载线程
	 * @return
	 */
	public boolean siteRestart() {
		return false;
	}
	
	public void splitterStop() {
		bStop = true;
	}
	
	/**     
	 * 累计已下载大小
	 * @param size    
	 * @throws ApiException 
	 */
	protected synchronized void append(int size, int softid,boolean issend) {
		downloadFileLength += size;
		siteInfoBean.downloadLength = (int)downloadFileLength;

		if(issend&&handlerProcess(siteInfoBean)){
			Intent intent = new Intent(Common.DOWNLOAD_NOTIFY);
			intent.putExtra("softid", siteInfoBean.softID);
			intent.putExtra("dprocess",((float)siteInfoBean.getProgress())/10);
	//		Log.i("infodown", siteInfoBean.softName+"="+siteInfoBean.getProgress()+"="+siteInfoBean.flagprocess);
			MyApplication.getInstance().sendBroadcast(intent);
		}	
		//更新notification的进度
		if (siteInfoBean.notification != null) {
			siteInfoBean.notification.updateProcess(
					siteInfoBean);
		}
		//更新下载界面的进度条
		if (siteInfoBean.listener != null) {
			if(siteInfoBean.listener!=AppContext.getInstance().downloadlistener){
				siteInfoBean.listener=AppContext.getInstance().downloadlistener;
			}
			siteInfoBean.listener.updateProcess(siteInfoBean);
		}
	}
	
	/**
	 * 获取文件大小
	 * @return
	 * @throws ApiException 
	 */
	public long getnFileLength() throws ApiException {
		return getFileSize();
	}
	
	/**
	 * 获取文件信息
	 * @return
	 */
	public SiteInfoBean getSiteInfoBean() {
		return siteInfoBean;
	}
	
	/**
	 * 异常提示
	 * @param e
	 * @param msg
	 */
	public void errPrint(Exception e, String msg) {
		if (siteInfoBean.listener != null) {
			siteInfoBean.listener.updateProcess(e, msg, siteInfoBean);
		}
		if (siteInfoBean.notification != null) {
			siteInfoBean.notification.updateProcess(e, msg, siteInfoBean);
		}
	}
	
	/**
	 * 判断内存空间
	 * @return
	 */
	public String checkSpace() {
		String msg = "下载失败";
		if (siteInfoBean.place == 0 && ServiceUtils.readSdCardAvailableSpace() <= 0) {
			msg = "下载失败，SD卡存储空间已满";
		} else if (siteInfoBean.place == 1 
				&& ServiceUtils.readDeviceAvailableInternalSpace() <= 0) {
			msg = "下载失败，手机存储空间已满";
		}
		return msg;
	}
	private boolean handlerProcess(SiteInfoBean bean){
		int progress=bean.getProgress();
		if(bean.fileSize<1024*1000){
			return true;
		}else{
			if(progress>=bean.flagprocess){
				bean.flagprocess=progress;
				bean.flagprocess++;
				return true;
			}else{
				return false;
			}
		}	
	}
	
	public int handlerpro(int filesize){
		if(filesize>10485760*2){
			return Common.DOWNPROCESS;
		}else if(filesize>10485760){
			return Common.DOWNPROCESS1;
		}else if(filesize>10485760/2){
			return Common.DOWNPROCESS2;
		}else if(filesize>1048576){
			return Common.DOWNPROCESS3;
		}else{
			return Common.DOWNPROCESS4;
		}
	}
}