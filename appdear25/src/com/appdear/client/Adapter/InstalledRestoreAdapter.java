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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appdear.client.R;
import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
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
public class InstalledRestoreAdapter extends ArrayAdapter<SoftlistInfo> implements
OnClickListener {
	private ListBaseActivity context;
	private List<SoftlistInfo> list;
	private ListViewRefresh listview;
	private LayoutInflater mInflater;
	HashMap<Integer,Boolean> isSelected;
	/**
	 * 初始化数据
	 */
	public InstalledRestoreAdapter(ListBaseActivity context,
			List<SoftlistInfo> list, ListViewRefresh listview,HashMap<Integer,Boolean> isSelected) {
		super(context, R.layout.installed_restore_item_layout, list);
		this.context = context;
		this.list = list;
		this.listview = listview;
		this.isSelected=isSelected;
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
			view = mInflater.inflate(R.layout.installed_restore_item_layout, parent,
					false);
		}
		
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {

			holder = new ItemViewHolder();
			holder.softIcon = (AsynLoadImageView) view.findViewById(R.id.imageView);
			holder.titleTextView = (TextView) view.findViewById(R.id.title);
			holder.sizeTextView = (TextView) view.findViewById(R.id.size);
			holder.checkBox=(CheckBox)view.findViewById(R.id.checkbox);
			holder.installedTextView=(TextView)view.findViewById(R.id.installedTextView);
 
		} 
		if (item != null) {
				holder.position = position;
				holder.item = item;
				holder.softid = item.softid;
				holder.titleTextView.setText(item.softname);
				holder.sizeTextView.setText("" + ServiceUtils.returnSpace(item.softsize));
				holder.softIcon.setDefaultImage(R.drawable.soft_lsit_icon_default);
				holder.softIcon.setImageResource(R.drawable.soft_lsit_icon_default);
				//设置下载任务的图片
				if(item.type==1)//显示已经安装
				{
					holder.installedTextView.setVisibility(View.VISIBLE);
					holder.checkBox.setVisibility(View.GONE);
				}else
				{
					holder.installedTextView.setVisibility(View.GONE);
					holder.checkBox.setVisibility(View.VISIBLE);
					boolean isCheck=isSelected.get(position);
					if(isCheck)
					{
						holder.checkBox.setChecked(true);
					}else
					{
						holder.checkBox.setChecked(false);
					}
					
				}
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
				
				holder.checkBox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						 
						if(isSelected.get(position)){
							isSelected.put(position, false);
						}else{
							isSelected.put(position, true);
						}
						//notifyDataSetChanged();
					}
				});
			}
			return view;  
		}


	@Override
	public void onClick(View v) {
	 
	}
 
	private class ItemViewHolder  
	{
		public AsynLoadImageView softIcon;
		public TextView titleTextView;
		public TextView sizeTextView;
		public CheckBox checkBox;
		public TextView installedTextView;
 		public String imgurl;
 		public int softid;
		// 0--下载，1--下载中 2--安装     3（哈希code）--已安装 4 升级  5 暂停
		public int state = 0;
		public int position;
		public SoftlistInfo item;
 	}
}