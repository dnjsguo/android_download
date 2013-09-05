package com.appdear.client.commctrls;

import java.util.ArrayList;

import com.appdear.client.R;
import com.appdear.client.service.MyApplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RecognizerDialog extends Dialog {
	private ArrayList<String> list = null;
	private Handler handler;
	private final int UPDATE_EIDT = 23;
	private final int OPEN_VOICE = 24;
	public RecognizerDialog(Context context, ArrayList<String> list,Handler handler) {
//		super(context);
		super(context,R.style.dialog);
		this.list = list;
		this.handler =handler;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_search_words);
		ListView lv_search_words = (ListView) findViewById(R.id.lv_search_words);
		lv_search_words.setAdapter(new MyVoiceAdapter());
		ImageView iv_search_mike = (ImageView) findViewById(R.id.iv_search_mike);
		iv_search_mike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = handler.obtainMessage();
				msg.what = OPEN_VOICE;
				handler.sendMessage(msg);
				RecognizerDialog.this.dismiss();
			}
		});
		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RecognizerDialog.this.dismiss();
			}
		});
		lv_search_words.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Message msg = handler.obtainMessage();
				msg.obj = list.get(position);
				msg.what = UPDATE_EIDT;
				handler.sendMessage(msg);
				RecognizerDialog.this.dismiss();
			}
		});
	}
	private class MyVoiceAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView ==null){
				holder = new ViewHolder();
				convertView = View.inflate(MyApplication.getInstance(), R.layout.voice_search_words_list_item, null);
				holder.tv = (TextView) convertView.findViewById(R.id.tv_voice_search_word);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(list.get(position));
			return convertView;
		}
	}
	static class ViewHolder{
		//ImageView iv;
		TextView tv;
	}
}
