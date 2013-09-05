package com.appdear.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appdear.client.model.WeiBoUser;

/**
 * 微博账号相关，在用户第一次通过验证之后保存认证accessToken和accessSecret
 * @author zxy
 *
 */
public class WeiBoAccountDb extends BaseDB{

	String table = "weiboTb";
	public WeiBoAccountDb(Context context) {
		super(context);
		SQLiteDatabase db = getDbHelper().getWritableDatabase();
	}
	
	public int getUserCount(){
		//TODO:Read db to get user count .
		SQLiteDatabase db = getDbHelper().getWritableDatabase();
	 
		Cursor c = db.query(table, new String[]{"_id"}, "_id>0", null, null, null, null);
		return c.getCount();
	}
 
	public void insert(WeiBoUser u){
		//TODO:insert user info into db
		SQLiteDatabase db = getDbHelper().getWritableDatabase();
		ContentValues values  = new ContentValues();
		values.put("type", 0);
		values.put("uid",u.getUid());
		values.put("accessToken", u.getAccessToken());
		values.put("accessSecret",u.getAccessToken());
		db.insert(table, null, values);
	}
	
	public WeiBoUser getUserInfo(int id){
		//TODO:read user account info from db .
		WeiBoUser u = new WeiBoUser();
		SQLiteDatabase db = getDbHelper().getWritableDatabase();
		Cursor c = db.query(table, new String[]{"uid","type","accessToken","accessSecret“"}, 
				"uid=?", new String []{""+id}, null, null, null);
		u.setUid(c.getInt(c.getColumnIndex("uid")));
		u.setAccessToken(c.getString(c.getColumnIndex("accessToken")));
		u.setAccessSecret(c.getString(c.getColumnIndex("accessSecret")));
		
		return  u;
	}
}
