/**
 * TheSpecialListAdatper.java
 * created at:2011-5-26下午03:32:23
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.Adapter;

import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.utility.BitmapTemp;
import com.appdear.client.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
  
/**
 * 积分信息adapter
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
public class MatchTargetUsersGridAdatper extends BaseAdapter {

	private Context context;
	String[] iconUrl;
	String[] title;
	private LayoutInflater mInflater;
	TextView text;
	AsynLoadImageView image;
	private boolean flag=true;
	int count=0;
	/**
	 * 初始化数据
	 */
	public MatchTargetUsersGridAdatper(Context context,String[] iconUrl,String[] title) {
		this.context = context;
		this.iconUrl = iconUrl;
		this.title=title;
		mInflater= LayoutInflater.from(context);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return iconUrl.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (position < iconUrl.length)
			return iconUrl[position]+"-"+title[position];
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		if (position < iconUrl.length)
			return position;
		return -1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String itemUrl = iconUrl[position];
		String tit=title[position];
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.match_targetusers_grid_item, parent, false);
		}
		text=(TextView) view.findViewById(R.id.text); 
		image=(AsynLoadImageView)view.findViewById(R.id.icon); 
		text.setText(tit);
		text.setTextColor(Color.parseColor("#777777"));
		text.setTextSize(14);
		image.setDefaultImage(R.drawable.soft_lsit_icon_default);
		if (itemUrl != null && !"".equals(itemUrl)){
//			if(position==0&&flag==true){
//				handler(view,itemUrl);
//				count++;
//				flag=false;
//			}else if(flag==false&&position!=0){
//				handler(view,itemUrl);
//			}
			handler(view,itemUrl);
		}
		return view;
	}
	private void handler(View view,String itemUrl){
			 Bitmap temp=null;
			//if((temp=AsyLoadImageService.getInstance().getImageCache().isCached(itemUrl))!=null){
			 if((temp=MyApplication.getInstance().getBitmapByUrl(itemUrl))!=null){
				image.setImageBitmap(temp);
			}else{
				image.setImageUrl(itemUrl, true);
			}
	}
}

 