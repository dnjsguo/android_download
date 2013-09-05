package com.appdear.client.Adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdear.client.MainActivity;
import com.appdear.client.R;
import com.appdear.client.Adapter.UpdateListAdapter.ItemViewHolder;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.download.DownloadUtils;
import com.appdear.client.download.SiteInfoBean;
import com.appdear.client.model.PackageinstalledInfo;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.utility.ServiceUtils;

public class ElideUpdateListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private PackageManager pm;
	private Map<Integer,Boolean> isSelectedMap;
	// private CallBack mCallBack;
	// public CallBack getmCallBack() {
	// return mCallBack;
	// }
	// public void setmCallBack(CallBack mCallBack) {
	// this.mCallBack = mCallBack;
	// }
	//
	public ElideUpdateListAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		pm=context.getPackageManager();
		
		initSelectMap();
	}
	public void initSelectMap()
	{
		isSelectedMap=new HashMap<Integer,Boolean>();
		int i=0;
		for(PackageinstalledInfo info:AppContext.getInstance().elideupdatelist)
		{
			
			isSelectedMap.put(i, false);
			i++;
		}
	}
	@Override
	public int getCount() {
		return AppContext.getInstance().elideupdatelist.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < AppContext.getInstance().elideupdatelist.size())
			return AppContext.getInstance().elideupdatelist.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public synchronized View getView(final int position, View convertView,
			ViewGroup parent) {
		if (position > AppContext.getInstance().elideupdatelist.size() - 1)
			return null;
		PackageinstalledInfo item = AppContext.getInstance().elideupdatelist
				.get(position);

		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(
					R.layout.more_update_elide_list_item_layout, parent, false);
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

			holder.buttonelide = (Button) view.findViewById(R.id.canle_elide);

			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);

			holder.buttonelide.setTag(holder);
			MyListener myListener = new MyListener(holder);

			holder.buttonelide.setOnClickListener(myListener);

			
			holder.shrinkButton=(Button)view.findViewById(R.id.shrinkButton);
 		    holder.descriptionLayout=(RelativeLayout)view.findViewById(R.id.descriptionLayout);
		    holder.shrinkButton.setOnClickListener(myListener);
 		    holder.descriptionTextView=(TextView)view.findViewById(R.id.descriptionTextView);
		
		    
		    holder.shrinkButton.setTag(holder);
 			
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
			view.setTag(holder);
		}
//		holder.button_text.setText("升级");
//		SiteInfoBean bean1 = ServiceUtils.checkTaskState(item.softID);
//		// 下载中判断是否完成下载
//		if (bean1 != null && bean1.state == 2) {
//			// apk文件完整
//			holder.state = 2;
//			holder.item.status = 2;
//			// 可点击时设置状态
//			holder.button.setText("");
//			holder.button
//					.setBackgroundResource(R.drawable.softinfo_install);
//			holder.button.setEnabled(true);
//			holder.button_text.setText("安装");
//		} else {
//			if (bean1 != null) {
//				// apk文件不完整
//				holder.button_text.setText("升级中");
//				holder.button
//						.setBackgroundResource(R.drawable.download_image_downloading);
//				holder.button.setTextColor(Color.rgb(0, 148, 245));
//				holder.state = 1;
//				holder.item.status = 1;
//				// 可点击时设置状态
//
//				holder.button.setEnabled(false);
//			} else {
//				holder.button.setEnabled(true);
//			}
//		}
		
		if (item != null) {
			if(item.icon==null){
				try {
					item.icon=pm.getApplicationIcon(item.pname);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(item.appname==null||item.appname.equals("")){
				try {
					item.appname=pm.getPackageInfo(item.pname,PackageManager.GET_ACTIVITIES).applicationInfo.loadLabel(pm).toString();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			holder.icon.setImageDrawable(item.icon);
			holder.titleTextView.setText(item.appname);
			if(item.versionName!=null&&item.versionName.toLowerCase().indexOf("v")!=-1)
				holder.contentTextView.setText("目前版本：" + item.versionName);
			else{
				if(item.versionName!=null){
					holder.contentTextView.setText("目前版本：" + "v"+item.versionName);
				}
				else{
					holder.contentTextView.setText("目前版本：");
				}
			}
			holder.alertTextView.setText("最新版本：" + item.updateVesrionName);
			
			
			if("".equals(item.updatedesc))
			{
				holder.descriptionLayout.setVisibility(View.GONE);
			}else
			{
				/* String[] temps=item.updatedesc.split("//n");
				 String temp="";
				 int count=temps.length;
				 int i=0;
				 for(String str :temps)
				 {
					 i++;
					 //System.out.println("temps==="+str);
					 if(i==temps.length)
					 {
						 temp=temp+str ; ;
					 }else
					 {
						 temp=temp+str+"\n";
					 }
					 
				 }*/
				 /*if(temps.length<2)
					{
				     if(temp.equals("暂无更新信息")||temp.length()<10)
					  {
						holder.descriptionTextView.setHeight(20);
					  }
					}*/
				holder.descriptionLayout.setVisibility(View.VISIBLE);
				holder.descriptionTextView.setText(item.updatedesc);
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
		return view;
	}

	protected class ItemViewHolder {
		public ImageView icon;
		public TextView titleTextView;
		public TextView contentTextView;
		public TextView alertTextView;
		public Button button;
		public Button buttonelide;
		// 0--下载，1--下载中 2--安装 3--已安装
		public int state = 0;
		public int position;
		public PackageinstalledInfo item;
		public ProgressBar progressBar;
		
		public TextView descriptionTextView;
		public Button shrinkButton;
 		public RelativeLayout descriptionLayout;
	}

	class MyListener implements OnClickListener {
		private ItemViewHolder holder = null;

		public MyListener(ItemViewHolder holder) {
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {
		 
			if (v.getId() == R.id.canle_elide) {
 
				PackageinstalledInfo mPackageinstalledInfo = AppContext
						.getInstance().elideupdatelist.remove(holder.position);
				AppContext.getInstance().updatelist.add(0,mPackageinstalledInfo);
				ServiceUtils.removeOneElidePackages(context,
						mPackageinstalledInfo.pname);

				ElideUpdateListAdapter.this.notifyDataSetChanged();
				MyApplication.getInstance().elideupdate++;
				((MainActivity)MyApplication.getInstance().mainActivity).updateNumView();
 			}else if (v.getId() == R.id.shrinkButton) {
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

	public void handlerAllview() {
		Log.i("info", "handlerAllView");
		for (PackageinstalledInfo info : AppContext.getInstance().elideupdatelist) {
			String filename = info.downloadUrl.substring(info.downloadUrl
					.lastIndexOf("/") + 1);
			if (filename.lastIndexOf("?") > 0)
				filename = filename.substring(0, filename.lastIndexOf("?"));
			SoftlistInfo item = new SoftlistInfo();
			item.appid = (info.pname);
			item.softid = (info.softID);
			item.softname = (info.appname);
			item.icon = info.icon;
			item.version = info.versionName;
			item.softsize = info.softsize;
			item.downloadurl = info.downloadUrl;
			if (download(item, filename, info)) {
			}
		}
	}
}
