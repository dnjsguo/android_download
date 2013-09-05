package com.appdear.client.service.api;

public  class ApiAddpopularResult{
//	Sv	平台服务版本号	xsd:string	1.0
//	2.0
//	Imei	手机身份识别码	xsd:string	根据request内的imei值回填到response中。 
//	Resultcode	请求结果码	xsd:string	非000000的情况下，为存在错误或故障
//	isok	是否成功	xsd:string	0失败
//	1成功
	
	public String sv ;
	public String imei;
	public String resultcode;
	public int isok;

}