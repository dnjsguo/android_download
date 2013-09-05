package com.appdear.client.commctrls;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.appdear.client.R;
import com.appdear.client.Adapter.ElideUpdateListAdapter;

public class ElideUpdateView extends LinearLayout {
	
	Context mContext;
	ListViewRefresh mListViewRefresh;
	private ElideUpdateListAdapter adapter;
	
	public void setDivider(Drawable drawable) {
		mListViewRefresh.setDivider(drawable);
	}
	
	public void setDividerHeight(int spiner) {
		mListViewRefresh.setDividerHeight(spiner);
	}

	public ElideUpdateListAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ElideUpdateListAdapter adapter) {
		this.adapter = adapter;
	}

	public ElideUpdateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext=context;
		init();
	}

	public ElideUpdateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	
	
  void init(){
	  
	View view=inflate(mContext, R.layout.elideupdateview, null);
	mListViewRefresh=(ListViewRefresh) view.findViewById(R.id.soft_elide_list);
	
	mListViewRefresh.setAdapter(adapter=new ElideUpdateListAdapter(mContext));
	
	this.addView(view,LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
	
	  
  }
	
	
}
