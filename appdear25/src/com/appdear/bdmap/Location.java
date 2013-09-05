package com.appdear.bdmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class Location {
    public static String LOCATIONS_URL = "http://www.google.com.hk/loc/json";
    public static String currentCity=null;
    public static String getLocations(Context context) {
        // generate json request
        String jr = generateJsonRequest(context);

        try {
            DefaultHttpClient client = new DefaultHttpClient();

            StringEntity entity = new StringEntity(jr);

            HttpPost httpost = new HttpPost(LOCATIONS_URL);
            httpost.setEntity(entity);

            HttpResponse response = client.execute(httpost);

            String locationsJSONString = getStringFromHttp(response.getEntity());
       //     Log.i("info111", locationsJSONString+"=locationsJSONString");
            return extractLocationsFromJsonStringForCity(locationsJSONString);
        } catch (ClientProtocolException e) {
        //	Log.i("info111", e.getMessage()+"=ClientProtocolException");
            //e.printStackTrace();
        } catch (IOException e) {
        //	Log.i("info111", e.getMessage()+"=IOException");
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        //	Log.i("info111", e.getMessage()+"=Exception");
        }
        return null;

    }

    private static String extractLocationsFromJsonStringForCity(String jsonString) {
        String country = "";
        String region = "";
        String city = "";
        String street = "";
        String street_number = "";
        double latitude = 0.0;
        double longitude = 0.0;
        
        //"accuracy":901.0
        double accuracy = 0.0;
        
        try {
            JSONObject jo = new JSONObject(jsonString);
            JSONObject location = (JSONObject) jo.get("location");
            latitude = (Double) location.get("latitude");
            longitude = (Double) location.get("longitude");
            accuracy = (Double) location.get("accuracy");
            JSONObject address = (JSONObject) location.get("address");

            country = (String) address.get("country");
            region = (String) address.get("region");
            city = (String) address.get("city");
            street = (String) address.get("street");
            street_number = (String) address.get("street_number");
        } catch (JSONException e) {

            //e.printStackTrace();
        }
        currentCity=(city==null||city.equals(""))?null:city;
        return city;
    }

    // 获取所有的网页信息以String 返回
    private static String getStringFromHttp(HttpEntity entity) {

        StringBuffer buffer = new StringBuffer();

        try {
            // 获取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    entity.getContent()));

            // 将返回的数据读到buffer中
            String temp = null;

            while ((temp = reader.readLine()) != null) {
                buffer.append(temp);
            }
        } catch (IllegalStateException e) {
            
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return buffer.toString();
    }

    private static String generateJsonRequest(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Log.i("info111","generateJsonRequest");
        List<NeighboringCellInfo> cellList = manager.getNeighboringCellInfo();
        Log.i("info111",cellList+"=cellList");
        GsmCellLocation location =null;
        if (cellList.size() == 0){
        	CellLocation loction1=manager.getCellLocation();
        	if(loction1 instanceof GsmCellLocation){
        		location = (GsmCellLocation)loction1; 
        	}else {
        		return null;
        	}
        	if(location==null)return null;
        }
        JSONStringer js = new JSONStringer();
        try {

            js.object();

            js.key("version").value("1.1.0");
            js.key("host").value("maps.google.com.hk");
            js.key("home_mobile_country_code").value(460);
            js.key("home_mobile_network_code").value(0);
            js.key("radio_type").value("gsm");
            js.key("request_address").value(true);
            js.key("address_language").value("zh_CN");

            JSONArray ct = new JSONArray();
            if (cellList.size() > 0){
	            for (NeighboringCellInfo info : cellList) {
	
	                JSONObject c = new JSONObject();
	                c.put("cell_id", info.getCid());
	                c.put("location_area_code", info.getLac());
	                c.put("mobile_country_code", 460);
	                c.put("mobile_network_code",0);
	                c.put("signal_strength", info.getRssi()); // 获取邻居小区信号强度
	
	                ct.put(c);
	            }
            }else{
            	    JSONObject c = new JSONObject();
	                c.put("cell_id", location.getCid());
	                c.put("location_area_code", location.getLac());
	                c.put("mobile_country_code", 460);
	                c.put("mobile_network_code", 0); // 获取邻居小区信号强度
	                ct.put(c);
            }
            js.key("cell_towers").value(ct);
            js.endObject();
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }
        
        return js.toString().replace("true", "True");
    }
    public static String getWifiLocation(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiManager.startScan();
	//	List<ScanResult> lsScanResult = mWifiManager.getScanResults();
		List<WifiConfiguration> lsWifiConfiguration = mWifiManager.getConfiguredNetworks();
		String str = "";
		for(WifiConfiguration config : lsWifiConfiguration) {
			if(config.status == WifiConfiguration.Status.CURRENT)
				str += config.SSID + ",";
		}
		WifiInfo info = mWifiManager.getConnectionInfo();
        String mac = info.getMacAddress();
      
		JSONObject holder = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject data = new JSONObject();
		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com.hk");
			holder.put("address_language", "zh_CN");
			holder.put("request_address", true);
			data.put("ssid", info.getSSID());
			data.put("mac_address", mac);
		    data.put("signal_strength", 8);
		   data.put("age", 0);
			array.put(data);
			holder.put("wifi_towers", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(LOCATIONS_URL);
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(holder.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setEntity(stringEntity);
		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		//	Log.i("info111","ClientProtocolException="+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		//Log.i("info111","IOException="+e.getMessage());
		}
		StringBuffer stringBuffer =new StringBuffer();
		Log.i("info111","httpResponse="+httpResponse);
		if(httpResponse!=null){
		  int state = httpResponse.getStatusLine().getStatusCode(); 
	      if (state == HttpStatus.SC_OK) { 
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = null;
			try {
				is = httpEntity.getContent();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			stringBuffer = new StringBuffer();
			try {
				String result = "";
				while ((result = reader.readLine()) != null) {
					stringBuffer.append(result);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	        }
	      return extractLocationsFromJsonStringForCity(stringBuffer.toString());
		}
		return null;
	}
}