package com.appdear.client;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.PagerCallback;
import com.appdear.client.commctrls.PagerContolerDetail;
import com.appdear.client.commctrls.TabCallBack;
import com.appdear.client.commctrls.TabbarCallback;
import com.appdear.client.commctrls.TabbarNewDetail;

/**
 * 类别二级界面(免费--收费--最新)
 * 
 * @author zhangxinyong
 *
 */
public class CategoryDetailsActivity extends BaseGroupActivity implements PagerCallback{
	/**
	 * 全局标志
	 */
	String TAG = CategoryDetailsActivity.class.getName();
	/**
	 * 头部标志
	 */
	TabbarNewDetail  mTopTab = null;
	TextView mTv_categoryDetailTitle = null;
	String categoryId = null;
	String categoryTitle = null;
	Intent onNewIntent;
	String tempCategoryId;
	/**
	 * 视图
	 */
	View v1 = null;
	View v2 = null;
	View v3 = null;
	
	View v4 = null;
	View v5 = null;
	View temp = null;
	/**
	 * 在页面暂停的时候用户选中的tab标签
	 */
	selectType type;
	private LinearLayout freeLayout;
	private LinearLayout feeLayout;
	private LinearLayout latestLayout;
	private LocalActivityManager mangaer;
	
	TextView tv_navigation;
	ImageButton btn_return;
	RelativeLayout tab_ll_linear;
	public   PagerContolerDetail pagerContoler ;
	 private LayoutInflater  mLayoutInflater ;
	public void onCreate(Bundle b){
		super.onCreate(b);
		this.setContentView(R.layout.category_app_detail_layout);
	}
	
	@Override
	public void init() {
		tv_navigation = (TextView) findViewById(R.id.tv_navigation);
		tab_ll_linear = (RelativeLayout) findViewById(R.id.layout);
		tab_ll_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_return = (ImageButton) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		categoryTitle = this.getIntent().getStringExtra("category_title");
		categoryId = this.getIntent().getStringExtra("category_id");
		tv_navigation.setText(this.getIntent().getStringExtra("category_navigation"));
		mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v4= mLayoutInflater.inflate(R.layout.pagerview_init, null);
		v5= mLayoutInflater.inflate(R.layout.pagerview_init, null);
		this.tempCategoryId = categoryId;
		type = selectType.TabOne;
		mangaer=getLocalActivityManager();
		
	}
	TabCallBack callback=new TabCallBack(){
		@Override
		public void callback(int position) {
		//	Log.i("info909", "moremanager======callback="+position);
			if(position == 0)
			{
				type = selectType.TabOne;
				if(v1==null){
					v1 = StartAcitivityForView(1);
				}
			}else if (position == 1) {
				type = selectType.TabSecond;
				if(v2==null){
					v2 = StartAcitivityForView(2);

				}
		 	}else if(position == 2)
		 	{
		 		type = selectType.TabThree;
				if(v3==null){
					v3 = StartAcitivityForView(3);
				}
		 	}
		}
	};
	@Override
	protected void onNewIntent(Intent intent) {
		this.onNewIntent = intent;
		categoryTitle = intent.getStringExtra("category_title");
		categoryId = intent.getStringExtra("category_id");
		//set null.
		if(!categoryId.equals(this.tempCategoryId)){
			v1 = null;
			v2 = null;
			v3 = null;
		}
		
		//tab 
		switch(type){
		case TabOne:
		if(!categoryId.equals(this.tempCategoryId)){
			v1 = StartAcitivityForView(1);
			freeLayout.removeAllViews();
			freeLayout.addView(v1);
		}
		
		break;
		case TabSecond:
		//
		if(!categoryId.equals(this.tempCategoryId)){
				v2 = StartAcitivityForView(2);
				feeLayout.removeAllViews();
				feeLayout.addView(v2);
		}	
		break;
		case TabThree:
		//
		if(!categoryId.equals(this.tempCategoryId)){
			    v3 = StartAcitivityForView(3);
				latestLayout.removeAllViews();
				latestLayout.addView(v3);
			}
		break;
		}
		super.onNewIntent(intent);
		this.tempCategoryId = categoryId;
	}
	
//	@Override
//	public void fistItemClickCallback() {
//		type = selectType.TabOne;
//		if(v1==null){
//			v1 = StartAcitivityForView(1);
//			freeLayout.removeAllViews();
//			freeLayout.addView(v1);
//		}
//	}
//	
//	@Override
//	public void secondItemClickCallback() {
//		type = selectType.TabSecond;
//		if(v2==null){
//			v2 = StartAcitivityForView(2);
//			feeLayout.removeAllViews();
//			feeLayout.addView(v2);
//		}
//	}
//
//	@Override
//	public void thirdItemClickCallback() {
//		type = selectType.TabThree;
//		if(v3==null||!categoryId.equals(this.tempCategoryId)){
//			v3 = StartAcitivityForView(3);
//			latestLayout.removeAllViews();
//			latestLayout.addView(v3);
//		}
//	}
	
	private enum selectType{
		TabOne,TabSecond,TabThree
	}
	
	private View StartAcitivityForView(int tabIndex){
		return getLocalActivityManager().startActivity(
				tabIndex+"",
             new Intent(CategoryDetailsActivity.this,CategoryDetailListActivity.class)
				.putExtra("category_id",categoryId).putExtra("tabIndex", tabIndex)
                     .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                     .putExtra("state", "refresh")
				).getDecorView();
	}
	@Override
	public void finish() {
//		Log.i("info","finish");
//		mangaer.destroyActivity("1",true);
//		mangaer.destroyActivity("2",true);
//		mangaer.destroyActivity("3",true);
		
		super.finish();
	}

	@Override
	public View viewFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View viewSecend() {
		// TODO Auto-generated method stub
		if(v2==null){
			v2 = StartAcitivityForView(2);
		}
		return v2;
	}

	@Override
	public View viewThird() {
		// TODO Auto-generated method stub
		if(v3==null){
			v3 = StartAcitivityForView(3);
		}
		return v3;
	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		inittab();
	}
	private void inittab(){
		mTopTab = (TabbarNewDetail)this.findViewById(0x1234);
		//1.将原有“免费”、“收费”、“最新”三个标签，修改为“最多下载”、“上升排行”、“最新上架”
		//4 按下载销量排序
		mTopTab.setTitle(new String[]{"最受欢迎", "最多下载", "最新上架"});
		
		//free .
		if (v1 == null) {
		v1 = mangaer.startActivity(
				"1",
                new Intent(CategoryDetailsActivity.this, CategoryDetailListActivity.class)
				.putExtra("category_id",categoryId).putExtra("tabIndex", 1)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        )
                .getDecorView();
		}
		pagerContoler = new PagerContolerDetail(this);
		pagerContoler.setTabCallback(callback);
		pagerContoler.initViewPager(mTopTab,v1,v4,v5,false,true,true);
	}
	
}
