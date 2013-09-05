package com.appdear.client.model;

import java.io.Serializable;

/**
 * Œ¢≤©’À∫≈ µÃÂ
 * @author zxy
 *
 */
public class WeiBoUser implements Serializable {
	int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessSecret() {
		return accessSecret;
	}
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	long uid;
	String accessToken;
	String accessSecret;
}
