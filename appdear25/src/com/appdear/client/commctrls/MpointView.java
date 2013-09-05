package com.appdear.client.commctrls;





import com.appdear.client.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MpointView extends TextView {

	private int totalPic = -1; //图片的总数
	private int currentPicPos = -1;  // 当前显示的图片的索引（显示的时第几张）
	
	private int width;  
	private int height;  
	
	private int xStartPos; //绘制最左边第一个圆圈的x轴位置（从左往右依次绘制）
	private int yPos; //绘制的圆的y轴位置
	private int yPosOffset; //Y轴的偏移量
	
	private int pointDistance; //两个圆（点）之间的距离
	private int pointRadius; //圆（点）的半径 直径
	private int pointDiameter; //圆（点）的直径
	
	public MpointView(Context context) {
		super(context);
	}

	public MpointView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Bitmap greenPoint = BitmapFactory.decodeResource(getResources(), R.drawable.adpoints2);
		Bitmap whitePoint = BitmapFactory.decodeResource(getResources(), R.drawable.adpoints);
		
		//从左到右依次摆放图片
		for (int i = 0; i < this.totalPic; i++) {
			if (this.currentPicPos - 1  == i) {
				//如果是当前图片对应的圆，则将其特殊显示
				canvas.drawBitmap(greenPoint, 
						xStartPos + i * (pointDistance + pointRadius), 
						this.yPos, null);
				
				continue;
			}
			//其他圆则显示普通显示
			canvas.drawBitmap(whitePoint, 
					xStartPos + i * (pointDistance + pointRadius), 
					this.yPos, null);
		}
	}
	
	/**
	 * 刷新数据
	 * @param totalPic  总图片数
	 * @param currentPicPos 当前图片索引
	 */
	public void refreshData(int totalPic, int currentPicPos) {
		this.totalPic = totalPic;
		this.currentPicPos = currentPicPos;
		
		//计算出圆的y轴位置与x轴的起始绘制位置
		/**
		 * 算法：
		 * 如果图片数为偶数，则x轴的起始位置为总宽度（width）的一半减去（n-1）个（直径+距离），再减去一个半径与半个距离
		 * 如果图片数为基数，则x轴的起始位置为总宽度（width）的一半减去n个（直径+距离）
		 */
		if (totalPic % 2 == 0) {  //偶数
			this.xStartPos = this.width - 
					(totalPic+totalPic/2 - 1) * (this.pointDiameter + this.pointDistance)  -
					this.pointDistance / 2 - this.pointRadius;
		} else { //基数
			this.xStartPos = this.width - 
					(totalPic+totalPic/2) * (this.pointDiameter + this.pointDistance);
		}
		
		this.yPos = this.height / 2 - yPosOffset;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		this.width = w;
		this.height = h;
		
		this.pointRadius = 0; //圆的半径，根据不同分辨率自适应
		this.pointDiameter = pointRadius * 2; //圆的直径
		this.pointDistance =w/25; //两个圆的间隔距离
		
		yPosOffset = height/4 ;
		//更新数据
		refreshData(this.totalPic, this.currentPicPos);
	}
	
	/**
	 * 放大动画
	 * @return
	 */
/*	protected Animation toBigAnimation() {
		Animation anima = new ScaleAnimation(0.0f, 1.4f, 
				0.0f, 1.4f, 0.5f, 0.5f);
		anima.setDuration(1000);
		return anima;
	}*/
}
