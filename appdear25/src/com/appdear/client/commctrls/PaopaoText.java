package com.appdear.client.commctrls;
 
 
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.R;
import com.appdear.client.service.Constants;

public class PaopaoText  extends RelativeLayout implements OnClickListener{

	private int fontSize = 14;
	private String text = "ÅÝÅÝ";
	private String textColor = "#4C4894";
	private int pos = 1;
	private PaopaoTextOnClick clickListener;
	

	public PaopaoText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setAttribute(Context context,int fontSize,String text,String textColor,int pos) {
		this.fontSize = fontSize;
		this.text= text ;
		
		this.textColor = textColor ;
		this.pos = pos;
		this.removeAllViews();
		
//		this.setOrientation(LinearLayout.VERTICAL);
		//
		this.setOnClickListener(this);
		this.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout root = (LinearLayout) inflater.inflate(R.layout.paopaotext_layout,null);
		
		//imageView 
		TextView temImg = (TextView) root.findViewById(R.id.paopao_image);
//		temImg.setScaleType(ScaleType.FIT_XY);
		
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int densityDpi  = metrics.densityDpi;
	
	
 
		if(densityDpi == DisplayMetrics.DENSITY_HIGH){
			changeFontSize_height();
		
				
			temImg.setLayoutParams(new RelativeLayout.LayoutParams(2*this.fontSize *text.length()-10,2*this.fontSize));
		}
		else if(densityDpi == DisplayMetrics.DENSITY_MEDIUM){
			changeFontSize_medium();
		
				 
			temImg.setLayoutParams(new RelativeLayout.LayoutParams(this.fontSize *text.length()+9,2*this.fontSize));
		}
		else if(densityDpi == DisplayMetrics.DENSITY_LOW){
			changeFontSize_low();
		
			temImg.setLayoutParams(new RelativeLayout.LayoutParams((int) (0.85*this.fontSize *text.length()+7),2*this.fontSize));
		}else{
			
			temImg.setLayoutParams(new RelativeLayout.LayoutParams(this.fontSize *text.length()+5,2*this.fontSize));
		}
		
		//text ;
		TextView tv = (TextView) root.findViewById(R.id.paopao_text);
		tv.setText(text);
		
		tv.setTextSize(this.fontSize); 
		tv.setTextColor(Color.parseColor(textColor));
		//ÒõÓ°
		tv.setShadowLayer(1, 2, 2, Color.WHITE);
		tv.setPadding(4,2,4,0);
		
	//	temImg.setBackgroundColor(Color.BLUE);
		this.addView(root);
	
	}
	
	private void changeFontSize_height(){
		if(text.trim().length()==1)
			fontSize = (int) (22);
		if(text.trim().length()==2)
			fontSize = (int) (22);
		if(text.trim().length()==3)
			fontSize = (int) (22);
		if(text.trim().length()==4)
			fontSize = (int) (22);
		if(text.trim().length()>=5){
			fontSize = (int) (22);
			text = text.substring(0,4);
		}
	}
	private void changeFontSize_medium(){
		if(text.trim().length()==1)
			fontSize = (int) (20);
		if(text.trim().length()==2)
			fontSize = (int) (20);
		if(text.trim().length()==3)
			fontSize = (int) (20);
		if(text.trim().length()==4)
			fontSize = (int) (20);
		if(text.trim().length()>=5){
			fontSize = (int) (20);
			text = text.substring(0,4);
		}
	}
	private void changeFontSize_low(){
		if(text.trim().length()==1)
			fontSize = (int) (18);
		if(text.trim().length()==2)
			fontSize = (int) (18);
		if(text.trim().length()==3)
			fontSize = (int) (18);
		if(text.trim().length()==4)
			fontSize = (int) (18);
		if(text.trim().length()>=5){
			fontSize = (int) (18);
			text = text.substring(0,4);
		}
	}
	
	@Override
	public void onClick(View v) {
		 if(this.clickListener != null){
			 this.clickListener.onClick(pos);
			 if (Constants.DEBUG)
				Log.i("PaopaoText", "PaopaoText: object id :"+pos+" is clicked !");
		 }
	}
	
	public interface PaopaoTextOnClick{
		public void onClick(int pos);
	}
	
	public void setClickListener(PaopaoTextOnClick clickListener) {
		this.clickListener = clickListener;
	}
}
