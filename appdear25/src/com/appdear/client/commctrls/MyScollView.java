package com.appdear.client.commctrls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyScollView extends HorizontalScrollView {

	private float mLastMotionX;
	private float mLastMotionY;
	boolean is;

	int deltaY, deltaX;
	public ScrollChangedCallBack lister;
	public boolean isShow = true;
	public int count; // 图片总数

	public void setScrollChangedListener(ScrollChangedCallBack lister) {
		this.lister = lister;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MyScollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * n (n-2)/2 * width= l 3 3-2 4 4-2 5 5-2 6 6-2
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// 0 向右指 1 向右指消失 2 向左指 3 左指消失
		/*
		 * System.out.println("l="+l+"   t="+t+"   oldl="+oldl+"   oldt="+oldt);
		 * System.out.println("width="+this.getWidth());
		 * System.out.println("count="+count);
		 */
		if (count > 2) {
			int width = this.getWidth();
			// float x= (count-2)/2) *width;

			if ((l > oldl) && (l != width) && (isShow == true)) {
				lister.scrollChangedCallback(1);
				isShow = false;
			} else if (getScrollX() + getWidth() == computeHorizontalScrollRange()) {

				isShow = true;
				lister.scrollChangedCallback(2);

			} else if ((l < oldl)
					&& (getScrollX() + getWidth() != computeHorizontalScrollRange())
					&& (isShow == true)) { // getScrollX()+getWidth()==computeHorizontalScrollRange())
				isShow = false;
				lister.scrollChangedCallback(3);
			} else if (l == 0) {
				isShow = true;
				lister.scrollChangedCallback(0);
			}
		}

		super.onScrollChanged(l, t, oldl, oldt);
	}
	

}
