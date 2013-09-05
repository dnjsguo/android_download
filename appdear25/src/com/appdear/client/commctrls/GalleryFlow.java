/**
 * GalleryFlow
 * created at:2011-5-12上午10:25:13
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.commctrls;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;
public class GalleryFlow extends Gallery {

    /**
     * Graphics Camera used for transforming the matrix of ImageViews
     */
    private Camera mCamera = new Camera();
    /**
     * The Centre of the Coverflow
     */
    private int mCoveflowCenter;

    public GalleryFlow(Context context) {
            super(context);
            this.setStaticTransformationsEnabled(true);
            this.setSpacing(10);
    		//this.setSelection(1000000000);
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
            this.setSpacing(10);
    		//this.setSelection(1000000000);
    }

    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
            this.setSpacing(10);
    		//this.setSelection(1000000000);
    }

    /**
     * Get the Centre of the Coverflow
     * 
     * @return The centre of this Coverflow.
     */
    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }

    /**
     * Get the Centre of the View
     * 
     * @return The centre of the given view.
     */
    private  int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see #setStaticTransformationsEnabled(boolean)
     */
    protected boolean getChildStaticTransformation(View child, Transformation t) {
//            final int childCenter = getCenterOfView(child);
//            float scale = 0.8f;
//            int transSize = 0;
//            t.clear();
//            t.setTransformationType(Transformation.TYPE_MATRIX);
            //如果是当前对象则放大！
            //去掉放大效果
//            if (mCoveflowCenter == childCenter) {
//            	if (Constants.DEBUG) Log.i("equal ","equal"+childCenter);
//            	//放大
//            	changeToBigImage((ImageView)child,t,50);
//            } else{
//            	  transSize = (int)(scale*(Math.abs(mCoveflowCenter-childCenter))*3);
//            	  
//            	  if (Constants.DEBUG) Log.i("mCoveflowCenter", ""+mCoveflowCenter);
//            	  
//            	  changeToBigImage((ImageView)child,t,transSize);
//            }
            return true;
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     * 
     * @param w
     *            Current width of this view.
     * @param h
     *            Current height of this view.
     * @param oldw
     *            Old width of this view.
     * @param oldh
     *            Old height of this view.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mCoveflowCenter = getCenterOfCoverflow();
            super.onSizeChanged(w, h, oldw, oldh);
    }

   private void changeToBigImage(ImageView child,Transformation t,int zSize){
    	 mCamera.save();
    	 final Matrix imageMatrix = t.getMatrix();
    	 final int imageHeight = child.getLayoutParams().height;
         final int imageWidth = child.getLayoutParams().width;
         // 在Z轴上正向移动camera的视角，实际效果为放大图片。
         // 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
         mCamera.translate(0.0f, 0.0f,zSize);
         mCamera.getMatrix(imageMatrix);
         //根据距离调整大小 ，使得图片移动之后还可以在每一个Item中间显示,距离中心点越远越小
         //使放大后图片相对位置不变，调整矩阵 Preconcats matrix相当于右乘矩阵，Postconcats  matrix相当于左乘矩阵。
         imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
         imageMatrix.postTranslate((imageWidth / 2), (imageHeight/ 2));
         mCamera.restore();
    }
  
    /**
     * 控制 滑动 
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
    return false;
}

}
