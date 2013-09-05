package com.appdear.client.commctrls;

import com.appdear.client.R;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SavedState;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class ViewPagerDetail extends ViewPager{
	private int height;
	private int heighte;
	int currentindex;
	View detailview;
	
	public void setDetailview(View detailview) {
		this.detailview = detailview;
	}
	public void setOnTouch(int height,int heighte){
		this.height=height;
		this.heighte=heighte;
	}
	public ViewPagerDetail(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewPagerDetail(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
		if(currentindex==0){
			int h=heighte;
			if(detailview!=null){
				MyScollViewDetail sv=(MyScollViewDetail)detailview.findViewById(R.id.scolldetail);
				if(sv!=null){
					h=heighte-sv.getH();
				}
			}
			if(ev.getY()<h){
				return false;
			}else {
				return super.onInterceptTouchEvent(ev);
			}
		}else{
			return super.onInterceptTouchEvent(ev);
		}
	
	}
	public void setcurrent(int index){
		currentindex=index;
	}
	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		currentindex=item;
		super.setCurrentItem(item);
	}
	@Override
	public Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		if(this.getAdapter()!=null){
			return super.onSaveInstanceState();
		}else{
	        return null;
		}
		
	}
	
}
