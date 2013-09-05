/**
 * ListBaseActivity.java
 * created at:2011-5-11上午11:05:09
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */
package com.appdear.client.commctrls;

import java.io.File;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.CategoryDetailListActivity;
import com.appdear.client.CategorySubjectDetailsActivity;
import com.appdear.client.FavoriteListActivity;
import com.appdear.client.HomePageBestListActivity;
import com.appdear.client.HomePageNewSoftListActivity;
import com.appdear.client.HomePageSpecialListActivity;
import com.appdear.client.MainActivity;
import com.appdear.client.MatchSoftListActivity;
import com.appdear.client.MoreManagerUpdateActivity;
import com.appdear.client.MyShareListActivity;
import com.appdear.client.R;
import com.appdear.client.RefreshDataListener;
import com.appdear.client.SearchActivity;
import com.appdear.client.SearchResultActivity;
import com.appdear.client.SoftwareDetailAuthorInfoActivity;
import com.appdear.client.Adapter.SoftwarelistAdatper;
import com.appdear.client.Adapter.SoftwarelistAdatper.ItemViewHolder;
import com.appdear.client.Adapter.UpdateListAdapter;
import com.appdear.client.download.FileDownloaderService;
import com.appdear.client.download.MoreManagerDownloadActivity;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ServiceUtils;

/**
 * 列表基类
 * 
 * @author zqm
 */
public abstract class ListBaseActivity extends BaseActivity implements
		RefreshDataListener {
	/**
	 * 显示列表
	 */
	protected ListViewRefresh listView;
	/**
	 * zxy 2011 10 12 添加标志，覆盖基类的抬出加载提示框
	 */
	protected boolean isShowAlert = false;
	// private LinearLayout loadingView = null;
	// protected GifView gifView = null;
	protected LinearLayout loadingView = null;
//	protected Toast mScreenHint;
	protected LayoutParams params;
	protected static int width = 0;
	protected static int height = 0;
	/**
	 * 总页码
	 */
	protected int PAGE_TOTAL_SIZE = 1;
	/**
	 * 每页显示10条
	 */
	protected int PAGE_SIZE = 15;

	/**
	 * adapter
	 */
	public BaseAdapter adapter;

	/**
	 * 页码
	 */
	protected int page = 1;

	public IntentFilter filter;
	// 解决已分享界面动画位置问题
	public boolean isOyyAlreadActivity = false;

	/**
	 * 刷新UI
	 */
	public abstract void refreshDataUI();

	protected boolean flag = true;

	/**
	 * 
	 */
	public final static int LOADV = 11;

	/**
	 * 
	 */
	public final static int LOADG = 12;

	/**
	 * 刷新数据
	 */
	public abstract void doRefreshData();

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		if (flag) {

			if (height == 0 && width == 0) {
				height = this.getWindowManager().getDefaultDisplay()
						.getHeight();
				width = this.getWindowManager().getDefaultDisplay().getWidth();
			}
			// ));//添加控件,-1是设置高度和宽度充满布局，-2是按照需要设置宽度高度。
			if (!AppContext.getInstance().isNetState) {
				handler(false);
				return;
			}
			// loadingView=new MProgress(this,false);
			// this.addContentView(loadingView, params);
		}
	}

	public void refreshState(int state) {
		if (page > PAGE_TOTAL_SIZE) {
			if (View.VISIBLE == state) {
//				if (mScreenHint != null)
//					mScreenHint.cancel();
				Toast.makeText(this, "已经是最后一页",Toast.LENGTH_SHORT).show();
//				this.mScreenHint.show();
			} else {
//				if (mScreenHint != null)
//					mScreenHint.cancel();
				return;
			}
		} else {
			if (View.VISIBLE == state) {
//				if (mScreenHint != null)
//					mScreenHint.cancel();
				if (flag) {
					if (AppContext.getInstance().isNetState) {
						Log.i("info444","handler(true)");
						handler1(true);
					}
				}
			} else {
				if (flag) {
					handler1(false);
				}
				refreshDataUI();
				if (adapter != null) {
					actionLayoutNotShow();
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * 更新UI
	 * 
	 * @author
	 */
	public Handler handler1 = new Handler() {

		ItemViewHolder holder;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case JUST_NOW_UPDATE_FAVORITE: // 刚刚点击收藏的msg
				holder = (ItemViewHolder) msg.getData().get("info");
				holder.favoriteImageView
						.setBackgroundResource(R.drawable.favorite_list);
				holder.favoriteImageView.setEnabled(false);

				// 如果用户点击了收藏按钮，则自动隐藏“分享、收藏”界面
				// Utils.sleep(500);
				holder.actionLayout.setVisibility(View.GONE);
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.sizeAndPriceLayout.setVisibility(View.VISIBLE);
				break;
			case UPDATE_DOWNLOAD: // 点击下载
				holder = (ItemViewHolder) msg.getData().get("info");
				holder.actionLayout.setVisibility(View.GONE);
				holder.contentLayout.setVisibility(View.VISIBLE);
				break;
			case UPDATE_FAVORITE: // 检测到已收藏
				holder = (ItemViewHolder) msg.getData().get("info");
				holder.favoriteImageView
						.setBackgroundResource(R.drawable.favorite_list);
				holder.favoriteImageView.setEnabled(false);
				break;
			case UPDATE_UNFAVORITE: // 检测到未收藏
				holder = (ItemViewHolder) msg.getData().get("info");
				holder.favoriteImageView
						.setBackgroundResource(R.drawable.favorite_button_list);
				holder.favoriteImageView.setEnabled(true);
				break;

			case CALL_STATUS: // 检测到未收藏
				refreshDataUI();
				break;
			case SHARE_WEIBO_SUCESS: // 分享微博成功
				Toast.makeText(ListBaseActivity.this, "微博发送成功",
						Toast.LENGTH_SHORT).show();
				break;
			case SHARE_WEIBO_FAILURE: // 分享微博失败
				Bundle b = msg.getData();
				String message = b.getString("message");
				Toast.makeText(ListBaseActivity.this, message,
						Toast.LENGTH_SHORT).show();
				break;
			case LOADV:
				if (loadingView != null)
					loadingView.setVisibility(View.VISIBLE);
				break;
			case LOADG:
				if (loadingView != null)
					loadingView.setVisibility(View.GONE);
				break;
			}
		}
	};

	public void refreshUI(int position) {
		dataNotifySetChanged();
	}
	public void refreshUI(int first,int last) {
 		int count=first;
 		if(listView==null||!Common.ISLOADSOFTICON)return;
		for(int i=0;i<=last-first;i++){
			View v=listView.getChildAt(i);
			
			if(v!=null){
				
				ItemViewHolder holder=(ItemViewHolder)v.getTag();
 				if(holder!=null&&holder.isupload==false){
					if ("".equals(holder.item.softicon)) {
						holder.icon
						.setImageResource(R.drawable.soft_lsit_icon_default);
					} else {
						if ("".equals(getStr(holder.item.softiconPath))) {

							File fileDir = ServiceUtils
									.getSDCARDImg(Constants.CACHE_IMAGE_DIR);

							String f[] = holder.item.softicon.replace("http://", "")
									.split("/");

							String filename = f[f.length - 1];

							// 查看文件是不是存在
							if (fileDir != null
									&& fileDir.exists()
									&& new File(fileDir.getAbsoluteFile() + "/"
											+ filename).exists()) {

								String filepath = fileDir.getAbsoluteFile() + "/"
										+ filename;

								
								try{
									holder.icon.setImageURI(Uri.parse(filepath));
						    	}catch (OutOfMemoryError e) {
									System.gc();
									Log.e("load image", "内存溢出啦");
								}	
						    	holder.item.softiconPath = filepath;

							} else {
	                               
								Bitmap bitmap =MyApplication.getInstance().getBitmapByUrl(holder.item.softicon);
								
							//	BitmapTemp bite = imageCache.isCached(item.softicon);
								if (bitmap != null) {
										holder.icon.setImageBitmap( bitmap);
							    	 		
								} else {
									holder.icon.setImageResource(R.drawable.soft_lsit_icon_default);

									// 下载
									if (listView != null
											&& listView.getCountflag() != OnScrollListener.SCROLL_STATE_FLING
											&& listView.getCountflag() != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

										holder.icon.setImageUrl(holder.item.softicon,
												count, listView, true);
									}

								}

								//
							}

						} else {
							// 已经赋值过

							if (!new File(getStr(holder.item.softiconPath)).exists()) {
								// 下载到到内存
								Bitmap bitmap =MyApplication.getInstance().getBitmapByUrl(holder.item.softicon);

								//BitmapTemp bite = imageCache.isCached(item.softicon);
								if (bitmap != null ) {
									holder.icon.setImageBitmap( bitmap);
								} else {

									holder.icon
											.setImageResource(R.drawable.soft_lsit_icon_default);

									// 下载
									if (listView != null
											&& listView.getCountflag() != OnScrollListener.SCROLL_STATE_FLING
											&& listView.getCountflag() != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

										holder.icon.setImageUrl(holder.item.softicon,
												count, listView, true);
									}

								}

							} else {
								//
								try{
									holder.icon.setImageURI(Uri.parse(getStr(holder.item.softiconPath)));
								}catch (OutOfMemoryError e) {
									System.gc();
								}
							}

						}
				
					}
			
				}
			}
				count++;
			}
	}

	public void dataNotifySetChanged() {
	}
	public void dataNotifySetChanged(int first,int last) {
	}
	public void refreshData() {
		doRefreshData();
	}

	@Override
	public void showendalert() {
//		if (mScreenHint != null)
//			mScreenHint.cancel();
		 Toast.makeText(this, "已经是最后一页",Toast.LENGTH_SHORT).show();
//		this.mScreenHint.show();
	}

	/**
	 * @param adapter
	 *            the adapter to set
	 */
	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * @return the adapter
	 */
	public BaseAdapter getAdapter() {
		return adapter;
	}

	private void handler(boolean flag) {
		if (flag == true) {
			if (loadingView != null) {
				loadingView.setBackgroundDrawable(null);
				loadingView.setVisibility(View.VISIBLE);
			}
		} else {
			if (loadingView != null) {
				loadingView.setVisibility(View.GONE);
			}
		}
	}
	private void handler1(boolean flag) {
		if (flag == true) {
			if (loadingView != null) {
				((MProgress)loadingView).cancelBack();
				loadingView.setVisibility(View.VISIBLE);
			}
		} else {
			if (loadingView != null) {
				((MProgress)loadingView).showBack();
				loadingView.setVisibility(View.GONE);
			}
		}
	}
	@Override
	protected void updateUIStart() {
		if (flag) {
			if (AppContext.getInstance().isNetState) {
				handler(true);
			} else {
				handler(false);
			}
		}
		// do nothing ,just override super class .
	}

	@Override
	protected void updateUIErr(String msg) {
		if (flag) {
			handler(false);
		}
		// do nothing ,just override super class .
	}

	@Override
	protected void updateUIEnd() {
		if (flag) {
			handler(false);
		}
	}

	/**
	 * 排除不显示的activity
	 */
	private boolean notShowActivity() {
		if (this instanceof SearchActivity
				|| this instanceof SearchResultActivity) {
			Log.i("test", "test,notShowActivity," + true);
			return true;
		}
		Log.i("test", "test,notShowActivity," + false);
		return false;
	}
	public void actionLayoutNotShow()
	{
		if (listView!=null&&  listView.getIndex()!=-1 && listView.findViewWithTag("index"+listView.getIndex())!=null) 
		{
    		
			RelativeLayout   layout=(RelativeLayout )listView.findViewWithTag("index"+listView.getIndex());
			layout.setVisibility(View.GONE);
			//((LinearLayout)layout.findViewById(R.id.actionLayout)).setVisibility(View.GONE);
			layout.setTag(null);
			listView.setIndex(-1);
		}
	}
	@Override
	protected void onResume() {
		actionLayoutNotShow();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	protected void onStart() {
		if( (this  instanceof HomePageBestListActivity) || (this instanceof HomePageSpecialListActivity)||
				(this instanceof HomePageNewSoftListActivity)||(this instanceof MoreManagerDownloadActivity)||
				(this instanceof MoreManagerUpdateActivity)||(this instanceof CategoryDetailListActivity)||
				(this instanceof CategorySubjectDetailsActivity)||(this instanceof FavoriteListActivity)||
				(this instanceof MatchSoftListActivity)||(this instanceof MyShareListActivity)||
				(this instanceof SearchResultActivity)||(this instanceof SoftwareDetailAuthorInfoActivity))
		{
			filter = new IntentFilter();
			// 下载完成刷新界面
			filter.addAction(Common.DOWNLOAD_NOTIFY);
			registerReceiver(mReceiver1, filter);
			// 安装完成刷新界面
			filter.addAction(Intent.ACTION_PACKAGE_ADDED);
			filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			filter.addDataScheme("package");
			registerReceiver(mReceiver, filter);
		}
		
		super.onStart();
	}

	protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
 			if (adapter != null
					&& (adapter instanceof SoftwarelistAdatper || adapter instanceof UpdateListAdapter)) {
 				if (intent == null || intent.getDataString() == null
						|| intent.getDataString().equals(""))
					return;
 		        String packageName = intent.getData().getSchemeSpecificPart();
//				String packageName = intent.getDataString().substring(8);
				if (intent.getAction().equals(
						"android.intent.action.PACKAGE_ADDED")) {
					int softid = -1;
					if (MyApplication.getInstance().getAppMap()
							.get(packageName) != null) {
						softid = MyApplication.getInstance().getAppMap()
								.get(packageName);
 					}
					if (softid != -1) {
						MyApplication.getInstance().getSoftMap()
								.put(softid, Math.abs(packageName.hashCode()));
						MyApplication.getInstance().getAppMap()
								.remove(packageName);
						if (FileDownloaderService.notificationManager != null) {
							FileDownloaderService.notificationManager
									.cancel(softid);
						}
					}
				} else if (intent.getAction().equals(
						"android.intent.action.PACKAGE_REMOVED")) {
					AppContext.getInstance().installlists.remove(packageName);

					ServiceUtils.removeSoftStateUninstall(packageName);

					
					boolean isdelete = false;
					Iterator<PackageinstalledInfo> it = AppContext
							.getInstance().updatelist.iterator();
					while (it.hasNext()) {
						PackageinstalledInfo info = it.next();
						if (info.pname.equals(packageName)) {
							AppContext.getInstance().installlists.remove(info);
//							Log.e("suyi", "delete");
							isdelete = true;
							
							break;
						}
					}
                    boolean isUpdateMainView=true;
					if (!isdelete) {
						it = AppContext.getInstance().elideupdatelist
								.iterator();
						while (it.hasNext()) {
							PackageinstalledInfo info = it.next();
							if (info.pname.equals(packageName)) {
								AppContext.getInstance().elideupdatelist
										.remove(info);
//								Log.e("suyi", "delete");
								isdelete = true;
								isUpdateMainView=false;
								break;
							}
						}
					}
					if(isUpdateMainView&&isdelete)
					{
						MyApplication.getInstance().elideupdate--;
						((MainActivity)MyApplication.getInstance().mainActivity).updateNumView();
					}
				}
				actionLayoutNotShow();
				adapter.notifyDataSetChanged();
			}
			isrefreshUI = true;
		};
	};

	protected BroadcastReceiver mReceiver1 = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
 			int softid = intent.getIntExtra("softid", -1);
			 float process=intent.getFloatExtra("dprocess",0);
			// dprocess -1 完成 -2 暂停 -3 删除
		    int downloadfinsh=intent.getIntExtra("downloadfinsh", 0);
			//System.out.println("-listview--onReceive-------dprocess="+process);

			if (listView == null || listView.findViewWithTag(softid) == null) {
				return;
			}
			if (downloadfinsh == 1) {
				if (adapter != null&& (adapter instanceof SoftwarelistAdatper || adapter instanceof UpdateListAdapter)) 
				{
					if (listView != null) 
					{
						TextView processTextView = (TextView) listView
								.findViewWithTag(softid);

						if (processTextView != null) 
						{
							actionLayoutNotShow();
 							adapter.notifyDataSetChanged();
						}
					}
				}
			} else {
				if (listView != null && process != 0) {

					TextView processTextView = (TextView) listView
							.findViewWithTag(softid);
 					if (processTextView != null) {
						processTextView.setVisibility(View.VISIBLE);
						processTextView.setText(process + "%");
					}
				}
			}

		}
		 
	};

	protected void onDestroy() {
		listView=null;
		if (filter != null) {
			try{
				this.unregisterReceiver(mReceiver);
			}catch(java.lang.IllegalArgumentException e){
				
			}
		}
		if (filter != null) {
			try{
			this.unregisterReceiver(mReceiver1);
			}catch(java.lang.IllegalArgumentException e){
				
			}
		}
		if (loadingView != null) {
			loadingView = null;
		}
		// if(gifView!=null){
		// gifView.destroyDrawingCache();
		// gifView=null;
		// }
		super.onDestroy();
		
	};
	
	public String getStr(String str){
		if(str==null)return "";
		else return str;
	}
 
}