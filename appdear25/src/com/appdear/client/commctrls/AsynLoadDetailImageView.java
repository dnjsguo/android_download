/**
 * AsynLoadImageView.java
 * created at:2011-5-11下午04:04:03
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.commctrls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.lang.ref.WeakReference;
//import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;
import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.appdear.client.R;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.utility.BitmapTemp;
import com.appdear.client.utility.ImageCache;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.utility.cache.QueueDownloadTask;
 
/**  
 * 异步加载图片
 * 
 * @author zqm
 */
public class AsynLoadDetailImageView extends ImageView {
	protected  final String nameSpace = "http://meiyitianabc.blog.163.com";
	private boolean isfalg=false;
	
	/**
	 * Maximum number of unsuccesful tries of downloading an image
	 */
	protected static int MAX_FAILURES = 2;
	/**
	 * Remote image location
	 */
	protected String mUrl;
	
	private static Bitmap bitmap;
	
	/**
	 * Currently successfully grabbed url
	 */
	protected String mCurrentlyGrabbedUrl;
	public  boolean  isLoadSuc = true;
	/**
	 * Remote image download failure counter
	 */
	public  int mFailure;

	/**
	 * Position of the image in the mListView
	 */
	protected int mPosition=-1;

	/**
	 * ListView containg this image
	 */
	protected ListViewRefresh mListView;
	
	/**
	 * Default image shown while loading or on url not found
	 */
	protected Integer mDefaultImage;
	
	/**
	 * 是否添加缓存
	 */
	protected boolean isAddImageCache = true;
	
	/**
	 * 屏幕宽度
	 */
	protected int width = 0;
	private boolean infoflag=false;
	
	/**
	 * 屏幕高度
	 */
	protected int height = 0;
	
	//private static ImageCache imageCache = AsyLoadImageService.getInstance().getImageCache();
	
	//public static final ExecutorService pool = Executors.newFixedThreadPool(5);
	
	private DownloadTask task;
	
	
	public DownloadTask getTask() {
		return task;
	}

	public void setTask(DownloadTask task) {
		this.task = task;
	}

	public AsynLoadDetailImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
		
		size(context);
		init();
	}
	
	public AsynLoadDetailImageView(Context context, int imgid) {
		super(context);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
		size(context);
		
		setImageResource(imgid);
	}

	public AsynLoadDetailImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
		size(context);
		//根据属性设置的网址来加载图片
		init();
	}

	public AsynLoadDetailImageView(Context context) {
		super(context);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
		size(context);
		init();
	}

	/**
	 * Sharable code between constructors
	 */
	private void init(){
		
	}
	
	private void size(Context context) {
		
		DisplayMetrics metrics = ServiceUtils.getMetrics(((Activity) context).getWindowManager());
		width = metrics.widthPixels;
		height = metrics.heightPixels;
	}
	
	/**
	 * Loads image from remote location
	 * 
	 * @param url eg. http://random.com/abz.jpg
	 */
	public void setImageUrl(String url, boolean isAdd) {
		 
		BitmapTemp temp=null;
		mUrl=url;
		try{
			new DownloadTask(url).execute(url);
		}catch(java.lang.ExceptionInInitializerError e){
			
		}catch(java.lang.NoClassDefFoundError e){
		
		}catch(Exception e) {
			 		 
		}
		
		 
	}
	
	public void setImageUrl(String url, boolean isAdd,boolean infoflag) {
		this.infoflag=infoflag;
		setImageUrl(url,isAdd);
	}
	
	//20110905 修改无sd卡启动异常
	public Bitmap checkDb(String url) {
		Bitmap bm =null;
		String imgurl=url.substring( url.lastIndexOf('/') + 1);;
		//System.out.println("url   ="+url);
		//System.out.println("imgurl="+imgurl);
		if(imgurl.equals("")) {
			return null;
		} else {
			File file = ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR);
			if (file == null)
				return null;
			try {
				bm = BitmapFactory.decodeFile(file.getPath()+"/"+imgurl);
				if (bm == null)
					return null;
				if (isAddImageCache){
					BitmapTemp temp=null;
					
					///imageCache.put2(url,   new BitmapTemp(bm) );
				//	temp.setIsflag(true);
				}
				
			} catch (OutOfMemoryError e) {
				//imageCache.clear();
				System.gc();
			}
			return bm;
		}
	}

	/**
	 * Sets default local image shown when remote one is unavailable
	 * 
	 * @param resid
	 */
	public void setDefaultImage(Integer resid){
		mDefaultImage = resid;
	}
	
	/**
	 * Loads default image
	 */
	protected  void loadDefaultImage(){
		
		if (mDefaultImage != null&&mDefaultImage==R.drawable.soft_lsit_icon_default){
			if(bitmap!=null){
				AsynLoadDetailImageView.this.setImageBitmap(bitmap);
			}
		}else{
			if(mDefaultImage!=null)
				setImageResource(mDefaultImage);
		}
		isLoadSuc = false ;
	}
	
	/**
	 * Loads image from remote location in the ListView
	 * 
	 * @param url eg. http://random.com/abz.jpg
	 * @param position ListView position where the image is nested
	 * @param listView ListView to which this image belongs
	 */
	public void setImageUrl(String url, int position, ListViewRefresh listView, boolean isAdd) {
		if (url == null)
			return;
		mPosition = position;
		mListView = listView;
		isAddImageCache = isAdd;
		mUrl=url;
		setImageUrl(url, isAdd);
	}

	/**
	 * top banner 展示的图片的个数
	 * @param urls
	 */
	public void setTopBannerImageUrl(String  urls){
		
	}
	/**
	 * Asynchronous image download task
	 * 
	 */
	class DownloadTask extends AsyncTask<String, Void, String>{
		
		private String mTaskUrl;
		private boolean cacheflag=false;
		private boolean dbflag=false;
		private String url;
		private BitmapTemp temp;
		private Bitmap bitmap;
		
		
		public DownloadTask(String url){
			this.url=url;
		}
		@Override
		public void onPreExecute() {
			/**
			 * 判断listview是否滚动
			 */
			   loadDefaultImage();
				
			super.onPreExecute();
		}
		public void saveBitmap(Bitmap bitmap,String filename,String url) throws IOException
        {
			if (ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR) == null)
            	return;
            File file = new File(ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR),filename);
            if(!file.exists() && file != null){
            	file.createNewFile();
            }
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(file);
                if (url.endsWith(".jpg")) {
               	 	if(bitmap.isRecycled()==false&&bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
                            out.flush();
                            out.close();
                            out=null;
                    }
               } else {
               	 	if(bitmap.isRecycled()==false&&bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)) {
                            out.flush();
                            out.close();
                            out=null;
                    }
               }
             //  AsyLoadImageService.getInstance().getImageDb().add(url, filename);
            } 
            catch (FileNotFoundException e) 
            {
                    e.printStackTrace();
            } 
            catch (IOException e) 
            {
                    e.printStackTrace(); 
            }finally{
            	if(out!=null){
            		out.close();
            	}
            }
        }
	
		@Override
		public String doInBackground(String... params) {
			
			//Log.i("doback","doInBackground");
			/*if((temp=imageCache.isCached(url))!=null) {
 				cacheflag=true;
				return "";
			} else if ((bitmap=checkDb(url))!=null) {
				dbflag=true;
				return "";
			}*/
			if ((bitmap=checkDb(url))!=null) {
				dbflag=true;
				return "";
			}
			if(mListView!=null&&(mListView.getCountflag()==OnScrollListener.SCROLL_STATE_FLING
					|| mListView.getCountflag()==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)){
				return "";
			}
			mTaskUrl = params[0];
			URL imageUrl;
			InputStream stream=null;
			//Bitmap bmp = null;
			String filename = this.mTaskUrl.substring(this.mTaskUrl.lastIndexOf('/') + 1);
			try {
				imageUrl = new URL(mTaskUrl);
				stream=imageUrl.openStream(); 
				bitmap = BitmapFactory.decodeStream(stream);
				//判断连接是否关闭如果关闭则返回
				 
				if(bitmap != null){
					 
					saveBitmap(bitmap,filename,mTaskUrl);
				 
				}
				 
			} catch (OutOfMemoryError e) {
				//imageCache.clear();
				e.printStackTrace();
				Log.e("load image", "内存溢出啦");
			} catch (Exception e) {
			//	Log.i("stream","Exception");
				e.printStackTrace();
			} finally {
				try {
					if(stream != null){
						stream.close();
					}
				} catch (IOException e) {}
			}
			return mTaskUrl;
		}

		@Override
		public void onPostExecute(String url) {
			super.onPostExecute(url);
			// target url may change while loading
			if(mTaskUrl!=null&&mUrl!=null&&!mTaskUrl.equals(mUrl))
				return;
			
			/*if(mListView != null)
				if(mPosition < mListView.getFirstVisiblePosition() || mPosition > mListView.getLastVisiblePosition())
				{
					return;		
				}*/
			
		/*	if(cacheflag==true){
				if(temp!=null){
					AsynLoadDetailImageView.this.setImageBitmap(temp.bitmap);
					temp.setIsflag(true);
					return;
				}
			}*/
			//if(dbflag==true){
				if(bitmap!=null){
					AsynLoadDetailImageView.this.setImageBitmap(bitmap);
					return;
				}
			//} 
			
			/*BitmapTemp bmptemp = imageCache.get(url);
			if (bmptemp != null) {
//				if(mListView != null)
//					if(mPosition < mListView.getFirstVisiblePosition() || mPosition > mListView.getLastVisiblePosition())
//						return;
				if(!bmptemp.isRequested&&bmptemp.bitmap!=null&&bmptemp.bitmap.isRecycled()==false){
					AsynLoadDetailImageView.this.setImageBitmap(bmptemp.bitmap);
					//bmptemp.setIsflag(true);
				}
				else
					 loadDefaultImage();
//					if(AppContext.defaulticon!=null){
//						AsynLoadImageView.this.setImageBitmap(AppContext.defaulticon);
//					}
					//AsynLoadImageView.this.setImageBitmap(bitmap);
				mCurrentlyGrabbedUrl = url;
			}*/
			this.cancel(true);
		}
	}
	
	 public static  Bitmap readBitMap(Context context, int resId){  
		  BitmapFactory.Options opt = new BitmapFactory.Options();  
		    opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		    opt.inPurgeable = true;  
		   opt.inInputShareable = true;  
	   //获取资源图片  
		  InputStream is = context.getResources().openRawResource(resId);  
		  try{
			  return BitmapFactory.decodeStream(is,null,opt); 
		  }finally{
			  if(is!=null){
			  try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		  }
	 }

	public boolean isIsfalg() {
		return isfalg;
	}

	public void setIsfalg(boolean isfalg) {
		this.isfalg = isfalg;
	}

	public boolean isInfoflag() {
		return infoflag;
	}

	public void setInfoflag(boolean infoflag) {
		this.infoflag = infoflag;
	}
	
}

 