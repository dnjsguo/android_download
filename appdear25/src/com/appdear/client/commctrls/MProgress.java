package com.appdear.client.commctrls;

import com.appdear.client.R;
import com.appdear.client.utility.ServiceUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MProgress extends LinearLayout {
	//是否加背景
	private ProgressBar progress=null;
	private TagView t;
	private static DisplayMetrics  metrics = new DisplayMetrics(); 
	private float ds=-1;
	public MProgress(Context context,boolean flag) {
		super(context);
		if(context instanceof Activity){
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
			ds=metrics.density;
		}
		progress=new ProgressBar(context);
		progress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.myprogress));
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		if(flag==true){
			t=new TagView(context,ds);
			this.addView(t);
			this.addView(progress);
			progress.setPadding(0,5,0,0);
			TextView view=new TextView(context);
//			view.setText("正在加载...");
			view.setTextColor(Color.parseColor("#333333"));
			view.setGravity(Gravity.CENTER);
			view.setPadding(0,5,0,0);
			this.addView(view);
		}else{
			this.addView(progress);
		}
		this.setGravity(Gravity.CENTER);
		if(flag==true){
			this.setClickable(false);
			this.setBackgroundColor(Color.WHITE);
		}
		this.setOrientation(VERTICAL);
	}
	
	public MProgress(Context context) {
		super(context);
		progress=new ProgressBar(context);
		progress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.myprogress));
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		this.addView(progress);
		this.setGravity(Gravity.CENTER);
		this.setBackgroundColor(Color.WHITE);
	}

	public MProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public void cancelBack(){
		this.setBackgroundDrawable(null);
		if(t!=null)t.setVisibility(View.GONE);
	}
	public void showBack(){
		this.setBackgroundDrawable(null);
		if(t!=null)t.setVisibility(View.VISIBLE);
	}
}
