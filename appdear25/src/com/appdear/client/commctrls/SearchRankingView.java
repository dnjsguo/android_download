package com.appdear.client.commctrls;

import java.util.List;
import java.util.Random;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appdear.client.MainActivity;
import com.appdear.client.R;
import com.appdear.client.SearchActivity;
import com.appdear.client.SearchResultActivity;
import com.appdear.client.Adapter.ElideUpdateListAdapter;
import com.appdear.client.Adapter.KeyWordlistAdatper;
import com.appdear.client.model.SearchList;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.SoftFormTags;

public class SearchRankingView extends LinearLayout {
	LinearLayout container = null;
	LocalActivityManager manager = null;
	MainActivity activity = null;
	Context mContext;
	ListViewRefresh mListViewRefresh;
	private KeyWordlistAdatper adapter;
	private Button qihuan_button;
	private QHListener listener;
	private SearchList<String> keywordlist;

	public QHListener getListener() {
		return listener;
	}

	public void setListener(QHListener listener) {
		this.listener = listener;
		if (adapter != null) {
			adapter.setListener(listener);
		}
	}

	public void setDivider(Drawable drawable) {
		mListViewRefresh.setDivider(drawable);
	}

	public void setDividerHeight(int spiner) {
		mListViewRefresh.setDividerHeight(spiner);
	}

	public KeyWordlistAdatper getAdapter() {
		return adapter;
	}

	public void setAdapter(KeyWordlistAdatper adapter) {
		this.adapter = adapter;
	}

	public SearchRankingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		init();
	}

	public SearchRankingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public void setKeywordlist(SearchList<String> keywordlist) {
		this.keywordlist = keywordlist;
		if (adapter != null) {
			adapter.setList(keywordlist);
			adapter.notifyDataSetChanged();
		}
	}

	void init() {

		View view = inflate(mContext, R.layout.rankingview, null);
		mListViewRefresh = (ListViewRefresh) view
				.findViewById(R.id.soft_elide_list);
		TextView detail_info = new TextView(mContext);
		detail_info.setPadding(20, 7, 20, 7);
		detail_info.setTextColor(Color.parseColor("#888888"));
		detail_info.setBackgroundColor(Color.parseColor("#e8ebed"));
		detail_info.setGravity(Gravity.CENTER);
		detail_info.setTextSize(16);
		detail_info.setText("ËÑË÷ÈÈ´Ê°ñ");
		detail_info.setGravity(Gravity.LEFT);
		detail_info.setClickable(false);
		mListViewRefresh.addHeaderView(detail_info);
		mListViewRefresh.setAdapter(adapter = new KeyWordlistAdatper(mContext));
		
		this.addView(view, LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mListViewRefresh
				.setOnItemClickListener(new ListView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (keywordlist != null) {
							if (keywordlist.size() < arg2 || (arg2 -1)<0) {
								return;
							} else {
								startSearchResultActivity(
										keywordlist.get(arg2 - 1), "2");
							}
						}
					}
				});
	}

	public void startSearchResultActivity(String key, String searchway) {
		/*
		 * Intent search = new Intent(mContext, SearchResultActivity.class);
		 * search.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * search.putExtra("keyWord", key.trim());
		 * search.putExtra("searchway",searchway);
		 * mContext.startActivity(search);
		 */
		activity = (MainActivity) MyApplication.getInstance().mainActivity;
		manager = activity.getLocalActivityManager();
		View searchView = null;
		container = (LinearLayout) activity.findViewById(R.id.body);
		BaseGroupActivity.currentType = SoftFormTags.SEARCH_RESULT;
		activity.setFocus(BaseGroupActivity.currentType);
		activity.unbindDrawables(container);
		Intent search = new Intent(mContext, SearchResultActivity.class);
		search.putExtra("keyWord", key.trim());
		search.putExtra("searchway", searchway);
		searchView = manager.startActivity("searchresult",
				search.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
				.getDecorView();
		searchView.clearFocus();
		container.addView(searchView);
		MyApplication.getInstance().mView.put("searchresult", searchView);
	}

}
