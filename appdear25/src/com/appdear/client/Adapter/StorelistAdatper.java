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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appdear.client.R;
import com.appdear.client.model.ShopModel;
  
/**
 * 定制店面信息adapter
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
public class StorelistAdatper extends BaseAdapter {

	//private Context context;
	private List<ShopModel> list;
	private LayoutInflater mInflater;
	/**
	 * 初始化数据
	 */
	public StorelistAdatper(Context context, List<ShopModel> list) {
		//this.context = context;
		this.list = list;
		mInflater=LayoutInflater.from(context);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return (list==null||list.size() == 0)?0:list.size();
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
		ShopModel item = list.get(position);
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.storelist_item, parent, false);
		}
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {
			holder = new ItemViewHolder();
			holder.store_name = (TextView) view.findViewById(R.id.store_name);
			holder.store_dec = (TextView) view.findViewById(R.id.store_dec);
			holder.store_tel = (TextView) view.findViewById(R.id.store_tel);
			view.setTag(holder);
		}
		
		if (item != null) {
			view.setTag(holder);
		}
		
		holder.store_name.setText(item.name);//"店面："+
		holder.store_dec.setText("地址："+item.addr);
		holder.store_tel.setText("电话："+item.tel);
		view.setEnabled(false);
		return view;
	}

	protected class ItemViewHolder {
		public TextView store_name;
		public TextView store_dec;
		public TextView store_tel;
	}
}