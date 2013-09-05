package com.appdear.client.commctrls;
import android.content.Context;   
import android.graphics.Canvas;   
import android.graphics.Color;   
import android.graphics.Paint;   
import android.util.AttributeSet;   
import android.widget.EditText;   
  /**
   * ÎÞ¿ò±à¼­
   * @author jdan
   *
   */
public class LineEditText extends EditText {   
  
    private Paint mPaint;   
    /**  
     * @param context  
     * @param attrs  
     */  
    public LineEditText(Context context, AttributeSet attrs) {   
        super(context, attrs);   
        // TODO Auto-generated constructor stub   
    }   
       
    @Override  
    public void onDraw(Canvas canvas)   
    {   
        super.onDraw(canvas);   
    }   
}  
