package com.appdear.client.utility;

import com.appdear.client.service.Constants;

import android.util.Log;

/**
 * 将生成的xml转化为字符串
 * @author jdan
 *
 */
public class XMLhelper   
{   
	private final static  String header="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static String addDoc(String key,String[] kv){
		int length=0;
		StringBuffer buffer=new StringBuffer(header);
		buffer.append("<apclient>");
		if(key==null){
			for(String k:kv){
				length=k.split(",").length;
				buffer.append("<").append(k.split(",")[0]).append(">").append(length==2?k.split(",")[1]:"").append("</").append(k.split(",")[0]).append(">");
			}
		}else{
			buffer.append("<").append(key).append(">");
			for(String k:kv){
				length=k.split(",").length;
				buffer.append("<").append(k.split(",")[0]).append(">").append(length==2?k.split(",")[1]:"").append("</").append(k.split(",")[0]).append(">");
			}
			buffer.append("</").append(key).append(">");
		}
		buffer.append("</apclient>");
		if (Constants.DEBUG)
			Log.i("jineefo", buffer.toString());
		return buffer.toString();
	}  
}  
