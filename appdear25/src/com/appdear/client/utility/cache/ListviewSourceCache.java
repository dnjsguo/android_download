package com.appdear.client.utility.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

import com.appdear.client.model.InitModel;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ServiceUtils;

public class ListviewSourceCache implements ListviewSourceCacheInterface{
	private static ListviewSourceCache sourceCache=new ListviewSourceCache();
//	private static final Long timeout=72*3600*1000L;
	private static final Long timeout=3600*24*7*1000L;
	private static final Long inittimeout=3600*24*7*1000L;
	private ListviewSourceCache(){
		
	}
	public static ListviewSourceCache getInstance(){
		if(sourceCache==null){
			sourceCache=new ListviewSourceCache();
		}
		return sourceCache;
	}
	/**
	 * 添加序列化对象信息
	 */
	@Override
	public boolean addListview(String key, Object source) {
		// TODO Auto-generated method stub
		boolean flag=isUserExternal();
		if(flag){
			File file = ServiceUtils.getSDCARDImg(Constants.CACHE_SOURCE_DIR);
			ObjectOutputStream  stream=null;
			if (file == null||file.exists()==false||file.isDirectory()==false)
				return false;
			try {
				OutputStream out=new FileOutputStream(file.getAbsoluteFile()+"/"+key);
				stream=new ObjectOutputStream(out);
				stream.writeObject(source);
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}finally{
				if(stream!=null){
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			return false;
		}
		return true;
	}
	/**
	 * 添加序列化对象信息
	 */
	public boolean addInitModel(String key, Object source) {
		// TODO Auto-generated method stub
	
			//File file = ServiceUtils.getSDCARDImg(Constants.DATA_APK);
			FileOutputStream outStream=null;
			try {
				outStream = MyApplication.getInstance().openFileOutput(key, Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				return false;
			}
			ObjectOutputStream  stream=null;
			if (outStream == null)
				return false;
			try {
				stream=new ObjectOutputStream(outStream);
				stream.writeObject(source);
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}finally{
				if(stream!=null){
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		
		return true;
	}
	
	/**
	 * 添加序列化对象信息
	 */
	public boolean removeInitModel(String key) {
		// TODO Auto-generated method stub
	
			//File file = ServiceUtils.getSDCARDImg(Constants.DATA_APK);
		return MyApplication.getInstance().deleteFile(key);
	}
	public Object getInitModel(String key) {
		// TODO Auto-generated method stub
	
		//	File file = ServiceUtils.getSDCARDImg(Constants.DATA_APK);
		FileInputStream inStream=null;
		try {
			inStream = MyApplication.getInstance().openFileInput(key);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			return null;
		}
			ObjectInputStream  stream=null;
			if (inStream == null)
				return null;
			try {
//				File f=new File(file.getAbsoluteFile()+"/"+key);
				
				//Log.i("info","f="+f.getAbsolutePath());
//				InputStream out=new FileInputStream(f);
				stream=new ObjectInputStream(inStream);
				if(key==SourceCommon.INIT_MODEL){
					InitModel init=(InitModel)stream.readObject();
					if(System.currentTimeMillis()-init.time>inittimeout){
						return null;
					}else{
						return init;
					}
				}else{
					return stream.readObject();
				}
				
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				return null;
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				return null;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}finally{
				if(stream!=null){
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
	@Override
	public Object getListview(String key) {
		// TODO Auto-generated method stub
		boolean flag=isUserExternal();
		if(flag){
			File file = ServiceUtils.getSDCARDImg(Constants.CACHE_SOURCE_DIR);
			ObjectInputStream  stream=null;
			if (file == null||file.exists()==false||file.isDirectory()==false)
				return null;
			try {
				File f=new File(file.getAbsoluteFile()+"/"+key);
				if(isValidDate(f)==false)return null;
				//Log.i("info","f="+f.getAbsolutePath());
				InputStream out=new FileInputStream(f);
				stream=new ObjectInputStream(out);
				return stream.readObject();
				
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				return null;
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				return null;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}finally{
				if(stream!=null){
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 判断缓存时间是否过期
	 * @param f
	 * @return
	 */
	private static boolean isValidDate(File f){
		if(f!=null&&f.exists()){
			if(System.currentTimeMillis()-f.lastModified()>timeout){
				 f.deleteOnExit();
			}else{
				return true;
			}
		}
		return false;
	}
	private static boolean isValidDateModel(File f){
		if(f!=null&&f.exists()){
			if(System.currentTimeMillis()-f.lastModified()>inittimeout){
				 f.deleteOnExit();
			}else{
				return true;
			}
		}
		return false;
	}
	
	
	 public static boolean isUserExternal(){
//		 Log.i("info",android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)+"");
		  if(android .os.Environment.getExternalStorageState().equals(
				 android.os.Environment.MEDIA_MOUNTED)){
			return true;
		 }else{
			 return false;
		 }
	 } 
}
