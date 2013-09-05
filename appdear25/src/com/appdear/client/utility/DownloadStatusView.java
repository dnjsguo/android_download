package com.appdear.client.utility;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DownloadStatusView extends TextView{
    private int status=-1;
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		updateUI();
	}

	public DownloadStatusView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void updateUI(){
		if(status==-1){
			this.setText("下载");
		}else if(status==2){
			this.setText("安装");
		}else if(status==3){
			this.setText("已安装");
		}
	}
}
