/**
 * ImageCache.java
 * created at:2011-5-11下午04:09:32
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
  
/** 
 * Image cache, one for all activities and orientations
 * 
 * @author zqm 
 */
public class ImageCache extends  Hashtable<String, BitmapTemp>{
	
	public synchronized BitmapTemp isCached(String url) {
		if(containsKey(url)){
			return get(url);
		}else{
			return null;
		}
	}
	
	public boolean isHasCached(String url) {
		return containsKey(url) && get(url) != null;
	}
	
 
	public void put2(String key, BitmapTemp value) {
		//if (size() < 50){
		
		    if(containsKey(key) )
		    {	    	 
		    	if(value!=null&&value.bitmap!=null&&!value.bitmap.isRecycled())
		    	{
		    		value.bitmap.recycle();
		    		//System.out.println("-----ImageCache--put2()---recycle-"+key);
		    	}
		    }else
		    {
		    	  super.put(key, value);
		    }
		 
	}
	
	public void clear(){
		super.clear();
//		try{
//		if(size()>0){
//			BitmapTemp temp=null;
//			for(java.util.Map.Entry<String,BitmapTemp> set:this.entrySet()){
//				if((temp=set.getValue())!=null){
//					if(temp.bitmap!=null&&!temp.bitmap.isRecycled()){
//						temp.bitmap=null;
//					}
//				}
//			}
//			super.clear();
//		}
//		}catch(Exception e){
//			Log.i("info",e.getMessage());
//		}
	}
}

 