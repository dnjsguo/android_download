package com.appdear.client.Adapter;
 
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.R;

/**
 * 类别列表首页
 * @author zxy
 *
 */
public class SoftSubjectCategoryListAdapter extends BaseAdapter {
	Context mContext = null;
	List<CatalogListInfo>  mSoftCategory = null;
	LayoutInflater mLayoutInflater = null;
	ListViewRefresh list;
	public SoftSubjectCategoryListAdapter(Context context,List<CatalogListInfo> categoryList, ListViewRefresh list){
		this.mContext = context;
		this.mSoftCategory = categoryList;
		this.list = list;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if(mSoftCategory!=null)
			return this.mSoftCategory.size();
		return 0;
	}
	
	@Override
	public Object getItem(int position) {
		return this.mSoftCategory.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CatalogListInfo item = mSoftCategory.get(position);
		
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.softsubjectcategory_listitem_layout, parent, false);
		}
		CategoryHolder holder = (CategoryHolder) view.getTag();
		if (holder == null) {
			holder = new CategoryHolder();
			holder.imgIcon = (AsynLoadImageView) view.findViewById(R.id.category_image);
			holder.tvTip = (TextView) view.findViewById(R.id.category_tip);
			holder.tvTitle = (TextView) view.findViewById(R.id.category_name);
			holder.tvCount = (TextView) view.findViewById(R.id.category_count);
//			holder.tvTime = (TextView) view.findViewById(R.id.category_time);
			
		}
		if (item != null){
			holder.imgIcon.setDefaultImage(R.drawable.soft_lsit_icon_default_new);
			holder.imgIcon.setImageResource(R.drawable.soft_lsit_icon_default_new);
			if (Common.ISLOADSOFTICON)
				holder.imgIcon.setImageUrl(item.catalogicon, position, list, true);
			String con=this.mSoftCategory.get(position).catalogdesc;
			if(con != null && !"".equals(con)){
					if(con.length()>12){
						con=con.substring(0,12);
					}
			}else{
				con="";
			}
			if(!"".equals(con))holder.tvTip.setText(con.replace(" ", " | "));
			else holder.tvTip.setText(con);
			String catalogname=this.mSoftCategory.get(position).catalogname;
			holder.tvTitle.setText(catalogname==null?"":catalogname.length()>10?catalogname.substring(0,10):catalogname);
			holder.tvCount.setText(this.mSoftCategory.get(position).appCount);
//			holder.tvTime.setText(this.mSoftCategory.get(position).catalogTime);
//			holder.tvTime.setText("2012-01-16");
		}
		return view;
	}

	//缓存类
	private  class CategoryHolder {
		AsynLoadImageView imgIcon;
		TextView  tvTitle;
		TextView  tvTip;
		TextView tvCount;
		TextView tvTime;
	}
	
}
