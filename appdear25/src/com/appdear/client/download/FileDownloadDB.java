package com.appdear.client.download;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.appdear.client.db.BaseDB;
import com.appdear.client.service.AppContext;
import com.appdear.client.utility.ServiceUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 文件下载DB
 * @author zqm
 *
 */
public class FileDownloadDB extends BaseDB {

	public FileDownloadDB(Context context) {
		super(context);
	}
	
	/**
	 * 读取文件下载信息
	 */
	public synchronized Hashtable<Integer,SiteInfoBean> read() {
		SQLiteDatabase db=null;
		Hashtable<Integer,SiteInfoBean> list = new Hashtable<Integer,SiteInfoBean>();
		/*
		 * 清除任务列表
		 */
		AppContext.getInstance().taskSoftList.clear();
		try {
		    db = dbHelper.getReadableDatabase();
			db.beginTransaction();
			Cursor cursor = db.query("filedownlog", new String[]{
					"downpath", "savepath", "icon", "savename", "softname", "appid",
					"filesize", "versionid", "downlength", "downstate", "downstateheader", "softid"},
					null, null, null, null, null, null);
			int numRows = cursor.getCount();
			cursor.moveToFirst();
			
			for (int i = 0; i < numRows; i ++) {
				SiteInfoBean bean = new SiteInfoBean();
				bean.sSiteURL = cursor.getString(0);
				bean.sFilePath = cursor.getString(1);
				bean.softIcon = cursor.getString(2);
				bean.sFileName = cursor.getString(3);
				bean.softName = cursor.getString(4);
				bean.appID = cursor.getString(5);
				bean.fileSize = cursor.getInt(6);
				bean.versionID = cursor.getString(7);
				bean.downloadLength = cursor.getInt(8);
				bean.state = cursor.getInt(9);
				bean.downloadstateHeader = cursor.getInt(10);
				bean.softID = cursor.getInt(11);
				
				if (!bean.sFilePath.equals("") && bean.sFilePath != null 
						&& !ServiceUtils.isHasFile(bean.sFilePath + "/" + bean.sFileName)
						&& bean.state == 2) {
					db.execSQL("delete from filedownlog where softid=?", 
							new Object[]{bean.softID});
				} else {
					list.put(bean.softID, bean);
					if(!AppContext.getInstance().taskSoftList.contains(String.valueOf(bean.softID))){
						AppContext.getInstance().taskSoftList.add(String.valueOf(bean.softID));
					}
				}
				//如果读取的时候下载的状态为下载中修改为暂停
				if (bean.state == 0 || bean.state == -1)
					bean.state = 1;
				cursor.moveToNext();
			}
			cursor.close();
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch(Exception e){
			
		}
		finally {	
			if(db!=null)
			db.close();
			return list;
		}
		
	}
	
	/**
	 * 保存下载信息
	 * @param bean
	 */
	public synchronized void save(SiteInfoBean bean) {
		SQLiteDatabase db=null;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction(); 
			db.execSQL(
					"insert into filedownlog(downpath, savepath, " +
					"icon, savename, softname, appid, filesize, versionid, downlength" +
					",downstate, downstateheader, softid) values(?,?,?,?,?,?,?,?,?,?,?,?)",                  
					new Object[] {bean.sSiteURL, bean.sFilePath,
							bean.softIcon, bean.sFileName, bean.softName,
							bean.appID, bean.fileSize, 
							bean.versionID, bean.downloadLength,
							bean.state, bean.downloadstateHeader, 
							bean.softID});
			db.setTransactionSuccessful();   
			if(db!=null)db.endTransaction();
		}catch(Exception e){
			//Log.i("info","error");
		}finally {
			if(db!=null)db.close();
		}        
		
	}
	
	/**
	 * 更新进度
	 * @param bean
	 */
	public synchronized void update(SiteInfoBean bean) {
		
		SQLiteDatabase db = null;
		      
		try {          
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();  
			db.execSQL(
					"update filedownlog set downlength=?, filesize=?, downstate=?, " +
					"downstateheader=? where softid=?",                  
					new Object[] {bean.downloadLength, bean.fileSize,
							bean.state, bean.downloadstateHeader, bean.softID});
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch(Exception e) {
			
		}finally {
			if(db!=null)
			db.close();
		}        
		
	}
	
	/**
	 * 删除记录
	 * @param bean
	 */
	public void delete(int softid) {
		SQLiteDatabase db=null;
		try {
			db= dbHelper.getWritableDatabase();
			db.beginTransaction();
			db.execSQL("delete from filedownlog where softid=?", 
					new Object[]{softid});
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch(Exception e) {
			
		} finally {
			if(db!=null)
			db.close();
		}
		
	}
	
	/**
	 * 设置所有下载状态为暂停
	 */
	public void updatePause() {
		if (AppContext.getInstance().taskList == null 
				|| AppContext.getInstance().taskList.size() == 0) {
			AppContext.getInstance().taskList = read();
		}
		for (SiteInfoBean bean : AppContext.getInstance().taskList.values()) {
			//非完成--2 ，非暂停--1， 非失败--3
			if (bean.state != 2 && bean.state != 1) {
				if (bean.state != 3)
					bean.state = 1;
				update(bean);
			}
		}
	}
}
