package testapi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.api.ApiSoftListResult;

public class WangUtil
{
	public static String wangUrl="http://media.aishangdian.com/";
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
				info.imgurl=wangUrl+detail.content.icon;
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
    
    //http://admin.aishangdian.com/app/apps/page?o=0&l=50&od=updated_on+desc&history_type=0&content_type=0&_=1378457069267
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
				info.softicon=wangUrl+detail.content.icon;
				info.softprice =0;
				info.softname=detail.content.title;
				info.appid=detail.content.guid;
				info.softsize= detail.app.fileSize;
				if(i==0)
				{
					info.isfirst="1";
				}else
				{
					info.isfirst="0";
				}
				
				info.version=detail.app.versionName;
				info.versioncode=Integer.parseInt(detail.app.versionCode);
				info.softgrade=10;
				info.downloadurl=wangUrl+detail.app.appInstallPkgItem;
				
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
			.getResourceAsStream("json2.txt");
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
