package com.appdear.client.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appdear.client.service.Constants;
/**
 * ¹ã¸æ¹ö¶¯Í¼Æ¬
 * @author zxy
 *
 */
public class GalleryAdDb extends BaseDB {
	String TAG = GalleryAdDb.class.getName();
	public GalleryAdDb(Context context) {
		super(context);
	}
	
	public synchronized void add(String name,String url){
		SQLiteDatabase db = this.getDbHelper().getWritableDatabase();
		try{
			db.execSQL("insert into "+Constants.DB_TABLENAME_GALLERYAD+"(name,url)  values(?,?)  " +
					new String []{name,url});
			if (Constants.DEBUG)
				Log.i(TAG, "Insert data into "+Constants.DB_TABLENAME_GALLERYAD);
		}catch(SQLException ex){
			Log.e(TAG, "error while insert into table "+Constants.DB_TABLENAME_GALLERYAD+" exception :"+ex.getMessage(),ex);
		}finally{
			db.close();
		}
	}
	
	public void delete(String url){
		SQLiteDatabase db = this.getDbHelper().getWritableDatabase();
		try{
			db.execSQL("delete  from  "+Constants.DB_TABLENAME_GALLERYAD+"  where url = ?",new String []{url});
			if (Constants.DEBUG)
				Log.i(TAG, "delete data from  "+Constants.DB_TABLENAME_GALLERYAD);
		}catch(SQLException ex){
			if (Constants.DEBUG)
				Log.e(TAG,"error while delete from table "+Constants.DB_TABLENAME_GALLERYAD+" exception :"+ex.getMessage(),ex);
		}finally{
			db.close();
		}
	}
	
	public String  search(String url){
		SQLiteDatabase db = this.getDbHelper().getReadableDatabase();
		String url1 = "";
		try{
			Cursor cursor = db.rawQuery("select url from "+Constants.DB_TABLENAME_GALLERYAD+"where  url = ?",new String []{url});	
			while (cursor.moveToNext()){
				url1 = cursor.getString(0);
			}
			cursor.close();
		}catch(SQLException ex){
			if (Constants.DEBUG)
				Log.e(TAG,"error while query data from "+Constants.DB_TABLENAME_GALLERYAD+" exception :"+ex.getMessage(),ex);
		}finally{
			db.close();
		}
		return url1;
	}

}
