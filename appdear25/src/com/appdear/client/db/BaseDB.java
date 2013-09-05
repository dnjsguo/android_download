/**
 * BaseDB.java
 * created at:2011-5-11下午04:15:34
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.db;

import com.appdear.client.service.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
  
/** 
 * DB存储
 * 
 * @author zqm 
 */
public class BaseDB {

	/**
	 * 数据库
	 */
	public SQLiteOpenHelper dbHelper;
	
	public BaseDB(Context context) {
		dbHelper = new MyDBOpenHelper(context);
	}
	
	/**
	 * @return the dbHelper
	 */
	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}
}

 