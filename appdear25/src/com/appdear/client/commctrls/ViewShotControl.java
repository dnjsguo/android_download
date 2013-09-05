package com.appdear.client.commctrls;

import java.util.LinkedHashSet;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.appdear.client.R;
import com.appdear.client.service.Constants;
//import com.appdear.client.utility.cache.LargeImageCache;

/**
 * 自定义ViewShot 查看软件截图；
 * @author zxy
 *
 */
public class ViewShotControl extends PopupWindow implements OnTouchListener {
	private enum  nav {left,right}
	List<String> imgurl = null;
	/**
	 * 图片切换控件
	 */
	 private ViewFlipper mViewFlipper;   
	 /**
	  * 图片展示控件 布局
	  */
	 private View mView;
	 /**
	  * 各个截图控件
	  */
	 private AsynLoadImageView mShotImage1; 
	 private AsynLoadImageView mShotImage2; 
	 private AsynLoadImageView mShotImage3; 
	 private LinearLayout shotContainer = null;
	 private String softid;
	 private PointView mPointView;
	 
	 /**
	  * 手势相关
	  */
	 private GestureDetector mGestureDetector;

	/**
	 * nowPos ，指示图片的位置！
	 */
	public  int nowPos = 1;
	/**
	 * 图片URL
	 */
	private List<String> urls;
	RelativeLayout bottom_switch ; 
	/**
	  * 上下文环境
	  * @param context
	  */
	 Context context ;
	 /**
	  * 各个屏幕
	  */
	 LinearLayout shot_view_1;
	 LinearLayout shot_view_2;
	 LinearLayout shot_view_3;
	 /**
	  * count 图片的总数
	  */
	 int mTotalCount = -1;
	 /**
	  * 当前图片位置 ;
	  * 用当前中间的图片位置代替。向左滑动 加 1，向右侧滑动 减1 ；
	  */
	 int nowPicPosition = -1;
	 /**
	  * 构造函数
	  * @param context
	  * @param contentView
	  * @param width
	  * @param height
	  * @param focusable
	  */
	 public ViewShotControl (Context context,View view , int width, int height, boolean focusable){
		 
		 super(view,width,height,focusable);
		 this.mView = view;
		 this.setContentView(mView);
		 mGestureDetector = new GestureDetector(context,new ViewShotGestureDetectorListener());
		 mView.setOnTouchListener(this);
		 initUi();
		 
		 this.setWidth(width);
		 this.setHeight(height);
		 
		 LinearLayout shot_inner_container = (LinearLayout)mView.findViewById(R.id.shot_inner_container);
		 //shot_inner_container.setLayoutParams(new LinearLayout.LayoutParams(width-width/5,height-height/5));
		 shot_inner_container.setGravity(Gravity.CENTER);
		 
		 ViewFlipper soft_shot_flipper = (ViewFlipper)this.mView.findViewById(R.id.soft_shot_flipper);
		 width = (height-height/5)/3*2;
		 soft_shot_flipper.setLayoutParams(new LinearLayout.LayoutParams(width,height-height/5));
		 this.context = context;
		 /**
		  * init 
		  */
		 shot_view_1 = (LinearLayout) mView.findViewById(R.id. shot_view_1);
		 shot_view_2 = (LinearLayout) mView.findViewById(R.id. shot_view_2);
		 shot_view_3 = (LinearLayout) mView.findViewById(R.id. shot_view_3);
		 
		 //
		 bottom_switch = (RelativeLayout)this.mView.findViewById(R.id.bottom_switch);
		 bottom_switch.setLayoutParams(new LinearLayout.LayoutParams(width, height/15));
		 //响应按键
		 //this.setBackgroundDrawable(new BitmapDrawable());
		 //this.setBackgroundDrawable(null);
		 this.setFocusable(true);
		 
		 shotContainer = (LinearLayout) mView.findViewById(R.id.shot_inner_container);
		 shotContainer.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) 
					ViewShotControl.this.dismiss(); 
				return false;
			}});
	 }
	
	private void initUi() {
		//Shot image container .
		mShotImage1 = (AsynLoadImageView)mView.findViewById(R.id.soft_shot_img1);
		mShotImage2 = (AsynLoadImageView)mView.findViewById(R.id.soft_shot_img2);
		mShotImage3 = (AsynLoadImageView)mView.findViewById(R.id.soft_shot_img3);
		
		mShotImage1.setScaleType(ScaleType.FIT_XY);
		mShotImage2.setScaleType(ScaleType.FIT_XY);
		mShotImage3.setScaleType(ScaleType.FIT_XY);
		
		
		mPointView = (PointView) mView.findViewById(R.id.pointview_id);
		//初始化
		mViewFlipper = (ViewFlipper) mView.findViewById(R.id.soft_shot_flipper);
	}

	/**
	 * 捕获点击事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (Constants.DEBUG)
			Log.i("Fling", "SoftWareInfoActivity onTouchEvent!");   
	    return this.mGestureDetector.onTouchEvent(event); 
	}
	
	 /** 
     * 定义从右侧进入的动画效果 
     * @return 
     */ 
    protected Animation inFromRightAnimation() { 
            Animation inFromRight = new TranslateAnimation( 
                            Animation.RELATIVE_TO_PARENT, +1.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f); 
            inFromRight.setDuration(500); 
            inFromRight.setInterpolator(new AccelerateInterpolator()); 
            return inFromRight; 
    } 

    /** 
     * 定义从左侧退出的动画效果 
     * @return 
     */ 
    protected Animation outToLeftAnimation() { 
            Animation outtoLeft = new TranslateAnimation( 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, -1.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f); 
            outtoLeft.setDuration(500); 
            outtoLeft.setInterpolator(new AccelerateInterpolator()); 
            return outtoLeft; 
    } 

    /** 
     * 定义从左侧进入的动画效果 
     * @return 
     */ 
    protected Animation inFromLeftAnimation() { 
            Animation inFromLeft = new TranslateAnimation( 
                            Animation.RELATIVE_TO_PARENT, -1.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f); 
            inFromLeft.setDuration(500); 
            inFromLeft.setInterpolator(new AccelerateInterpolator()); 
            return inFromLeft; 
    } 

    /** 
     * 定义从右侧退出时的动画效果 
     * @return 
     */ 
    protected Animation outToRightAnimation() { 
            Animation outtoRight = new TranslateAnimation( 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, +1.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f, 
                            Animation.RELATIVE_TO_PARENT, 0.0f); 
            outtoRight.setDuration(500); 
            outtoRight.setInterpolator(new AccelerateInterpolator()); 
            return outtoRight; 
    } 

    /**
     * 手势 
     * @author zxy
     */
	class ViewShotGestureDetectorListener extends SimpleOnGestureListener{
		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Constants.DEBUG)
				if (Constants.DEBUG) Log.i("SoftWareInfoActivity","[onFiling]:enter");
			if(e1.getX()>e2.getX()){//move left .
				if (Constants.DEBUG) Log.i("SoftWareInfoActivity","[onFiling]:move left");
				mViewFlipper.setInAnimation(inFromRightAnimation());
				mViewFlipper.setOutAnimation(outToLeftAnimation());
				mViewFlipper.showNext();
				changePos(ViewShotControl.this.nowPos,nav.left);
			}else if(e1.getX()<e2.getX()){//move right .
				mViewFlipper.setInAnimation(inFromLeftAnimation());
				mViewFlipper.setOutAnimation(outToRightAnimation());
				mViewFlipper.showPrevious();  
				changePos(ViewShotControl.this.nowPos,nav.right);
				if (Constants.DEBUG)
					if (Constants.DEBUG) Log.i("SoftWareInfoActivity","[onFiling]:move right");
			}
			return false;
		}
		
		/**
		 * 切换
		 * @param nowPos
		 * @param nav1
		 */
		private void changePos(int nowPos ,nav nav1){
			if (nav1 == nav.left) {
				if ((ViewShotControl.this.nowPos+1) >= ViewShotControl.this.mTotalCount){
					ViewShotControl.this.nowPos = 0;
				}else{
					ViewShotControl.this.nowPos++;
				}
			} else if (nav1 == nav.right) {
				if (ViewShotControl.this.nowPos <= 0){
					ViewShotControl.this.nowPos = ViewShotControl.this.mTotalCount-1;
				}else{
					ViewShotControl.this.nowPos--;
				}
			}
			AsynLoadImageView imageView = (AsynLoadImageView)mView.findViewById(ViewShotControl.this.nowPos);
			imageView.setImageUrl(urls.get(ViewShotControl.this.nowPos), true);
		//	mImageCurrent.setText((ViewShotControl.this.nowPos +1) +"/" + ViewShotControl.this.mTotalCount);
			//added by wangsong 2011-10-31
			mPointView.refreshData(mTotalCount, ViewShotControl.this.nowPos +1);
			mPointView.invalidate();
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
	}
	
	public void setDefaultBgResId(int defaultBgResId) {
		//设置背景
		if(defaultBgResId!=-1){
			mShotImage1.setBackgroundResource(defaultBgResId);
			mShotImage2.setBackgroundResource(defaultBgResId);
			mShotImage3.setBackgroundResource(defaultBgResId);
		}
	}
	
	public void setUrls(List<String> urls) {
		LinkedHashSet<String> set=null;
		this.urls = urls;
		this.mTotalCount = urls.size();
		mViewFlipper = (ViewFlipper) this.mView.findViewById(R.id.soft_shot_flipper);
		//flipper remove  All  Views
		mViewFlipper.removeAllViews();
		LinearLayout tempContainer = null;
//		if(LargeImageCache.getInstance().get(this.getSoftid())==null){
//			set=new LinkedHashSet<String>();
//		}
		for(int i = 0;i<urls.size();i++){
			tempContainer = getLinearLayout(urls.get(i),set, i);
//			tempContainer.setId(i);
			mViewFlipper.addView(tempContainer);
			if (Constants.DEBUG)
				Log.i("urls", "url :"+urls.get(i));
		}
//		if(LargeImageCache.getInstance().get(this.getSoftid())==null){
//			LargeImageCache.getInstance().put(this.getSoftid(),set);
//		}
	}
	
	/**
	 * 设定默认显示的View
	 * @param pos
	 */
	public void setCurrentView(int pos){
		this.nowPos = pos;
		if (Constants.DEBUG)
			Log.i("urls", "url position :"+ pos);
		//设置默认显示的View
		mViewFlipper.setDisplayedChild(pos);
		AsynLoadImageView imageView = (AsynLoadImageView)mView.findViewById(ViewShotControl.this.nowPos);
		imageView.setImageUrl(urls.get(ViewShotControl.this.nowPos), true,true);
		//  mImageCurrent.setText((pos+1)+"/" + ViewShotControl.this.mTotalCount);
		
		mPointView.refreshData(mTotalCount, pos + 1);
		//mPointView.invalidate();
	}

	public void setSourceList(List<String> imgurl) {
		this.imgurl = imgurl;
	} ;
	
	private  LinearLayout  getLinearLayout(String url,LinkedHashSet<String> set, int index){
		if(url!=null){
			if(set!=null)set.add(url);
		}
		LinearLayout  tempContainer = new LinearLayout(this.context);
		tempContainer.setLayoutParams(new ViewFlipper.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		tempContainer.setOrientation(LinearLayout.VERTICAL);
		com.appdear.client.commctrls.AsynLoadImageView  tempimg = new com.appdear.client.commctrls.AsynLoadImageView(this.context);
		tempimg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		tempimg.setDefaultImage(R.drawable.software_default_img);
		tempimg.setImageResource(R.drawable.software_default_img);
		tempimg.setId(index);
		if (index == 0)
			tempimg.setImageUrl(url, true);
		tempimg.setScaleType(ScaleType.FIT_XY);
		tempContainer.addView(tempimg);
		return tempContainer;
	}

	public String getSoftid() {
		return softid;
	}

	public void setSoftid(String softid) {
		this.softid = softid;
	}
}
