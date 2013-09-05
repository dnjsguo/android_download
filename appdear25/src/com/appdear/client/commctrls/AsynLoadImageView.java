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
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.appdear.client.R;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
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
public class AsynLoadImageView extends ImageView {
	protected  final String nameSpace = "http://meiyitianabc.blog.163.com";
	private boolean isfalg=false;
	private  AlphaAnimation am=null;
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

	public AsynLoadImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
		
	//	size(context);
		init();
	}
	
	public AsynLoadImageView(Context context, int imgid) {
		super(context);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
	//	size(context);
		
		setImageResource(imgid);
		init();
	}

	public AsynLoadImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
	//	size(context);
		//根据属性设置的网址来加载图片
		init();
	}

	public AsynLoadImageView(Context context) {
		super(context);
		if(bitmap==null){
			bitmap=readBitMap(context,R.drawable.soft_lsit_icon_default);
		}
	//	size(context);
		init();
	}

	/**
	 * Sharable code between constructors
	 */
	private void init(){
		am=new AlphaAnimation(0,1);
		am.setDuration(300);
	}
	
//	private void size(Context context) {
//		
//		DisplayMetrics metrics = ServiceUtils.getMetrics(((Activity) context).getWindowManager());
//		width = metrics.widthPixels;
//		height = metrics.heightPixels;
//	}
	
	/**
	 * Loads image from remote location
	 * 
	 * @param url eg. http://random.com/abz.jpg
	 */
	public void setImageUrl(String url, boolean isAdd) {
 		if(mListView == null && mCurrentlyGrabbedUrl != null && mCurrentlyGrabbedUrl.equals(url)){
			// do nothing image is grabbed & loaded, we are golden
 			return;
		}
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
		//String imgurl=AsyLoadImageService.getInstance().getImageDb().search(url);
		String imgurl= url.substring( url.lastIndexOf('/') + 1);
 
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
					MyApplication.getInstance().setBitmapByUrl(url, bm);
					/*BitmapTemp temp=null;
					imageCache.put(url, (temp=new BitmapTemp(bm)));
					temp.setIsflag(true);*/
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
				AsynLoadImageView.this.setImageBitmap(bitmap);
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
		//private BitmapTemp temp;
		private Bitmap bitmap_temp;
		
		
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
            } catch (Exception e) 
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
			if((bitmap_temp=MyApplication.getInstance().getBitmapByUrl(url))!=null) {
 				cacheflag=true;
				return "";
			} else if ((bitmap_temp=checkDb(url))!=null) {
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
			Bitmap bmp = null;
			String filename = this.mTaskUrl.substring(this.mTaskUrl.lastIndexOf('/') + 1);
			try {
				imageUrl = new URL(mTaskUrl);
				stream=imageUrl.openStream();
				 
				bmp = BitmapFactory.decodeStream(stream);
				 
				if(bmp != null){
					if (isAddImageCache)
					{
 					MyApplication.getInstance().setBitmapByUrl(mTaskUrl, bmp);
					}
					saveBitmap(bmp,filename,mTaskUrl);
				}
				else {
 				}
			} catch (OutOfMemoryError e) {
			//	imageCache.clear();
				e.printStackTrace();
				System.gc();
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
			{
 				return;
			}
			if(mListView != null)
			{
 				if(mPosition < mListView.getFirstVisiblePosition() || mPosition > mListView.getLastVisiblePosition())
				{
 					return;		
				}
			}
			if(cacheflag==true){
				if(bitmap_temp!=null){
					if(am!=null){
						AsynLoadImageView.this.startAnimation(am);
					}
 					AsynLoadImageView.this.setImageBitmap(bitmap_temp);
				//	temp.setIsflag(true);
					return;
				} 
			}
			if(dbflag==true){
				if(bitmap_temp!=null){
					if(am!=null){
						AsynLoadImageView.this.startAnimation(am);
					}
					AsynLoadImageView.this.setImageBitmap(bitmap_temp);
					return;
				}
			}
			Bitmap bmptemp= MyApplication.getInstance().getBitmapByUrl(url);
			//BitmapTemp bmptemp = imageCache.get(url);
			if (bmptemp != null) {
			
//				if(mListView != null)
//					if(mPosition < mListView.getFirstVisiblePosition() || mPosition > mListView.getLastVisiblePosition())
//						return;
				if( bmptemp!=null&&bmptemp.isRecycled()==false){
					if(am!=null){
						AsynLoadImageView.this.startAnimation(am);
					}
 					AsynLoadImageView.this.setImageBitmap(bmptemp);
					//bmptemp.setIsflag(true);
				}
				else
				{
 					 loadDefaultImage();
				}
//					if(AppContext.defaulticon!=null){
//						AsynLoadImageView.this.setImageBitmap(AppContext.defaulticon);
//					}
					//AsynLoadImageView.this.setImageBitmap(bitmap);
				mCurrentlyGrabbedUrl = url;
			}
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

 