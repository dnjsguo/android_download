package com.appdear.client.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.widget.Toast;

public class SmsUtil {
	/*private Context mContext;
	
	public SmsUtils(Context mContext) {
		this.mContext = mContext;
	}*/

	static final int NO_CHANGE = 0;//短信没有变化
	static final String SMS_URI_ALL = "content://sms"; // 所有 0
	static final String SMS_URI_INBOX = "content://sms/inbox";// 收件箱 1
	static final String SMS_URI_OUTBOX = "content://sms/outbox";// 发件箱 4
	static final String SMS_URI_SEND = "content://sms/sent";// 已发送 2
	static final String SMS_URI_DRAFT = "content://sms/draft";// 草稿 3
	static final String SMS_URI_FAILED = "content://sms/failed";// 发送失败5
	static final String SMS_URI_QUEUED = "content://sms/queued";// 待发送列表6

	/**
	 * _id：短信序号，如100 thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的 address：
	 * 发件人地址，即手机号，如+8613811810000 person： 发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
	 * date：日期，long型，如1256539465022，可以对日期显示格式进行设置 protocol： 协议 0 SMS_RPOTO 短信， 1
	 * MMS_PROTO 彩信 read： 是否阅读 0 未读， 1 已读 status：短信状态 -1接收，0 complete, 64
	 * pending, 128 failed type：短信类型 1是接收到的，2是已发出 body：短信具体内容
	 * service_center：短信服务中心号码编号，如+8613800755500
	 * 
	 * @param context
	 * @return
	 */
	public static String getSmsInPhone(Context context, int page, int count,
			int pagecount, int flag) {

		JSONObject json = new JSONObject();
		try {
			ContentResolver cr = context.getContentResolver();
			String[] projection = new String[] { "_id", "address", "person",
					"body", "date", "type", "read" };
			Uri uri = Uri.parse(SMS_URI_ALL);//and address = 10010
			Cursor cur = cr.query(uri, projection,
					"type in (?,?,?) ", new String[] { "1",
							"2", "3" }, "date desc");
			// Cursor cur = cr.query(uri, projection,null,null, "date desc");
			if (flag == -1) {
				int length = (cur == null) ? 0 : cur.getCount();
				if (cur != null)
					cur.close();
				return String.valueOf(length);
			}
			int count1 = 0;
			try {
				json.put("protocoltype", "sms");
				JSONArray contacts = new JSONArray();
				JSONObject contact = null;
				if (cur != null) {
					int postion = 0;
					int end = 0;
					int pages = (cur.getCount() % pagecount == 0 ? cur
							.getCount() / pagecount : cur.getCount()
							/ pagecount + 1);
					if (page > pages)
						return json.toString();
					if (page <= 0)
						page = 1;
					postion = (page - 1) * pagecount;
					end = page * pagecount > cur.getCount() ? cur.getCount()
							: page * pagecount;
					if (cur.moveToPosition(postion)) {
						String name;
						String phoneNumber;
						String smsbody;
						String date = "";
						String type;
						// String username=null;
						int nameColumn = cur.getColumnIndex("person");
						int phoneNumberColumn = cur.getColumnIndex("address");
						int smsbodyColumn = cur.getColumnIndex("body");
						int dateColumn = cur.getColumnIndex("date");
						int typeColumn = cur.getColumnIndex("type");
						int read = cur.getColumnIndex("read");
						int id = cur.getColumnIndex("_id");

						do {
							if (postion >= end)
								break;
							contact = new JSONObject();
							name = cur.getString(nameColumn);
							phoneNumber = cur.getString(phoneNumberColumn);
							smsbody = cur.getString(smsbodyColumn);
//							SimpleDateFormat dateFormat = new SimpleDateFormat(
//									"yyyy-MM-dd HH:mm:ss");
							if (cur.getString(dateColumn) != null
									&& !cur.getString(dateColumn).equals("")) {

								String str = cur.getString(dateColumn);
//								if (!str.contains(" ")) {
//									date = dateFormat.format(new Date(Long
//											.parseLong(cur
//													.getString(dateColumn))));
//								}
							}
							Long str = cur.getLong(dateColumn);
							// Log.i("date1",date);
							int typeId = cur.getInt(typeColumn);
							int status = cur.getInt(read);
							int _id = cur.getInt(id);
							if (smsbody == null)
								smsbody = "";
							// username=getContactByAddr(context,phoneNumber);
							contact.put("address", phoneNumber);
							contact.put("body", smsbody);
							contact.put("person", name); 
							contact.put("date", str); 
							contact.put("type", String.valueOf(typeId));
							contact.put("read", String.valueOf(status));
							contact.put("id", String.valueOf(_id));
							// if(username!=null&&!username.equals("")){
							// contact.put("name",username);
							// }
							contacts.put(contact);
							postion++;
							count1++;
						} while (cur.moveToNext());
						json.put("count", count1 + "");
					}
				}
				json.put("sms", contacts);
				// map.clear();
			} catch (SQLiteException ex) {
				ex.printStackTrace();
				// Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(">>>>>>>>>>>>>"+json.toString());
		return json.toString();

	}

	/**
	 * 短信还原
	 * @param context
	 * @param json
	 * @return 0没有变化.其它为还原条数
	 * @throws Exception 还原失败
	 */
	public static int smsRestroe(Context context ,String json)throws Exception{
		System.out.println(json);
		JSONObject obj = new JSONObject(json);
		JSONArray arr = obj.getJSONArray("sms");
		//Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[]{"date"}, "date = ?", new String[]{arr.get(0).toString()}, null);
		Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), new String[]{"date"}, null, null, null);
		System.out.println("test>>>");
		System.out.println(cursor.getCount());
		Uri datauri = Uri.parse(SMS_URI_ALL);
		HashSet<Long> sms = new HashSet<Long>();
		while(cursor.moveToNext()){
			//System.out.println(cursor.getColumnCount());
			//Long temp = cursor.getLong(cursor.getColumnIndex("date"));
			//if(!sms.contains(temp)){
				sms.add(cursor.getLong(cursor.getColumnIndex("date")));
			//}
			System.out.println(cursor.getLong(cursor.getColumnIndex("date"))+"");
		}
		cursor.close();
		int i=0;
		int count =0;
		for(int j= 0;j<arr.length();j++){
			JSONObject temp = arr.getJSONObject(j);
			Long l = temp.getLong("date");
			//Date date = new Date(l);
			if(sms.contains(l)){
				++i;
				System.out.println(i);
				//Toast.makeText(context, i, 0).show();
			}else{
				//"address", "person","body", "date", "type", "read" };
				ContentValues values = new ContentValues();
				values.put("address", getJsonObj(temp, "address")!=null?getJsonObj(temp, "address").toString():"");
				values.put("person",getJsonObj(temp,"person")!=null?getJsonObj(temp,"person").toString():"");
				values.put("body",getJsonObj(temp,"body")!=null?getJsonObj(temp,"body").toString():"");
				values.put("type",getJsonObj(temp,"type")!=null?getJsonObj(temp,"type").toString():"0");
				values.put("date",getJsonObj(temp,"date")!=null?getJsonObj(temp,"date").toString():"0");
				values.put("read",getJsonObj(temp,"read")!=null?getJsonObj(temp,"read").toString():"0");
				switch(getJsonObj(temp,"type")!=null?Integer.parseInt(getJsonObj(temp,"type").toString()):0){
					case 0: datauri=Uri.parse(SMS_URI_ALL);break;
					case 1: datauri=Uri.parse(SMS_URI_INBOX);break;
					case 2: datauri=Uri.parse(SMS_URI_SEND); break;
					case 3: datauri=Uri.parse(SMS_URI_DRAFT);break;
					case 4: datauri=Uri.parse(SMS_URI_OUTBOX);break;
					case 5: datauri=Uri.parse(SMS_URI_FAILED);break;
					case 6: datauri=Uri.parse(SMS_URI_QUEUED);break;
				}
				//values.put("person", temp.getString("person")!=null?temp.getString("person").toString():"");
				//values.put("person", temp.getString("person"));
				/*
				 * values.put("date", temp.getString("date"));
				 * values.put("body", temp.getString("body"));
				 * values.put("type", temp.getString("type"));
				 * values.put("read", temp.getString("read"));
				 */
				context.getContentResolver().insert(datauri, values);
				++ count;
				System.out.println(count);
			}
		}
		System.out.println(i);
		System.out.println(arr.length());
		if(i == arr.length()){
			return NO_CHANGE;
		}
		return count;
	}
	
	private static Object getJsonObj(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				if (json.get(key) == null) {
					return null;
				} else {
					return json.get(key);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}
}
