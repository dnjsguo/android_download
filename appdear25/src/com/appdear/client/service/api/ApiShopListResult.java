package com.appdear.client.service.api;

import java.util.ArrayList;

import com.appdear.client.model.ShopModel;

public class ApiShopListResult {
//	{
//	    "result": {
//	        "resultcode": "000000"
//	    },
//	    "imei": "SDF434TTTTTTTTT",
//	    "sv": "1.0",
//	    "list": {
//	        "count": 5,
//	        "items": [
//	            {
//	                "tel": "010-89898989",
//	                "name": "乐语家门口店",
//	                "addr": "家门口"
//	            },  
//	            {
//	                "tel": "010-89111189",
//	                "name": "乐语天安门店",
//	                "addr": "天安门大街110号东城区广渠门内大街45号雍贵中心东城区广渠门内大街45号雍贵中心东城区广渠门内大街45号雍贵中心"
//	            }
//	        ]
//	    }
//	}
	
	public String resultcode;
	public String imei;
	public String sv;
//	public  int shopcCount;
	public ArrayList<ShopModel> shopList;
	public int pagenum = 0;
	public int totalcount = 0;
	public int pageno = 0;
}
