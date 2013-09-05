/**
 * SearchActivity.java
 * created at:2011-5-16下午01:38:13
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */
package com.appdear.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.StarTextControl;
import com.appdear.client.commctrls.StarTextControl.StarTextOnClick;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.utility.BitmapTemp;
import com.appdear.client.utility.ImageCache;
import com.appdear.client.utility.ScreenManager;
import com.appdear.client.utility.ServiceUtils;
import com.appdear.client.R;

/**
 * 搜索
 * 
 * @author zqm
 */
public class SearchResultActivity extends ListBaseActivity implements
		StarTextOnClick, OnClickListener {
	/**
	 * 输入框
	 */
	private AutoCompleteTextView inputKeyView;
	TextView num = null;

	private ApiSoftListResult result;

	private Button searchButton;

	private final int SHOW_SEARCH_DIALOG = 1234056;
	private final int CANCEL_SEARCH_DIALOG = 12034567;
	private final int SET_ADAPTER = 1234567;

	private static final int UPDATE_DATA = 0;
	/**
	 * ImageView 删除图标
	 */
	ImageView mBtn_del;

	private static int PAGE_SIZE = 25;
	/**
	 * 关键字推荐ID
	 */
	private int keywordid = 100008;

	private String orderType = "0";
	List<String> keyWordList;
	/**
	 * 控件
	 */
	StarTextControl d2start1;
	StarTextControl d2start2;
	StarTextControl d2start3;
	StarTextControl d2start4;
	StarTextControl d2start5;
	StarTextControl d2start6;
	StarTextControl d2start7;
	/**
	 * Dialog
	 */
	public static Dialog dialog = null;
	/**
	 * 
	 */
	LinearLayout mResultLayout;

	/**
	 * 下拉表的适配器
	 */
	ArrayAdapter<String> mAutocompleteAdapter = null;

	/**
	 * 自动下拉列表
	 */
	List<String> AutoWordList;

	List<SoftlistInfo> listData = null;
	/**
	 * 更新下拉列表
	 */
	private final int UPDATE_AUTOCOMPLETE = 0X002;

	private String keyWord;
	private String searchway = "1";

	/**
	 * 键盘弹出状态
	 */
	private boolean iskeyShowFlag = false;

	// ImageCache imageCache =
	// AsyLoadImageService.getInstance().getImageCache();
	private boolean flag = false;

	// private static int count;

	public synchronized boolean getFalg() {
		return flag;
	}

	public synchronized void setFalg(boolean flag) {
		this.flag = flag;
	}

	TextView tv_navigation;
	ImageButton btn_return;
	RelativeLayout tab_ll_linear;
	RelativeLayout searchres;
	Animation a;
	/**
	 * 线程池
	 */
	AbstractExecutorService pool = new ThreadPoolExecutor(1, 10, 3L,
			TimeUnit.SECONDS, new SynchronousQueue(),
			new ThreadPoolExecutor.DiscardOldestPolicy());
	/**
	 * autoListItemClickListener
	 */
	private OnItemClickListener autoListItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			iskeyShowFlag = true;
			String key = inputKeyView.getText().toString().trim();
			startSearchResultActivity(key, "3");
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search_result_layout);
		MyApplication.getInstance().searchResultActivity=this;
		DisplayMetrics metrics = new DisplayMetrics(); 
	    	this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    	
	    	float ds=metrics.density;
	    	int dsdpi=metrics.densityDpi;
			width=metrics.widthPixels;
			height=metrics.heightPixels;
			params = new LayoutParams(width,
					height);
//		params = new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT);
		loadingView = new MProgress(this, true);
		loadingView.setVisibility(View.VISIBLE);
		a = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		a.setFillAfter(true);
		a.setDuration(2000);
		this.addContentView(loadingView, params);
		if (!AppContext.getInstance().isNetState) {
			handler1.sendEmptyMessage(LOADG);
			showRefreshButton();
			return;
		}
		// count++;
	}

	@Override
	public void init() {

		ScreenManager.getScreenManager().pushActivity(this);// 压栈

		mResultLayout = (LinearLayout) findViewById(R.id.search_resultList_layout);
		searchButton = (Button) findViewById(R.id.search_button);
		inputKeyView = (AutoCompleteTextView) findViewById(R.id.auto_complete);
		// actv.set
		inputKeyView.addTextChangedListener(mTextWatcher);
		inputKeyView.setOnItemClickListener(autoListItemClickListener);
		inputKeyView.setOnClickListener(this);
		// initStar();
		listData = new ArrayList<SoftlistInfo>();
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		/*
		 * listView.setDivider(getResources().getDrawable(R.drawable.listspiner))
		 * ; listView.setDividerHeight(2);
		 */

		listView.setCacheColorHint(Color.TRANSPARENT);

		// 接收关键字
		keyWord = this.getIntent().getStringExtra("keyWord");
		// 搜索方式
		searchway = this.getIntent().getStringExtra("searchway");
		// listview.setRefreshDataListener(this);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Log.i("tst", "tst button.setOnClickListener");
				if (ServiceUtils.checkNetState(SearchResultActivity.this)) {
					String key = inputKeyView.getText().toString().trim();
					if (keyWord != null && keyWord.trim().equals(key.trim())) {
						if ("".equals(key)) {
							Toast.makeText(SearchResultActivity.this,
									"关键字不可为空...", Toast.LENGTH_SHORT).show();
						}
						return;
					}
					hideInputMode();
					// actv.setInputType(InputType.TYPE_NULL);
					clear();
					if ("".equals(key)) {
						Toast.makeText(SearchResultActivity.this, "关键字不可为空...",
								Toast.LENGTH_SHORT).show();
					} else {
						startSearchResultActivity(key, "1");
					}
				} else {
					Toast.makeText(SearchResultActivity.this, "手机没有开启网络",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		if ("".equals(keyWord)) {
			Toast.makeText(this, "搜索关键字为空.", Toast.LENGTH_SHORT).show();
		} else {
			if (Common.autosearch == false) {
				mAutocompleteAdapter = new ArrayAdapter<String>(
						SearchResultActivity.this,
						android.R.layout.simple_dropdown_item_1line,
						new String[] {});
				SearchResultActivity.this.handler
						.sendEmptyMessage(UPDATE_AUTOCOMPLETE);
				hideInputMode();
				Common.autosearch = true;
				this.inputKeyView.setText(keyWord);
				// return;
			}
			// handler.sendEmptyMessage(SHOW_SEARCH_DIALOG);
			// showWaitingDialog(SearchResultActivity.this, true);
			updateUIStart();
			pool.execute(new SearchThread(keyWord, searchway));
			this.inputKeyView.setText(keyWord);
		}
		// 删除图标
		mBtn_del = (ImageView) findViewById(R.id.mBtn_del);
		mBtn_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = inputKeyView.getText().toString().trim();
				if (Common.autosearch == false) {
					inputKeyView.dismissDropDown();
					Common.autosearch = true;
					return;
				} else {
					if (!"".equals(content)) {
						inputKeyView.setText("");
						// clear();
						// adapter = new SoftwarelistAdatper(
						// SearchResultActivity.this, listData, listview);
						// listview.setAdapter(adapter);
						// adapter.notifyDataSetChanged();
						// ((TextView) findViewById(R.id.search_result_num))
						// .setText("搜索结果");
						hideInputMode();
						// Intent i = new Intent();
						// listview.setEndTag(true);
						// i.putExtra(
						// com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG,
						// SoftFormTags.SEARCH_FORM);
						// i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						// i.setClass(SearchResultActivity.this,
						// MainActivity.class);
						// SearchResultActivity.this.startActivity(i);
					}
				}
			}
		});

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
		tv_navigation.setText("搜索-" + keyWord);

		searchres = (RelativeLayout) findViewById(R.id.searchres);

		this.getWindow().getDecorView().requestFocus();
		adapter = new SoftwarelistAdatper(SearchResultActivity.this, listData,
				listView);
	}

	public void startSearchResultActivity(String key, String searchway) {
		/*
		 * clear(); // 显示结果界面 Intent resultIntent = new Intent();
		 * Common.autosearch = false; hideInputMode();
		 * resultIntent.setClass(SearchResultActivity.this,
		 * SearchResultActivity.class);
		 * resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * resultIntent.putExtra("keyWord", key);
		 * resultIntent.putExtra("searchway", searchway);
		 * startActivity(resultIntent);
		 */
		clear();
		Common.autosearch = false;
		hideInputMode();
		// 显示结果界面
		View searchView = null;
		LinearLayout container = null;
		LocalActivityManager manager = null;
		MainActivity activity = null;
		activity = (MainActivity) MyApplication.getInstance().mainActivity;
		container = (LinearLayout) activity.findViewById(R.id.body);
		manager = activity.getLocalActivityManager();
		BaseGroupActivity.currentType = SoftFormTags.SEARCH_RESULT;
		activity.setFocus(BaseGroupActivity.currentType);
		activity.unbindDrawables(container);
		Intent search = new Intent(SearchResultActivity.this,
				SearchResultActivity.class);
		search.putExtra("keyWord", key.trim());
		search.putExtra("searchway", searchway);
		searchView = manager.startActivity("searchresult",
				search.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
				.getDecorView();
		searchView.clearFocus();
		container.addView(searchView);
		MyApplication.getInstance().mView.put("searchresult", searchView);
	}

	/**
	 * 监听变化
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String content = null;

			content = inputKeyView.getText().toString().trim();
			if (!"".equals(content)) {
				if (content.length() == 2) {
					// 到服务器匹配关键字
					requestServerStrings(content);
				}
			} else {
				inputKeyView.dismissDropDown();
				keyWord = "";
			}
			mBtn_del = (ImageView) findViewById(R.id.mBtn_del);
			if (!"".equals(content)) {
				mBtn_del.setImageResource(R.drawable.sreach_closed_buts);
			} else {
				mBtn_del.setImageResource(0);
			}
			if (Constants.DEBUG)
				Log.i("searchActivity", "onTextChanged ...");
		}
	};

	@Override
	public void initData() {
		try {
			// 显示关键字
			keywordid = SharedPreferencesControl.getInstance().getInt("301",
					com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
			ApiSoftListResult keywordRresult = ApiManager.keywordlist(keywordid
					+ "", "0", page + "", PAGE_SIZE + "");
			if (keywordRresult != null)
				keyWordList = keywordRresult.keywordList;
		} catch (ApiException e) {
			showException(e);
			e.printStackTrace();
		}
		super.initData();
	}

	private class SearchThread implements Runnable {
		String keyWord = "";
		String searchway = "";

		@SuppressWarnings("unused")
		public SearchThread(String key, String searchway) {
			this.keyWord = key;
			this.searchway = searchway;
		}

		@Override
		public void run() {
			try {
				Common.autosearch = false;
				if (keyWord != "") {
					clear();
					result = ApiManager.resultsoftlist(keyWord, orderType, "1",
							PAGE_SIZE + "", searchway);
					if (result == null)
						return;
					if (listData != null && listData.size() == 0) {
						PAGE_TOTAL_SIZE = (result.totalcount % PAGE_SIZE == 0 ? result.totalcount
								/ PAGE_SIZE
								: result.totalcount / PAGE_SIZE + 1);
						if (page > PAGE_TOTAL_SIZE) {
							listView.setEndTag(true);
						}
						AddListData();
						ServiceUtils.setSoftState(SearchResultActivity.this,
								result.softList);
						// adapter = new
						// SoftwarelistAdatper(SearchResultActivity.this,
						// listData, listview);
						// 改为使用高亮显示搜索关键字的适配器
						adapter = new SoftwarelistAdatper(
								SearchResultActivity.this, listData, listView,
								keyWord);
						handler.sendEmptyMessage(UPDATE_DATA);

						if (page < PAGE_TOTAL_SIZE)
							listView.setRefreshDataListener(SearchResultActivity.this);
					}
				}
				handler.sendEmptyMessage(CANCEL_SEARCH_DIALOG);
			} catch (ApiException e) {
				showException(e);
				showRefreshButton();
				handler.sendEmptyMessage(CANCEL_SEARCH_DIALOG);
			}
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_DATA:
				updateUI1();
				break;
			case UPDATE_AUTOCOMPLETE:
				SearchResultActivity.this.inputKeyView
						.setAdapter(SearchResultActivity.this.mAutocompleteAdapter);
				SearchResultActivity.this.mAutocompleteAdapter
						.notifyDataSetChanged();
				break;
			case SHOW_SEARCH_DIALOG:
				// showWaitingDialog(SearchResultActivity.this, true);
				break;
			case CANCEL_SEARCH_DIALOG:
				// showWaitingDialog(SearchResultActivity.this, false);
				updateUIEnd();
				break;
			case SET_ADAPTER:
				if (listView == null)
					return;
				listView.setAdapter(adapter);
				break;
			}
		}
	};

	/**
	 * 根据关键字匹配
	 */
	private void requestServerStrings(String key) {
		if (Common.autosearch == false) {
			// mAutocompleteAdapter = new ArrayAdapter<String>(
			// SearchResultActivity.this,
			// android.R.layout.simple_dropdown_item_1line,
			// new String[] {});
			// SearchResultActivity.this.handler
			// .sendEmptyMessage(UPDATE_AUTOCOMPLETE);
			Common.autosearch = true;
			return;
		}
		new RequestStringsThread(key).start();
	};

	/**
	 * 网络线程
	 * 
	 * @author zxy
	 */
	class RequestStringsThread extends Thread {
		String content = "";

		public RequestStringsThread(String key) {
			this.content = key;
		}

		@Override
		public void run() {
			// Log.i("info8", "SearchResultActivity="+Common.autosearch+"=");
			if (Constants.DEBUG)
				Log.i("aotusearch", "init-" + Common.autosearch
						+ "-RequestStringsThread-SearchResultActivity");

			// keywordid = SharedPreferencesControl.getInstance().getInt("301",
			// com.appdear.client.commctrls.Common.SECTIONCODEXML,
			// SearchActivity.this);
			ApiSoftListResult autoCompRresult = null;
			try {
				AutoWordList = null;
				if (!"".equals(content))
					autoCompRresult = ApiManager.autolist(content, "1", "1",
							"1", "6");
			} catch (ApiException e) {
				e.printStackTrace();
			}

			if (autoCompRresult != null) {
				AutoWordList = autoCompRresult.autolist;
				if (AutoWordList != null) {
					mAutocompleteAdapter = new ArrayAdapter<String>(
							SearchResultActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							AutoWordList);
					SearchResultActivity.this.handler
							.sendEmptyMessage(UPDATE_AUTOCOMPLETE);
				}
			}
		}
	}

	/**
	 * 当前项操作
	 * 
	 * @param position
	 */
	public void setSelectedValues(int position) {
		ServiceUtils.setSelectedValues(this, listData, position);
	}

	@Override
	public void refreshDataUI() {
		if (result == null)
			return;
		if (getPage() > 1) {
			if (getPage() == PAGE_TOTAL_SIZE) {
				AddListData();
				addPage();

			} else if (getPage() < PAGE_TOTAL_SIZE) {
				AddListData();
			}
		} else {
			addPage();
		}
	}

	@Override
	public void doRefreshData() {
		try {
			if (getPage() < PAGE_TOTAL_SIZE) {
				addPage();
			} else {
				listView.setEndTag(true);
				return;
			}
			if (getPage() > 1 && getPage() <= PAGE_TOTAL_SIZE) {
				result = null;
				result = ApiManager.resultsoftlist(keyWord, orderType,
						String.valueOf(getPage()), PAGE_SIZE + "", "");
				ServiceUtils.setSoftState(this, result.softList);
			}
		} catch (ApiException e) {
			// 抛异常不添加数据，页数减一
			result = null;
			subPage();
			e.printStackTrace();
			if (listView != null)
				listView.setErrTag(true);
		}
		return;
	}

	@Override
	public void onClick(int pos) {
		this.inputKeyView.setText(keyWordList.get(pos - 1));
		Common.autosearch = false;
		startSearchResultActivity(keyWordList.get(pos - 1), "2");
		mResultLayout.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		 
		// 设置软键盘为不显示
		iskeyShowFlag = false;
		super.onResume();
	}

	/**
	 * 控件按键事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		// 软键盘弹出状态
		if (v.getId() == R.id.auto_complete) {
			iskeyShowFlag = true;
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
 		// Log.i("tst", "tst button.setOnClickListener");
		if (inputKeyView.length() > 0) {
			Selection.setSelection(inputKeyView.getEditableText(),
					inputKeyView.length());
		}
		// 接收关键字
		clear();

		keyWord = intent.getStringExtra("keyWord");
		tv_navigation.setText("搜索-" + keyWord);
		String searchway = intent.getStringExtra("searchway");
		if ("".equals(keyWord)) {
			Toast.makeText(this, "搜索关键字为空.", Toast.LENGTH_SHORT).show();
		} else {
			// showWaitingDialog(SearchResultActivity.this, true);
			updateUIStart();
			pool.execute(new SearchThread(keyWord, searchway));
			this.inputKeyView.setText(keyWord);
		}
		this.getWindow().getDecorView().requestFocus();
		super.onNewIntent(intent);
	}

	/**
	 * 返回事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// 判断软键盘是否弹出
			if (iskeyShowFlag) {
				hideInputMode();
				iskeyShowFlag = false;
				return true;
			} else {
				// 返回到搜索主界面
				// Intent i = new Intent();
				// i.putExtra(
				// com.appdear.client.service.SoftFormTags.ACTIVITY_SWITCH_FLAG,
				// SoftFormTags.SEARCH_FORM);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// i.setClass(SearchResultActivity.this,
				// MainActivity.class);
				// SearchResultActivity.this.startActivity(i);

				return super.dispatchKeyEvent(event);
			}
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideInputMode() {
		View view = SearchResultActivity.this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager
					.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private synchronized void clear() {
		if(listView!=null){
			listView.setEndTag(false);
			listView.setRefreshTag(true);
			page = 1;
			if (num != null)
				num.setText("");
			listData = new ArrayList<SoftlistInfo>();
			adapter = new SoftwarelistAdatper(SearchResultActivity.this, listData,
					listView);
			SearchResultActivity.this.handler.sendEmptyMessage(SET_ADAPTER);
			adapter.notifyDataSetChanged();
		}
	}

	private synchronized int addPage() {
		page++;
		return page;
	}

	private synchronized int subPage() {
		page--;
		return page;
	}

	private synchronized int getPage() {
		return page;
	}

	private synchronized void AddListData() {
		if (result == null || listData == null)
			return;
		listData.addAll(result.softList);
	}

	/**
	 * 提示对话框，可定制；
	 * 
	 * @param context
	 *            界面显示
	 * @param flag
	 *            是否关闭,true 关闭
	 */
	public static void showWaitingDialog(final Context context, boolean tag) {
		if (Constants.DEBUG)
			Log.i("showWaitingDialog", "showWaitingDialog");
		if (dialog == null) {
			dialog = new Dialog(context, R.style.SreachDialog);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			// View dialogView =
			// inflater.inflate(R.layout.waiting_dialog_layout,
			// null);
			//
			// dialog.addContentView(dialogView, params);
		}
		if (tag) {

			dialog.show();
		} else {
			dialog.dismiss();

		}
	}

	@Override
	protected void onDestroy() {
		// count--;
		// Log.i("info8","onDestroy->SearchResultActivity="+count);
		// Log.i("finsh","finsh");
		ScreenManager.getScreenManager().popActivity(this);// 出栈

		adapter = null;
		listView = null;
		mAutocompleteAdapter = null;
		// recycledImg(listData);
		listData.clear();
		listData = null;
		result = null;
		super.onDestroy();
	}

	@Override
	public void dataNotifySetChanged() {
		if (adapter == null)
			return;
		adapter.notifyDataSetChanged();
		super.dataNotifySetChanged();
	}

	/*
	 * private void recycledImg(List<SoftlistInfo> imgurl) { if (imgurl == null
	 * || imgurl.size() == 0) { return; } if (imageCache != null) { BitmapTemp
	 * temp = null; for (SoftlistInfo url : imgurl) { if ((temp =
	 * imageCache.isCached(url.softicon)) != null) { // if (temp != null &&
	 * temp.bitmap != null // && temp.bitmap.isRecycled() == false // &&
	 * temp.isIsflag() == true) { // if (Constants.DEBUG) // Log.i("finsh",
	 * "recycle"); // temp.bitmap.recycle(); // }
	 * imageCache.remove(url.softicon); } } } }
	 */

	@Override
	public void updateUI() {
		setFalg(false);
		update();
		super.updateUI();
	}

	public void updateUI1() {
		setFalg(true);
		update();
	}

	private void update() {
		searchres.setVisibility(View.VISIBLE);
		if (result != null) {
			num = (TextView) findViewById(R.id.search_result_num);
			if (result.totalcount == 0) {
				inputKeyView.dismissDropDown();
				num.setText("没有检索到结果");
			} else {
				inputKeyView.dismissDropDown();
				num.setText("找到" + result.totalcount + "项符合的软件");
				if (listView == null || adapter == null)
					return;
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new ListView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						setSelectedValues(arg2);
					}
				});
			}

			a.setAnimationListener(new Animation.AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					searchres.setVisibility(View.GONE);
					actionLayoutNotShow();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

			});
			searchres.startAnimation(a);
		}
	}

	@Override
	protected void updateUIEnd() {
		// TODO Auto-generated method stub
		if (this.getFalg() == true) {
			super.updateUIEnd();
		}
	}

}
