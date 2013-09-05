package com.appdear.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class NoviceGuideActivity extends Activity implements OnGestureListener,
		OnTouchListener {
	private ViewFlipper mFlipper;
	private GestureDetector mGestureDetector;
	private int mCurrentLayoutState;
	private int mTotalCount=-1;
	private int nowPos=1;
	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 200;
	private static final int  IMAGE_VIEW1 = 0;
	private static final int  IMAGE_VIEW2 = 1;
	private static final int  IMAGE_VIEW3 = 2;
	private ImageView imageView01;
	private ImageView imageView02;
	private ImageView imageView03;
	private String isRoll_cancel = "false";
	
	int i=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novice_guide_layout);
		mFlipper = (ViewFlipper) findViewById(R.id.mViewFlipper);
		imageView01 = (ImageView) findViewById(R.id.imageView01);
		imageView02 = (ImageView) findViewById(R.id.imageView02);
		imageView03 = (ImageView) findViewById(R.id.imageView03);
		mGestureDetector = new GestureDetector(this);
		mFlipper.setOnTouchListener(this);
		mCurrentLayoutState = 0;
		mFlipper.setLongClickable(true);
		
		isRoll_cancel = getIntent().getStringExtra("roll_cancel");

	}

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

	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			mFlipper.setInAnimation(inFromRightAnimation());
			mFlipper.setOutAnimation(outToLeftAnimation());
			mFlipper.showNext();
			i--;
			if(i==-1){
				i=1;
			}
			switch (i) {
			case IMAGE_VIEW1:
				if (isRoll_cancel.equals("true")) {
					finish();
					break;
				}
				imageView02.setImageResource(android.R.drawable.presence_invisible);
				imageView01.setImageResource(android.R.drawable.presence_online);
				break;
			case IMAGE_VIEW2:
				imageView01.setImageResource(android.R.drawable.presence_invisible);
				imageView02.setImageResource(android.R.drawable.presence_online);
				
				break;

			
			}
			
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			mFlipper.setInAnimation(inFromLeftAnimation());
			mFlipper.setOutAnimation(outToRightAnimation());
			mFlipper.showPrevious();
			i++;
			if(i==2){
				i=0;
			}
			switch (i) {
			case IMAGE_VIEW1:
				imageView02.setImageResource(android.R.drawable.presence_invisible);
				imageView01.setImageResource(android.R.drawable.presence_online);
				break;
			case IMAGE_VIEW2:
				if (isRoll_cancel.equals("true")) {
					finish();
					break;
				}
				imageView01.setImageResource(android.R.drawable.presence_invisible);
				imageView02.setImageResource(android.R.drawable.presence_online);
				break;

			
			}
		}		
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

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

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return mGestureDetector.onTouchEvent(event);
	}

}
