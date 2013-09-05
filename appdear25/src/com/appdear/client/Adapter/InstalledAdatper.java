package com.appdear.client.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.download.ListAdatperDataProcessListener;
 import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.MoreManagerInstalledActivity;
import com.appdear.client.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class InstalledAdatper extends BaseAdapter implements OnClickListener{
	private Animation mTrackAnim;
	private Context context;
	private List<PackageinstalledInfo> list = new ArrayList<PackageinstalledInfo>();
	private ListAdatperDataProcessListener listener;
	private int postionreal=0;
	private LayoutInflater mInflater;
 	private ListViewRefresh listview;
	public InstalledAdatper(Context context, List<PackageinstalledInfo> list, 
			ListAdatperDataProcessListener listener, ListViewRefresh listview) {
		this.listener = listener;
		this.context = context;
		this.list = list;
		this.listview = listview;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if (list == null)
			return null;
		if (position < list.size())
			return list.get(position);
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		if (list == null)
			return -1;
		return list.get(position).pname.hashCode();
	}
	public List<PackageinstalledInfo> getList() {
		return list;
	}

	public void setList(List<PackageinstalledInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PackageinstalledInfo item = list.get(position);
		
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.more_manager_installed_list_item_layout, parent, false);
		}
	
		ItemViewHolder holder = null;
		if (holder == null) {
			holder = new ItemViewHolder();
			holder.icon = (ImageView) view.findViewById(R.id.imageView);
			holder.titleTextView = (TextView) view.findViewById(R.id.title);
			holder.contentTextView = (TextView) view.findViewById(R.id.desc);
			//TODO 如果恢复这个价格控件的话，把View的状态改成可见即可！
			holder.contentTextView.setVisibility(View.GONE);
			holder.alertTextView = (TextView) view.findViewById(R.id.alert);
			holder.alertTextView.setPadding(0, 3, 0, 0);
			holder.button = (Button) view.findViewById(R.id.uninstall);
			holder.ordertitle=(TextView) view.findViewById(R.id.ordertitle);
			holder.layout=(RelativeLayout)view.findViewById(R.id.layout);
			holder.layout1=(RelativeLayout)view.findViewById(R.id.layout1);
			
			holder.actionLayout = (LinearLayout) view.findViewById(R.id.actionLayout);
			holder.shareAndfavLayout=(RelativeLayout)view.findViewById(R.id.shareAndfavLayout);
		    holder.open_button = (Button) view.findViewById(R.id.open_button);
			holder.move_button = (Button) view.findViewById(R.id.move_button);
			 
			holder.contentLayout = (RelativeLayout) view.findViewById(R.id.content_layout);
			holder.pname=item.pname;
			holder.item=item;
			holder.installlocation=item.installlocation;
			holder.position = position;
			
		}
		if (item != null) {
			if(item.pname.equals("com.appdear.client")){
				holder.button.setVisibility(View.INVISIBLE);
			}else{
				holder.button.setVisibility(View.VISIBLE);
			}
			if(item.isCharProxy==false){
				holder.layout.setVisibility(View.GONE);
				holder.layout1.setVisibility(View.VISIBLE);
				holder.icon.setImageDrawable(item.icon);
				holder.titleTextView.setText(item.appname);
				//holder.contentTextView.setText("费用：未知");
				holder.alertTextView.setText("存储位置：" + (item.softsd==1?"SD卡(":"手机内存(")+item.formatsofttsize+")");
				//view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.soft_list_bg));
				holder.open_button.setOnClickListener(this);
				holder.move_button.setOnClickListener(this);
				holder.icon.setOnClickListener(this);
				holder.icon.setTag(holder);
				holder.open_button.setTag(holder);
				holder.move_button.setTag(holder);
				holder.button.setTag(holder);
				holder.button.setOnClickListener(this);
				holder.alertTextView.setTag(item.pname);
				
				view.setTag(holder);
			}else{
				holder.layout1.setVisibility(View.GONE);
				holder.layout.setVisibility(View.VISIBLE);
//				holder.layout.setBackgroundColor(Color.parseColor("#e8edf6"));
				holder.layout.setBackgroundColor(Color.parseColor("#e8ebed"));
				holder.ordertitle.setVisibility(View.VISIBLE);
				holder.ordertitle.setTextSize(14f);
//				holder.ordertitle.setTextColor(Color.parseColor("#555555"));
				holder.ordertitle.setTextColor(Color.parseColor("#4c4c4c"));
				holder.ordertitle.setText(item.firstC);
			 
			}
 
		}
		
	
		return view;
	}
	
	protected class ItemViewHolder {
		public ImageView icon;
		public String pname;
		public int position;
		public TextView titleTextView;
		public TextView contentTextView;
		public TextView alertTextView;
		public Button button;
		public TextView ordertitle;
		public RelativeLayout layout;
		public RelativeLayout layout1;
		public RelativeLayout layoutparent;
		public LinearLayout actionLayout;
		public RelativeLayout contentLayout;
	    public RelativeLayout shareAndfavLayout;
		public Button open_button;
		public Button move_button;
		public int installlocation;
		public PackageinstalledInfo item;
	}

	@Override
	public void onClick(View v) {
		v.requestFocus();
		ItemViewHolder holder=(ItemViewHolder)v.getTag();
		if (Constants.DEBUG)
			Log.i("holder", holder+"");
		if(holder!=null){
		switch(v.getId()){
			case R.id.uninstall:
				if (listener != null)
					listener.keyPressProcess(holder.pname, 
							MoreManagerInstalledActivity.REMOVE_ACTION, holder.position);
				break;
			case R.id.move_button:
				actionLayoutNotShow();
				if (listener != null)
					listener.keyPressProcess(holder.pname,
							MoreManagerInstalledActivity.MOVE_ACTION, holder.position);
				break;
			case R.id.open_button:
				actionLayoutNotShow();
				if (listener != null){
					if(holder.pname!=null&&!holder.pname.equals("com.appdear.client")){
						listener.keyPressProcess(holder.pname,
							MoreManagerInstalledActivity.OPEN_ACTION, holder.position);
					}
				}
				break;
			case R.id.imageView:
				clickIconImage(v);
				break;
			}
		}
	}
	 public void  actionLayoutNotShow()
	    {
	    	if(listview.getIndex()==-1) return;

	    		if ( listview.findViewWithTag("index"+listview.getIndex())!=null) 
				{
					RelativeLayout   layout=(RelativeLayout )listview.findViewWithTag("index"+listview.getIndex());
					layout.setVisibility(View.GONE);
					layout.setTag(null);
					listview.setIndex(-1);
	 			}
	 
	    }
	/**
	 * 处理icon图标按钮
	 */
	 public void clickIconImage(View v) {
		
			ItemViewHolder holder = (ItemViewHolder) v.getTag();
			if(listview==null)return;
     	   if(listview.getIndex()!=-1)
			{
				if (listview != null&&listview.findViewWithTag("index"+listview.getIndex())!=null) 
				{
					RelativeLayout   layout=(RelativeLayout )listview.findViewWithTag("index"+listview.getIndex());
					layout.setVisibility(View.GONE);
					//((LinearLayout)layout.findViewById(R.id.actionLayout)).setVisibility(View.GONE);
					layout.setTag(null);
					//index = -1;
					listview.setIndex(-1);
					if(layout==holder.shareAndfavLayout)
					{
						return;
					}
				}
			}
			v.requestFocus();
			int[] xy = new int[2];
			v.getLocationInWindow(xy);
			 Rect rect = new Rect(xy[0], xy[1]+30, xy[0]+v.getWidth(), xy[1]+v.getHeight());	
		 
			Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); 

			int height = display.getHeight();
			int actualHeight = 0;
			 
			if (height > 480) {
				actualHeight = (int) (height * 0.75);
			} else {
				actualHeight = (int) (height * 0.7);
			}
 		   if (rect.top > actualHeight) {
 
			 if(listview.getHeaderViewsCount()==0)
			 {
				 listview.setSelectionFromTop(holder.position,(listview.getHeight() - ServiceUtils.dip2px(67+63,(Activity)context)+3));
			 }else
			 {
				 listview.setSelectionFromTop(holder.position,(listview.getHeight() - ServiceUtils.dip2px(67*2+63,(Activity)context)+3)); 
			 }
 
		 	} 
 			 holder.shareAndfavLayout.setTag("index"+holder.position);
			 listview.setIndex(holder.position);
 			 holder.shareAndfavLayout.setVisibility(View.VISIBLE);
 			 
 			if(android.os.Build.VERSION.SDK!=null&&Integer.parseInt(android.os.Build.VERSION.SDK)<=7){
 				holder.move_button.setText("不可移动");
 				holder.move_button.setEnabled(false);
 			}else{
 				if (holder.installlocation != 0 && holder.installlocation != 2) {
 					//表示不可移动
 					//holder.move_button.setText("不可移动");
 					holder.move_button.setBackgroundResource(R.drawable.move_not_installed_list);
 					holder.move_button.setEnabled(false);
 				} else {
// item.softsd
 					if(holder.item.softsd==1)
 					{
 						// sd
 						holder.move_button.setBackgroundResource(R.drawable.move_device_installed_list);
 	 					holder.move_button.setEnabled(true);
 					}else
 					{
 						// 手机
 						holder.move_button.setBackgroundResource(R.drawable.move_sd_installed_list);
 	 					holder.move_button.setEnabled(true);
 					}
 					
 				}
 			}
 			 
 			 
 		     mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.quickaction);
			 mTrackAnim.setInterpolator(new Interpolator() {
					public float getInterpolation(float t) {	 
						final float inner = (t * 1.55f) - 1.1f;
 						return 1.2f - inner * inner;
					}
				});			 
			 holder.actionLayout.startAnimation(mTrackAnim);
		}
}
