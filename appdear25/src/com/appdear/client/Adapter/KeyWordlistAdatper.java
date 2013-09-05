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
import java.util.Random;

import com.appdear.client.Adapter.SoftwarelistAdatper.ItemViewHolder;
import com.appdear.client.commctrls.QHListener;
import com.appdear.client.model.SearchList;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class KeyWordlistAdatper extends ArrayAdapter<String> implements
OnClickListener{

	private Context context;
	private SearchList<String> list;
	private LayoutInflater mInflater;
	private QHListener listener;
	
	public void setListener(QHListener listener) {
		this.listener = listener;
	}

	/**
	 * 初始化数据
	 */
	
	
	public SearchList<String> getList() {
		return list;
	}

	public void setList(SearchList<String> list) {
		this.list = list;
	}

	public KeyWordlistAdatper(Context context, SearchList<String> list,
			LayoutInflater mInflater) {
		super(context,R.layout.keyword_item,list);
		this.context = context;
		this.list = list;
		this.mInflater = mInflater;
	}
  
	public KeyWordlistAdatper(Context context) {
		super(context,R.layout.keyword_item);
		this.context=context;
		mInflater=LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return list==null?0:list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public String getItem(int position) {
		if(list!=null){
		if (position < list.size())
			return list.get(position);
		}
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
		final String item = list.get(position);
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.keyword_item, parent, false);
		}
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {
			holder = new ItemViewHolder();
//			holder.time = (TextView) view.findViewById(R.id.point_time);
			holder.info = (TextView) view.findViewById(R.id.keyword); 
			holder.image = (ImageView) view.findViewById(R.id.keyword_image);
			holder.r=(ImageView) view.findViewById(R.id.r_image);
			holder.t=(TextView) view.findViewById(R.id.text1); 
			view.setTag(holder);
		}
		if (item != null) {
//			holder.time.setText(item.time);
			holder.info.setText(item);
			view.setTag(holder);
		}
		if(position<=2){
			holder.r.setImageResource(R.drawable.search_1);
		}else{
			holder.r.setImageResource(R.drawable.search_2);
		}
		holder.t.setText((position+1)+"");
		if(position<=2){
			holder.t.setTextColor(Color.WHITE);
		}else{
			holder.t.setTextColor(Color.parseColor("#ffa636"));
		}
		int i=list.getTrend(position);
		if(i>0){
			holder.image.setImageResource(R.drawable.search_t);
		}else if(i<0){
			holder.image.setImageResource(R.drawable.search_b);
		}else {
			holder.image.setImageResource(R.drawable.search_r);
		}
		
		holder.image.setTag(item);
		holder.image.setOnClickListener(this);
		view.setEnabled(false);
		return view;
	}
	private int getRandom(int seed){
		Random rand=new Random(seed);
		return rand.nextInt(3);
	}

	protected class ItemViewHolder {
		public ImageView image;
//		public TextView time;
		public TextView info;
		public ImageView r;
		public TextView t;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.keyword_image){
		final String str = (String) v.getTag();
			if(listener!=null){
				listener.addKeyword(str);
			}
		}
	}
}

 