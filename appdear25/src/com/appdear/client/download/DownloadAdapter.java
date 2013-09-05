/**
 * DownloadAdapter.java
 * created at:2011-5-26下午03:32:23
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.download;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.appdear.client.R;
import com.appdear.client.Adapter.SoftwarelistAdatper.ItemViewHolder;
import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ServiceUtils;
  
/**
 * 下载列表adapter
 * 
 * <code>title</code>
 * abstract
 * <p>description
 * <p>example:
 * <blockquote><pre>
 * </blockquote></pre>
 * @author Author
 * @version Revision Date
 */
public class DownloadAdapter extends BaseAdapter implements
OnClickListener {
	private Animation mTrackAnim;
	private Context context;
	private ListViewRefresh listview;
	private ListAdatperDataProcessListener listener;
	
	public final int CANCEL = 0;
	public final int CONTINUE = 1;
	public final int PAUSE = 2;
	public final int DELETE = 7;
	public final int INSTALL = 4;
	public final int FINISH = 5;
	public final int UPDATE_PROCESS = 6;
	
	private LayoutInflater mInflater;
	private SiteInfoBean item;
	private View view;
	private PackageManager pm;
//	private static ImageCache imageCache = AsyLoadImageService.getInstance().getImageCache();
	
	/**
	 * 初始化数据
	 */
	public DownloadAdapter(Context context,
			ListViewRefresh listview, ListAdatperDataProcessListener listener) {
		this.context = context;
		this.listview = listview;
		this.listener = listener;
		pm = context.getPackageManager();
	}
	
	@Override
	public Object getItem(int positon) {
		return AppContext.getInstance().getTaskList().get(positon);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return AppContext.getInstance().taskList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {

		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public synchronized View getView(final int position, View convertView, ViewGroup parent) {
		item = AppContext.getInstance().getTaskList().get(position);
		//Log.i("", "downloadadapter=" + item.softName +"========position=" + position);
		if (mInflater == null)
			mInflater = LayoutInflater.from(context);
		
 		if (convertView != null) {
 			view = convertView;
 		} else {
			view = mInflater.inflate(R.layout.more_downloadlist_item_layout, parent, false);
 		}
		
		ItemViewHolder holder = null;
		if (holder == null) {
			holder = new ItemViewHolder();
			holder.softIcon = (AsynLoadImageView) view.findViewById(R.id.imageView);
			holder.titleTextView = (TextView) view.findViewById(R.id.title);
			holder.sizeTextView = (TextView) view.findViewById(R.id.size);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			holder.downloadt=(TextView) view.findViewById(R.id.downloadt);
			holder.leftButton = (ImageView) view.findViewById(R.id.button_left);
			// 点击图标出现的浮动框
			holder.actionLayout = (LinearLayout) view.findViewById(R.id.actionLayout);
			holder.shareAndfavLayout=(RelativeLayout)view.findViewById(R.id.shareAndfavLayout);
			holder.delImageView=(ImageView)view.findViewById(R.id.delImageView);
			holder.detailImageView=(ImageView)view.findViewById(R.id.detailImageView);
			setState(item.state, holder);
		//	view.setTag(holder);
		}
		if (item != null) {
			holder.position = position;
			holder.bean = item;
			holder.softid = item.softID;
			holder.leftButton.setTag(item.softID+"_");
			if (item.softName == null || item.softName.equals("")) {
				try {
					item.softName = pm.getPackageInfo(item.appID,
							PackageManager.GET_ACTIVITIES).applicationInfo
							.loadLabel(pm).toString();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			holder.titleTextView.setText(item.softName);
			holder.sizeTextView.setText("" + ServiceUtils.returnSpace(item.fileSize));
			holder.progressBar.setMax(item.fileSize);
			holder.progressBar.setProgress(item.downloadLength);
			float size = (float)item.downloadLength/item.fileSize;
			holder.downloadt.setText((float) Math.floor(size*1000)/10+"%");
			holder.progressBar.setTag(item.softID+1000000);
			int state = item.state;
			setState(state, holder);
			
			holder.leftButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.requestFocus();
					ImageView leftButton = (ImageView) v.findViewById(R.id.button_left);
					String softid_ = (String) v.getTag();
					if(softid_==null||softid_.equals("")){
						return;
					}
					int softid = 0;
					if (softid_.endsWith("_")) {
						softid = Integer.parseInt(softid_.substring(0, softid_.length()-1));
					}else{
						softid=Integer.parseInt(softid_);
					}
					
					View holder = v;
					SiteInfoBean bean = AppContext.getInstance().taskList.get(softid);
					int state = bean.state;
					Log.i("", "state=" + state);
					if (state == 1) {
						//暂停变成继续
						leftButton.setBackgroundResource(R.drawable.pause);
						if (listener != null) {
							if (holder != null)
								listener.keyPressProcess(bean, CONTINUE);
						}
					} else if (state == 2) {
						//安装
						leftButton.setBackgroundResource(R.drawable.download_image_install);
						if (listener != null) {
							if (holder != null)
								listener.keyPressProcess(bean, INSTALL);
						}
					} else if (state == 3) {
						//重试变下载中
						leftButton.setBackgroundResource(R.drawable.pause);
						if (listener != null) {
							if (holder != null)
								listener.keyPressProcess(bean, CONTINUE);
						}
					} else if (state == 0) {
						//下载变暂停
						leftButton.setBackgroundResource(R.drawable.conti);
						if (listener != null) {
							if (holder != null)
								listener.keyPressProcess(bean, PAUSE);
						}
					} else if (state == -1) {
						//等待变成暂停
						leftButton.setBackgroundResource(R.drawable.conti);
						if (listener != null) {
							if (holder != null)
								listener.keyPressProcess(bean, PAUSE);
						}
					}
				}
			});
			
			holder.softIcon.setDefaultImage(R.drawable.soft_lsit_icon_default);
			holder.softIcon.setImageResource(R.drawable.soft_lsit_icon_default);
			//设置下载任务的图片
			 
			item.getSofticonPath();
			if (item.softIcon != null && !item.softIcon.equals("")) {
				if (Common.ISLOADSOFTICON) {
					//BitmapTemp temp = null;
					Bitmap temp = null;
				//	if ((temp= imageCache.isCached(item.softIcon))!=null)
					if ((temp= MyApplication.getInstance().getBitmapByUrl(item.softIcon))!=null)
					{
						holder.softIcon.setImageBitmap(temp);
					}else 
					{
						String filename=item.getSofticonPath();
						File fileDir = ServiceUtils.getSDCARDImg(Constants.CACHE_IMAGE_DIR);
						// 查看文件是不是存在
						if (fileDir != null&& fileDir.exists()&& new File(fileDir.getAbsoluteFile() + "/"+ filename).exists()) 
						{
							String filepath = fileDir.getAbsoluteFile() + "/"+ filename;
							try{
								holder.softIcon.setImageURI(Uri.parse(filepath));
					    	}catch (OutOfMemoryError e) {
								System.gc();
								Log.e("load image", "内存溢出啦");
							}	
						} else {
							holder.softIcon.setImageUrl(item.softIcon, position, listview, true);
						}
						//holder.softIcon.setImageUrl(item.softIcon, position, listview, true);
					}
				}
			} else {
				if (item.dicon == null) {
					item.dicon = ServiceUtils.getInstallIcon(context, item.appID);
					holder.softIcon.setImageDrawable(item.dicon);	
				} else
					holder.softIcon.setImageDrawable(item.dicon);	
			}
		}
 
		holder.softIcon.setTag(holder);
	 	holder.delImageView.setTag(holder);
	 	holder.detailImageView.setTag(holder);
 		holder.softIcon.setOnClickListener(this); 
		holder.delImageView.setOnClickListener(this);
		holder.detailImageView.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		

		switch (v.getId()) {
		 
		case R.id.delImageView:
			  actionLayoutNotShow();
			 clickDelImage(v);
			
			break;
		case R.id.detailImageView:
		 	 actionLayoutNotShow();
		  	clickDetailImage(v);

			break;
		case R.id.imageView:
			clickIconImage(v);
			break;
		}
	}
	 public void  actionLayoutNotShow()
	    {
	    	if(listview.getIndex()==-1) return;

	    		if ( listview.findViewWithTag("index"+listview.getIndex())!=null) 
				{
					RelativeLayout   layout=(RelativeLayout )listview.findViewWithTag("index"+listview.getIndex());
					layout.setVisibility(View.GONE);
					layout.setTag(null);
					listview.setIndex(-1);
	 			}
	 
	    }
	 
	 public void clickDelImage(View v)
	 {
		 v.requestFocus();
		 ItemViewHolder holder=(ItemViewHolder)v.getTag();
		 SiteInfoBean saveBean=holder.bean;
		 listener.keyPressProcess(saveBean, DELETE);
		 
	 }
	 public void clickDetailImage(View v)
	 {
		 v.requestFocus();
		 ItemViewHolder holder=(ItemViewHolder)v.getTag();
		 SiteInfoBean saveBean=holder.bean;
		 //softid,String softicon,String downloadurl,String... intentcentent
		 if(saveBean!=null)
		 ServiceUtils.setSelectedValuesDownload(context, saveBean.softID, saveBean.softIcon,saveBean.sSiteURL );
	 }
	/**
	 * 处理icon图标按钮
	 */
	 public void clickIconImage(View v) {
			ItemViewHolder holder = (ItemViewHolder) v.getTag();
			if(listview==null)return;
			if(listview.getIndex()!=-1)
			{
				if (listview != null&&listview.findViewWithTag("index"+listview.getIndex())!=null) 
				{
					RelativeLayout   layout=(RelativeLayout )listview.findViewWithTag("index"+listview.getIndex());
					layout.setVisibility(View.GONE);
					//((LinearLayout)layout.findViewById(R.id.actionLayout)).setVisibility(View.GONE);
					layout.setTag(null);
					//index = -1;
					listview.setIndex(-1);
					if(layout==holder.shareAndfavLayout)
					{
						return;
					}
				}
			}
			v.requestFocus();
			int[] xy = new int[2];
			v.getLocationInWindow(xy);
			 Rect rect = new Rect(xy[0], xy[1]+30, xy[0]+v.getWidth(), xy[1]+v.getHeight());	
		 
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); 

			int height = display.getHeight();
			int actualHeight = 0;
			 
			if (height > 480) {
				actualHeight = (int) (height * 0.75);
			} else {
				actualHeight = (int) (height * 0.7);
			}
 		   if (rect.top > actualHeight) {
 
			 if(listview.getHeaderViewsCount()==0)
			 {
				 listview.setSelectionFromTop(holder.position,(listview.getHeight() - ServiceUtils.dip2px(67+63,(Activity)context)+3));
			 }else
			 {
				 listview.setSelectionFromTop(holder.position,(listview.getHeight() - ServiceUtils.dip2px(67*2+63,(Activity)context)+3)); 
			 }
 
		 	} 
 			 holder.shareAndfavLayout.setTag("index"+holder.position);
			 listview.setIndex(holder.position);
 			 holder.shareAndfavLayout.setVisibility(View.VISIBLE);
 		     mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.quickaction);
			 mTrackAnim.setInterpolator(new Interpolator() {
					public float getInterpolation(float t) {	 
						final float inner = (t * 1.55f) - 1.1f;
 						return 1.2f - inner * inner;
					}
				});			 
			 holder.actionLayout.startAnimation(mTrackAnim);
			    
		 
			 
	 	 
		}
	/**
	 * 设置下载任务的状态
	 * @param state
	 */
	public void setState(int state, ItemViewHolder holder) {
		if (state == 2) {
			//下载完成
			holder.leftButton.setBackgroundResource(R.drawable.download_image_install);
			holder.progressBar.setVisibility(View.GONE);
			holder.downloadt.setVisibility(View.GONE);
		} else if (state == 0) {
			//正在下载
			holder.leftButton.setBackgroundResource(R.drawable.pause);
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.downloadt.setVisibility(View.VISIBLE);
		} else if (state == -1) {
			//等待下载
			holder.leftButton.setBackgroundResource(R.drawable.pause);
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.downloadt.setVisibility(View.VISIBLE);
		} else if (state == 1) {
			//暂停
			holder.leftButton.setBackgroundResource(R.drawable.conti);
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.downloadt.setVisibility(View.VISIBLE);
		} else if (state == 3) {
			//重试
			holder.leftButton.setBackgroundResource(R.drawable.retry);
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.downloadt.setVisibility(View.VISIBLE);
		} 
	}
	
	protected class ItemViewHolder {
		public AsynLoadImageView softIcon;
		public TextView titleTextView;
		public ProgressBar progressBar;
		public TextView progressValueTV;
		public TextView sizeTextView;
		public ImageView leftButton;
		public int softid = 0;
		public int position = 0;
        public RelativeLayout shareAndfavLayout;
		public LinearLayout actionLayout;
		public ImageView delImageView;
		public ImageView detailImageView;
		public SiteInfoBean bean;
		public TextView downloadt;

	}
}