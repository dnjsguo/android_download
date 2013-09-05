package com.appdear.client.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;

import com.appdear.client.R;
import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.commctrls.Common;
import com.appdear.client.commctrls.GalleryFlow;
import com.appdear.client.model.GalleryFlowInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.utility.ServiceUtils;

public class GalleryAdAdapter extends BaseAdapter{
	private String TAG = "GalleryAdAdapter";
	public Context context;
	ArrayList<GalleryFlowInfo> images = null;
	
	public GalleryAdAdapter(Context context,ArrayList<GalleryFlowInfo> galleryFlowAdModel){
		this.context = context;
		this.images = galleryFlowAdModel;
	}
	
	@Override
	public int getCount() {
		//return  Integer.MAX_VALUE;
		return images.size();
	}
	
	@Override
	public Object getItem(int position) {
		return images.get(position-1);
	}
	
	@Override
	public long getItemId(int position) {
		return position-1;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		DisplayMetrics metrics = ServiceUtils.getMetrics(((Activity)context).getWindowManager());
		int winHeight = metrics.heightPixels;
		int winWidth = metrics.widthPixels;
		
		AsynLoadImageView asynImageView = new AsynLoadImageView(context);
		asynImageView.setPadding(5, 10, 5, 8);
		asynImageView.setScaleType(ScaleType.FIT_XY);
		
		if(winWidth<=480){
			asynImageView.setLayoutParams(new GalleryFlow.LayoutParams(75,75));
		}
		
		if(winWidth<=320){
			asynImageView.setLayoutParams(new GalleryFlow.LayoutParams(65,65));
		}
		
		if(winWidth<=240){
			asynImageView.setLayoutParams(new GalleryFlow.LayoutParams(50,50));
		}
		
		asynImageView.setDefaultImage(R.drawable.soft_lsit_icon_default);
		asynImageView.setImageResource(R.drawable.soft_lsit_icon_default);
		if (Constants.DEBUG) {
			if(position<=this.images.size())
				Log.i(TAG,"Load image from :"+ this.images.get(position).url);
		}
		if (Common.ISLOADSOFTICON&&position<=this.images.size())
			asynImageView.setImageUrl(this.images.get(position).url, true);
		return asynImageView;
	}
	
}
