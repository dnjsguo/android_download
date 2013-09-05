package com.appdear.client.db;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.db.BaseDB;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.api.ApiSoftListResult;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 文件下载DB
 * @author zqm
 *
 */
public class SoftWareInfoDB extends BaseDB {

	public SoftWareInfoDB(Context context) {
		super(context);
	}
	
	/**
	 * 读取文件下载信息
	 */
	public synchronized ApiSoftListResult read(int softid) {
		ApiSoftListResult result = null;
		SQLiteDatabase db = null;
		Cursor cursor=null;
		try {
			db=dbHelper.getReadableDatabase();
			//db.beginTransaction();
			cursor = db.rawQuery("select * from softwaredetail where softid=?", new String[]{softid+""});
			int numRows = cursor.getCount();
			Log.i("info111", numRows+"=numRows");
			if (numRows == 0)
				return null;
			
			SoftlistInfo info = new SoftlistInfo();
			String iconUrl = "";
			if(cursor.moveToFirst()){
				info.softid = cursor.getInt(0);
			//	Log.i("info111", info.softid+"=info.softid");
				info.appid = cursor.getString(1);
				info.softname = cursor.getString(2);
				info.softicon = cursor.getString(3);
				info.softsize = cursor.getInt(4);
				info.language = cursor.getString(5);
				info.softprice = cursor.getInt(6);
				info.publishtime = cursor.getString(7);
				info.version = cursor.getString(8);
				info.download = cursor.getInt(9);
				info.detail = cursor.getString(10);
				info.summary = cursor.getString(11);
				iconUrl = cursor.getString(12);
				info.downloadurl = cursor.getString(13);
				info.catid = cursor.getInt(14);
				info.time = cursor.getString(15);
			}

			if (System.currentTimeMillis() > (Long.parseLong(info.time) + 1000*60*60*48)) {
				Log.i("softinfo", "softinfo=超过有效期");
				db.delete("softwaredetail","softid=?",new String[]{softid+""});
				return null;
			}
			
			List<String> imageurl = new ArrayList<String>();
			if (iconUrl.length()>=1){
				int index = 0;
				while ((index=iconUrl.indexOf(",")) != -1) {
					imageurl.add(iconUrl.substring(0, index));
					if (iconUrl.length()>= 1)
						iconUrl = iconUrl.substring(index+1, iconUrl.length());
				}
			}
			result = new ApiSoftListResult();
			result.imgurl = imageurl;
			result.detailinfo = info;
			//db.endTransaction();
			return result;
		}catch(Exception e){
			Log.i("softinfo","error");
		}finally {
			if(cursor!=null)cursor.close();
			if(db!=null)db.close();
			return result;
		}
	}
	
	/**
	 * 保存下载信息
	 * @param bean
	 */
	public void save(ApiSoftListResult result) {
		SQLiteDatabase db = null;
		long time = System.currentTimeMillis();
		StringBuffer iconUrl = new StringBuffer();
		for (int i = 0; i < result.imgurl.size(); i++) {
			iconUrl.append(result.imgurl.get(i)).append(",");
		}
		String url = iconUrl.toString();
		try {
			db=dbHelper.getWritableDatabase();
		//	db.beginTransaction();
			//Log.i("softinfo","beginTransaction"+"=");
			ContentValues values = new ContentValues();

//			"(softid bigint(20) PRIMARY KEY, " +
//			"appid VARCHAR(300), " +
//			"softname VARCHAR(120), " +
//			"softicon VARCHAR(1000), " +
//			"softsize INTEGER(11), " +
//			"softlangue VARCHAR(120)," +
//			"softprice INTEGER(11), " +
//			"softpublish_time VARCHAR(20), " +
//			"softversion VARCHAR(300), " +
//			"softdownloadtime INTEGER(11), " +
//			"softinfo VARCHAR(5000), " +
//			"softuseinfo VARCHAR(1000), " +
//			"softiconurl VARCHAR(1000)," +
//			"downloadurl VARCHAR(1000)," +
//			"catid bigint(20)," +
//			"time VARCHAR(20))");
			values.put("softid",result.detailinfo.softid);
			values.put("appid",result.detailinfo.appid);	
			values.put("softname",result.detailinfo.softname);
			values.put("softicon",result.detailinfo.softicon);	
			values.put("softsize",result.detailinfo.softsize);	
			values.put("softlangue", result.detailinfo.language);
			values.put("softprice",result.detailinfo.softprice);	
			values.put("softpublish_time", result.detailinfo.publishtime);
			values.put("softversion",result.detailinfo.version);
			values.put("softdownloadtime",result.detailinfo.download);	
			values.put("softinfo",result.detailinfo.detail);
			values.put("softuseinfo",result.detailinfo.summary);
			values.put("softiconurl",url);	values.put("downloadurl", result.detailinfo.downloadurl);
			values.put("catid",result.detailinfo.catid);
			values.put("time",time+"");
			db.insert("softwaredetail", null, values);
//			db.execSQL(
//					"insert into softwaredetail(softid,appid,softname,softicon," +
//					"softsize, softlangue, softprice, softpublish_time, softversion, softdownloadtime, softinfo" +
//					",softuseinfo, softiconurl , downloadurl, catid, time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",                  
//					new Object[] {result.detailinfo.softid,result.detailinfo.appid,result.detailinfo.softname, result.detailinfo.softicon, 
//							result.detailinfo.softsize, result.detailinfo.language, 
//							result.detailinfo.softprice, result.detailinfo.publishtime,
//							result.detailinfo.version, result.detailinfo.download, 
//							result.detailinfo.detail, result.detailinfo.summary,
//							url, result.detailinfo.downloadurl, result.detailinfo.catid, time+""});
//			Log.i("softinfo","setTransactionSuccessful"+"=");
		//	db.setTransactionSuccessful();   
		//	Log.i("softinfo","save"+"=");
		}catch(Exception e){
			e.printStackTrace();
		//	Log.i("softinfo","error"+"="+e.getMessage());
		} finally {
			if(db!=null)db.close();
		}        
	}
}
