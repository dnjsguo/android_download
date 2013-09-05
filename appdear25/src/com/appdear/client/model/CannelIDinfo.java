package com.appdear.client.model;

import java.io.Serializable;

/**
 * 频道ID
 * @author zqm
 *
 */
public class CannelIDinfo implements Serializable {
	/**
	 * 频道ID
	 */
	public int sectionid;
	
	/**
	 * 频道ID的ID
	 */
	public int code;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sectionid+"="+code;
	}
	
	
	
}
