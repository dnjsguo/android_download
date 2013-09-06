package testapi;

import java.io.Serializable;
import java.util.List;

/**
 * App详情
 */
public class AppDetail implements Serializable {
	public String id; // 应用Id
	public DetailContent content;
	public DetailApp app;
	
	public int  status= 3;
	public String statusDescOpt;
	public String staffIdOpt;
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public DetailContent getContent()
	{
		return content;
	}
	public void setContent(DetailContent content)
	{
		this.content = content;
	}
	public DetailApp getApp()
	{
		return app;
	}
	public void setApp(DetailApp app)
	{
		this.app = app;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getStatusDescOpt()
	{
		return statusDescOpt;
	}
	public void setStatusDescOpt(String statusDescOpt)
	{
		this.statusDescOpt = statusDescOpt;
	}
	public String getStaffIdOpt()
	{
		return staffIdOpt;
	}
	public void setStaffIdOpt(String staffIdOpt)
	{
		this.staffIdOpt = staffIdOpt;
	} 
	
	/*public String appName; // 应用名称
	public String category; // 应用类别
	public String package_name; // 应用包Id
	public String version; // 应用VersionName
	public int version_code; // 应用VersionCode
	public String thumbnail; // 缩略图（即图标）url
	public long appSize; // 文件大小
	public String developer; // 开发者
	public String description; // 应用描述
	public String download_url; // 下载地址
	public List<String> preview; // 预览图列表
    public String released_on; //发布时间
//    public String _tid; //终端提供参数值
	public String title; // 无用（与description相同）
	// 以下字段是方便下载附加的信息
	public int downStatus; //0--未下载 1--正在下载 2--下载完成 -1--下载失败 -2--服务器无返回
*/
	 
}
