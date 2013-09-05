package com.appdear.client.utility.cache;

import java.security.Timestamp;
import java.util.LinkedHashSet;


public interface ImageCacheInterface {
	/**
	 * 清除所有缓存
	 */
	public void clearAll();
	
	/**
	 * 清除key的缓存信息
	 * @param key
	 */
	public void clear(String key);
	/**
	 * 加入缓存
	 * @param key
	 * @param obj
	 */
	public void put(String key,LinkedHashSet<String>  obj);
	
	/**
	 * 得到缓存值
	 * @param key
	 * @param obj
	 */
	public LinkedHashSet<String> get(String key);
}	
