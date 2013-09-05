package com.appdear.client.commctrls;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdear.client.R;
import com.appdear.client.service.Constants;

/**
 * 星星和文字控件
 * @author zxy
 *
 */
public class StarTextControl extends LinearLayout implements AnimationListener {
	/**
	 * 显示星星的 ImageView 
	 */
	ImageView mStartImage;
	/**
	 * 显示题目的 TextView
	 */
	TextView mStartText;
	/**
	 * 图片的高度Height
	 */
	int mImgHeight=-1;

	/**
	 * 控件的宽度
	 */
	int  mWidth=-1;
	
	/**
	 * 字体颜色
	 */
	int mColor = -1;
	/**
	 * 字体大小
	 */
	int mFontSize = -1;
	
	/**
	 * 文字
	 */
	String mText = null;
	
	/**
	 * 图片资源
	 */
	int  mImageViewId = -1;
	/**
	 * 点击事件
	 */
	StarTextOnClick mTextOnClick ;
	/**
	 * 星星动画
	 */
	Animation starAnimation ;
	/**
	 * 标志 -- 序号 -- 可以提供访问的 顺序
	 */
	int mPosition = -1;
	
	/**
	 * 星星动画 -- 变化返回动画
	 */
	Animation starAnimation_return1 ;
	/**
	 * context 
	 */
	Context context ;
	
	/**
	 * 构造函数，可以直接在XML文件定义！
	 * @param context
	 * @param attrs
	 * @throws Exception 
	 */
	public StarTextControl(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);
		this.context = context ;
		this.setOrientation(LinearLayout.VERTICAL);
		mStartImage = new ImageView(context);
		mStartText = new TextView(context);
		starAnimation = AnimationUtils.loadAnimation(context, R.anim.startanimation);
		starAnimation_return1 = AnimationUtils.loadAnimation(context, R.anim.startanimation_return);
		starAnimation.setAnimationListener(this);
		starAnimation_return1.setAnimationListener(this);
		this.setGravity(Gravity.CENTER);
	}
	
	public void initUI(){
		//set control's attribute .
		if(mImgHeight==-1){
			this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			if (Constants.DEBUG)
				Log.e("StarTextControl", " width  not set ,use  LayoutParams.FILL_PARENT!");
		}else{
			this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,mImgHeight));
			if (Constants.DEBUG)
				Log.e("StarTextControl", " width   set ,use  mHeight:"+mImgHeight);
		}
		
		//设置控件的动画方式
		//TODO:控件移动淡入
		Animation animation = AnimationUtils.loadAnimation(context,R.anim.startextanimation);
    	this.startAnimation(animation);
		
		//set imageView  Attribute .
		mStartImage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		mStartImage.setScaleType(ScaleType.FIT_CENTER);
		
		if(mImageViewId == -1){
			if (Constants.DEBUG)
				Log.e("StarTextControl", " mImageViewId is not set  ");
	    	//throw new Exception(" mImageViewId is not set");
	    }else{
	    	mStartImage.setImageResource(mImageViewId);
	    }
		//set imageView animation .
		//TODO:星星闪烁
		mStartImage.startAnimation(starAnimation);
		
		//设置文字的属性
		mStartText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		//阴影
		mStartText.setShadowLayer(1, 2, 2, Color.WHITE);
		
		//color 
		if(mColor == -1){
			mColor = Color.WHITE;
			if (Constants.DEBUG)
				if (Constants.DEBUG) Log.i("StarTextControl ", " use default color :white");
		}else{
			mStartText.setTextColor(R.color.hese);
			if (Constants.DEBUG)
				 Log.i("StarTextControl ", " text color :"+mColor);
		}
		mStartText.setTextColor(R.color.hese);
		//set fontsize . 
		if(mFontSize != -1){
			mStartText.setTextSize(mFontSize);
			if (Constants.DEBUG)
				 Log.i("StarTextControl ", " text size :"+mFontSize);
		}else{
			//
		}
		//text .
		if(mText == null){
			mStartText.setText("文   字");
			if (Constants.DEBUG)
				Log.i("StarTextControl ", " text is not set  ");
		}else{
			mStartText.setText(mText);
		}
		
		//set onclick . 
		mStartText.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(mTextOnClick!=null)
					mTextOnClick.onClick(StarTextControl.this.mPosition);
			}
		});
		
		//add to this 
		this.removeAllViews();
		this.addView(mStartImage);
		this.addView(mStartText);
	}
	
	///属性设置函数
	public void setmColor(int mColor) {
		this.mColor = mColor;
	}
	
	public void setmFontSize(int mFontSize) {
		this.mFontSize = mFontSize;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	public void setmImageViewId(int mImageViewId) {
		this.mImageViewId = mImageViewId;
	}

	public void setmOnClick(StarTextOnClick mOnClick) {
		this.mTextOnClick = mOnClick;
	}

	public TextView getmStartText() {
		return mStartText;
	}
	
	public void setmImgHeight(int mImgHeight) {
		this.mImgHeight = mImgHeight;
	}
	
	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		switch(mPosition){
		case 1:
			this.starAnimation.setDuration(1*500);this.starAnimation_return1.setDuration(1*500);break;
		case 2:
			this.starAnimation.setDuration(2*500);this.starAnimation_return1.setDuration(2*500);break;
		case 3:
			this.starAnimation.setDuration(3*500);this.starAnimation_return1.setDuration(3*500);break;
		case 4:
			this.starAnimation.setDuration(4*500);this.starAnimation_return1.setDuration(4*500);break;
		case 5:
			this.starAnimation.setDuration(5*500);this.starAnimation_return1.setDuration(5*500);break;
		case 6:
			this.starAnimation.setDuration(6*500);this.starAnimation_return1.setDuration(6*500);break;
		case 7:
			this.starAnimation.setDuration(7*400);this.starAnimation_return1.setDuration(7*400);break;
		}
		this.mPosition = mPosition;
	}
	
	/**
	 * 回调
	 * @author zxy
	 */
	public interface StarTextOnClick{
		public void onClick(int pos);
	}

	public void onAnimationEnd(Animation animation) {
		if(animation.hashCode()==starAnimation.hashCode()){
			mStartImage.startAnimation(starAnimation);
		}else{
			mStartImage.startAnimation(starAnimation_return1);
		}
	}
	
	public void onAnimationRepeat(Animation animation) {
	}

	public void onAnimationStart(Animation animation) {
	}
}
