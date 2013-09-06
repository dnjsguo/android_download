package testapi;

 

import java.util.List;

 
import com.alibaba.fastjson.annotation.JSONField;

/**
 * App¡–±Ì
 */
public class AppList {
	public int offset;
	public int limit;
	public int total;
	@JSONField(name="items")
	public List<AppDetail> appList;

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public List<AppDetail> getAppList()
	{
		return appList;
	}

	public void setAppList(List<AppDetail> appList)
	{
		this.appList = appList;
	}
}
