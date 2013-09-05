package com.appdear.client.commctrls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.appdear.client.R;
import com.appdear.client.Adapter.ViewPicAdapter;

/**
 * 图片浏览布局
 * @author zqm
 *
 */
public class ViewPicLayout extends LinearLayout implements OnTouchListener
{
	
	/**
	 * 适配器
	 */
	private ViewPicAdapter adapter;
	
	/**
	 * 宽度
	 */
	private int width;
	
	/**
	 * 高度
	 */
	private int height;
	
	/**
	 * 是否加载图片
	 */
	public boolean isLoadotherpic = false;
	
	public ViewPicLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	/**
	 * 设置布局格式
	 * @param adapter
	 * @param width
	 * @param height
	 */
	public void setAdapter(ViewPicAdapter adapter, View layoutview, int width, int height,Context context ) {
		this.adapter = adapter;
		this.width = width;
		this.height = height;
		layoutview.setOnTouchListener(this);
		removeAllViews();
 		for (int i = 0; i < adapter.getCount(); i ++) {			
			if (i==3)
				break;
			View view=null;
			if(i==2)
			{
			    view = LayoutInflater.from(context).inflate(R.layout.viewpic_layout, null);
			    AsynLoadDetailImageView imgview = (AsynLoadDetailImageView) view.findViewById(R.id.software_image);
				imgview.setImageResource(R.drawable.software_default_img);
				imgview.setBackgroundResource(R.drawable.soft_shot_bg);
				imgview.setScaleType(ScaleType.FIT_XY);
				
			}else
			{
				  view = adapter.getView(i, null, null);				
				//	adapter.notifyDataSetChanged();

			}
			setOrientation(HORIZONTAL);
			width = (height/7*3)/3*2;
			LayoutParams layout = new LinearLayout.LayoutParams(width, height/7*3);
			layout.setMargins(1, 0, 1, 0);
			if(i==0){
				layout.setMargins(10, 0, 1, 0);
			}
		
			if (i==3) {
				layout.setMargins(1, 0, 10, 0);
			}
			
			addView(view, layout);
		}
		isLoadotherpic = true;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isLoadotherpic) {
			if(adapter.getCount()>2)
			 removeViewAt(2);
			
			for (int i = 2; i < adapter.getCount(); i ++) {
				View view = adapter.getView(i, null, null);
				adapter.notifyDataSetChanged();
				setOrientation(HORIZONTAL);
				width = (height/7*3)/3*2;
				LayoutParams layout = new LinearLayout.LayoutParams(width, height/7*3);
				layout.setMargins(1, 0, 1, 0);
				if(i==0){
					layout.setMargins(10, 0, 1, 0);
				}
			
				if (i==3) {
					layout.setMargins(1, 0, 10, 0);
				}
				
				addView(view, layout);
			}
			isLoadotherpic = false;
		}
		return false;
	}
}
