package com.appdear.client.Adapter;




import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.appdear.client.HomePageMainActivity;
import com.appdear.client.R;
import com.appdear.client.commctrls.AsynLoadImageView;
import com.appdear.client.model.GalleryFlowInfo;

public class TopGalleryAdatper extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private List<GalleryFlowInfo>  galleryFlowAdModel;
	private int[] wh;
	public TopGalleryAdatper(Context c, List<GalleryFlowInfo>  galleryFlowAdModel, int[] wh) {
		mContext = c;
		this.galleryFlowAdModel = galleryFlowAdModel;
		this.wh = wh;
	}

	private Resources getResources() {
		return null;
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	
	
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		AsynLoadImageView i = new AsynLoadImageView(mContext);
		
		if(galleryFlowAdModel.size()<=0){
			if(mContext instanceof HomePageMainActivity){
//				i.setImageResource(R.drawable.topaddefault);
//				i.setDefaultImage(R.drawable.topaddefault);
			}
		}
		else
		i.setImageUrl(galleryFlowAdModel.get(position % galleryFlowAdModel.size()).url, true);
        i.setScaleType(ImageView.ScaleType.FIT_XY);
         
        i.setLayoutParams(new Gallery.LayoutParams(wh[0]/3, wh[1]/10));
//        i.setBackgroundResource(R.drawable.cf_bg);
       
//        i.getBackground().setAlpha(100);
          
        return i;
	}
	
	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

	
	
	}


		