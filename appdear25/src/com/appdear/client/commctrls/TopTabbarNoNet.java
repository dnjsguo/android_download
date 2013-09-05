package com.appdear.client.commctrls;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.AlertDialogView;
import com.appdear.client.AlterUserRegActivity;
import com.appdear.client.MoreSettingsActivity;
import com.appdear.client.MyCommentlistActivity;

import com.appdear.client.R;
import com.appdear.client.SearchActivity;
import com.appdear.client.SearchResultActivity;
import com.appdear.client.SoftWareDetailInfoActivity;
import com.appdear.client.SoftwareDetailAuthorInfoActivity;
import com.appdear.client.SoftwareDetailCommentActivity;
 
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;
/**
 * top tab control ,usage see user book .
 * @author zxy
 *
 */
public class TopTabbarNoNet extends LinearLayout implements OnClickListener {
	 
	/**
	 * namespace
	 */
	private final String nameSpace = "http://meiyitianabc.blog.163.com";
	/**
	 * tab文字颜色（普通状态）
	 */
	private static final int TEXT_NORMAL_COLOR = Color.parseColor("#8be4f7");
	
	/**
	 * tab文字颜色（按下颜色）
	 */
	private static final int TEXT_PRESSED_COLOR = Color.WHITE;
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
	 * 子view布局
	 */
	private LinearLayout contentContainer;
	
	/**
	 * 子view
	 */
	private View contentContainer1;
	private View contentContainer2;

	
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
	 * tab布局
	 */
	private LinearLayout topContainer = null;
	
	public TopTabbarNoNet(Context context,AttributeSet attr) {
		super(context);
		
		this.setOrientation(LinearLayout.HORIZONTAL); 
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.tabbarnonet, this);
		
		//设置Tab的按键监听
		topContainer = (LinearLayout) view.findViewById(R.id.tabbar_layout_topContainer);
		
		//第一个Tab的按键监听
		firstItem = (RelativeLayout) topContainer.findViewById(R.id.tabbar_linearlayout_fistItem);
		FrameLayout firstItemClick = (FrameLayout) topContainer.findViewById(R.id.tabbar_linearlayout_fistItemClick);
		firstItemClick.setOnClickListener(this);

		//firstItem.setOnClickListener(this);
		//firstItem.setPadding(6, 0, 22, 0);
		//firstItem.setBackgroundResource(0);
		//第二个Tab的按键监听
		secondItem = (RelativeLayout)topContainer.findViewById(R.id.tabbar_linearlayout_secondItem);
		FrameLayout secondItemClick = (FrameLayout)topContainer.findViewById(R.id.tabbar_linearlayout_secondItemClick);
		secondItemClick.setOnClickListener(this);
		//secondItem.setOnClickListener(this);
		//secondItem.setPadding(0, 0, 22, 0);
		//secondItem.setBackgroundResource(0);
		

		contentContainer = (LinearLayout)view.findViewById(R.id.tabbar_layout_ContentContainer);

		contentContainer1 = (LinearLayout) contentContainer.findViewById(R.id.tabbar_layout_ContentContainer_1);
		contentContainer2 = (LinearLayout) contentContainer.findViewById(R.id.tabbar_layout_ContentContainer_2);
		
		
		//firstItem.setBackgroundResource(R.drawable.tab_selected);
//		secondItem.setBackgroundResource(0);

		
		contentContainer1.setVisibility(View.VISIBLE);
		contentContainer2.setVisibility(View.GONE);

		
		int id = attr.getAttributeIntValue(nameSpace, "id", 0x12345);
		if (id == 0x12345) {
			if (Constants.DEBUG)
				Log.i("TopTabbar", "sorry ,TopTabbar id is not set , you can access it by default id 0x12345");
			setId(0x12345);
		} else{
			setId(id);
		}
	}
	 
	@Override
	public void onClick(View v) {
		if (v == null)
			return;
		onClick(v.getId());
	}
	
	/**
	 * 按键响应
	 * @param id
	 */
	private void onClick(int id) {
		switch(id){
		case R.id.tabbar_linearlayout_fistItemClick:
			
			if(tv1!=null)
			{
				tv1.setBackgroundResource(R.drawable.tab_bottom_white_line);
				tv2.setBackgroundResource(0);
				tv1.setTextColor(TEXT_PRESSED_COLOR);
				tv2.setTextColor(TEXT_NORMAL_COLOR);
				
			}			
			contentContainer1.setVisibility(View.VISIBLE);
			contentContainer2.setVisibility(View.GONE);
			//set callback .
			if(tabOnClick!=null)
			tabOnClick.fistItemClickCallback();
			break;
		case R.id.tabbar_linearlayout_secondItemClick:
			tv1.setBackgroundResource(0);
			tv2.setBackgroundResource(R.drawable.tab_bottom_white_line);
			tv1.setTextColor(TEXT_NORMAL_COLOR);
			tv2.setTextColor(TEXT_PRESSED_COLOR);			
			//set whitch content view should visibile .
			contentContainer1.setVisibility(View.GONE);
			contentContainer2.setVisibility(View.VISIBLE);
			//set callback .
			if(tabOnClick!=null){
				tabOnClick.secondItemClickCallback();
			}
			break;  
		}
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
		
		/*tv1 = ((TextView) findViewById(R.id.tabbar_tv_title_1));
		tv1.setText(title[0]); 
		//tv1.setShadowLayer(1, 2, 2, Color.WHITE);
		tv1.setTextColor(TEXT_PRESSED_COLOR);
		tv1.setBackgroundResource(R.drawable.tab_bottom_white_line);
		
		//tv1.setTextColor(R.color.topbar_font_color);
	
		tv2 = ((TextView) findViewById(R.id.tabbar_tv_title_2));
		tv2.setText(title[1]);
		//tv2.setShadowLayer(1, 2, 2, Color.WHITE);
		tv2.setTextColor(TEXT_NORMAL_COLOR);*/
		
		tv1 = ((TextView) findViewById(R.id.tabbar_tv_title_1));
		tv1.setText(title[0]); 
		//tv1.setShadowLayer(1, 2, 2, Color.WHITE);
		tv1.setTextColor(TEXT_PRESSED_COLOR);
		tv1.setBackgroundResource(R.drawable.tab_bottom_white_line);
		//tv1.setTextColor(R.color.topbar_font_color);
	
		tv2 = ((TextView) findViewById(R.id.tabbar_tv_title_2));
		tv2.setText(title[1]);
		//tv2.setShadowLayer(1, 2, 2, Color.WHITE);
		tv2.setTextColor(TEXT_NORMAL_COLOR);
		//tv2.setTextColor(R.color.topbar_font_color);  
		
	
		 
	}
	
	public LinearLayout getContentContainer() {
		return contentContainer;
	}
	
	public LinearLayout getTopContainer() {
		return topContainer;
	}
	
	/**
	 * 设置监听
	 * @param tabOnClick
	 */
	public void setTabOnClick(TabbarCallback tabOnClick) {
		this.tabOnClick = tabOnClick;
	}
	private boolean handler(Object obj) {
		boolean flag = true;
		if (obj instanceof ListBaseActivity
				|| obj instanceof SoftwareDetailAuthorInfoActivity
				|| obj instanceof SoftwareDetailCommentActivity
				|| obj instanceof SoftWareDetailInfoActivity 
				||obj instanceof AlterUserRegActivity) {
			if (obj instanceof SearchActivity
					|| obj instanceof SearchResultActivity 
					|| obj instanceof MyCommentlistActivity) {
				return false;
			}
			return flag;
		} else {
			return false;
		}
	}
}
