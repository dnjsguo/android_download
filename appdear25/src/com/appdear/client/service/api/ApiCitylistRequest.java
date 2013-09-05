package com.appdear.client.service.api;

public class ApiCitylistRequest {
//{"citys":["北京","上海"],"result":{"resultcode":"000000"},"imei":"SDF434TTTTTTTTT","sv":"1.0"}
	public String resultcode;
	public String imei;
	public String sv;
	/**
	 * 返回的城市列表，用英文字符逗号隔开
	 */
	public String citys ;
}
