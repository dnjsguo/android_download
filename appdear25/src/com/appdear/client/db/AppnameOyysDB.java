package com.appdear.client.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 文件下载DB
 * @author zqm
 *
 */
public class AppnameOyysDB extends BaseDB {

	public AppnameOyysDB(Context context) {
		super(context);
	}
	
	@SuppressWarnings("null")
	public List<String> read(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		Cursor cursor = null;
		try {
			List<String> dbinfolist = new ArrayList<String>();
			cursor= db.query("pname",new String[]{"pname"},
					null, null, null, null, null, null);
			if(cursor!=null&&cursor.getCount()>0){
				cursor.moveToFirst();
		    	do{
					String name = cursor.getString(0);
					dbinfolist.add(name);
					Log.i("", "name=db=" + name);
					cursor.moveToNext();
				}while(cursor.moveToNext());
				cursor.close();
				db.setTransactionSuccessful();
			}
			return dbinfolist;
		} finally {
			if(cursor==null)
				cursor.close();
			db.endTransaction();
			db.close();
		}
	}
	
	/**
	 * 保存下载信息
	 * @param bean
	 */
	public void save(String pname,String pid,SQLiteDatabase db) {
		
		db.beginTransaction();        
		try {
			db.execSQL(
					"insert into pname(pname) values(?)",                  
					new Object[]{pname});
			
			db.setTransactionSuccessful();   
		} finally {
			db.endTransaction();
		}        
	}
	public void delete(String pname,String pid) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();        
		try {
			db.execSQL(
					"delete from pname where pname=?",new Object[] {pname});
			db.setTransactionSuccessful();   
		} finally {
			db.endTransaction();
		}        
		db.close();
	}
}
