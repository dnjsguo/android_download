package com.appdear.client.commctrls;

import java.util.List;

import com.appdear.client.R;
import com.appdear.client.utility.ServiceUtils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;

/**
 * 自动翻页广告
 * @author zqm
 *
 */
public class AdView extends View implements AnimationListener {

	private List<String> urls = null;
	//private MpointView pointView = null;
	private ViewFlipper flipper = null;
	private DisplayMetrics  metrics;
	private int nPost = 1;
	private int height;
	/**
	 * 广告总条数
	 */
	private int mCount = 0;
	
	private Context context;
	
	public AdView(Context context,int height) {
		super(context);
		this.height=height;
		this.context = context;
	}
	/**
	 * 添加url
	 * @param urls
	 * @param resourceId
	 */
	public void setUrls(List<String> urls, int resourceId) {
		if (urls == null || urls.size() == 0) {
			return;
		}
		if(flipper.getChildCount()>0){
			flipper.removeAllViews();
		}
		this.urls = urls;
		mCount = urls.size();
//		pointView.refreshData(mCount, nPost);
//		pointView.invalidate();
		for (int i = 0; i < urls.size(); i ++) {
			LinearLayout container = new LinearLayout(this.context);
			container.setLayoutParams(new ViewFlipper.LayoutParams(LayoutParams.FILL_PARENT, height));
			container.setOrientation(LinearLayout.VERTICAL);
			
			AsynLoadImageView imageview = new AsynLoadImageView(context);
			imageview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageview.setDefaultImage(resourceId);
			imageview.setImageResource(resourceId);
			imageview.setId(i);
			imageview.setImageUrl(urls.get(i), true);
			imageview.setScaleType(ScaleType.FIT_XY);
			imageview.setTag(i+1);
			container.addView(imageview);
			if (flipper != null)
				flipper.addView(container);
			if(i==0)break;
		}
	}
	public void setDefaultView()
	{
		LinearLayout container = new LinearLayout(this.context);
		container.setLayoutParams(new ViewFlipper.LayoutParams(LayoutParams.FILL_PARENT, height));
		container.setOrientation(LinearLayout.VERTICAL);
		AsynLoadImageView imageview = new AsynLoadImageView(context);
		imageview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		imageview.setDefaultImage(R.drawable.ad_src);
		imageview.setImageResource(R.drawable.ad_src);
		container.addView(imageview);
		if (flipper != null)
			flipper.addView(container);

	}
	public void setFlipper(ViewFlipper flipper) {
		this.flipper = flipper;
//		flipper.setInAnimation(inFromRightAnimation());
//		flipper.setOutAnimation(outToLeftAnimation());
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
    	outtoLeft.setAnimationListener(this);
    	outtoLeft.setDuration(500); 
    	outtoLeft.setInterpolator(new AccelerateInterpolator());
    	return outtoLeft; 
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
     * 定义从左侧进入的动画效果 
     * @return 
     */ 
    protected Animation inFromLeftAnimation() { 
        Animation inFromLeft = new TranslateAnimation( 
                        Animation.RELATIVE_TO_PARENT, -1.0f, 
                        Animation.RELATIVE_TO_PARENT, 0.0f, 
                        Animation.RELATIVE_TO_PARENT, 0.0f, 
                        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setAnimationListener(this);
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
        outtoRight.setAnimationListener(this);
        outtoRight.setDuration(500); 
        outtoRight.setInterpolator(new AccelerateInterpolator()); 
        return outtoRight; 
    }
	
//	public void setPointView(MpointView pointView) {
//		this.pointView = pointView;
//	}

	@Override
	public void onAnimationEnd(Animation animation) {
		nPost=(Integer) ((LinearLayout)flipper.getCurrentView()).getChildAt(0).getTag();
//		nPost++;
//		if (nPost > mCount)
//			nPost = 1;
//		if (pointView != null) {
//			pointView.refreshData(mCount, nPost);
//			pointView.invalidate();
//		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		Log.i("", "onAnimationRepeat");
	}

	@Override
	public void onAnimationStart(Animation animation) {
		Log.i("", "onAnimationStart");
	}

	public int getnPost() {
		return nPost-1;
	}
}
