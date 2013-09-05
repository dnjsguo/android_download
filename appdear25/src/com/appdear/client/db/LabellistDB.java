package com.appdear.client.db;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.CatalogListInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 万花筒标签列表
 * @author User
 *
 */
public class LabellistDB extends BaseDB {

	public LabellistDB(Context context) {
		super(context);
	}
	
	/**
	 * 根据类型获取列表
	 * 
	 * @param type 0--适用人群， 1--使用场景， 2--职业
	 * @return
	 */
	public List<CatalogListInfo> getLabellist(String type) {
		SQLiteDatabase db=null;
		List<CatalogListInfo> list = new ArrayList<CatalogListInfo>();
		try {
		    db = dbHelper.getReadableDatabase();
			db.beginTransaction();
			Cursor cursor = db.query("labellist", new String[]{
					"labelid", "labelname", "icon", "type", "ctype"},
					"type=?",new String[]{type}, null, null, null, null);
			int numRows = cursor.getCount();
			
			cursor.moveToFirst();
			for (int i = 0; i < numRows; i ++) {
				CatalogListInfo info = new CatalogListInfo();
				info.catalogid = cursor.getInt(0)+"";
				info.catalogname = cursor.getString(1);
				info.catalogicon = cursor.getString(2);
				info.ctype = cursor.getString(4);
				
				list.add(info);
				cursor.moveToNext();
			}
			cursor.close();
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
		} finally {
			if(db!=null)
			db.close();
		}
		return list;
	}
	
	/**
	 * 插入标签列表数据
	 * @param info 列表信息
	 * @param type 列表类型 0 -- 适用人群，1--使用场景，2--职业
	 */
	public void insertLabellist(CatalogListInfo info, String type) {
		SQLiteDatabase db=null;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction(); 
			db.execSQL(
					"insert into labellist(labelid, labelname, " +
					"icon, type, ctype) values(?,?,?,?,?)",                  
					new Object[] {info.catalogid, info.catalogname,
							info.catalogicon, type, info.ctype});
			db.setTransactionSuccessful();   
			if(db!=null)db.endTransaction();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(db!=null)db.close();
		}
	}
	
	/**
	 * 根据类型删除标签列表数据
	 * @param type 0--适用人群，1--使用场景，2--职业
	 */
	public void deleteAll(String type) {
		SQLiteDatabase db=null;
		try {
			db= dbHelper.getWritableDatabase();
			db.beginTransaction();
			db.delete("labellist", "type=?", new String[]{type});
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch(Exception e) {
		} finally {
			if(db!=null)
			db.close();
		}
	}
}
