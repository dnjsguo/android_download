/**
 * TheSpecialListAdatper.java
 * created at:2011-5-26下午03:32:23
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.Adapter;

import java.util.List;

import com.appdear.client.model.MessagelistInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
  
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
public class MeunAdatper extends BaseAdapter {

	private Context context;
	int[] icon;
	String[] title;
	private LayoutInflater mInflater;
	TextView text;
	ImageView image;
	/**
	 * 初始化数据
	 */
	public MeunAdatper(Context context,int[] icon,String[] title) {
		this.context = context;
		this.icon = icon;
		this.title=title;
		mInflater= LayoutInflater.from(context);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return icon.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (position < icon.length)
			return icon[position]+"-"+title[position];
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		if (position < icon.length)
			return position;
		return -1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int item = icon[position];
		String tit=title[position];
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.meun_layout, parent, false);
		}
		text=(TextView) view.findViewById(R.id.meuntext); 
		image=(ImageView)view.findViewById(R.id.meunicon); 
		text.setText(tit);
		image.setBackgroundResource(item);
		return view;
	}
}

 