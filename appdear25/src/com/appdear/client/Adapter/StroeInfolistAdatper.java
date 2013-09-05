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

import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.R;

import android.content.Context;
import android.util.Log;
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
public class StroeInfolistAdatper extends BaseAdapter {

	private Context context;
	private List<String[]> list;
	private LayoutInflater mInflater;
	/**
	 * 初始化数据
	 */
	public StroeInfolistAdatper(Context context, List<String[]> list) {
		this.context = context;
		this.list = list;
		mInflater=LayoutInflater.from(context);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (position < list.size())
			return list.get(position);
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		if (position < list.size())
			return position;
		return -1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String[] item = list.get(position);
		View view;
		if(position==list.size()-1||position==0){
			view = mInflater.inflate(R.layout.storeinfolist_item_1, parent, false);
		}else{
			view = mInflater.inflate(R.layout.storeinfolist_item, parent, false);
		}
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {
			holder = new ItemViewHolder();
//			holder.time = (TextView) view.findViewById(R.id.point_time);
			holder.name = (TextView) view.findViewById(R.id.stroename); 
			holder.info = (TextView) view.findViewById(R.id.stroeinfo);
			view.setTag(holder);
		}
		if (item != null) {
			holder.name.setText(item[0]);
			holder.info.setText(item[1]);
			view.setTag(holder);
		}
		view.setEnabled(false);
		return view;
	}


	protected class ItemViewHolder {
		public TextView name;
//		public TextView time;
		public TextView info;
	}
}

 