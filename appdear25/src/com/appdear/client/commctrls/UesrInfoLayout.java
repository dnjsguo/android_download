package com.appdear.client.commctrls;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdear.client.R;
/**
 * @author jdan
 * 用户登录后的头信息
 */
public class UesrInfoLayout extends LinearLayout implements OnClickListener {
	
	 /**
     * 用户登录信息显示
     */
	private Context context ;
	/**
	 * the first
	 */
    private View child ;
    public TextView useraccount_1;
    public TextView useraccount_2;
    public TextView useraccount_3;
    public TextView useraccount_4;
    public ImageView header_user;//头像
   
	public UesrInfoLayout(Context context,AttributeSet attr) {
		super(context, attr);
		this.context = context;
	    this.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater   layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		child = layoutInflater.inflate(R.layout.user_info, null);
		this.addView(child);
		useraccount_1=(TextView)child.findViewById(R.id.useraccount_1);
		useraccount_2=(TextView)child.findViewById(R.id.useraccount_2);
		useraccount_3=(TextView)child.findViewById(R.id.useraccount_3);
		useraccount_4=(TextView)child.findViewById(R.id.useraccount_4);
		new MyThread().start();
		
	}
	/** 
	 * 初始化底部视图
	 */
	class MyThread extends Thread{
		
		@Override
		public void run() {
			handler.sendEmptyMessage(2);
			super.run();
		}
	}
	
	private void initUserInfo() {
		 if(child!=null){
		
			header_user=(ImageView) child.findViewById(R.id.header_user);
			String nickname=SharedPreferencesControl.getInstance().getString("nickname",Common.USERLOGIN_XML,context);
			useraccount_1.setText((nickname==null||nickname.equals(""))?SharedPreferencesControl.getInstance().getString("username",Common.USERLOGIN_XML,context):nickname);
			useraccount_2.setText(SharedPreferencesControl.getInstance().getString("account",Common.USERLOGIN_XML,context)+"元");
			useraccount_3.setText(SharedPreferencesControl.getInstance().getString("level",Common.USERLOGIN_XML,context));
			useraccount_4.setText(SharedPreferencesControl.getInstance().getString("point",Common.USERLOGIN_XML,context));
			//header_user.setDefaultImage(R.drawable.soft_lsit_icon_default);
			String str=SharedPreferencesControl.getInstance().getString("gender", Common.USERLOGIN_XML,context);
			if(str!=null){
				if(str.equals("1")){
					header_user.setImageBitmap(readBitMap(context,R.drawable.user_icon_man));
				}else if(str.equals("0")){
					header_user.setImageBitmap(readBitMap(context,R.drawable.user_icon_weman));
				}else{
					header_user.setImageBitmap(readBitMap(context,R.drawable.user_icon_default));
				}
			}else{
				header_user.setImageBitmap(readBitMap(context,R.drawable.user_icon_default));
			}
			
		 }
	}
	
	@Override
	public void onClick(View v) {
		
	}
	public Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				useraccount_4.setText(msg.obj+"");
				break;
			case 2:
				initUserInfo();
			default:return;
			}
		}
		
	};
	 public static Bitmap readBitMap(Context context, int resId){  
		  BitmapFactory.Options opt = new BitmapFactory.Options();  
		    opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		    opt.inPurgeable = true;  
		   opt.inInputShareable = true;  
	   //获取资源图片  
		  InputStream is = context.getResources().openRawResource(resId);  
		  WeakReference<Bitmap> weakRerference =new WeakReference<Bitmap>(BitmapFactory.decodeStream(is,null,opt)); 
	    return weakRerference.get();  
	 }
}
