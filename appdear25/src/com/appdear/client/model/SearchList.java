package com.appdear.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SearchList<T> extends  ArrayList<String>{

	@Override
	public String get(int index) {
		// TODO Auto-generated method stub
		String str=super.get(index);
		if(str.contains(":")){
			String[] ss=str.split(":");
			if(ss.length>0){
				return ss[0];
			}else{
				return "";
			}
		}else{
			return str;
		}
	}
	
	public int getTrend(int index){
		String str=super.get(index);
		if(str.contains(":")){
			String[] s=str.split(":");
			if(s!=null&&s.length==2){
				if(s[1]!=null&&!s[1].equals("")){
					return Integer.parseInt(s[1]);
				}
			}else{
				return 0;
			}
		}
		return 0;
	}
}
