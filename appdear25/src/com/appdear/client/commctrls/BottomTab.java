package com.appdear.client.commctrls;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appdear.client.MainActivity;
import com.appdear.client.R;
import com.appdear.client.service.SoftFormTags;

/**
 * 底部按钮控件
 * @author zxy
 *
 */
public class BottomTab extends LinearLayout implements OnClickListener {
    /**
     * 上下文环境
     */
	private Context context ;
	
	/**
	 * 主页
	 */
	private RelativeLayout firstClick;
	private ImageView firstpage;
	private ImageView firstpagebg;

	/**
	 * 类别
	 */
	private RelativeLayout categoryClick;
	private ImageView category;
	private ImageView categorybg;

	/**
	 * 排行
	 */
	private RelativeLayout rankingClick;
	private ImageView ranking;
	private ImageView rankingbg;

	/**
	 * 搜索
	 */
	private RelativeLayout searchClick;
	private ImageView search;
	private ImageView searchbg;
	
	/**
	 * 更多
	 */
	private RelativeLayout moreClick;
	private ImageView more;
	private ImageView morebg;
	
	/**
	 * 底部布局视图
	 */
	private View child ;
	
	/**
	 * 底部按钮项目点击的时候外部监听处理事件
	 */
	private BottomTabItemClick bottomClickListener;
	
	/**
	 * 当前操作的项目，应该高亮显示
	 */
	private int  itemId;
	
	/**
	 * 底部tab构造函数,不必自己手动实现！
	 * @param context
	 * @param attrs
	 */
	public BottomTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	    this.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater   layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		child = layoutInflater.inflate(R.layout.menutoolbar_tab_layout, null); 
		this.addView(child);
//		manager =((ActivityGroup) context).getLocalActivityManager();
		initBottomTab();
	}
	
	/**
	 * 初始化底部视图
	 */
	private void initBottomTab() {
		//container = (LinearLayout) findViewById(R.id.body);
	if(child!=null){
		
		firstpage = (ImageView) findViewById(R.id.firstpage_img);
		if(firstpage!=null){
			firstClick = (RelativeLayout) findViewById(R.id.home);
			firstpage = (ImageView) findViewById(R.id.firstpage_img);
			firstpagebg = (ImageView) findViewById(R.id.firstpage_bg);
			
			categoryClick = (RelativeLayout) findViewById(R.id.category);
			category = (ImageView) findViewById(R.id.category_img);
			categorybg = (ImageView) findViewById(R.id.category_bg);
			
			rankingClick = (RelativeLayout) findViewById(R.id.ranking);
			ranking = (ImageView) findViewById(R.id.ranking_img);
			rankingbg = (ImageView) findViewById(R.id.ranking_bg);
			
			searchClick = (RelativeLayout) findViewById(R.id.search);
			search = (ImageView) findViewById(R.id.search_img);
			searchbg = (ImageView) findViewById(R.id.search_bg);
			
			moreClick = (RelativeLayout) findViewById(R.id.more);
			more = (ImageView) findViewById(R.id.more_img);
			morebg = (ImageView) findViewById(R.id.more_bg);
			
			rankingbg.setVisibility(View.VISIBLE);
			
			firstClick.setOnClickListener(this);
			categoryClick.setOnClickListener(this);
			rankingClick.setOnClickListener(this);
			searchClick.setOnClickListener(this);
			moreClick.setOnClickListener(this);
		}
	}
	}


	/**
	 * 点击每一个按钮的时候跳到对应的activity
	 */
	@Override
	public void onClick(View v) {
		Intent i=new Intent();
		switch(v.getId()){
		case R.id.home:
			//首页
			if(bottomClickListener!=null)
			bottomClickListener.fistPageClick();
			//跳转到MainActivity ，在那个页面根据Flag加载想要的视图
		//	changeTabColor(R.id.firstpage_img);
			//跳转到MainActivity ，在那个页面根据Flag加载想要的视图
			i.putExtra(
					com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG, SoftFormTags.MAIN_FORM);
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.setClass(context,MainActivity.class);
			context.startActivity(i);
			break;
		case R.id.category:
			//类别
			if(bottomClickListener!=null)
			bottomClickListener.categoryClick();  
		//	changeTabColor(R.id.category_img);
			i.putExtra(
					com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG, SoftFormTags.CATEGORY_FORM);
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.setClass(context,MainActivity.class);
			context.startActivity(i);
//			container.removeAllViews();
//			if (categoryView == null) {
//				categoryView = manager.startActivity(
//						"category",
//						new Intent(context, CateGoryMainActivity.class)
//								.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
//						.getDecorView();
//			}
//			container.addView(categoryView);
			break;
		case R.id.ranking:
			//排行
			if(bottomClickListener!=null)
			bottomClickListener.rankingClick();
			changeTabColor(R.id.ranking_img);
			i.putExtra(
					com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG, SoftFormTags.USER_LIST_CENTER);
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.setClass(context,MainActivity.class);
			context.startActivity(i);
			break;
		case R.id.search:
			//搜索
			if(bottomClickListener!=null)
			bottomClickListener.searchClick();
			//changeTabColor(R.id.search_img);
			i.putExtra(
					com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG, SoftFormTags.SEARCH_FORM);
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.setClass(context,MainActivity.class);
			context.startActivity(i);
			break; 
		case R.id.more:
			//更多
			if(bottomClickListener!=null)
			bottomClickListener.moreClick();
//			changeTabColor(R.id.more_img);
			Intent aboutIntent4 = new Intent();
			//跳转到MainActivity ，在那个页面根据Flag加载想要的视图
			aboutIntent4.setClass(context, MainActivity.class);
			aboutIntent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			aboutIntent4.putExtra(SoftFormTags.ACTIVITY_SWITCH_FLAG,SoftFormTags.MORE_ITEM);
			context.startActivity(aboutIntent4);
//			Intent moreIntent = new Intent(context, MoreMainActivity.class)
//			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			context.startActivity(moreIntent);
			break;
		}
	}
	
	/**
	 * 高亮显示的Tab按钮ID
	 * @param id
	 */
	private void changeTabColor(int id){
		switch(id){
		case R.id.home:
			//首页
			firstpage.setBackgroundResource(R.drawable.main_page_btn_1);
			category.setBackgroundResource(R.drawable.category_page_btn_1);
			ranking.setBackgroundResource(R.drawable.ranking_page_btn_1);
			search.setBackgroundResource(R.drawable.search_page_btn_1);
			more.setBackgroundResource(R.drawable.more_page_btn_1);
			firstpagebg.setVisibility(View.VISIBLE);
			categorybg.setVisibility(View.GONE);
			rankingbg.setVisibility(View.GONE);
			searchbg.setVisibility(View.GONE);
			morebg.setVisibility(View.GONE);
			break;
		case R.id.category:
			//类别
			firstpage.setBackgroundResource(R.drawable.main_page_btn_1);
			ranking.setBackgroundResource(R.drawable.ranking_page_btn_1);
			search.setBackgroundResource(R.drawable.search_page_btn_1);
			more.setBackgroundResource(R.drawable.more_page_btn_1);
			firstpagebg.setVisibility(View.GONE);
			categorybg.setVisibility(View.VISIBLE);
			rankingbg.setVisibility(View.GONE);
			searchbg.setVisibility(View.GONE);
			morebg.setVisibility(View.GONE);
			break;
		case R.id.ranking:
			//排行
			firstpage.setBackgroundResource(R.drawable.main_page_btn_1);
			category.setBackgroundResource(R.drawable.category_page_btn_1);
			search.setBackgroundResource(R.drawable.search_page_btn_1);
			more.setBackgroundResource(R.drawable.more_page_btn_1);
			firstpagebg.setVisibility(View.GONE);
			categorybg.setVisibility(View.GONE);
			rankingbg.setVisibility(View.VISIBLE);
			searchbg.setVisibility(View.GONE);
			morebg.setVisibility(View.GONE);
			break;
		case R.id.search:
			//搜索
			firstpage.setBackgroundResource(R.drawable.main_page_btn_1);
			category.setBackgroundResource(R.drawable.category_page_btn_1);
			ranking.setBackgroundResource(R.drawable.ranking_page_btn_1);
			more.setBackgroundResource(R.drawable.more_page_btn_1);
			firstpagebg.setVisibility(View.GONE);
			categorybg.setVisibility(View.GONE);
			rankingbg.setVisibility(View.GONE);
			searchbg.setVisibility(View.VISIBLE);
			morebg.setVisibility(View.GONE);
			break;
		case R.id.more:
			//更多
			firstpage.setBackgroundResource(R.drawable.main_page_btn_1);
			category.setBackgroundResource(R.drawable.category_page_btn_1);
			ranking.setBackgroundResource(R.drawable.ranking_page_btn_1);
			search.setBackgroundResource(R.drawable.search_page_btn_1);
			firstpagebg.setVisibility(View.GONE);
			categorybg.setVisibility(View.GONE);
			rankingbg.setVisibility(View.GONE);
			searchbg.setVisibility(View.GONE);
			morebg.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	/**
	 * 点击接口
	 * @author zxy
	 *
	 */
	public interface BottomTabItemClick{
		public void fistPageClick();
		public void categoryClick();
		public void rankingClick();
		public void searchClick();
		public void moreClick();
	}
	
	/**
	 * 设置底部按钮点击监听类
	 * @param bottomClickListener
	 */
	public void setBottomClickListener(BottomTabItemClick bottomClickListener) {
		this.bottomClickListener = bottomClickListener;
	}

	/**
	 * 设置底部当前点击的按钮的ID，可取的值：
	 * R.id.category_img 、R.id.firstpage_img、等
	 * @param itemId
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
		//设置高亮显示的tab
		changeTabColor(itemId);
	}
}
