/**
 * ImageDB.java
 * created at:2011-5-11下午04:15:03
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.db;

import com.appdear.client.service.Constants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
  
/** 
 * 图片存储
 * 
 * @author zqm 
 */
public class ImageDB extends BaseDB {
	
	public ImageDB(Context context) {
		super(context);
	}
	
	/**
	 * 添加图片
	 * @param url
	 * @param path
	 */
	public synchronized void add(String url, String path) {
		SQLiteDatabase db = getDbHelper().getWritableDatabase();
		try {
			db.execSQL("insert into "+Constants.CACHE_IMAGE_TABLE_NAME+"(imgurl, imgpath) values(?, ? )", new String[]{url, path});
		} catch(Exception e) {
		} finally {
           db.close();
		}
	}

	/**
	 * 删除图片
	 * 
	 * @param imgurl
	 */
	public void delete(String imgUrl) {
		SQLiteDatabase db = getDbHelper().getWritableDatabase();
		try {
			db.execSQL("delete from "+Constants.CACHE_IMAGE_TABLE_NAME+" where imgurl=?", new String[]{imgUrl});
		} finally {
			db.close();
		}
	}
	
	/**
	 * 搜索
	 * @param imgUrl
	 * @return
	 */
	public synchronized String search(String imgUrl) {
		SQLiteDatabase db =null;
		try {
		  db = getDbHelper().getReadableDatabase();
		String imgpath="";
		
			Cursor cursor = db.rawQuery("select imgpath from "+Constants.CACHE_IMAGE_TABLE_NAME+" where imgurl = ?", new String[]{imgUrl});	
			while (cursor.moveToNext()){
				imgpath = cursor.getString(0);
			}
			cursor.close();
			return imgpath;
		} catch (Exception  e) {
			db =null;
			 return "";
		}finally {
			if(db!=null)
			db.close();
		}
	}
}

 