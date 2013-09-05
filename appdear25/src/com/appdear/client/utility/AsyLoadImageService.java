/**
 * AsyLoadImageService.java
 * created at:2011-5-11下午04:07:46
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.utility;

import com.appdear.client.db.GalleryAdDb;
import com.appdear.client.db.ImageDB;
import com.appdear.client.service.AppContext;
  
/** 
 * 图片缓存
 * 
 * @author zqm 
 */
public class AsyLoadImageService {

	private ImageCache imageCache;
	private ImageDB imageDb;
	public static String TAG = "AsyLoadImageService";
	public static AsyLoadImageService instance;
	public static GalleryAdDb galleryDb = null;
	public  GalleryAdDb getGalleryDb() {
		return galleryDb;
	}

	public void setGalleryDb(GalleryAdDb galleryDb) {
		AsyLoadImageService.galleryDb = galleryDb;
	}

	public AsyLoadImageService() { 
		imageCache = new ImageCache();
		imageDb = new ImageDB(AppContext.getInstance().appContext);
		galleryDb = new GalleryAdDb(AppContext.getInstance().appContext);
	}
	
	/**
	 * @return the instance
	 */
	public static AsyLoadImageService getInstance() {
		if (instance == null)
			instance = new AsyLoadImageService();
		return instance;
	}
	
	/**
	 * @return the imageCache
	 */
	public ImageCache getImageCache() {
		return imageCache;
	}
	
	/**
	 * @return the imageDb
	 */
	public ImageDB getImageDb() {
		return imageDb;
	}
}

 