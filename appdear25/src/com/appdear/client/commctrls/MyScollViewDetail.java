package com.appdear.client.commctrls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class MyScollViewDetail extends ScrollView{
	//scroll»¬¶¯µÄ¾àÀë 
	private int h;
	
	public int getH() {
		return h;
	}

	public void setHeight(int height) {
		this.h = height;
	}

	public MyScollViewDetail(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyScollViewDetail(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScollViewDetail(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
		if(ev.getAction()==MotionEvent.ACTION_UP){
			h=this.getScrollY();
		}
		return super.onTouchEvent(ev);
	}
	

}
