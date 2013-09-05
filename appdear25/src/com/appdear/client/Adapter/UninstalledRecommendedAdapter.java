/**
 * DownloadAdapter.java
 * created at:2011-5-26下午03:32:23
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.Adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.AlterInstalledRestoreActivity;
import com.appdear.client.R;
import com.appdear.client.Adapter.SoftwarelistAdatper.ItemViewHolder;
import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
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
public class UninstalledRecommendedAdapter extends ArrayAdapter<SoftlistInfo>  {
	private ListBaseActivity context;
	private List<SoftlistInfo> list;
	private ListViewRefresh listview;
	private LayoutInflater mInflater;
	HashMap<Integer,Boolean> isSelected;
	/**
	 * 初始化数据
	 */
	public UninstalledRecommendedAdapter(ListBaseActivity context,
			List<SoftlistInfo> list, ListViewRefresh listview ) {
		super(context, R.layout.installed_restore_item_layout, list);
		this.context = context;
		this.list = list;
		this.listview = listview;
		 
		mInflater = LayoutInflater.from(context);
	 
		this.setNotifyOnChange(true);
	}
	
	 
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return  list == null ? 0 : list.size();
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
		SoftlistInfo item = this.getItem(position);
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.uninstalled_recommend_item_layout, parent,
					false);
		}
		
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {

			holder = new ItemViewHolder();
			holder.softIcon = (AsynLoadImageView) view.findViewById(R.id.imageView);
			holder.titleTextView = (TextView) view.findViewById(R.id.title);
			holder.sizeTextView = (TextView) view.findViewById(R.id.size);
			holder.downloadLayout=(RelativeLayout)view.findViewById(R.id.pricelayout);
			holder.downloadLayout.setClickable(true);
			//holder.pricelayout = (RelativeLayout) view.findViewById(R.id.pricelayout);
 
		} 
		if (item != null) {
				holder.position = position;
				holder.item = item;
				holder.softid = item.softid;
				holder.titleTextView.setText(item.softname);
				if(holder.sizeTextView!=null)holder.sizeTextView.setText("" + ServiceUtils.returnSpace(item.softsize));
				holder.softIcon.setDefaultImage(R.drawable.soft_lsit_icon_default);
				holder.softIcon.setImageResource(R.drawable.soft_lsit_icon_default);
				//设置下载任务的图片
				holder.downloadLayout.setTag(holder);
				
				item.getSofticonPath();
				if (item.softicon != null && !item.softicon.equals("")) {
					if (Common.ISLOADSOFTICON) {
						//BitmapTemp temp = null;
						Bitmap temp = null;
					//	if ((temp= imageCache.isCached(item.softIcon))!=null)
						if ((temp= MyApplication.getInstance().getBitmapByUrl(item.softicon))!=null)
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
								Drawable installedDrawable=ServiceUtils.getInstallIcon(context, item.appid);
								/*if(installedDrawable!=null)
								{
									holder.softIcon.setImageDrawable(installedDrawable);
								}else
								{*/
									holder.softIcon.setImageUrl(item.softicon, position, listview, true);
								//}
							 
							}
	 					}
						
					}
				} 
				if(item.type==1)//显示已经安装
				{
					//holder.downloadLayout.setBackgroundDrawable()
					holder.downloadLayout.setBackgroundResource(R.drawable.download_image_installed);
					holder.downloadLayout.setClickable(false);
				} else
				{
					holder.downloadLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							 
							ItemViewHolder holder = (ItemViewHolder) v.getTag();
							final int  temp=holder.position;
							 new Thread() {
									public void run() {
										
										 SoftlistInfo item=(SoftlistInfo)list.get(temp);
								    		
								    		if(item.type!=1)
								    		{	 
								    			Integer Status = MyApplication.getInstance().getSoftMap().get(item.softid);
								    			if (Status == null||Status==0) {
								    				 if (download(item,  item.downloadurl,0)) {
								    					context.showMessageInfo(item.softname+"在下载管理中，请查看"); 
								    				 } 
								    			} 

								             }
										 /*for(int i = 0; i<list.size(); i++){
			  
												 SoftlistInfo item=(SoftlistInfo)list.get(i);
										    		
										    		if(item.type!=1)
										    		{	 
										    			Integer Status = MyApplication.getInstance().getSoftMap().get(item.softid);
										    			if (Status == null||Status==0) {
										    				 if (download(item,  item.downloadurl,0)) {
										    					context.showMessageInfo(""); 
										    				 } 
										    			} 
 
										             }
										 }*/
									}
								}.start();
								context.finish(); 
						}
					});
				}
				
			}
			return view;  
		}

	private boolean download(SoftlistInfo info, String apkname,int state) {
		final SiteInfoBean downloadbean = new SiteInfoBean(info.downloadurl,
				ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null ? ""
						: ServiceUtils.getSDCARDImg(Constants.APK_DATA)
								.getPath(), ServiceUtils.getApkname(info.downloadurl), info.softname,
				info.softicon, info.version, info.softid, info.appid,
				info.softsize, 0, 1, null, null,
				BaseActivity.downloadUpdateHandler,state==4?Constants.UPDATEPARAM:"");
		String[] msg = DownloadUtils.download(downloadbean, context);
		// 任务已存在
		//Toast mScreenHint = Toast.makeText(this, msg[0],Toast.LENGTH_SHORT);
		//mScreenHint.show();
		if (msg[1] != null && msg[1].equals("0"))
			return true;
		return false;
	}
	 
 
	private class ItemViewHolder  
	{
		public AsynLoadImageView softIcon;
		public TextView titleTextView;
		public TextView sizeTextView;
		public RelativeLayout downloadLayout;
		
		//	public CheckBox checkBox;
		public TextView installedTextView;
 		public String imgurl;
 		public int softid;
		// 0--下载，1--下载中 2--安装     3（哈希code）--已安装 4 升级  5 暂停
		public int state = 0;
		public int position;
		public SoftlistInfo item;
 	}
}