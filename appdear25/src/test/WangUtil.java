package test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.api.ApiSoftListResult;

public class WangUtil
{
    public static ApiSoftListResult getAd()
    {
    	ApiSoftListResult adresult =new ApiSoftListResult();
    	try
		{
			String json = getJsonOrmliteByReadLocal();
			//System.out.println("json="+json);
			AppList result=JSON.parseObject(json,AppList.class);
			//System.out.println("size="+result.getAppList().size()+"limit="+result.limit);
			
			  List<SoftlistInfo> softList = new ArrayList<SoftlistInfo>();
			 
			for(AppDetail detail:result.appList)
			{
				SoftlistInfo info= new SoftlistInfo();
				info.adid=Integer.parseInt(detail.id);
				info.imgurl=detail.thumbnail;
				info.softid=Integer.parseInt(detail.id);
				info.type=3;
				info.download=0;
				
			    softList.add(info); 
			}
			adresult.softList=softList;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}
    	return adresult;
    }
    
    
    public static ApiSoftListResult getSoftList()
    {
    	ApiSoftListResult  softListResult =new ApiSoftListResult();
    	try
		{
			String json = getJsonOrmliteByReadLocal();
			//System.out.println("json="+json);
			AppList result=JSON.parseObject(json,AppList.class);
			//System.out.println("size="+result.getAppList().size()+"limit="+result.limit);
			softListResult.totalcount=result.total;
			  List<SoftlistInfo> softList = new ArrayList<SoftlistInfo>();
			 int i=0;
			for(AppDetail detail:result.appList)
			{
				SoftlistInfo info= new SoftlistInfo();
				
				/*info.imgurl=detail.thumbnail;
				item.softprice
				item.softprice == 0
				item.softicon
				item.softname
				item.softdesc item.softsize
				item.isfirst =1 /0    
				info.version
				item.softgrade	
				info.downloadurl*/
				info.softid=Integer.parseInt(detail.id);
				info.softicon=detail.thumbnail;
				info.softprice =0;
				info.softname=detail.appName;
				info.appid=detail.package_name;
				info.softsize=(int)detail.appSize;
				if(i==0)
				{
					info.isfirst="1";
				}else
				{
					info.isfirst="0";
				}
				
				info.version=detail.version;
				info.versioncode=detail.version_code;
				info.softgrade=5;
				info.downloadurl=detail.download_url;
				
			    softList.add(info); 
			    i++;
			}
			softListResult.softList=softList;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}
    
    	return softListResult;
    }
    
    
    
    
    
    public static String getJsonOrmliteByReadLocal() 
    {
  	  String json=null;
  	  InputStream inStream = TestSoftList.class.getClassLoader()
			.getResourceAsStream("json.txt");
  	  try
		{
			byte[] data = NetAndStreamTool.readStream(inStream);
			  json = new String(data, "utf-8");
		} catch (Exception e)
		{
			return null;
		}
		return json;
    }
}
