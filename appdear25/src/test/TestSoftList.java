package test;

 
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.appdear.client.model.SoftlistInfo;
import com.appdear.client.service.api.ApiSoftListResult;
 
 

import android.test.AndroidTestCase;

public class TestSoftList  extends AndroidTestCase
{
	 
	public void testAd()
	   {
		try
		{
			String json = getJsonOrmliteByReadLocal();
			//System.out.println("json="+json);
			AppList result=JSON.parseObject(json,AppList.class);
			//System.out.println("size="+result.getAppList().size()+"limit="+result.limit);
			ApiSoftListResult adresult =new ApiSoftListResult();
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
		}
 
	   }
	 
    public String getJsonOrmliteByReadLocal() 
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
	public static String getString(String filename) throws Exception
	{
		InputStream in = TestSoftList.class.getClassLoader().getSystemResourceAsStream(filename);
		StringBuffer sb = new StringBuffer("");
		byte[] buffer = new byte[2 * 1024];
		int len = 0;
		while ((len = in.read(buffer)) != -1)
		{
			sb.append(new String(buffer, 0, len,"UTF-8"));
		}
		return sb.toString();
	}
}

