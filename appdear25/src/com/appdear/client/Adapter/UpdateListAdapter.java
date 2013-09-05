package com.appdear.client.Adapter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.MainActivity;
import com.appdear.client.R;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.utility.ServiceUtils;

public class UpdateListAdapter extends BaseAdapter  {
	private Animation mTrackAnim;
	private ListBaseActivity context;
	private LayoutInflater mInflater;
	private PackageManager pm;
	private Handler handler;
	private ListViewRefresh listview;
	private Map<Integer,Boolean> isSelectedMap;
 
	// private CallBack mCallBack;
	// public CallBack getmCallBack() {
	// return mCallBack;
	// }
	// public void setmCallBack(CallBack mCallBack) {
	// this.mCallBack = mCallBack;
	// }
	//
	public void initSelectMap()
	{
		isSelectedMap=new HashMap<Integer,Boolean>();
		int i=0;
		for(PackageinstalledInfo info:AppContext.getInstance().updatelist)
		{
			isSelectedMap.put(i, false);
			i++;
		}
	}
	public UpdateListAdapter(ListBaseActivity context, Handler handler,ListViewRefresh listview) {
		this.context = context;
		this.listview=listview;
		mInflater = LayoutInflater.from(context);
		pm = context.getPackageManager();
		this.handler = handler;
		initSelectMap();
	}

	@Override
	public int getCount() {
		return AppContext.getInstance().updatelist.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < AppContext.getInstance().updatelist.size())
			return AppContext.getInstance().updatelist.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public synchronized View getView(final int position, View convertView,
			ViewGroup parent) {
		 

		if (position > AppContext.getInstance().updatelist.size() - 1)
			return null;
		PackageinstalledInfo item = AppContext.getInstance().updatelist
				.get(position);

		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.more_update_list_item_layout,
					parent, false);
		}
		
		ItemViewHolder holder = null;
		if (holder == null) {
			holder = new ItemViewHolder();
			holder.position = position;
			holder.item = item;
			holder.icon = (ImageView) view.findViewById(R.id.imageView);
			holder.titleTextView = (TextView) view.findViewById(R.id.title);
			holder.contentTextView = (TextView) view.findViewById(R.id.desc);
			holder.alertTextView = (TextView) view.findViewById(R.id.alert);
			holder.button = (Button) view.findViewById(R.id.uninstall);
			holder.processView=(TextView)view.findViewById(R.id.process);
			holder.button
					.setBackgroundResource(R.drawable.download_image_update);
			holder.buttonelide = (Button) view.findViewById(R.id.elide);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			holder.button.setTag(holder);
			holder.buttonelide.setTag(holder);
			MyListener myListener = new MyListener(holder, position);
			holder.button.setOnClickListener(myListener);
			holder.buttonelide.setOnClickListener(myListener);
		//	holder.shareAndfavLayout=(RelativeLayout)view.findViewById(R.id.shareAndfavLayout);
			holder.actionLayout = (LinearLayout) view.findViewById(R.id.actionLayout);
		    holder.shrinkButton=(Button)view.findViewById(R.id.shrinkButton);
		    holder.ignoreButton=(Button)view.findViewById(R.id.ignoreButton);
		    holder.descriptionLayout=(RelativeLayout)view.findViewById(R.id.descriptionLayout);
		    holder.shrinkButton.setOnClickListener(myListener);
		    holder.ignoreButton.setOnClickListener(myListener);
		    holder.descriptionTextView=(TextView)view.findViewById(R.id.descriptionTextView);
		    
		    
		//	holder.shareImageView = (ImageView) view.findViewById(R.id.shareImageView);
		//	holder.editImageView=(ImageView) view.findViewById(R.id.favoriteImageView);
		//	holder.editImageView.setBackgroundResource(R.drawable.hulue);
	// 分享
		//	holder.shareImageView.setBackgroundResource(R.drawable.detail_donload_list);
			
			view.setTag(position);
			holder.shrinkButton.setTag(holder);
			holder.ignoreButton.setTag(holder);
			
			if(isSelectedMap==null||isSelectedMap.get(position)==null)
			{
				initSelectMap();
			}
			 if(isSelectedMap.get(position))
			 {
				 holder.shrinkButton.setBackgroundResource(R.drawable.update_less);
				 holder.descriptionTextView.setMaxLines(context.getWallpaperDesiredMinimumHeight());
			 }else
			 {
				  holder.shrinkButton.setBackgroundResource(R.drawable.update_expansion);
				  holder.descriptionTextView.setLines(2);
			 }
	//		holder.editImageView.setTag(holder);
	//		holder.shareImageView.setTag(holder);
			//holder.icon.setTag(holder);
		}
		holder.processView.setVisibility(View.GONE);
		holder.button.setBackgroundResource(R.drawable.download_image_update2);
		SiteInfoBean bean1 = ServiceUtils.checkTaskState(item.softID);
		// 下载中判断是否完成下载
		if (bean1 != null && bean1.state == 2) {
			// apk文件完整
			holder.state = 2;
			holder.item.status = 2;
			// 可点击时设置状态
			holder.button.setEnabled(true);
			holder.button
					.setBackgroundResource(R.drawable.download_image_install2);
		} else {
			if (bean1 != null) {
				// apk文件不完整
				holder.button
						.setBackgroundResource(R.drawable.download_image_downloading2);
				holder.state = 1;
				holder.item.status = 1;
				holder.processView.setVisibility(View.VISIBLE);
				if(bean1!=null)
				{
					holder.processView.setText(bean1.getProgress1()+"%");
				}
				// 可点击时设置状态
				holder.button.setEnabled(false);
			} else {
				holder.button.setEnabled(true);
			}
		}
		if (item != null) {
			if (item.icon == null) {
				try {
					item.icon = pm.getApplicationIcon(item.pname);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			holder.icon.setImageDrawable(item.icon);
			if (item.appname == null || item.appname.equals("")) {
				try {
					item.appname = pm.getPackageInfo(item.pname,
							PackageManager.GET_ACTIVITIES).applicationInfo
							.loadLabel(pm).toString();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(item.appname.length()>=10)
			{
				 holder.titleTextView.setText(item.appname.substring(0, 9)+"...");
 			}else
			{
			holder.titleTextView.setText(item.appname);
			}
			if(item.versionName!=null){
				if (item.versionName.toLowerCase().indexOf("v") != -1)
					holder.contentTextView.setText("目前版本：" + item.versionName);
				else
					holder.contentTextView
							.setText("目前版本：" + "v" + item.versionName);
			}else
				holder.contentTextView.setText("目前版本：");
			
			holder.alertTextView.setText("最新版本：" + item.updateVesrionName);
			 
			//System.out.println(item.udlinenum+"desc====="+item.updatedesc);
 			if(item.updatedesc==null||"".equals(item.updatedesc))
			{
				holder.descriptionLayout.setVisibility(View.GONE);
			}else
			{
				//String temp=handlerToText(item.updatedesc);
				/* String[] temps=item.updatedesc.split("\\n");
				 String temp="";
				 int count=temps.length;
				 int i=0;
				 for(String str :temps)
				 {
					 i++;
					// System.out.println(i+"temps==="+str);
					 if(i==temps.length)
					 {
						 temp=temp+str ;  
					 }else
					 {
						 temp=temp+str+"\n";
					 }
				 }*/

				holder.descriptionLayout.setVisibility(View.VISIBLE);
				holder.descriptionTextView.setText(item.updatedesc);
				/*if(temps.length<2)
				{
			     if(temp.equals("暂无更新信息")||temp.length()<10)
				  {
					holder.descriptionTextView.setHeight(50);
				  }
				}*/
				
				if(item.udlinenum>2)
				{
					holder.shrinkButton.setVisibility(View.VISIBLE);
				}else if(item.udlinenum<=2&&item.updatedesc.length()>40)
				{
					holder.shrinkButton.setVisibility(View.VISIBLE);
				}else
				{
					holder.shrinkButton.setVisibility(View.INVISIBLE);
				}
			}

		}
		holder.processView.setTag(item.softID);
		return view;
	}
	public String handlerToText(String text){
		if(text!=null){
			StringBuffer sb=new StringBuffer("");
			String[] s=text.split("\\\n");
			int count=0;
			for(String r:s){
				sb.append(r);
				if(count<s.length-1){
					sb.append("\n");
				}
				count++;
			}
			return sb.toString();
		}
		return null;
	}
	protected class ItemViewHolder {
		public ImageView icon;
		public TextView titleTextView;
		public TextView contentTextView;
		public TextView alertTextView;
		public Button button;
		public Button buttonelide;
	 //   public RelativeLayout shareAndfavLayout;
	    public LinearLayout actionLayout;
		// 0--下载，1--下载中 2--安装 3--已安装
		public int state = 0;
		public int position;
		public PackageinstalledInfo item;
		public ProgressBar progressBar;
		public TextView processView;
		public TextView descriptionTextView;
		public Button shrinkButton;
		public Button ignoreButton;
		public RelativeLayout descriptionLayout;
		
	//	public ImageView shareImageView;
	//	public ImageView editImageView;
		
	}

	class MyListener implements OnClickListener {
		private ItemViewHolder holder = null;
		private int postion;

		public MyListener(ItemViewHolder holder, int postion) {
			this.holder = holder;
			this.postion = postion;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.uninstall) {

				if (holder.state == 0) {
					
					ItemViewHolder holder = (ItemViewHolder) v.getTag();
					holder.processView.setVisibility(View.VISIBLE);
					holder.processView.setText("0.0%");
					SoftlistInfo item = new SoftlistInfo();
					item.appid = (holder.item.pname);
					item.softid = (holder.item.softID);
					item.softname = (holder.item.appname);
					item.icon = holder.item.icon;
					item.version = holder.item.updateVesrionName;
					item.softsize = holder.item.softsize;
					item.downloadurl = holder.item.downloadUrl;
					if (download(item,
							ServiceUtils.getApkname(item.downloadurl),
							holder.item)) {
						holder.state = 1;
						holder.button
								.setBackgroundResource(R.drawable.download_image_downloading);
						// 可点击时设置状态
						holder.button.setEnabled(false);
					}
				} else if (holder.state == 2) {
					String path = "";
					File file = ServiceUtils.getSDCARDImg(Constants.APK_DATA);
					if (file != null && ServiceUtils.isHasFile(file.getPath() +"/" + 
							ServiceUtils.getApkname(holder.item.downloadUrl))) {
						//取资源文件
						path = file.getPath() +"/" + ServiceUtils.getApkname(holder.item.downloadUrl);
					} else {
						//取内存
						path = Constants.DATA_APK + "/" + ServiceUtils.getApkname(holder.item.downloadUrl);
					}
					holder.state = 2;
					// 安装
				if (ServiceUtils.isHasFile(path)) {
					ServiceUtils.Install(context, path, holder.item.pname,  holder.item.softID, handler);
					new Thread(new Runnable() {

						@Override
						public void run() {
							ApiManager.downloadcomplete(
									holder.item.downloadUrl, "2", "update");
						}
					}).start();
				  }else{
					  Toast mScreenHint = Toast.makeText(context, "安装文件不存在",Toast.LENGTH_SHORT);
						mScreenHint.show();
				  }
				}
			} else if (v.getId() == R.id.elide) {

				// if(mCallBack!=null){
				// mCallBack.doSome(null);
				// }

				PackageinstalledInfo mPackageinstalledInfo = AppContext
						.getInstance().updatelist.remove(postion);
				AppContext.getInstance().elideupdatelist
						.add(mPackageinstalledInfo);
				ServiceUtils.putOneElidePackages(context,
						mPackageinstalledInfo.pname);

				UpdateListAdapter.this.notifyDataSetChanged();
				
			} else if (v.getId() == R.id.ignoreButton) {
				clickeditImage(v);
			} else if (v.getId() == R.id.shrinkButton) {
			//	textView.setMaxLines(context.getWallpaperDesiredMinimumHeight());
				ItemViewHolder holder = (ItemViewHolder) v.getTag();
				int index= holder.position;
				if(isSelectedMap.get(index))
				{
					isSelectedMap.put(index, false);
					holder.descriptionTextView.setLines(2);
					holder.shrinkButton.setBackgroundResource(R.drawable.update_expansion);
				}else
				{
					isSelectedMap.put(index, true);
					System.out.println("MinimumHeight="+context.getWallpaperDesiredMinimumHeight());
					holder.descriptionTextView.setMaxLines(context.getWallpaperDesiredMinimumHeight());
					holder.shrinkButton.setBackgroundResource(R.drawable.update_less);
				}
			}
		}
	}

	private boolean download(SoftlistInfo info, String apkname,
			PackageinstalledInfo item) {
		final SiteInfoBean downloadbean = new SiteInfoBean(info.downloadurl,
				ServiceUtils.getSDCARDImg(Constants.APK_DATA) == null ? ""
						: ServiceUtils.getSDCARDImg(Constants.APK_DATA)
								.getPath(),
				ServiceUtils.getApkname(info.downloadurl), info.softname,
				info.softicon, info.version, info.softid, info.appid,
				info.softsize, 0, 1, null, null,
				BaseActivity.downloadUpdateHandler, Constants.UPDATEPARAM);
		item.downloadUrl = downloadbean.sSiteURL;
 		downloadbean.dicon = info.icon;

		// 提示下载信息
		String[] msg = DownloadUtils.download(downloadbean, context);
		Toast mScreenHint = Toast.makeText(context, msg[0],Toast.LENGTH_SHORT);
		mScreenHint.show();
		if (msg[1] != null && msg[1].equals("0"))
			return true;
		return false;
	}
	/**
	 * 处理icon图标按钮
	 *//*

	 public void clickIconImage(View v) {
			ItemViewHolder holder = (ItemViewHolder) v.getTag();
        //  System.out.println("-------clickIconImage-------position="+holder.position);
        //  System.out.println("last="+listview.getLastVisiblePosition()+"    first="+listview.getFirstVisiblePosition());
			if(listview==null)return;
			if(listview.getIndex()!=-1)
			{
				if (listview != null&&listview.findViewWithTag("index"+listview.getIndex())!=null) 
				{
					RelativeLayout   layout=(RelativeLayout )listview.findViewWithTag("index"+listview.getIndex());
					layout.setVisibility(View.GONE);
					//((LinearLayout)layout.findViewById(R.id.actionLayout)).setVisibility(View.GONE);
					layout.setTag(null);
					//index = -1;
					listview.setIndex(-1);
					if(layout==holder.shareAndfavLayout)
					{
						return;
					}
				}
			}
			v.requestFocus();
			int[] xy = new int[2];
			v.getLocationInWindow(xy);
			 Rect rect = new Rect(xy[0], xy[1]+30, xy[0]+v.getWidth(), xy[1]+v.getHeight());	
			Display display = context.getWindowManager().getDefaultDisplay(); 

			int height = display.getHeight();
			int actualHeight = 0;
			 
			if (height > 480) {
				actualHeight = (int) (height * 0.75);
			} else {
				actualHeight = (int) (height * 0.7);
			}
 		   if (rect.top > actualHeight) {
 
			 if(listview.getHeaderViewsCount()==0)
			 {
				 listview.setSelectionFromTop(holder.position,(listview.getHeight() - ServiceUtils.dip2px(67+63,context)+3));
			 }else
			 {
				 listview.setSelectionFromTop(holder.position,(listview.getHeight() - ServiceUtils.dip2px(67*2+63,context)+3)); 
			 }
 
		 	} 
 			 holder.shareAndfavLayout.setTag("index"+holder.position);
			 listview.setIndex(holder.position);
 			 holder.shareAndfavLayout.setVisibility(View.VISIBLE);
 		     mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.quickaction);
			 mTrackAnim.setInterpolator(new Interpolator() {
					public float getInterpolation(float t) {	 
						final float inner = (t * 1.55f) - 1.1f;
 						return 1.2f - inner * inner;
					}
				});			 
			 holder.actionLayout.startAnimation(mTrackAnim);
 
		}*/
	 	/*public void  actionLayoutNotShow()
	    {
	    	if(listview.getIndex()==-1) return;
	 
	    		
	    		if ( listview.findViewWithTag("index"+listview.getIndex())!=null) 
				{
					RelativeLayout   layout=(RelativeLayout )listview.findViewWithTag("index"+listview.getIndex());
					layout.setVisibility(View.GONE);
					layout.setTag(null);
					listview.setIndex(-1);
	 			}
	 
	    }*/
	 	/*@Override
		public void onClick(View v) {
			

			switch (v.getId()) {
				case R.id.shareImageView:
				 actionLayoutNotShow();
				 clickDetailImage(v);
				break;
				case R.id.favoriteImageView:
				 actionLayoutNotShow();
				 clickeditImage(v);
				break;
				case R.id.imageView:
				clickIconImage(v);
				break;
			}
		}*/
	 	 public void clickDetailImage(View v)
		 {
			 v.requestFocus();
			 ItemViewHolder holder=(ItemViewHolder)v.getTag();
			 PackageinstalledInfo saveBean=holder.item;
			 //softid,String softicon,String downloadurl,String... intentcentent
			 if(saveBean!=null)
			 ServiceUtils.setSelectedValuesDownload(context, saveBean.softID, null,saveBean.downloadUrl );
		 }
	 	 public void clickLoadImage(View v) {
	 		ItemViewHolder holder = (ItemViewHolder) v.getTag();
	 		try
			{
	 			if(holder!=null&&holder.item!=null){
	 				Intent intent = context.getPackageManager().getLaunchIntentForPackage(holder.item.pname);
	 				context.startActivity(intent);
	 			}
			}catch (Exception e) {
				Toast mScreenHint = Toast.makeText(context, "该应用不能直接打开",Toast.LENGTH_SHORT);
				mScreenHint.show();
			}
		 }

	 	/**
	 	 * 点击忽略按钮
	 	 */
	 	public void clickeditImage(View v) {
	 		ItemViewHolder holder = (ItemViewHolder) v.getTag();
	 		if(holder!=null&&holder.item!=null){
	 			canalAllUpdate(holder.item.pname);
	 		}
	 		
	 	}
	 	private synchronized void canalAllUpdate(String appname) {
			if (AppContext.getInstance().updatelist != null
					&& AppContext.getInstance().updatelist.size() > 0) {
				for (int i = AppContext.getInstance().updatelist.size() - 1; i >= 0; i--) {
					PackageinstalledInfo mPackageinstalledInfo = AppContext.getInstance().updatelist.get(i);
					if(mPackageinstalledInfo.pname.equals(appname)){
						AppContext.getInstance().elideupdatelist
						.add(mPackageinstalledInfo);
						ServiceUtils.putOneElidePackages(context,
								mPackageinstalledInfo.pname);
						AppContext.getInstance().updatelist.remove(i);
						MyApplication.getInstance().elideupdate--;
						((MainActivity)MyApplication.getInstance().mainActivity).updateNumView();
						break;
					}else{
						continue;
					}
				}
				if (this != null) {
					this.notifyDataSetChanged();
				}
			}
		}
}
