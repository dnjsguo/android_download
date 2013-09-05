/**
 * TheSpecialListAdatper.java
 * created at:2011-5-26下午03:32:23
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.utility.BitmapTemp;
import com.appdear.client.R;

import android.content.Context;
import android.graphics.Bitmap;
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
public class DarenRecommendGridAdatper extends BaseAdapter {

	private Context context;
	List<String> recommendIconUrls=null;
	List<String> recommendNames=null;
	private LayoutInflater mInflater;
	TextView text;
	AsynLoadImageView image;
	private boolean flag=true;
	int count=0;
	/**
	 * 初始化数据
	 */
	public DarenRecommendGridAdatper(Context context,List<String> iconUrl,List<String> title) {
		this.context = context;
		this.recommendIconUrls = iconUrl;
		this.recommendNames=title;
		mInflater= LayoutInflater.from(context);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return recommendIconUrls.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (position < recommendIconUrls.size())
			return recommendIconUrls.get(position);
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		if (position < recommendIconUrls.size())
			return position;
		return -1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  //System.out.println("-------getView------position="+position);
		String itemUrl =recommendIconUrls.get(position);
		String name=recommendNames.get(position);
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.darenre_commend_item, parent, false);
		}
		text=(TextView) view.findViewById(R.id.text); 
		image=(AsynLoadImageView)view.findViewById(R.id.icon); 
		text.setText(name);
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

 