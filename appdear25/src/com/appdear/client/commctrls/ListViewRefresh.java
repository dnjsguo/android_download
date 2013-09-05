/**
 * ListRefresh.java
 * created at:2011-5-11下午01:46:24
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.commctrls;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.appdear.client.R;
import com.appdear.client.RefreshDataListener;
import com.appdear.client.download.FileDownloaderService;
import com.appdear.client.service.AppContext;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
  
/** 
 * 动态刷新列表
 * 
 * @author zqm 
 */
public class ListViewRefresh extends ListView {
	private int index = -1;
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * 刷新tag
	 */
	private boolean refreshTag = true;
	
	/**
	 * 结束tag
	 */
	private boolean endTag = false;
	
	/**
	 * 出错了
	 */
	private boolean errTag = false;
	
	/**
	 * 首次刷新tag
	 */
	private boolean firstTag = true;
	
	/**
	 * 数据刷新
	 */
	private RefreshDataListener refreshDataListener;
	
	private TextView loaddingAlert;
	
	private ExecutorService pool = Executors.newCachedThreadPool();
	/**
	 * 线程池
	 */
//	AbstractExecutorService pool=new ThreadPoolExecutor(5,10,15L,TimeUnit.SECONDS,new SynchronousQueue(),new ThreadPoolExecutor.DiscardOldestPolicy());
	
	private int countflag;
	
	/**
	 * 记录当前可见item的position
	 */
	private int currentpostion;
	
	/**
	 * 记录可见item
	 */
	private int visibleItemc;
	
	/**
	 * 记录列表总数
	 */
	private int totalItemc;
	private int state = -1;

	private Context context;

	public int getCurrentpostion() {
		return currentpostion;
	}

	public void setCurrentpostion(int currentpostion) {
		this.currentpostion = currentpostion;
	}

	private boolean isShowAlert = true;
	
	/**
	 * 初始化
	 */
	private void init() {
		setOnScrollListener(new OnScrollListener() {
			/*scrollState有三种状态，
			（SCROLL_STATE_FLING ）， 2 用户之前通过触控滚动并执行了快速滚动。滚动动画正滑向停止点。
			(SCROLL_STATE_TOUCH_SCROLL ), 0  用户通过触控滚动，并且手指没有离开屏幕。
			 （SCROLL_STATE_IDLE ），1  视图没有滚动。注意，使用轨迹球滚动时，在滚动停止之前，一直处于空闲状态。 */
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			//	System.out.println("----onScrollStateChanged----");
				countflag=scrollState;
				
				//当获取数据失败时，滑动列表到非底部时设置err状态为false，
				//表示再次滑动到列表底部时可以重试
				if (countflag != OnScrollListener.SCROLL_STATE_IDLE 
						&& currentpostion + visibleItemc != totalItemc) {
					errTag = false;
				}
				
				//列表在停止状态时
				if (state != -1 && countflag == OnScrollListener.SCROLL_STATE_IDLE 
						&& refreshDataListener != null) {
					/*if (AppContext.getInstance().taskList.size() == 0)
						AppContext.getInstance().downloader.readDB();*/
					
					//refreshDataListener.refreshUI(currentpostion);
					int firstp=ListViewRefresh.this.getFirstVisiblePosition();
					int lastp=ListViewRefresh.this.getLastVisiblePosition();
					refreshDataListener.refreshUI(firstp,lastp);
					/*if(ListViewRefresh.this.getHeaderViewsCount()==0||firstp==0)
					{
						refreshDataListener.refreshUI(firstp,lastp);
					}else
					{
						refreshDataListener.refreshUI(firstp-1,lastp-1);
					}*/

//					int first=ListViewRefresh.this.getFirstVisiblePosition();
//					int last=ListViewRefresh.this.getLastVisiblePosition();
//					refreshDataListener.refreshUI(first>0?first-1:first,last<ListViewRefresh.this.getCount()-1?last+1:last);
				}
				state = scrollState;
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				currentpostion=firstVisibleItem;
				visibleItemc = visibleItemCount;
				totalItemc = totalItemCount;
				
				if (firstTag) {
					firstTag = false;
					return;
				}
				//如果当前页数已经加载完成且不到页尾时，提示设置为将可显示
				if (endTag && firstVisibleItem + visibleItemCount != totalItemCount) {
					isShowAlert = true;
					return;
				}
				//如果当前页数加载完成且到页尾时，显示提示且提示显示完毕后置为将不可显示，避免提示显示重复
				if (endTag && firstVisibleItem + visibleItemCount == totalItemCount) {
					if (isShowAlert) {
						if (refreshDataListener != null) {
							Toast.makeText(context, "已经是最后一页", Toast.LENGTH_SHORT);
							isShowAlert = false;
						}
					}
					return;
				}
				
				if (refreshTag && !endTag && !errTag) {
					if (firstVisibleItem + visibleItemCount == totalItemCount 
							&& totalItemCount != 0) {
						refreshTag = false;
						if (refreshDataListener == null)
							return;
						refreshDataListener.refreshState(View.VISIBLE);
					pool.execute(new Runnable(){
						//new Thread(){
							@Override
							public void run() {
								if (refreshDataListener == null){
									return;
								}
								refreshDataListener.refreshData();
								handler.sendEmptyMessage(1);
							}
						//}.start();
					});
				   }
				}
			}
		});
	}
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (refreshDataListener == null)
					return;
				refreshDataListener.refreshState(View.INVISIBLE);
//				removeFooterView(loaddingAlert);
				refreshTag = true;
				break;
			}
		}
	};
	
	/**
	 * @param refreshDataListener the refreshDataListener to set
	 */
	public void setRefreshDataListener(RefreshDataListener refreshDataListener) {
		this.refreshDataListener = refreshDataListener;
		if (refreshDataListener == null)
			return;
		init();
	}
	
	/**
	 * @param context
	 */
	public ListViewRefresh(Context context, AttributeSet set) {
		super(context, set);
		this.context = context;
//		addLoadingView("正在加载...");
//		addHeaderView(loaddingAlert);
	}
	
	/**
	 * 
	 * @param context
	 */
	public ListViewRefresh(Context context) {
		super(context);
		this.context = context;
//		addLoadingView("正在加载...");
//		addHeaderView(loaddingAlert);
	}
	
	/**
	 * 
	 * @param context
	 * @param set
	 * @param arg1
	 */
	public ListViewRefresh(Context context, AttributeSet set, int arg1) {
		super(context, set, arg1);
		this.context = context;
//		addLoadingView("正在加载...");
//		addHeaderView(loaddingAlert);
	}
	
	/**
	 * @param refreshTag the refreshTag to set
	 */
	public void setRefreshTag(boolean refreshTag) {
		this.refreshTag = refreshTag;
	}
	
	public void removeHeaderview() {
//		removeHeaderView(loaddingAlert);
	}

	/**
	 * @param endTag the endTag to set
	 */
	public void setEndTag(boolean endTag) {
		this.endTag = endTag;
	}
	
	public void addLoadingView(String text) {
//		if (loaddingAlert == null)  {
//			loaddingAlert = new TextView(AppContext.getInstance().appContext);
//			loaddingAlert.setBackgroundResource(R.drawable.soft_list_item_unselected_bg);
//		}
//		loaddingAlert.setText(text);
	}

	public int getCountflag() {
		return countflag;
	}

	public void setCountflag(int countflag) {
		this.countflag = countflag;
	}

	public void setErrTag(boolean errTag) {
		this.errTag = errTag;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(FileDownloaderService.delete_all==true)return false;
		View view=null;
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:

        case MotionEvent.ACTION_MOVE:
         	if (this != null&& index!=-1 && this.findViewWithTag("index"+index)!=null) 
			{
        		
				RelativeLayout   layout=(RelativeLayout )this.findViewWithTag("index"+index);
				layout.setVisibility(View.GONE);
				//((LinearLayout)layout.findViewById(R.id.actionLayout)).setVisibility(View.GONE);
				layout.setTag(null);
				index = -1;
			}
            break;
        default:
           break;
        }
        return  super.onTouchEvent(ev);
    }
}

 