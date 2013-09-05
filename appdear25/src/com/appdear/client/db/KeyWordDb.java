package com.appdear.client.db;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.SearchList;
import com.appdear.client.service.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class KeyWordDb extends BaseDB{
	SQLiteDatabase  db = null;
	public KeyWordDb(Context context) {
		super(context);
	}
	
	public void addData(List<String> keyWordList){
		ContentValues value = null;
		db = this.getDbHelper().getWritableDatabase();
		db.delete("keyWords", null, null);
		for(int i = 0;i<keyWordList.size();i++){
			value = new ContentValues();
			value.put("text", keyWordList.get(i));
			db.insert("keyWords", "text", value);
		}
		db.close();
		if(Constants.DEBUG&&keyWordList!=null)Log.i("appdear", "KeyWordDb insert table keywords size:"+keyWordList.size());
	}
	
	public void updateData(List<String> list){
		db = this.getDbHelper().getWritableDatabase();
		String delSql = "DELETE * FROM keyWords";
		db.execSQL(delSql);
		addData(list);
		if(Constants.DEBUG&&list!=null)Log.i("appdear", "KeyWordDb update table keywords size:"+list.size());
		db.close();
		//db.close();
	}
	
	public SearchList<String> getKeyWordList(){
		db = this.getDbHelper().getWritableDatabase();
		SearchList<String> list = new SearchList<String>();
		String selectSql = "SELECT * FROM keyWords ";//SELECT * FROM "keyWords"
		Cursor c = db.rawQuery(selectSql, null);	
		int textId = c.getColumnIndex("text");
		c.moveToFirst();
		if(Constants.DEBUG&&c!=null)Log.i("appdear", "KeyWordDb cursor size:"+c.getCount());
		for(int i=0;i<c.getCount();i++){
			list.add(c.getString(textId));
			c.moveToNext();
		}
		c.close();
		db.close();
		//db.close();
		return list;
	}
}
