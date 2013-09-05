package com.appdear.anim;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MuneTabAnimation extends Animation {
	private Matrix matrix;
	private int width,height;
	public MuneTabAnimation(int width,int height){
		this.width=width;
		this.height=height;
	}
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// TODO Auto-generated method stub
		matrix=t.getMatrix();
		matrix.preRotate(interpolatedTime*360);
		matrix.preScale(interpolatedTime,interpolatedTime);
		matrix.preTranslate(-width,-height);
		matrix.postTranslate(width*interpolatedTime,height*interpolatedTime);
		super.applyTransformation(interpolatedTime, t);
	}
	
}
