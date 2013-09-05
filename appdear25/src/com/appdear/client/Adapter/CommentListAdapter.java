package com.appdear.client.Adapter;

import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.R;

/**
 * 评论列表adapter
 * 
 * @author zqm
 *
 */
public class CommentListAdapter extends BaseAdapter {

	private Context context;
	private List<SoftlistInfo> list;
	private boolean isMyCommentlist;
	private String username = "";
	
	/**
	 * 初始化数据
	 */
	public CommentListAdapter(Context context, List<SoftlistInfo> list, boolean isMyCommentlist) {
		this.context = context;
		this.list = list;
		this.isMyCommentlist = isMyCommentlist;
		username = SharedPreferencesControl.getInstance().getString("nickname", 
				Common.USERLOGIN_XML, context)+"";
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
		return position;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		SoftlistInfo item = list.get(position);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.software_detail_comment_list_item, parent, false);
		}
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {
			holder = new ItemViewHolder();
			holder.background_layout = (RelativeLayout) view.findViewById(R.id.comment_layout);
			holder.titleTextView = (TextView) view.findViewById(R.id.title);
			holder.contentTextView = (TextView) view.findViewById(R.id.desc);
			holder.timeTextView = (TextView) view.findViewById(R.id.time); 
			holder.starImg = (RatingBar) view.findViewById(R.id.star);
		}
		if (item != null) {
			/*if (!isMyCommentlist){
				
			}*/
			//间色显示评论
			if (position % 2 == 0) {
				holder.background_layout.setBackgroundColor(Color.parseColor("#aae5e5e5")); // 半透明灰色
			}else {
				holder.background_layout.setBackgroundColor(Color.parseColor("#aaffffff")); // 半透明白色
			}
			if (item.commentimei.equals(AppContext.getInstance().IMEI) 
					&& username != null && !"".equals(username)) {
				holder.titleTextView.setText(username);
			} else
				holder.titleTextView.setText(item.username);
			holder.contentTextView.setText(item.text);
			holder.timeTextView.setText(item.time);
			holder.starImg.setProgress(item.softgrade*2/10);
		}
		view.setEnabled(false);
		return view;
	}


	protected class ItemViewHolder {
		public RelativeLayout background_layout;
		public TextView titleTextView;
		public TextView contentTextView;
		public TextView timeTextView;
		public RatingBar starImg;
		
	}
}
