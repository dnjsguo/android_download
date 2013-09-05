package com.appdear.client.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;

import com.appdear.client.R;
import com.appdear.client.commctrls.AsynLoadDetailImageView;
import com.appdear.client.download.ListAdatperDataProcessListener;
import com.appdear.client.utility.AsyLoadImageService;
import com.appdear.client.utility.BitmapTemp;
import com.appdear.client.utility.ImageCache;

public class ViewPicAdapter extends BaseAdapter {

	/**
	 * Õº∆¨µÿ÷∑
	 */
	private List<String> list = new ArrayList<String>();
	
	private Context context;
	
	private ListAdatperDataProcessListener listener;
 	private static ImageCache imageCache = AsyLoadImageService.getInstance().getImageCache();

	public ViewPicAdapter(Context context, List<String> list, 
			ListAdatperDataProcessListener listener) {
		this.list = list;
		this.context = context;
		this.listener = listener;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
 		String imageurl = list.get(position);
		View view = LayoutInflater.from(context).inflate(R.layout.viewpic_layout, null);
		AsynLoadDetailImageView imgview = (AsynLoadDetailImageView) view.findViewById(R.id.software_image);
		imgview.setImageResource(R.drawable.software_default_img);
		imgview.setId(position);
		if (!imageurl.equals("")){
		 	//recycledImg(imageurl);
			imgview.setImageUrl(imageurl, true,true);
		}	
		imgview.setBackgroundResource(R.drawable.soft_shot_bg);
		imgview.setScaleType(ScaleType.FIT_XY);
		if (listener != null)
			listener.keyPressProcess(imgview, 1);
		return view;
	}
	private void recycledImg(String imgurl){
		if(imgurl==null){
			return;
		}
		
		if(imageCache!=null){
			BitmapTemp temp=null;
				if((temp=imageCache.isCached(imgurl))!=null) {
					if(temp.bitmap!=null&&temp.bitmap.isRecycled()==false){
						temp.bitmap.recycle();
					}
					imageCache.remove(imgurl);
				}
			}
	}
}
