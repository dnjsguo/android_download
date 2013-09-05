package com.appdear.client;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.appdear.client.Adapter.StroeInfolistAdatper;
import com.appdear.client.commctrls.DateView;
import com.appdear.client.commctrls.ListBaseActivity;
import com.appdear.client.commctrls.ListViewRefresh;
import com.appdear.client.commctrls.MProgress;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.MyApplication;
import com.appdear.client.service.api.ApiManager;
import com.appdear.client.service.api.ApiNormolResult;

public class MorePhoneStoreInfoActivity extends ListBaseActivity{
	/**
	 * 请求城市列表结果
	 */
	ImageButton btn_return;
	private LinearLayout tab_ll_linear;
	private List<String[]> listdata=null;
	private DateView dateview;
	private String   phonedate="";
	String date0="";
	String date1=""; //7天
	String date2="";// 15天
	String date3="";// 6个月
	String date4="";// 1年
	public void onCreate(Bundle b){
		super.onCreate(b); 
		Intent intent =getIntent();
		setContentView(R.layout.user_phone_store_info);
		isShowAlert = false;
		loadingView=new MProgress(this,true);
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		this.addContentView(loadingView, params);
      }
	@Override
	public void initData() {
		phonedate=SharedPreferencesControl.getInstance().getString("phonedate",null,MorePhoneStoreInfoActivity.this);
		if(phonedate==null ||"".equals(phonedate))
		{
			if(MyApplication.getInstance().isLoadBuyTime==false)
	        {
					try
					{
						 //Thread.sleep(4000);
						ApiNormolResult  result=ApiManager.getwarranty() ;
						if(result!=null&&result.isok!=1&&result.buydate!=null&&!"".equals(result.buydate)){	
							
							phonedate=result.buydate;
							SharedPreferencesControl.getInstance().putString("phonedate",result.buydate,null,MorePhoneStoreInfoActivity.this);						
						} 
					} catch (Exception e)
					{ 
						phonedate="";
						e.printStackTrace();
					} 
				 
				MyApplication.getInstance().isLoadBuyTime=true;
	        } 
		} 
		//System.out.println("------initData()--------"+MyApplication.getInstance().isLoadBuyTime);
       /* if(MyApplication.getInstance().isLoadBuyTime==false)
        {
 
    		if(phonedate==null ||"".equals(phonedate))
    		{
    			//phonedate="2012-01-01";
    			try
    			{
    				ApiNormolResult  result=ApiManager.getwarranty() ;
    				if(result!=null&&result.isok==1&&result.buydate!=null&&!"".equals(result.buydate)){				
    					phonedate=result.buydate;
    					SharedPreferencesControl.getInstance().putString("phonedate",result.buydate,null,MorePhoneStoreInfoActivity.this);						
    				}
    			} catch (Exception e)
    			{
    				e.printStackTrace();
    			} 
    		}
    		MyApplication.getInstance().isLoadBuyTime=true;	
        }else
        { 
            phonedate=SharedPreferencesControl.getInstance().getString("phonedate",null,MorePhoneStoreInfoActivity.this);
        }*/
        
         
		
	}
	@Override
	public void init() {
		listView = (ListViewRefresh) findViewById(R.id.soft_list);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.requestFocus();
		listdata=new ArrayList<String[]>();
		if(dateview==null){
			dateview=new DateView(this,null,handler,loadingView);
		}
		/*params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);*/
		listView.addHeaderView(dateview );
 	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1){
				 
				phonedate=msg.obj.toString();
				SharedPreferencesControl.getInstance().putString("phonedate",msg.obj.toString(),null,MorePhoneStoreInfoActivity.this);
				Toast.makeText(MorePhoneStoreInfoActivity.this,"购机时间提交成功!",1000).show();
				 updateUI();
			}else if(msg.what==2){
				Toast.makeText(MorePhoneStoreInfoActivity.this,msg.obj.toString(),1000).show();
			}else if(msg.what==LOADV){
				if (loadingView != null)
					loadingView.setVisibility(View.VISIBLE);
			}else if(msg.what==LOADG){
				if (loadingView != null)
					loadingView.setVisibility(View.GONE);
			}
		}
		
	};
	@Override
	public void updateUI() {
		//System.out.println("------updateUI()--------phonedate="+phonedate);
 
		if(phonedate!=null &&!"".equals(phonedate)&&!"null".equals(phonedate))
		{
			date0=phonedate;
			date1 = getNextDay(phonedate,"7");
			date2 = getNextDay(phonedate,"15");
			date3 = getNextMonth(phonedate,6);
			date4 = getNextYear(phonedate,1);
		}else
		{
			  date0="";
			  date1="";// 7天
			  date2="";// 15天
			  date3="";// 6个月
			  date4="";// 3年
		}
		listdata.clear();
		listdata.add(new String[]{Constants.PHONETAG[6], "你的购机日期："+date0+"\n" 
		                                               +"7天包退截止日期："+date1+"\n" 
		                                               +"15天包换截止日期："+date2+"\n"  
		                                               +"配件半年保修截止日期："+date3+"\n" 
		                                               +"1年免费维修截止日期："+date4  });
		listdata.add(new String[]{Constants.PHONETAG[0],MyApplication.getInstance().modelCompany});
		listdata.add(new String[]{Constants.PHONETAG[7],MyApplication.getInstance().modelCompanyPhone});
		listdata.add(new String[]{Constants.PHONETAG[1],AppContext.getInstance().IMEI});
	//	listdata.add(new String[]{Constants.PHONETAG[2],"sb"});
	//	listdata.add(new String[]{Constants.PHONETAG[3],"2012-01-02"});
	//	listdata.add(new String[]{Constants.PHONETAG[4],"1年"});
		listdata.add(new String[]{Constants.PHONETAG[5],this.getResources().getString(R.string.baoxiu)});
		 btn_return = (ImageButton) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		tab_ll_linear = (LinearLayout) findViewById(R.id.ll_navigation);
				tab_ll_linear.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
		});
		//if(listView==null)return;
		adapter = new StroeInfolistAdatper(this, listdata);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void refreshDataUI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRefreshData() {
		// TODO Auto-generated method stub
		
	}
	 /**
	  * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	  */
	 public static String getNextDay(String nowdate, String delay) {
	  try{
	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  String mdate = "";
	  Date d = strToDate(nowdate);
	  long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
	  d.setTime(myTime * 1000);
	  mdate = format.format(d);
	  return mdate;
	  }catch(Exception e){
		 e.printStackTrace();
	   return "";
	  }
	 }
	 public  static String   getNextMonth(String nowdate,int n)   {  
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		 Date d = strToDate(nowdate);
         Calendar   rightNow   =   Calendar.getInstance(); 
         rightNow.setTime(d);
         //rightNow.add(Calendar.DAY_OF_MONTH,-1);  
         rightNow.add(Calendar.MONTH, n);  
         return   format.format(rightNow.getTime());  
     } 
	 public  static String   getNextYear(String nowdate,int n)   {  
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  
		  Date d = strToDate(nowdate);
         
         Calendar   rightNow   =   Calendar.getInstance(); 
         rightNow.setTime(d);
         //rightNow.add(Calendar.DAY_OF_MONTH,-1);  
         rightNow.add(Calendar.YEAR, n);  
         return   format.format(rightNow.getTime());  
     } 
	 /**
	  * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	  * 
	  * @param strDate
	  * @return
	  */
	 public static Date strToDate(String strDate) {
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	  ParsePosition pos = new ParsePosition(0);
	  Date strtodate =  formatter.parse(strDate, pos);
	  return strtodate;
	 } 
}
