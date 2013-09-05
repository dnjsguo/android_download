/**
 * MyDBOpenHelper.java
 * created at:2011-5-10上午11:12:55
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.db;

import com.appdear.client.service.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
  
/** 
 * 数据库管理
 * 
 * @author zqm 
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {
	
	public MyDBOpenHelper(Context context) {
		super(context, Constants.DB_FILENAME, null, Constants.DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		//		String sql = "create table if not exists " +Constants.DB_TABLENAME_GALLERYAD+
		//		" (_id int primary key,name varchar ,url varchar)";
		String sql = "create table if not exists " +Constants.CACHE_IMAGE_TABLE_NAME+
		"(_id int primary key,imgurl varchar ,imgpath varchar)";
		
		db.execSQL(sql);
		
		if (Constants.DEBUG)
			Log.i(MyDBOpenHelper.class.toString(), "create table filedownlog");
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog " +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"downpath VARCHAR(100), " +
				"savepath VARCHAR(100), " +
				"icon VARCHAR(100)," +
				"savename VARCHAR(100), " +
				"softname VARCHAR(100), " +
				"appid VARCHAR(100), " +
				"filesize INTEGER, " +
				"versionid VARCHAR(100), " +
				"downlength INTEGER, " +
				"downstate INTEGER," +
				"downstateheader INTEGER," +
				"softid INTEGER)");
		//downstateheader 1 新下载， 2 断点续传, 3 重新下载
		//downstate 0 -- 未下载完成  1-- 手动暂停 2 -- 下载完成
		
		//软件详情信息
		db.execSQL("CREATE TABLE IF NOT EXISTS softwaredetail " +
				"(softid bigint(20) PRIMARY KEY, " +
				"appid VARCHAR(300), " +
				"softname VARCHAR(120), " +
				"softicon VARCHAR(1000), " +
				"softsize INTEGER(11), " +
				"softlangue VARCHAR(120)," +
				"softprice INTEGER(11), " +
				"softpublish_time VARCHAR(20), " +
				"softversion VARCHAR(300), " +
				"softdownloadtime INTEGER(11), " +
				"softinfo VARCHAR(5000), " +
				"softuseinfo VARCHAR(1000), " +
				"softiconurl VARCHAR(1000)," +
				"downloadurl VARCHAR(1000)," +
				"catid bigint(20)," +
				"time VARCHAR(20))");
		
		//万花筒标签列表
		db.execSQL("CREATE TABLE IF NOT EXISTS labellist " +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"labelid bigint(20), " +
				"labelname VARCHAR(120), " +
				"icon VARCHAR(1000), " +
				"type VARCHAR(2), " +
				"ctype VARCHAR(2))");
		
		String keyWordSql = "CREATE  TABLE IF NOT EXISTS keyWords " +
		"(" +
		"_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
		"text TEXT" +
		")";
		db.execSQL(keyWordSql);
		/**
		 * 2011 - 10 - 14 增加了关键字第一次安装的时候缓存
		 */
		String [] tempWords = {"音乐  ","天天动听","地图","团购","小鸟","泡泡龙","连连看","斗地主","拼图","图片","电影",
				"桌面美化","手机安全","输入法","微博","电子书","街霸","流量","优化","主题","接龙"};
		for(int i = 0;i<tempWords.length;i++){
			if(Constants.DEBUG)Log.i("appdear", "init keywrods the first time user  install  the software ,insert into db word : "+tempWords[i]);
			ContentValues value = new ContentValues();
			value.put("text", tempWords[i]);
			db.insert("keyWords", "text", value);
		}
		
		db.execSQL("CREATE TABLE IF NOT EXISTS pname(pnameid integer primary key autoincrement, pname varchar(20),pid varchar(20))");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//filedownlog 数据表升级
		db.execSQL("DROP TABLE IF EXISTS filedownlog");
		db.execSQL("DROP TABLE IF EXISTS  "+ Constants.CACHE_IMAGE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS  keyWords");
		db.execSQL("DROP TABLE IF EXISTS  pname");
		db.execSQL("DROP TABLE IF EXISTS  softwaredetail");
		db.execSQL("DROP TABLE IF EXISTS  labellist");
		onCreate(db);
		if (Constants.DEBUG)
			Log.i("upgrade", "  -- upgrade oldVersion:newVersion--  "+oldVersion+":"+newVersion);
	}
}

 