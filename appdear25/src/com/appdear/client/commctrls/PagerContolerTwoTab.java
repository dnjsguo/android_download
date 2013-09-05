package com.appdear.client.commctrls;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.R;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
 

public class PagerContolerTwoTab {
	public ViewPager mPager;//页卡内容
	public List<View> listViews; // Tab页面列表
	public ImageView cursor;// 动画图片
	public TextView t1, t2;// 页卡头标
	public int offset = 0;// 动画图片偏移量
	public int initOffset=0;
	public int currIndex = 0;// 当前页卡编号
	public int bmpW;// 动画图片宽度
	Matrix matrix;
	public boolean isUpdata1;
	public boolean isUpdate2;
 	public PagerCallback callback;
	private  PagerAdapter pagerAdapter;
    boolean isFirstMove=true;
    int startPage;
    public PagerContolerTwoTab(PagerCallback callback)
	{
		   this.callback=callback;
	}
	public void initTextView(String[] args,Activity activity) {
 		t1 = (TextView)  activity.findViewById(R.id.text1);
		t2 = (TextView)  activity.findViewById(R.id.text2);
		
        t1.setText(args[0]);
        t2.setText(args[1]);
 
      
        switch (currIndex) {
		case 0:
			t1.setTextColor(Color.parseColor("#5b6d78"));
			t2.setTextColor(Color.parseColor("#a7a7a7"));
			break;
		case 1:
			t2.setTextColor(Color.parseColor("#5b6d78"));
			t1.setTextColor(Color.parseColor("#a7a7a7"));
			break;
	 
		default:
			break;
		}
        
	}
	/**
	 * 初始化ViewPager
	 */
	public void initViewPager(Activity activity,View view1,View view2,boolean isUpdate1,boolean isUpdate2) {
		 mPager = (ViewPager) activity.findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		this.isUpdata1=isUpdate1;
        this.isUpdate2=isUpdate2;
 
		
        listViews.add(view1);
		listViews.add(view2);
 		
		mPager.setAdapter( getPagerAdapterInstance());		 
		mPager.setCurrentItem(currIndex);
		mPager.setOnPageChangeListener( getMyOnPageChangeListener(offset, bmpW, cursor));
 
		t1.setOnClickListener(   getMyOnClickListenerInstance(mPager,0));
		t2.setOnClickListener(   getMyOnClickListenerInstance(mPager,1));
 
	}
	/**
	 * 初始化动画
	 */
	public void initImageView(Activity activity,int startPage) {
		currIndex=startPage;
		cursor = (ImageView) activity.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(activity.getResources(), R.drawable.header_line)
				.getWidth();// 获取图片宽度  120
		//System.out.println("bmpW="+bmpW);
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度 480
		 
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量  20
	 
		//System.out.println("offset="+offset);
		if(startPage==0)
		{
			initOffset=offset;
		}else if(startPage==1)
		{
			initOffset=  offset+screenW / 2;
			 
		} 
	    matrix = new Matrix();
		matrix.postTranslate(initOffset, 0);
 
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
	 
		 @Override   
	      public int getCount()    {    
 	          return listViews.size();    
	       }    
	      @Override   
	      public Object instantiateItem( View pager, int position )    { 
 	    	  View v1=listViews.get(position);
	    	  
	          ((ViewPager)pager).addView(v1);     
	           return v1;   
	       }   
	        @Override 
	       public void destroyItem( View pager, int position, Object view )    {
 	                ((ViewPager)pager).removeView((View)view );   
	       }     
	        @Override   
	       public boolean isViewFromObject( View view, Object object )    {  
 	               return view.equals( object );  
	       }     
	        @Override   
	       public void finishUpdate( View view ) {}   
	       @Override   
	       public void restoreState( Parcelable p, ClassLoader c ) {}   
	         @Override 
	       public Parcelable saveState() {       
 	                  return null;  
	       } 
	        @Override 
	      public void startUpdate( View view ) {
 	      }
			@Override
			public int getItemPosition(Object object) {
				// TODO Auto-generated method stub
				return POSITION_NONE;
			}
		 
	}
	  public  PagerAdapter getPagerAdapterInstance()
	    {
		  pagerAdapter= new MyPagerAdapter();
	    	return pagerAdapter;
	    }
	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;
		private ViewPager mPager;//页卡内容
		MyOnClickListener(ViewPager pager,int i)
		{
			
			mPager=pager;
			index = i;
		}
		@Override
		public void onClick(View v) {
			t1.getId();
			switch (v.getId()) {
			case 0:
				t1.setTextColor(Color.parseColor("#5b6d78"));
				t2.setTextColor(Color.parseColor("#a7a7a7"));
				break;
			case 1:
				t2.setTextColor(Color.parseColor("#5b6d78"));
				t1.setTextColor(Color.parseColor("#a7a7a7"));
				break;
			 
		 
			}
			mPager.setCurrentItem(index);
		}
	};
    public View.OnClickListener getMyOnClickListenerInstance(ViewPager pager,int i)
    {
    	return new MyOnClickListener(pager,i);
    }
    
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one ;// 页卡1 -> 页卡2 偏移量
		ImageView cursor;
		MyOnPageChangeListener(int offset,int bmpW,ImageView cursor)
		{
 
			one = offset * 2 + bmpW;
		 
			this.cursor=cursor;
		}
		@Override
		public void onPageSelected(int arg0) {
			
            if(isFirstMove)
            {
            	matrix=new Matrix();
    			matrix.postTranslate(offset, 0);
    			cursor.setImageMatrix(matrix);// 设置动画初始位置
    			isFirstMove=false;
            }
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				}  
				
				t1.setTextColor(Color.parseColor("#5b6d78"));
				t2.setTextColor(Color.parseColor("#a7a7a7"));
				 if(isUpdata1)
				 {
					View view1=	callback.viewFirst();
					listViews.remove(arg0);
					listViews.add(arg0, view1);
					pagerAdapter.notifyDataSetChanged();
					isUpdata1=false;
				 }
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} 
				t2.setTextColor(Color.parseColor("#5b6d78"));
				t1.setTextColor(Color.parseColor("#a7a7a7"));
 				
				 if(isUpdate2)
				 {
					View view2=	callback.viewSecend();
					listViews.remove(arg0);
					listViews.add(arg0, view2);
					pagerAdapter.notifyDataSetChanged();
					isUpdate2=false;
				 }
				
				break;
			 
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	//OnPageChangeListener
	   public OnPageChangeListener getMyOnPageChangeListener(int offset,int bmpW,ImageView cursor)
	    {
		   return new MyOnPageChangeListener(offset,bmpW, cursor);
	    	//return new MyOnClickListener(pager,i);
	    }
   public interface PagerCallback {
	    View viewFirst();
		View viewSecend();
 
	}
}
