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
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.QHListener;
import com.appdear.client.commctrls.RecognizerDialog;
import com.appdear.client.commctrls.SearchRankingView;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.commctrls.PaopaoText.PaopaoTextOnClick;
import com.appdear.client.db.KeyWordDb;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.exception.ApiException;
import com.appdear.client.model.SearchList;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiSoftListResult;
import com.appdear.client.utility.ServiceUtils;

  
/** 
 * 搜索
 * @author zqm
 */
public class SearchActivity extends ListBaseActivity 
	implements PaopaoTextOnClick ,OnGestureListener, OnTouchListener, OnClickListener {
	
	View mView;
	LinearLayout container = null;
	LocalActivityManager manager=null;
	MainActivity activity=null;
	
	/**
	 * voice recongnizer
	 */
	private ImageView iv_voice_search;
	
	/**
	 * 输入框
	 */ 
	private AutoCompleteTextView inputKeyView;
	
	private boolean flag=true;
	
	
	public synchronized boolean isFlag() {
		return flag;
	}

	public synchronized void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	private ApiSoftListResult result;
	
 
	 
	private Button searchButton;
	   
	private BaseAdapter adapter;
	
	private static final int UPDATE_DATA = 0;
	protected static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	protected static final int UPDATE_EIDT = 23;
	private static final int OPEN_VOICE = 24;
	private long start;
	
	/**
	 * 关键字推荐ID
	 */
	private int keywordid = 100008;
	private int mKwyWordPosition [] = {0,1,2,3,4,5,6,7,8,9,10,11,12};
	private String orderType = "0";
	private SearchList<String> keyWordList;
	
	
	/**
	 * 控件
	 */
	private GestureDetector detector = new GestureDetector(this); 
	private DisplayMetrics m;
	private LinearLayout	up;
	private boolean flg=true;
	private String colo[]={"#008000","#050505",
			"#800080","#ff69b4",
			"#ff00ff","#ff0000","#ff69b4",
			"#513118", "#050505",          
			"#d2691e","#b8860b","#f4a460","#daa520"};
	private int q=0;
	private int c;
	private View reImageView;
	private Dialog reDialog ;
	
	private TextView  text1;
	private TextView  text2;
	private TextView  text3;
	private TextView  text4;
	private TextView  text5;
	private TextView  text6;
	private TextView  text7;
	private TextView  text8;
	private TextView  text9;
	private TextView  text10;
	private TextView  text11;
	private TextView  text12;
	private TextView  text13;
	private RelativeLayout	mmRelativeLayout;
	private int width;
	private int height;
	private float ds;
	private int dsdpi;
	private DisplayMetrics metrics;
	Animation a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13;
	Animation a21,a22,a23,a24,a25,a26,a27,a28,a29,a210,a211,a212,a213;
	int time=1000;
	RelativeLayout layout=null;
	/**
	 * 下拉表的适配器
	 */
	private ArrayAdapter<String> mAutocompleteAdapter = null;
	private ImageView image;
	/**
	 * 自动下拉列表
	 */
	private List<String> AutoWordList;
	/**
	 * 更新下拉列表{}
	 */
	private final int UPDATE_AUTOCOMPLETE = 0X002;
	
	/**
	 * 键盘弹出状态
	 */
	public static boolean iskeyShowFlag = false;
	SearchRankingView view=null;
//	private Button qihuan_button;
	
	private int tag=1;
	
	/**
	 * autoListItemClickListener
	 */
	private OnItemClickListener autoListItemClickListener =  new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (Constants.DEBUG) Log.i("search", "search auto  onItemClick ");
			String key = inputKeyView.getText().toString().trim();
			if (Constants.DEBUG) Log.i("aotusearch",Common.autosearch+"-onItemClick-SearchActivity");
			startSearchResultActivity(key,"3");
			cheangeType();
		
		}
	};
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_main_layout);
		
		loadingView=new MProgress(this,true);
		 layout=(RelativeLayout) this.findViewById(R.id.layouview);
		// layout.setOnTouchListener(this);
		  metrics = new DisplayMetrics(); 
	    	this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    	ds=metrics.density;
	    	dsdpi=metrics.densityDpi;
			width=metrics.widthPixels;
			height=metrics.heightPixels;
			params = new LayoutParams(width,
					height);
			this.addContentView(loadingView, params);
		if (!AppContext.getInstance().isNetState) {
			handler1.sendEmptyMessage(LOADG);
			showRefreshButton();
			return;
		}
		
	   
		//Log.i("info",ds+"="+dsdpi+"="+width+"="+height);
	}
	
	int p=0;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void init() {
		
//		text1=new TextView(this);
//		text1.setId(1);
//		text2=new TextView(this);
//		text2.setId(2);
//		text3=new TextView(this);
//		text3.setId(3);
//		text4=new TextView(this);
//		text4.setId(4);
//		text5=new TextView(this);
//		text5.setId(5);
//		text6=new TextView(this);
//		text6.setId(6);
//		text7=new TextView(this);
//		text7.setId(7);
//		text8=new TextView(this);
//		text8.setId(8);
//		text9=new TextView(this);
//		text9.setId(9);
//		text10=new TextView(this);
//		text10.setId(10);
//		text11=new TextView(this);
//		text11.setId(11);
//		text12=new TextView(this);
//		text12.setId(12);
//		text13=new TextView(this);
//		text13.setId(13);
//		text1.setOnClickListener(this);
//		text2.setOnClickListener(this);
//		text3.setOnClickListener(this);
//		text4.setOnClickListener(this);
//		text5.setOnClickListener(this);
//		text6.setOnClickListener(this);
//		text7.setOnClickListener(this);
//		text8.setOnClickListener(this);
//		text9.setOnClickListener(this);
//		text10.setOnClickListener(this);
//		text11.setOnClickListener(this);
//		text12.setOnClickListener(this);
//		text13.setOnClickListener(this);
//		
		m=ServiceUtils.getMetrics(this.getWindowManager());
		mmRelativeLayout = (RelativeLayout)findViewById(R.id.layouview);
		searchButton = (Button) findViewById(R.id.search_button);
		inputKeyView = (AutoCompleteTextView)findViewById(R.id.auto_complete);

		inputKeyView.addTextChangedListener(mTextWatcher);
		inputKeyView.setOnItemClickListener(autoListItemClickListener);
		inputKeyView.setOnClickListener(this);
		image=(ImageView)this.findViewById(R.id.mBtn_del);
		
		image.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				inputKeyView.setText("");
			}
			
		});
		
		iv_voice_search=(ImageView)this.findViewById(R.id.iv_voice_search);
		iv_voice_search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				callVoiceRecognizer();
			}
		});
		
		view=(SearchRankingView)findViewById(R.id.rankingview);
		view.setListener(new QHListener(){

			@Override
			public void addKeyword(String keyword) {
				// TODO Auto-generated method stub
				inputKeyView.setText(keyword);
			}
			
		});
//		qihuan_button=(Button)findViewById(R.id.qihuan_button);
//		qihuan_button.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View v){
//					if(tag==1){
//						tag=2;
//						initRanking();
//					}else if(tag==2){
//						if(flg==false)return;
//						 tag=1;
//						 c=new Random().nextInt(2);
//						 if(c==0){  
//							  initStar1();
//					       }else if(c==1){  
//					    	  initStar(false,true);
//					       }else
//					    	   initStar(false,true);
//					}
//		   }
//		});
		searchButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v){
			
			if(ServiceUtils.checkNetState(SearchActivity.this)){
			String key = inputKeyView.getText().toString().trim();
			if("".equals(key)){
				Toast.makeText(SearchActivity.this,"关键字不可为空...", Toast.LENGTH_SHORT).show();
			}else{
				if (Constants.DEBUG) Log.i("aotusearch",Common.autosearch+"-onclick-SearchActivity");
				startSearchResultActivity(key,"1");
			}
			
			cheangeType();
			}else{
				Toast.makeText(SearchActivity.this, "手机没有开启网络",Toast.LENGTH_SHORT).show();
			}
		}
		});
	
	}

	/**
	 * 跳转到搜索结果界面
	 * @param key 搜索关键字
	 */
	public void startSearchResultActivity(String key,String searchway){
		/*Intent search = new Intent(SearchActivity.this, 
				SearchResultActivity.class);
		search.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		search.putExtra("keyWord", key.trim());
		search.putExtra("searchway",searchway);
		startActivity(search);*/
		 hideInputMode();
			
			activity = (MainActivity) MyApplication.getInstance().mainActivity;
			   manager = activity.getLocalActivityManager();
			   View searchView=null;
			   container = (LinearLayout) activity.findViewById(R.id.body);
			   BaseGroupActivity.currentType = SoftFormTags.SEARCH_RESULT;
			   activity.setFocus(BaseGroupActivity.currentType);
			   activity.unbindDrawables(container);
			   Intent search = new Intent(SearchActivity.this, SearchResultActivity.class);
			   search.putExtra("keyWord", key.trim());
			   search.putExtra("searchway",searchway);
			   searchView = manager.startActivity(
						"searchresult",
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
		public void afterTextChanged(Editable s) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (Constants.DEBUG) Log.i("aotusearch",Common.autosearch+"-SearchActivity-onTextChanged");
			
			if (Common.autosearch==false) {
				inputKeyView.dismissDropDown();
				Common.autosearch=true;return;
			} else {
				String content = inputKeyView.getText().toString().trim();
				int contents=0;
				 contents= (int) inputKeyView.getText().toString().length();
				if(!"".equals(content)){
					if(content.length()==2){
						//到服务器匹配关键字
						requestServerStrings(content);
					}
				}
				
				if(!"".equals(content)){
					image.setImageResource(R.drawable.sreach_closed_buts);
					
				
				}else{
					image.setImageResource(0);
				}
			}
		}
	};
	
	private List<Integer> getInter(){
		ArrayList<Integer> set = new ArrayList<Integer>();
		int temp=0;
		Random random=null;
		while(set.size()<=11){
			random =new java.util.Random(System.currentTimeMillis());
			temp = random.nextInt(12);
			if(!set.contains(temp)){
				set.add(temp);
			}
		}
		return set;
	}
	
	public void PaopaoDress(RelativeLayout.LayoutParams ii){
		if(m.widthPixels==240&&m.heightPixels==320){
			ii.leftMargin=m.widthPixels/3+20;
			ii.topMargin=m.heightPixels/4;
		}else{
			ii.leftMargin=m.widthPixels/3+45;
			ii.topMargin=m.heightPixels/4+45;
		}
		
	}
	private void initRanking(){
	//	Log.i("info111","initRanking-start");
		if(layout==null){
			return;
		}
//		text1.clearAnimation();
//		text2.clearAnimation();
//		text3.clearAnimation();
//		text4.clearAnimation();
//		text5.clearAnimation();
//		text6.clearAnimation();
//		text7.clearAnimation();
//		text8.clearAnimation();
//		text9.clearAnimation();
//		text10.clearAnimation();
//		text11.clearAnimation();
//		text12.clearAnimation();
//		text13.clearAnimation();
//		layout.removeAllViews();
		layout.removeAllViews();
		if(view!=null){
			view.setVisibility(View.VISIBLE);
			view.setKeywordlist(keyWordList);
			layout.addView(view);
		}
	//	Log.i("info111","initRanking-end-"+view);
	}
	private void initStar(boolean flag,boolean qh) {
		if(view!=null)view.setVisibility(View.GONE);
		if(flag==false){
			q=(q+1)%2;
		}
//		Random random = new Random();
//		layout.removeAllViews();
//		synchronized (this) {
			flg=false;
//		} 
			if(layout==null)return;
			if(qh==true){
				layout.removeAllViews();
			}
		 List<Integer> set=getInter();
		 if(keyWordList!=null){
		 if (Constants.DEBUG) Log.i("initStar", "initStar start count is   "+keyWordList.size());
		 int size = keyWordList.size();
		 if(size>=1){
			 //Log.i("info7",1+"="+keyWordList.get(mKwyWordPosition[0])+"="+mKwyWordPosition[0]);
			 View v=layout.findViewById(1);
			 if(v==null){
				 layout.addView(text1);
			 }
			 initText(text1,20,colo[set.get(0)],keyWordList.get(mKwyWordPosition[0]), mKwyWordPosition[0],1);
		 }if(size>=2){
			 //Log.i("info7",2+"="+keyWordList.get(mKwyWordPosition[1])+"="+mKwyWordPosition[1]);
			 View v=layout.findViewById(2);
			 if(v==null){
				 layout.addView(text2);
			 }
			 initText(text2,20,colo[set.get(1)],keyWordList.get(mKwyWordPosition[1]), mKwyWordPosition[1],2);
		 } if(size>=3){
			 //Log.i("info7",3+"="+keyWordList.get(mKwyWordPosition[2])+"="+mKwyWordPosition[2]);
			 View v=layout.findViewById(3);
			 if(v==null){
				 layout.addView(text3);
			 }
			 initText(text3,18,colo[set.get(2)],keyWordList.get(mKwyWordPosition[2]), mKwyWordPosition[2],3);
		 } if(size>=4){
			// Log.i("info7",4+"="+keyWordList.get(mKwyWordPosition[4])+"="+mKwyWordPosition[4]);
			 View v=layout.findViewById(4);
			 if(v==null){
				 layout.addView(text4);
			 }
			 initText(text4,18,colo[set.get(3)],keyWordList.get(mKwyWordPosition[3]), mKwyWordPosition[3],4);
		 } if(size>=5){
		//	 Log.i("info7",5+"="+keyWordList.get(mKwyWordPosition[5])+"="+mKwyWordPosition[5]);
			 View v=layout.findViewById(5);
			 if(v==null){
				 layout.addView(text5);
			 }
			 initText(text5,20,colo[set.get(4)],keyWordList.get(mKwyWordPosition[4]), mKwyWordPosition[4],5);
		 } if(size>=6){
	//		 Log.i("info7",6+"="+keyWordList.get(mKwyWordPosition[5])+"="+mKwyWordPosition[5]);
			 View v=layout.findViewById(6);
			 if(v==null){
				 layout.addView(text6);
			 }
			 initText(text6,16,colo[set.get(5)],keyWordList.get(mKwyWordPosition[5]), mKwyWordPosition[5],6);
		 } if(size>=7){
		//	 Log.i("info7",7+"="+keyWordList.get(mKwyWordPosition[6])+"="+mKwyWordPosition[6]);
			 View v=layout.findViewById(7);
			 if(v==null){
				 layout.addView(text7);
			 }
			 initText(text7,16,colo[set.get(6)],keyWordList.get(mKwyWordPosition[6]), mKwyWordPosition[6],7);
		 } if(size>=8){
		//	 Log.i("info7",8+"="+keyWordList.get(mKwyWordPosition[7])+"="+mKwyWordPosition[7]);
			 View v=layout.findViewById(8);
			 if(v==null){
				 layout.addView(text8);
			 }
			 initText(text8,16,colo[set.get(7)],keyWordList.get(mKwyWordPosition[7]), mKwyWordPosition[7],8);
		 } if(size>=9){
		//	 Log.i("info7",9+"="+keyWordList.get(mKwyWordPosition[8])+"="+mKwyWordPosition[8]);
			 View v=layout.findViewById(9);
			 if(v==null){
				 layout.addView(text9);
			 }
			 initText(text9,16,colo[set.get(8)],keyWordList.get(mKwyWordPosition[8]), mKwyWordPosition[8],9);
		 } if(size>=10){
		//	 Log.i("info7",10+"="+keyWordList.get(mKwyWordPosition[9])+"="+mKwyWordPosition[9]);
			 View v=layout.findViewById(10);
			 if(v==null){
				 layout.addView(text10);
			 }
			 initText(text10,18,colo[set.get(9)],keyWordList.get(mKwyWordPosition[9]), mKwyWordPosition[9],10);
		 } if(size>=11){
		//	 Log.i("info7",11+"="+keyWordList.get(mKwyWordPosition[10])+"="+mKwyWordPosition[10]);
			 View v=layout.findViewById(11);
			 if(v==null){
				 layout.addView(text11);
			 }
			 initText(text11,18,colo[set.get(10)],keyWordList.get(mKwyWordPosition[10]), mKwyWordPosition[10],11);
		 } if(size>=12){
			// Log.i("info7",12+"="+keyWordList.get(mKwyWordPosition[11])+"="+mKwyWordPosition[11]);
			 View v=layout.findViewById(12);
			 if(v==null){
				 layout.addView(text12);
			 }
			 initText(text12,20,colo[set.get(5)],keyWordList.get(mKwyWordPosition[11]), mKwyWordPosition[11],12);
			 
		 } if(size>=13){
			// Log.i("info7",13+"="+keyWordList.get(mKwyWordPosition[12])+"="+mKwyWordPosition[12]);
			 View v=layout.findViewById(13);
			 if(v==null){
				 layout.addView(text13);
			 }
			 initText(text13,20,colo[set.get(8)],keyWordList.get(mKwyWordPosition[12]), mKwyWordPosition[12],13);
		 }
		
		}
	}
		private void initStar1() {
			//layout.removeAllViews();
			 q=(q+1)%2;
			 initStar(true,false);
	    }
	
	@Override
	public void initData() {
	//	Log.i("info123", layout+"=layout");
		initWordlist();
		super.initData();
	}
	
	private KeyWordDb keyWordDb = null;
	
	/** 
	 * 获取关键字数据
	 * 
	 * 1.先显示上次缓存数据
	 * 2.后台偷偷从服务器获取，存入数据库，用于下次登录显示
	 * 3.保存新的时间戳
	 */
	private void findDataList() {
		//获取缓存数据
	//	Log.i("info111", "getKeyWordList-findDataList-start");
		keyWordList = keyWordDb.getKeyWordList();
	//	Log.i("info111", "getKeyWordList-findDataList-end-"+keyWordList);
		//获取服务端数据
		requestSearchKey();
		//保存新的时间戳
		SharedPreferencesControl.getInstance().putLong("lastTime", System.currentTimeMillis(), Common.SEARCH_XML,this);
	}
	
	/**
	 * 从服务器获取关键字数据
	 */
	private void requestSearchKey() {
		//请求数据
	//	Log.i("info123","requestSearchKey");
		keywordid = SharedPreferencesControl.getInstance().getInt("301", com.appdear.client.commctrls.Common.SECTIONCODEXML, this);
		try {
	//		Log.i("info111", "requestSearchKey-start-"+keywordid);
			ApiSoftListResult keywordRresult = ApiManager.keywordlist(keywordid+"", "0", page+"",50+"");
	//		Log.i("info111", "requestSearchKey-end-"+keywordRresult);
			if(keywordRresult != null && keywordRresult.keywordList.size() != 0) {
				keyWordDb.addData(keywordRresult.keywordList);
			}
		} catch (ApiException e) {
			showRefreshButton();
		}
	}

	/**
	 *  2011 - 09 - 13 增加了 关键字更新策略
	 *  2011 - 10 - 14 增加了关键字第一次安装的时候缓存
	 */
	public void initWordlist(){
	//	Log.i("info111", "initWordlist-start");
		if (keyWordDb == null)
			keyWordDb = new KeyWordDb(this);
		//添加策略 ，1天更新一次关键字数据库
		long updateTime = SharedPreferencesControl.getInstance().getLong("lastTime", Common.SEARCH_XML,this);
		if (updateTime == 0) {
			//表示是第一次进入搜索界面
			findDataList();
		} else {
		//	if (System.currentTimeMillis() >= (updateTime+60000*60*24)) {
			if (System.currentTimeMillis() >= (updateTime+10000)) {
				//如果缓存失效
				findDataList();
			} else {
				//直接显示缓存数据
		//		Log.i("info123","requestSearchKey=");
		//		Log.i("info111", "getKeyWordList-start");
				keyWordList = keyWordDb.getKeyWordList();
		//		Log.i("info111", "getKeyWordList-end");
			}
		}
		//Log.i("info111", "initWordlist-end");
		keyWordDb.dbHelper.close();
	}
		
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what){		
			case UPDATE_AUTOCOMPLETE:
				SearchActivity.this.inputKeyView.setAdapter(SearchActivity.this.mAutocompleteAdapter);
				SearchActivity.this.mAutocompleteAdapter.notifyDataSetChanged();
				break;
			case UPDATE_EIDT:
//				SearchActivity.this.inputKeyView.setAdapter(SearchActivity.this.mAutocompleteAdapter);
//				SearchActivity.this.mAutocompleteAdapter.notifyDataSetChanged();
				String key = msg.obj.toString();
				SearchActivity.this.inputKeyView.setText(msg.obj.toString());
				if(ServiceUtils.checkNetState(SearchActivity.this)){
					startSearchResultActivity(key,"4");
					cheangeType();
				}else{
					Toast.makeText(SearchActivity.this, "手机没有开启网络",Toast.LENGTH_SHORT).show();
				}
				break;
			case OPEN_VOICE:
				callVoiceRecognizer();
				break;
			}
		}
	};
	
	/**
	 * 根据关键字匹配
	 */
	private void requestServerStrings(String key) {
		new Thread(new RequestStringsThread(key)).start();
	};
	
	/**
	 * 网络线程
	 * @author zxy
	 *
	 */
	class RequestStringsThread extends Thread{
		String content = "";
		public RequestStringsThread(String key){
			this.content = key;
		}
		@Override
		public void run(){
			//keywordid = SharedPreferencesControl.getInstance().getInt("301", com.appdear.client.commctrls.Common.SECTIONCODEXML, SearchActivity.this);
			
			ApiSoftListResult autoCompRresult = null;
			if (Constants.DEBUG) Log.i("aotusearch","init-"+Common.autosearch+"-RequestStringsThread-SearchActivity");
			if(Common.autosearch==false){
				mAutocompleteAdapter =new ArrayAdapter<String>(SearchActivity.this,
						android.R.layout.simple_dropdown_item_1line,new String[]{});
				SearchActivity.this.handler.sendEmptyMessage(UPDATE_AUTOCOMPLETE);
				Common.autosearch=true;
				return;
			}
			try {
				AutoWordList = null;
				if(!"".equals(content))
					autoCompRresult = ApiManager.autolist(content, "1", "1", "1", "6");
			} catch (ApiException e) {
				e.printStackTrace();
			}
			if(autoCompRresult!=null){
			AutoWordList = autoCompRresult.autolist;
			if(AutoWordList!=null){
				mAutocompleteAdapter =new ArrayAdapter<String>(SearchActivity.this,
						android.R.layout.simple_dropdown_item_1line,AutoWordList);
				
				SearchActivity.this.handler.sendEmptyMessage(UPDATE_AUTOCOMPLETE);
			}
			}
		}
	}
	
	public void updateUI() {
//		 onNewIntent(getIntent());
		 initRanking();
//		 c=new Random().nextInt(2);
//		 if(c==0){  
//			  initStar1();
//	       }else if(c==1){  
//	    	  initStar(false,false);
//	       }else
//	    	   initStar(false,false);
		 
//	initStar();
		
		
	}
	
	/**
	 * 当前项操作
	 * @param position
	 */
	public void setSelectedValues(int position) {
		ServiceUtils.setSelectedValues(this, result.softList, position);
	}

	@Override
	public void refreshDataUI() {
		
	}

	@Override
	public void doRefreshData() {
		
	}
	

	
	@Override
	public void onClick(int pos) {
		if (!AppContext.getInstance().isNetState) {
			Toast.makeText(SearchActivity.this, "手机没有开启网络",Toast.LENGTH_SHORT).show();
			return;
		}
		Common.autosearch=false;
		this.inputKeyView.setText(keyWordList.get(pos));
		startSearchResultActivity(keyWordList.get(pos),"2");
		//new Thread(new SearchThread(keyWordList.get(pos-1))).start();
		cheangeType();
	}
	
	@Override
	protected void onResume() {
		//设置软键盘为不显示
		iskeyShowFlag = false;
		image.setImageResource(0);
		super.onResume();
		
	}
	
	@Override
	public void onClick(View v) {
		//软键盘弹出状态
		if (v.getId() == R.id.auto_complete) {
			iskeyShowFlag = true;
		}
		if(v.getId()>=1&&v.getId()<=13)
		{
			onClick((Integer)v.getTag());
		}
	}
	
	public void cheangeType(){
		//if(mStarLayout.VISIBLE!=View.VISIBLE){
			 if (Constants.DEBUG) Log.i("searchActivity", "change maninActivity type");
		//}
	}
	
	/**
	 * 隐藏软键盘
	 */
	private void hideInputMode(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
		View view = SearchActivity.this.getCurrentFocus();   
	    if (view != null){   
	    	imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//隐藏软键盘  
	    }  
	}
	
//	@Override
//	public void onNewIntent(Intent intent) {
//		if (Constants.DEBUG) Log.i("searchActivity ","onNewIntent");
//		if(keyWordList!=null){
//		int length = keyWordList.size();
//		int temp = -1;
//		mKwyWordPosition = new int[length];
//		Random random = new Random();
//		HashSet<Integer> set = new HashSet<Integer>();
//		
//		while(set.size()<=13){
//			random =new java.util.Random(System.currentTimeMillis());
//			temp = random.nextInt(length);
//			if(!set.contains(temp)){
//				set.add(temp);
//			}
//		}
//		
//		int i = -1;
//		for(Iterator<Integer> it=set.iterator();it.hasNext();){
//			i++;
//			mKwyWordPosition[i]=(Integer) it.next();
//			if (Constants.DEBUG) Log.i("search", "search :"+mKwyWordPosition[i]);
//		 }
//		set.clear();
//		//重新显示
//		//int q=1;
//
//		}
//		inputKeyView.setText("");
//		super.onNewIntent(intent);
//	}
	

	 
	 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, 
			 float velocityY) {
		onNewIntent(getIntent());		
		if (flg == true && q == 0) {
					initStar1();
		} else if (flg == true && q == 1){
					initStar(false,false);
		}
		return true;  
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
	//	Log.i("bug", "e.getX()="+e.getX());
	//	Log.i("bug", "e.getY()="+e.getY());
		return false;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		 return detector.onTouchEvent(event);  
	}  
	private TextView initText(TextView text1,int textsize,String color,String text,int pos,int id){
		id=text1.getId();
		text1.setTag(pos);
		text1.setPadding(0, 5, 0, 5);
		text1.setTextColor(Color.parseColor(color));
		text1.setTextSize(getTextSize(textsize+2));
		//text1.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tbg));
		text1.setText(text.trim());
		text1.setGravity(Gravity.CENTER);
		switch(id){
		case 1:
			text1.setLayoutParams(getLayoutText1(this));
			break;
		case 2:
			text1.setLayoutParams(getLayoutText2(this));
			break;
		case 3:
			text1.setLayoutParams(getLayoutText3(this));
			break;
		case 4:
			text1.setLayoutParams(getLayoutText4(this));
			break;
		case 5:
			text1.setLayoutParams(getLayoutText5(this));
			break;
		case 6:
			text1.setLayoutParams(getLayoutText6(this));
			break;
		case 7:
			text1.setLayoutParams(getLayoutText7(this));
			break;
		case 8:
			text1.setLayoutParams(getLayoutText8(this));
			break;
		case 9:
			text1.setLayoutParams(getLayoutText9(this));
			break;
		case 10:
			text1.setLayoutParams(getLayoutText10(this));
			break;
		case 11:
			text1.setLayoutParams(getLayoutText11(this));
			break;
		case 12:
			text1.setLayoutParams(getLayoutText12(this));
			break;
		case 13:
			text1.setLayoutParams(getLayoutText13(this));
			break;
		default:break;
		}
		 switch(id){
		 
			case 1:
				View v=layout.findViewById(1);
				if(q==0){
					if(a1==null){
						text1.clearAnimation();
						a1= getAnimation(0,0,-height/2,0);
						a1.setFillAfter(true);
				    	a1.setDuration(time);
				    	a1.setAnimationListener(listener);
					}
					text1.startAnimation(a1);
				}else if(q==1){
					if(a21==null){
						text1.clearAnimation();
						a21= getAnimation(0,0,height/2,0);
						a21.setFillAfter(true);
						a21.setDuration(time);
						a21.setAnimationListener(listener);
					}
					text1.startAnimation(a21);
				}
			
				break;
			case 2:
				if(q==0){
					if(a2==null){
						text1.clearAnimation();
						a2= getAnimation(0,0,height/2,0);
						a2.setFillAfter(true);
				    	a2.setDuration(time);
				    	a2.setAnimationListener(listener);
					}
					text1.startAnimation(a2);
				}else if(q==1){
					if(a22==null){
						text1.clearAnimation();
						a22= getAnimation(0,0,-height/2,0);
						a22.setFillAfter(true);
				    	a22.setDuration(time);
				    	a22.setAnimationListener(listener);
					}
					text1.startAnimation(a22);
				}
				
				break;
			case 3:
				if(q==0){
					if(a3==null){
						
						text1.clearAnimation();
						a3= getAnimation(-width/2,0,-height/2,0);
						a3.setFillAfter(true);
				    	a3.setDuration(time);
				    	a3.setAnimationListener(listener);
					}
					text1.startAnimation(a3);
				}else if(q==1){
					if(a23==null){
						text1.clearAnimation();
						a23= getAnimation(width/2-dip2px(20),0,height/2-dip2px(30),0);
						a23.setFillAfter(true);
						a23.setDuration(time);
						a23.setAnimationListener(listener);
					}
					text1.startAnimation(a23);
				}
				break;
			case 4:
				if(q==0){
					if(a4==null){
						
						text1.clearAnimation();
						a4= getAnimation(width/2,0,-height/2,0);
						a4.setFillAfter(true);
				    	a4.setDuration(time);
				    	a4.setAnimationListener(listener);
					}
					text1.startAnimation(a4);
		 		}	
				else if(q==1){
					if(a24==null){
						text1.clearAnimation();
						a24= getAnimation(-width/2+dip2px(20),0,height/2-dip2px(30),0);
						a24.setFillAfter(true);
						a24.setDuration(time);
						a24.setAnimationListener(listener);
					}
					text1.startAnimation(a24);
				}
				break;
			case 5:
				if(q==0){
					if(a5==null){
						
						text1.clearAnimation();
						a5= getAnimation(-width,0,0,0);
						a5.setFillAfter(true);
				    	a5.setDuration(time);
				    	a5.setAnimationListener(listener);
					}
					text1.startAnimation(a5);
				}	
				else if(q==1){
					if(a25==null){
						text1.clearAnimation();
						a25= getAnimation(-width/2,0,0,0);
						a25.setFillAfter(true);
						a25.setDuration(time);
						a25.setAnimationListener(listener);
					}
					text1.startAnimation(a25);
			    }
				break;
			case 6:
				if(q==0){
					if(a6==null){
						
						text1.clearAnimation();
						a6= getAnimation(-3*width/4,0,-2*height/3,0);
						a6.setFillAfter(true);
				    	a6.setDuration(time);
				    	a6.setAnimationListener(listener);
					}
					text1.startAnimation(a6);
				}else if(q==1){
					if(a26==null){
						text1.clearAnimation();
						a26= getAnimation(width/2-dip2px(25),0,height/2-dip2px(45),0);
						a26.setFillAfter(true);
						a26.setDuration(time);
						a26.setAnimationListener(listener);
					}
					text1.startAnimation(a26);
				}
				break;
			case 7:
				if(q==0){
					if(a7==null){
						
						text1.clearAnimation();
						a7= getAnimation(3*width/4,0,-2*height/3,0);
						a7.setFillAfter(true);
						a7.setDuration(time);
						a7.setAnimationListener(listener);
					}
					text1.startAnimation(a7);
				}
				else if(q==1){
					if(a27==null){
						text1.clearAnimation();
						a27= getAnimation(-width/2+dip2px(25),0,height/2-dip2px(45),0);
						a27.setFillAfter(true);
						a27.setDuration(time);
						a27.setAnimationListener(listener);
					}
					text1.startAnimation(a27);
				}
				break;
			case 8:
			if(q==0){
				if(a8==null){
					
					text1.clearAnimation();
					a8= getAnimation(-3*width/4,0,2*height/3,0);
					a8.setFillAfter(true);
					a8.setDuration(time);
					a8.setAnimationListener(listener);
				}
				text1.startAnimation(a8);
			}
			else if(q==1){
				if(a28==null){
					text1.clearAnimation();
					a28= getAnimation(width/2-dip2px(25),0,-height/2+dip2px(45),0);
					a28.setFillAfter(true);
					a28.setDuration(time);
					a28.setAnimationListener(listener);
				}
				text1.startAnimation(a28);
			}
			break;
			case 9:
			if(q==0){
				if(a9==null){
				
					text1.clearAnimation();
					a9= getAnimation(3*width/4,0,2*height/3,0);
					a9.setFillAfter(true);
			    	a9.setDuration(time);
			    	a9.setAnimationListener(listener);
				}
				text1.startAnimation(a9);
			}else if(q==1){
				if(a29==null){
					
					text1.clearAnimation();
					a29= getAnimation(-width/2+dip2px(20),0,-height/2+dip2px(30),0);
					a29.setFillAfter(true);
			    	a29.setDuration(time);
			    	a29.setAnimationListener(listener);
				}
				text1.startAnimation(a29);
			}
			
				break;
			case 10:
				if(q==0){
					if(a10==null){
						
						text1.clearAnimation();
						a10= getAnimation(-width/2,0,height/2,0);
						a10.setFillAfter(true);
				    	a10.setDuration(time);
				    	a10.setAnimationListener(listener);
					}
					text1.startAnimation(a10);
				}else if(q==1){
					if(a210==null){
						text1.clearAnimation();
						a210= getAnimation(width/2-dip2px(20),0,-height/2+dip2px(30),0);
						a210.setFillAfter(true);
						a210.setDuration(time);
						a210.setAnimationListener(listener);
					}
					text1.startAnimation(a210);
				}
				break;
			case 11:
			  if(q==0){
				if(a11==null){
					
					text1.clearAnimation();
					a11= getAnimation(width/2,0,height/2,0);
					a11.setFillAfter(true);
			    	a11.setDuration(time);
			    	a11.setAnimationListener(listener);
				}
				text1.startAnimation(a11);
		 	}else if(q==1){
				if(a211==null){
					text1.clearAnimation();
					a211= getAnimation(-width/2+dip2px(20),0,-height/2+dip2px(30),0);
					a211.setFillAfter(true);
					a211.setDuration(time);
					a211.setAnimationListener(listener);
				}
				text1.startAnimation(a211);
			}
				break;
			case 12:
			if(q==0){
				if(a12==null){
					
					text1.clearAnimation();
					a12= getAnimation(-width,0,0,0);
					a12.setFillAfter(true);
			    	a12.setDuration(time);
			    	a12.setAnimationListener(listener);
				}
				text1.startAnimation(a12);
			}else if(q==1){
				if(a212==null){
					text1.clearAnimation();
					a212= getAnimation(width/2-dip2px(10),0,0,0);
					a212.setFillAfter(true);
					a212.setDuration(time);
					a212.setAnimationListener(listener);
				}
				text1.startAnimation(a212);
			}
				break;
			case 13:
			if(q==0){
				if(a13==null){
					
					text1.clearAnimation();
					a13= getAnimation(width,0,0,0);
					a13.setFillAfter(true);
			    	a13.setDuration(time);
			    	a13.setAnimationListener(listener);
				}
				text1.startAnimation(a13);
		    }else if(q==1){
				if(a213==null){
					text1.clearAnimation();
					a213= getAnimation(-width/2+dip2px(10),0,0,0);
					a213.setFillAfter(true);
					a213.setDuration(time);
					a213.setAnimationListener(listener);
				}
				text1.startAnimation(a213);
			}
				break;
			}
		return text1;
	}
	
	private Animation getAnimation(int fx,int tx,int fy,int ty){
		return new TranslateAnimation(fx,tx,fy,ty);
	}

	private RelativeLayout.LayoutParams getLayoutText1(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		
		layout.topMargin=dip2px(10);
		layout.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
		layout.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText2(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.bottomMargin=dip2px(10);
		layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		layout.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText3(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.BELOW, text1.getId());
		layout.topMargin=dip2px(handlerDp(15));
		layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		layout.leftMargin=dip2px(dsdpi/4);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText4(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.BELOW, text1.getId());
		layout.topMargin=dip2px(handlerDp(15));
		layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		layout.rightMargin=dip2px(dsdpi/4);
		return layout;
	}
	
	private RelativeLayout.LayoutParams getLayoutText5(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText6(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.BELOW, text3.getId());
		layout.topMargin=dip2px(handlerDp(12));
		layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		layout.leftMargin=dip2px(dsdpi/4+10);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText7(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.BELOW, text4.getId());
		layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		layout.rightMargin=dip2px(dsdpi/4+10);
		layout.topMargin=dip2px(handlerDp(12));
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText8(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		layout.leftMargin=dip2px(dsdpi/4+10);
		layout.addRule(RelativeLayout.ABOVE,text10.getId());
		layout.bottomMargin=dip2px(handlerDp(12));
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText9(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		layout.rightMargin=dip2px(dsdpi/4+10);
		layout.addRule(RelativeLayout.ABOVE,text11.getId());
		layout.bottomMargin=dip2px(handlerDp(12));
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText10(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.ABOVE, text2.getId());
		layout.bottomMargin=dip2px(handlerDp(15));
		layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		layout.leftMargin=dip2px(dsdpi/4);
		return layout;
	}
	
	private RelativeLayout.LayoutParams getLayoutText11(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.ABOVE, text2.getId());
		layout.bottomMargin=dip2px(handlerDp(15));
		layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		layout.rightMargin=dip2px(dsdpi/4);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText12(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
		layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		layout.leftMargin=dip2px(10);
		return layout;
	}
	private RelativeLayout.LayoutParams getLayoutText13(Context context){
		RelativeLayout.LayoutParams  layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//layout.topMargin
		layout.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
		layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		layout.rightMargin=dip2px(10);
		return layout;
	}
	public int dip2px(float dipValue){ 
		 if(dsdpi<=120){
			 dipValue*=0.75f;
		 }else if(dsdpi<=160&&width>=600){
			 dipValue*=width/dsdpi;
		 }
//		 else if(dsdpi<=160){
//			 dipValue=dipValue;
//		 }else if(dsdpi<=240&&height>800){
//			 dipValue*=1.5f;
//		 }
		 return (int)(dipValue * ds + 0.5f); 
	} 
	public int getTextSize(float size){ 
		// size=size*ds;
		 if(ds>1.0&&width>720){
			 
			 if(dsdpi<=240){
				 size*=ds;
			 }else{
				 size=size;
			 }
			 
		 }else if(dsdpi<=160&&width>=600){
			 size*=1.5;
		 }
		 return (int)size; 
	} 	 
	public int handlerDp(int dp){
		 if(dsdpi<=120){
			 dp*=0.3f;
		 }else{
			 dp*=ds; 
		 }
		return dp;
	}
	
	private AnimationListener listener=new AnimationListener(){
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			flg=true;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub	
		}
		
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==VOICE_RECOGNITION_REQUEST_CODE && resultCode==RESULT_OK){
			final ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if(list!=null){
				RecognizerDialog mRecognizerDialog = new RecognizerDialog(SearchActivity.this, list,handler);
				mRecognizerDialog.show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	/**
	 * 如果手机中不支持谷歌语音,让其去下载
	 * @param info
	 * @param apkname
	 * @param state
	 * @return
	 */
	private boolean download(SoftlistInfo info, String apkname,int state) {
		final SiteInfoBean downloadbean = new SiteInfoBean(info.downloadurl,
				ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null ? ""
						: ServiceUtils.getSDCARDImg(Constants.APK_DATA)
								.getPath(), ServiceUtils.getApkname(info.downloadurl), info.softname,
				info.softicon, info.version, info.softid, info.appid,
				info.softsize, 0, 1, null, null,
				BaseActivity.downloadUpdateHandler,state==4?Constants.UPDATEPARAM:"");
		String[] msg = DownloadUtils.download(downloadbean, SearchActivity.this);
		// 任务已存在
		//Toast mScreenHint = Toast.makeText(this, msg[0],Toast.LENGTH_SHORT);
		//mScreenHint.show();
		if (msg[1] != null && msg[1].equals("0"))
			return true;
		return false;
	}
	/**
	 * 如果手机中不支持谷歌语音,让其确定是否下载
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
		builder.setTitle("提示");
		builder.setMessage("抱歉,您手机中尚未安装谷歌语音识别软件！是否下载安装?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Thread(){
					@Override
					public void run() {
						//"softid":109802
						String softid = "109802";
						ApiSoftListResult result;
						try {
							result = ApiManager.resultsoftinfo(softid, "","","");
							if (result == null ) 
							{
		 						return;
							}
							SoftlistInfo voiceSoft= result.detailinfo;
							//voiceSoft.downloadurl="http://od.appdear.com/soft/android/20120903/appdear_41875_0903_m.apk";
							Integer Status = MyApplication.getInstance().getSoftMap().get(voiceSoft.softid);
			    			if (Status == null||Status==0) {
			    				 if (download(voiceSoft,  voiceSoft.downloadurl,0)) {
			    					SearchActivity.this.showMessageInfo(voiceSoft.softname+"开始下载\"谷歌语音\""); 
			    				 } 
			    			} 
						} catch (ApiException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//SoftlistInfo voiceSoft = ApiManager.resultsoftinfo(softid+"", "","","");
						
					}
				}.start();
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	private void callVoiceRecognizer() {
		PackageManager pm = getPackageManager();
		List<ResolveInfo> mResolveInfos=pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if(mResolveInfos!=null && mResolveInfos.size()!=0){
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speech recognition demo");
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		}else{
			//TODO
			AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
			builder.setTitle("提示");
			builder.setMessage("对不起，您的手机未安装谷歌语音识别软件，请先下载安装“语音搜索”后再使用！");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
}