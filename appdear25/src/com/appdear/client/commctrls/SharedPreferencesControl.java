package com.appdear.client.commctrls;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * @author jdan 2011-06-25、
 *  手机本应用配置信息
 *
 */
public class SharedPreferencesControl{
	/**
	 * 默认配置文件
	*/
	private static final String perferencename="appstore";
	public static SharedPreferencesControl sharedPreferences;
	private SharedPreferencesControl(){
		
	}
	public static SharedPreferencesControl getInstance(){
		
		if(sharedPreferences==null){
			sharedPreferences=new SharedPreferencesControl();
		}
		return sharedPreferences;
	}
	
	public boolean putString(String key,String value,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).edit().putString(key,value).commit();
	}
	public String getString(String key,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).getString(key,"");
	}
	public boolean putBoolean(String key,boolean value,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).edit().putBoolean(key, value).commit();
	}
	public Boolean getBoolean(String key,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).getBoolean(key,false);
	}
	public boolean  putFloat(String key,Float value,String xmlname,Context contextutil){
		return  contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).edit().putFloat(key,value).commit();
	}
	public Float getFloat(String key,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).getFloat(key,0.0f);
	}
	public boolean putInt(String key,int value,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).edit().putInt(key,value).commit();
	}
	public int getInt(String key,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).getInt(key,0);
	}
	public boolean putLong(String key,Long value,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).edit().putLong(key,value).commit();
	}
	public long getLong(String key,String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname==null?perferencename:xmlname,contextutil.MODE_PRIVATE).getLong(key,0L);
	}
	
	/**
	 * 清除存储文件内容
	 * @param xmlname 保存的xml
	 */
	public void clear(String xmlname,Context contextutil){
		contextutil.getSharedPreferences(xmlname,contextutil.MODE_PRIVATE).edit().clear().commit();
	}
	/**
	 * 删除存储文件
	 * @param xmlname 存储文件
	 * @param key     关键字
	 */
	public void remove(String xmlname,Context contextutil){
		contextutil.getSharedPreferences(xmlname,contextutil.MODE_PRIVATE).edit().remove(xmlname).commit();
	}
	/**
	 * 是否存在该文件
	 * @param xmlname
	 * @return
	 */
	public boolean contains(String xmlname,Context contextutil){
		return contextutil.getSharedPreferences(xmlname,contextutil.MODE_PRIVATE).contains(xmlname);
	}
}
