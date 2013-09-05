package com.appdear.client.model;

import java.io.Serializable;
import java.util.List;

public class HomeObj implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 3213L;
	private List<SoftlistInfo> listData;
	private long timestamp;
	 private String dynamic="a";
	 private String asids="";
	public List<SoftlistInfo> getListData() {
		return listData;
	}
	public void setListData(List<SoftlistInfo> listData) {
		this.listData = listData;
	}
	public String getDynamic() {
		return dynamic;
	}
	public void setDynamic(String dynamic) {
		this.dynamic = dynamic;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getAsids() {
		return asids;
	}
	public void setAsids(String asids) {
		this.asids = asids;
	}
	 
}
