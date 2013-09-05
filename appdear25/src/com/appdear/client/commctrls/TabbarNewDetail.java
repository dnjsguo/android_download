package com.appdear.client.commctrls;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.R;
import com.appdear.client.service.Constants;
/**
 * top tab control ,usage see user book .
 * @author zxy
 *
 */
public class TabbarNewDetail extends LinearLayout {
	
	/**
	 * namespace
	 */
	private final String nameSpace = "http://meiyitianabc.blog.163.com";
	
	//each tab item
	/**
	 * the first
	 */
	private RelativeLayout firstItem = null;
	
	/**
	 * the second
	 */
	private RelativeLayout secondItem = null;
	
	/**
	 * the third
	 */
	private RelativeLayout thirdItem = null;
	
	/**
	 * 第一个tab的文本
	 */
	private TextView firstItemText = null;
	
	/**
	 * 第二个tab的文本
	 */
	private TextView secondItemText = null;
	
	/**
	 * 第三个tab的文本
	 */
	private TextView thirdItemText = null;

	
	
	/**
	 * click callback for  onClick .
	 */
	private TabbarCallback tabOnClick = null;
	
	/**
	 * 标题
	 */
	TextView tv1;
	/**
	 * 标题
	 */
	TextView tv2;
	/**
	 * 标题
	 */
	TextView tv3;
	
	/**
	 * 按钮上的普通状态文字颜色
	 */
	private int TEXT_NORMAL_COLOR = getResources().getColor(R.color.btn_text_color4);
	
	/**
	 * tab布局
	 */
	private LinearLayout topContainer = null;
	
	public TabbarNewDetail(Context context,AttributeSet attr) {
		super(context);
		
		this.setOrientation(LinearLayout.HORIZONTAL); 
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.tabbarnewdetail, this);
		
		//设置Tab的按键监听
		topContainer = (LinearLayout) view.findViewById(R.id.tabbar_layout_topContainer);
		
		//第一个Tab的按键监听
		firstItem = (RelativeLayout) topContainer.findViewById(R.id.tabbar_linearlayout_fistItem);
		firstItemText = (TextView) topContainer.findViewById(R.id.tabbar_tv_title_1);
		firstItem.requestFocus();
		firstItem.setFocusable(true);
		
		firstItemText.requestFocus();
		firstItemText.setFocusable(true);
		FrameLayout firstItemClick = (FrameLayout) topContainer.findViewById(R.id.tabbar_linearlayout_fistItemClick);
	//	firstItemClick.setOnClickListener(this);

		//firstItem.setOnClickListener(this);
		//firstItem.setPadding(6, 0, 22, 0);
		//firstItem.setBackgroundResource(0);
		//第二个Tab的按键监听
		secondItem = (RelativeLayout)topContainer.findViewById(R.id.tabbar_linearlayout_secondItem);
		secondItemText = (TextView) topContainer.findViewById(R.id.tabbar_tv_title_2);
		FrameLayout secondItemClick = (FrameLayout)topContainer.findViewById(R.id.tabbar_linearlayout_secondItemClick);
//		secondItemClick.setOnClickListener(this);
		//secondItem.setOnClickListener(this);
		//secondItem.setPadding(0, 0, 22, 0);
		//secondItem.setBackgroundResource(0);
		
		//第三个Tab的按键监听
		thirdItem = (RelativeLayout)topContainer.findViewById(R.id.tabbar_linearlayout_thirdItem);
		thirdItemText = (TextView) topContainer.findViewById(R.id.tabbar_tv_title_3);
		FrameLayout thirdItemClick = (FrameLayout)topContainer.findViewById(R.id.tabbar_linearlayout_thirdItemClick);
	//	thirdItemClick.setOnClickListener(this);
		//thirdItem.setOnClickListener(this);
		//thirdItem.setPadding(0, 0, 6, 0);
		//thirdItem.setBackgroundResource(0);
		//子View
//		contentContainer = (LinearLayout)view.findViewById(R.id.tabbar_layout_ContentContainer);
//
//		contentContainer1 = (LinearLayout) contentContainer.findViewById(R.id.tabbar_layout_ContentContainer_1);
//		contentContainer2 = (LinearLayout) contentContainer.findViewById(R.id.tabbar_layout_ContentContainer_2);
//		contentContainer3 = (LinearLayout) contentContainer.findViewById(R.id.tabbar_layout_ContentContainer_3);
		
		onClick(R.id.tabbar_linearlayout_fistItemClick);
		
		int id = attr.getAttributeIntValue(nameSpace, "id", 0x12345);
		if (id == 0x12345) {
			if (Constants.DEBUG)
				Log.i("TopTabbar", "sorry ,TopTabbar id is not set , you can access it by default id 0x12345");
			setId(0x12345);
		} else{
			setId(id);
		}
	}
	 
//	@Override
//	public void onClick(View v) {
//		if (v == null)
//			return;
//		onClick(v.getId());
//	}
//Handler handler=new Handler(){
//	@Override
//	public void handleMessage(Message msg) {
//		// TODO Auto-generated method stub
//			if(msg.arg1==R.id.tabbar_linearlayout_fistItemClick){
//				firstItem.setBackgroundResource(R.drawable.tab_selected_newz);
//				secondItem.setBackgroundResource(0);
//				thirdItem.setBackgroundResource(0);
//				
//				firstItemText.setTextColor(TEXT_NORMAL_COLOR);
//				secondItemText.setTextColor(TEXT_NORMAL_COLOR);
//				thirdItemText.setTextColor(TEXT_NORMAL_COLOR);
//			}else if(msg.arg1==R.id.tabbar_linearlayout_secondItemClick){
//				firstItem.setBackgroundResource(0);
//				secondItem.setBackgroundResource(R.drawable.tab_selected_newzhong);
//				thirdItem.setBackgroundResource(0);
//				firstItemText.setTextColor(TEXT_NORMAL_COLOR);
//				secondItemText.setTextColor(TEXT_NORMAL_COLOR);
//				thirdItemText.setTextColor(TEXT_NORMAL_COLOR);
//			}else if(msg.arg1==R.id.tabbar_linearlayout_thirdItemClick){
//				firstItem.setBackgroundResource(0);
//				secondItem.setBackgroundResource(0);
//				thirdItem.setBackgroundResource(R.drawable.tab_selected_newy);
//
//				firstItemText.setTextColor(TEXT_NORMAL_COLOR);
//				secondItemText.setTextColor(TEXT_NORMAL_COLOR);
//				thirdItemText.setTextColor(TEXT_NORMAL_COLOR);
//			}
//	}
//	
//};
	/**
	 * 按键响应
	 * @param id
	 */
	public void onClick(int id) {
		//Message msg=handler.obtainMessage();
		switch(id){
		case R.id.tabbar_linearlayout_fistItemClick:
			//set whitch tab view should light .
//			msg.arg1=R.id.tabbar_linearlayout_fistItemClick;
			firstItem.setBackgroundResource(R.drawable.tab_selected_newz);
			secondItem.setBackgroundResource(0);
			thirdItem.setBackgroundResource(0);
//			contentContainer1.setVisibility(View.VISIBLE);
//			contentContainer2.setVisibility(View.GONE);
//			contentContainer3.setVisibility(View.GONE);
			
			firstItemText.setTextColor(TEXT_NORMAL_COLOR);
			secondItemText.setTextColor(TEXT_NORMAL_COLOR);
			thirdItemText.setTextColor(TEXT_NORMAL_COLOR);
			
			//set callback .
			
			break;
		case R.id.tabbar_linearlayout_secondItemClick:
		//	msg.arg1=R.id.tabbar_linearlayout_secondItemClick;
			//set whitch tab view should light .
			firstItem.setBackgroundResource(0);
			secondItem.setBackgroundResource(R.drawable.tab_selected_newzhong);
			thirdItem.setBackgroundResource(0);

//			contentContainer1.setVisibility(View.GONE);
//			contentContainer2.setVisibility(View.VISIBLE);
//			contentContainer3.setVisibility(View.GONE);
			
			firstItemText.setTextColor(TEXT_NORMAL_COLOR);
			secondItemText.setTextColor(TEXT_NORMAL_COLOR);
			thirdItemText.setTextColor(TEXT_NORMAL_COLOR);
//			//set callback .
			
			break;  
		case R.id.tabbar_linearlayout_thirdItemClick:
//			msg.arg1=R.id.tabbar_linearlayout_thirdItemClick;
			firstItem.setBackgroundResource(0);
			secondItem.setBackgroundResource(0);
			thirdItem.setBackgroundResource(R.drawable.tab_selected_newy);

//			contentContainer1.setVisibility(View.GONE);
//			contentContainer2.setVisibility(View.GONE);
//			contentContainer3.setVisibility(View.VISIBLE);
			
			firstItemText.setTextColor(TEXT_NORMAL_COLOR);
			secondItemText.setTextColor(TEXT_NORMAL_COLOR);
			thirdItemText.setTextColor(TEXT_NORMAL_COLOR);

			break;
		}
	//	handler.sendMessageDelayed(msg,100);
		//handler.sendMessage(msg);
	}
	
	/**
	 * 设置焦点
	 * @param id
	 */
	public void setFocus(int id) {
		onClick(id);
	}

	/**
	 * 设置Tab的标题topbar_font_color
	 */
	public void setTitle(String[] title) {
		
		tv1 = ((TextView) findViewById(R.id.tabbar_tv_title_1));
		tv1.setText(title[0]); 
//		tv1.setShadowLayer(1, 2, 2, TEXT_NORMAL_COLOR);
//		tv1.setTextColor(R.color.topbar_font_color);
		tv1.setTextColor(TEXT_NORMAL_COLOR);
	
		tv2 = ((TextView) findViewById(R.id.tabbar_tv_title_2));
		tv2.setText(title[1]);
//		tv2.setShadowLayer(1, 2, 2, TEXT_NORMAL_COLOR);
//		tv2.setTextColor(Color.BLACK);
		tv2.setTextColor(TEXT_NORMAL_COLOR);
	
		tv3 = ((TextView) findViewById(R.id.tabbar_tv_title_3));
		tv3.setText(title[2]);  
//		tv3.setShadowLayer(1, 2, 2, TEXT_NORMAL_COLOR);
//		tv3.setTextColor(Color.BLACK);
		tv3.setTextColor(TEXT_NORMAL_COLOR);
		//效果
	}

	
	public LinearLayout getTopContainer() {
		return topContainer;
	}
	
}
