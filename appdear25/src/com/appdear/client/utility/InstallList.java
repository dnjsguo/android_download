package com.appdear.client.utility;

import java.util.ArrayList;
import java.util.List;

import com.appdear.client.model.PackageinstalledInfo;

public class InstallList extends  ArrayList<PackageinstalledInfo>{

	@Override
	public PackageinstalledInfo remove(int location) {
		// TODO Auto-generated method stub
		if(location>=super.size())return null;
		PackageinstalledInfo info=null;
		if(hasOneApp(location)){
			info=super.remove(location);
			super.remove(location-1);
		}else{
			info=super.remove(location);
		}
		return info;
	}
	/**
	 * 判断安装列表某位置中的应用是否是所属字母唯一的应用
	 */
	public boolean hasOneApp(int postion){
			boolean hasOne=false;
			int size=size();
			int count=0;
			//判断状态位
			boolean flag=false;
			if(size>0&&size>postion){
				for(PackageinstalledInfo info:this){
					if(count==(postion-1)){
						if(info.isCharProxy==true){
							flag=true;
						}
					}else if(count==(postion+1)){
						if(info.isCharProxy==true&&flag==true){
							return flag;
						}
					}
					count++;
				}
				if(flag==true&&postion+1==size){
					return flag;
				}
			}
			return false;
	}
}
