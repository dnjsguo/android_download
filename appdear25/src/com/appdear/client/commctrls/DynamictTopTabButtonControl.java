package com.appdear.client.commctrls;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdear.client.R;
/**
 * 动态生成Tab页面 控件
 * @author zxy
 *
 */
public class DynamictTopTabButtonControl extends LinearLayout implements OnClickListener {
	OnDynamicTabButtonItemClick itemClick;
	int displayWidth = 0;
	int tabCount = 2 ;
	int maxTabCount = 6;
	int tabItemWidth = 0;
	int tabItemHeight = 40;
	int tabPaddingLeftRight = 0;
	Context context;
	String labelTextList [];
	public final static String itemButtonIdPrefix = "4321";
	public final static String itemContainerIdPrefix = "1234";
	public final static int outerContainerIdPrefix =12345;
	LinearLayout tabContainer = null;
	LinearLayout containerContainer = null;
	List<View> container = null;
	int displayHeight = 0;
	public DynamictTopTabButtonControl(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}
	
	/**
	 * 
	 * @param context
	 * @param tabCount
	 * @param labelTextList
	 */
	public DynamictTopTabButtonControl(Context context,int tabCount,String labelTextList []) {
		super(context);
		
		int [] temp = getCurrentPhoneWidth(context);
		displayWidth = temp[0];
		displayHeight = temp[1];
		
		container =  new ArrayList<View>();
		//this.setBackgroundColor(0xFFFFFF);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
		//tabContainer attribute .
		tabContainer = new LinearLayout(context);
		tabContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabContainer.setBackgroundResource(R.drawable.tab_bg);
	 
		//设置上部Tab高度
		tabContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,(int) (displayHeight*(17/(float)200))));
		tabContainer.setWeightSum(47);
		this.addView(tabContainer);
		
		//tabContainer.
		
		containerContainer = new LinearLayout(context);
		containerContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		containerContainer.setBackgroundResource(R.drawable.soft_list_item_unselected_bg);
		containerContainer.setWeightSum(7);
		 
		this.tabCount = tabCount;
		if(tabCount <= 0){
			this.tabCount = 1;
		}else if(tabCount>maxTabCount){
			this.tabCount = 1;
		}
				
		if(tabCount!=labelTextList.length){
			throw new  java.lang.IllegalArgumentException("标签数目 和 标签文字数量不一致");
		}
		
		this.labelTextList = labelTextList;
		
		//tabItemWidth = Math.round((displayWidth - ((tabCount-1)*2*tabPaddingLeftRight))/tabCount-1);
		tabItemWidth = Math.round(((displayWidth-(tabCount-1)*5)/tabCount));// - ((tabCount-1)*2*tabPaddingLeftRight))/tabCount-1);
		initTab();
	}
	
	private void initTab() {
		//tab item ;
		for(int i=0;i<tabCount;i++){
			/**
			 * 添加上部 TAB
			 */
			LinearLayout tabItem = new LinearLayout(context);
			//tabItem.setMinimumHeight(37);
			//控制标签剧中对其
			//Log.i("DynamictTopTabButtonControl", "DynamictTopTabButtonControl itemWidth :"+tabItemWidth);
			tabItem.setLayoutParams(new  LinearLayout.LayoutParams(tabItemWidth,LayoutParams.FILL_PARENT));
			TextView tv = new TextView(context);
			 
			//设置字体属性
			tv.setShadowLayer(1, 2, 2,Color.WHITE);
			if(i==0)
			tv.setTextColor(Color.rgb(42, 157, 198));
			else
			tv.setTextColor(Color.rgb(0, 0, 0));	
			
			tv.setSingleLine();
			tv.setTextSize(18);
			tv.setText(""+labelTextList[i]);
			tv.setLayoutParams(new  LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			//控制位置
			tv.setGravity(Gravity.CENTER);
			//设置点击
			tabItem.setOnClickListener(this);
			//set id .
			String tempitemButtonId = itemButtonIdPrefix+""+i;
			int itemButtonId = Integer.valueOf(tempitemButtonId);
			tabItem.addView(tv);
			tv.setPadding(0, 2, 0, 3);
			tabItem.setId(itemButtonId);	
			tabItem.setPadding(2, 2, 2, 3);
			
			tabContainer.addView(tabItem);

			/*//分隔符号
			if(i!=0||i!=tabCount-1){
				ImageView splitImage = new ImageView(context);
				splitImage.setPadding(2, 0, 2, 0); 
				splitImage.setImageResource(R.drawable.tab_spnner_line);
				tabContainer.addView(splitImage);
			} */
			tabContainer.setGravity(Gravity.CENTER_VERTICAL);
			
			/**
			 * 添加下部Container 
			 */
			LinearLayout tabItemContainer = new LinearLayout(context);
			tabItemContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			String tempContainerId = itemContainerIdPrefix+i;
			int containerId = Integer.valueOf(tempContainerId);
			
			//2011 09 20 修改 容器使用Tab的ID
			tabItemContainer.setId(itemButtonId);
			//tabItemContainer.setId(itemButtonId);
			if(i!=0)//设置第一个默认的不隐藏
			{
				tabItemContainer.setVisibility(View.GONE);
			}else{
				 
				tabItem.setBackgroundResource(R.drawable.tab_selected);
				
			}
			container.add(tabItemContainer);
			containerContainer.addView(tabItemContainer);
		}
		
		this.addView(containerContainer);
		//set id .
		containerContainer.setId(outerContainerIdPrefix);
	}
	
	private int [] getCurrentPhoneWidth(Context context){
		int [] params = new int[2];
		 Activity ac = (Activity)context;
		 params[0] = ac.getWindowManager().getDefaultDisplay().getWidth();
		 params[1] = ac.getWindowManager().getDefaultDisplay().getHeight();
		 return params;
	}
	
	public interface OnDynamicTabButtonItemClick{
		public void itemClick(View ll,int itemId);
	}
	
	/**
	 * 点击回调函数，maxTabCount 设置改变的时候适当增加或者减少switch的分支
	 */
	public void onClick(View v) {
		switch(v.getId()){
		case 43210:
			//
			changeContainerVisible(43210);
			 
			break;
		case 43211:
			//
			changeContainerVisible(43211);
			break;
		case 43212:
			//
			changeContainerVisible(43212);
			break;
		case 43213:
			//
			changeContainerVisible(43213);
			break;
		case 43214:
			//
			changeContainerVisible(43214);
			break;
		case 43215:
			//
			changeContainerVisible(43215);
			break;
		}
	}
	
	/**
	 * 点击改变container的可见性 ，
	 * @param visibleItemId visibleItemId 可见的视图ID
	 */
	private void changeContainerVisible(int visibleItemId){
		//2011 09 19 修改由于加入了“  getChildCount ”分割线导致第二个Tab点击事件无法触发
		for(int i =0;i<tabCount ;i++){
			View temp = tabContainer.getChildAt(i);
			if(temp.getId()==visibleItemId){
				//listener 
				if(itemClick!=null){
					itemClick.itemClick(temp,visibleItemId);
				}
				containerContainer.findViewById(visibleItemId).setVisibility(View.VISIBLE);
				//更改背景
				temp.setBackgroundResource(R.drawable.tab_selected); 
				 
			 //	 System.out.println("--------------"+((TextView)((LinearLayout) temp).getChildAt(0))+"-----Count="+((LinearLayout) temp).getChildCount()+"---"+((LinearLayout) temp).getChildAt(0));
 			 	 
					((TextView)((LinearLayout) temp).getChildAt(0)).setTextColor(Color.rgb(42, 157, 198));
				 
			}else{
				temp.setBackgroundResource(0); 
 				
				 if(temp.getClass().getName().equals("android.widget.LinearLayout"))
				{
					((TextView)((LinearLayout) temp).getChildAt(0)).setTextColor(Color.rgb(0, 0, 0));
				} 
			 
			}
 	     try{
				View temContainer = containerContainer.getChildAt(i);
			 	if(temContainer!=null&&temp.getId()!=visibleItemId)
				 temContainer.setVisibility(View.GONE);					
			}catch(java.lang.IndexOutOfBoundsException e){
				//do nothing .
			}
		}
		 
	}
	
	public void setItemClick(OnDynamicTabButtonItemClick itemClick) {
		this.itemClick = itemClick;
	}
	
}
