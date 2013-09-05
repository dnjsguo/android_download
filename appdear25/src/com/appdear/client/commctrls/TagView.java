package com.appdear.client.commctrls;

import com.appdear.client.R;
import com.appdear.client.utility.ServiceUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TagView extends TextView{
    private static int[] wh=null;
    private static Bitmap bitmap;
    private static float ds;
	public TagView(Context context ,float ds) {
		super(context);
		this.ds=ds;
		if(wh==null){
			wh=ServiceUtils.getCurrentPhonePixInfo(context);
		}
		// TODO Auto-generated constructor stub
		if(bitmap==null){
			bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.tagprogress);
		}
		this.setBackgroundDrawable(new BitmapDrawable(bitmap));
		if(ds>0){
			this.setLayoutParams(new LayoutParams(dip2px(224),dip2px(159)));
		}else{
			this.setLayoutParams(new LayoutParams(wh[1]/3,wh[0]/3));
		}
		this.setText(ServiceUtils.getProcessMsg());
		this.setTextSize(15);
		this.setTextColor(Color.parseColor("#4c4c4c"));
		this.setGravity(Gravity.CENTER);
		this.setPadding(25,10,25,10);
		this.setClickable(true);
		this.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v!=null&&v instanceof TagView){
					((TagView)v).setText(ServiceUtils.getProcessMsg());
				}
			}
			
		});
	}
	public int dip2px(float dipValue){ 
//		 else if(dsdpi<=160){
//			 dipValue=dipValue;
//		 }else if(dsdpi<=240&&height>800){
//			 dipValue*=1.5f;
//		 }
		 return (int)(dipValue * ds + 0.5f); 
	} 
}
