package com.appdear.client.service.api;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.PermissionListInfo;

public class ApiSoftPermissionResult  implements ApiResult{
	public List<PermissionListInfo> permissionList = new ArrayList<PermissionListInfo>();
	public int resultcode = 0;
	public String imei = "";
	public String sv = "";
}
