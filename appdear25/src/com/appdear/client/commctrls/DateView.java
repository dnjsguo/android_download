package com.appdear.client.commctrls;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.appdear.client.MorePhoneStoreActivity;
import com.appdear.client.MorePhoneStoreInfoActivity;
import com.appdear.client.R;
import com.appdear.client.SplashActivity;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;
/**
 * top tab control ,usage see user book .
 * @author zxy
 *
 */
public class DateView extends LinearLayout{
	AutoCompleteTextView y,m,d;
	private ArrayAdapter a,b,c,e,f,g;
	private LinearLayout topContainer = null;
	private static String[] ay,am,ad,ad1,ad2,ad3;
	private TextView btn;
	private int currentid;
	private Context context;
	private Handler handler;
	private LinearLayout progree;
	private   boolean falg=false;
	
	public DateView(final Context context,AttributeSet attr,final Handler handler,LinearLayout loadingView) {
		super(context);
		this.setOrientation(LinearLayout.HORIZONTAL); 
		this.progree=loadingView;
		this.context=context;
		this.handler=handler;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.storeinfolist_item_2, this);
		btn=(TextView)this.findViewById(R.id.submit1);
		y=(AutoCompleteTextView)view.findViewById(R.id.y);
		m=(AutoCompleteTextView)view.findViewById(R.id.m);
		d=(AutoCompleteTextView)view.findViewById(R.id.d);
		y.setAdapter(a=new ArrayAdapter(context,R.layout.storedate,ay==null?ay=initArray(2008,2050):ay));
		m.setAdapter(b=new ArrayAdapter(context,R.layout.storedate,am==null?am=initArray(1,12):am));
		c=new ArrayAdapter(context,R.layout.storedate,ad==null?ad=initArray(1,31):ad);
		e=new ArrayAdapter(context,R.layout.storedate,ad1==null?ad1=initArray(1,30):ad1);
		f=new ArrayAdapter(context,R.layout.storedate,ad2==null?ad2=initArray(1,29):ad2);
		g=new ArrayAdapter(context,R.layout.storedate,ad3==null?ad3=initArray(1,28):ad3);
		y.setOnItemClickListener(autoListItemClickListener);
		m.setOnItemClickListener(autoListItemClickListener);
		d.setOnItemClickListener(autoListItemClickListener);
		y.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.y;
				y.showDropDown();
			}    	
        });
	    m.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.m;
				m.showDropDown();
			}    	
        });
	   d.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.d;
				if(m.getText().toString().equals("")){
					return;
				}
				d.showDropDown();
			}    	
        });
	   view.findViewById(R.id.b1).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.y;
				y.showDropDown();
			}    	
       });
	   view.findViewById(R.id.b2).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.m;
				m.showDropDown();
			}    	
       });
	   view.findViewById(R.id.b3).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.d;
				if(m.getText().toString().equals("")){
					return;
				}
				d.showDropDown();
			}    	
       });
	   btn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final  String y1,m1,d1;
			y1=y.getText().toString();
			m1=m.getText().toString();
			d1=d.getText().toString();
			if(isStr(y1)&&isStr(m1)&&isStr(d1)){
	        Date currentTime = new Date();
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String nowDate = formatter.format(currentTime);
		    String mm=m1;
			String dd=d1;
			if(Integer.parseInt(m1)<10)
			{
				mm = "0"+m1;
			}
			if(Integer.parseInt(d1)<10)
			{
				dd = "0"+m1;
			}
			String s=y1+"-"+mm+"-"+dd;
			//System.out.println("nowData==="+nowDate+" s="+s+"  "+nowDate.compareTo(s));
			 if(nowDate.compareTo(s)==-1)
			  {
				 Toast.makeText(context,"请输入正确的日期",1000).show();
				 return;
			  }	
			  if(falg==false){
				new Thread(){
					public void run() {
						// TODO Auto-generated method stub
						/*try
						{
							Thread.sleep(2000);
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						falg=true;
						 
						handler.sendEmptyMessage(ListBaseActivity.LOADV);
						try {
							//	Log.i("info909","isFirstInit");
							String mm=m1;
							String dd=d1;
							if(Integer.parseInt(m1)<10)
							{
								mm =  "0"+m1;
							}
							if(Integer.parseInt(d1)<10)
							{
								dd =  "0"+d1;
							}
							String s=y1+"-"+mm+"-"+dd;
							//String s=y1+"-"+m1+"-"+d1;
							ApiNormolResult result=ApiManager.registerwt(s);
							//System.out.println("---ruslt="+result.isok);
							if(result!=null&&result.isok==1){
								Message m=handler.obtainMessage();
								m.what=1;m.obj=s;
								handler.sendMessage(m);
							}
							//	Log.i("info909",AppContext.getInstance().initModel.updateurl+"=");
							} catch (ApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Message m=handler.obtainMessage();
								m.what=2;m.obj=e.getMessage();
								handler.sendMessage(m);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								falg=false;
								handler.sendEmptyMessage(ListBaseActivity.LOADG);
							}
					}
				}.start();
			}
			}else{
				Toast.makeText(context,"请输入正确的日期",1000).show();
			}
		}
		   
	   });
	}
	private boolean isStr(String str){
		if(str!=null&&!str.equals("")){
			return true;
		}else return false;
			
	}
	OnItemClickListener autoListItemClickListener =  new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		  if(currentid==R.id.y){
			  int mm=0;
			  if(!m.getText().toString().equals("")){
				  mm=Integer.parseInt(m.getText().toString());
			  }
			  if(mm>0){
				  if(mm==2){
					  if(!y.getText().toString().equals("")&&leapYear(Integer.parseInt(y.getText().toString()))){
						  d.setAdapter(f);
					  }else{
						  d.setAdapter(g);
					  }
				  }else if(mm==1||mm==3||mm==5||mm==7||mm==8||mm==10||mm==12){
					  d.setAdapter(c);
				  }else{
					  d.setAdapter(e);
				  }
				  String s=handlerYM(d.getText().toString());
				  if(!s.equals("-1")){
					  d.setText(s);
				  }
			  }
		  }else if(currentid==R.id.m){
			  int mm=0;
			  if(!m.getText().toString().equals("")){
				  mm=Integer.parseInt(m.getText().toString());
			  }
			  if(mm>0){
				  if(mm==2){
					  if(!y.getText().toString().equals("")&&leapYear(Integer.parseInt(y.getText().toString()))){
						  d.setAdapter(f);
					  }else{
						  d.setAdapter(g);
					  }
				  }else if(mm==1||mm==3||mm==5||mm==7||mm==8||mm==10||mm==12){
					  d.setAdapter(c);
				  }else{
					  d.setAdapter(e);
				  }
				  String s=handlerYM(d.getText().toString());
				  if(!s.equals("-1")){
					  d.setText(s);
				  }
			  }
		 }
		}
	};
	
	
	public  boolean leapYear(int year)
	{
		if(((year%100==0)&&(year%400==0))||((year%100!=0)&&(year%4==0)))
			return true;
		else return false;
	}
	
	public String[] initArray(int start,int end){
		String[] s=new String[end-start+1];
		int count=0;
		for(int i=start;i<=end;i++){
			s[count++]=String.valueOf(i);
		}
		return s;
	}
	
	public String handlerYM(String d){
		if(d!=null&&!d.equals("")){
			return "1";
		}else{
			return "-1";
		}
	}
}
