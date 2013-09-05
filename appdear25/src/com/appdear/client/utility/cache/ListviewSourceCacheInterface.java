package com.appdear.client.utility.cache;

import java.util.List;

public interface ListviewSourceCacheInterface<T> {
	//添加缓存信息到key文件/sdcard/appdear/source/key
	public boolean addListview(String key,T source);
	
	//从缓存取key文件资源
	public T getListview(String key);
	
}
