package com.appdear.client.utility;

import java.util.Stack;
import android.app.Activity;
import android.util.Log;

/**
 * 屏幕管理
 */
public class ScreenManager {
	private static Stack<Activity> activityStack;//Activity栈
	private static ScreenManager instance; //单例模式
	private  ScreenManager(){}
	public static ScreenManager getScreenManager(){
		if(instance == null){
			instance = new ScreenManager();
		}
		return instance;
	}
	
	/**
	 * 出栈
	 */
	public void popActivity(){ 
		Activity activity = activityStack.lastElement();
		if(activity != null){
			activity.finish();
			activity = null;
		}
	}
	public void popActivity(Activity activity){ 
		if(activity != null){
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}
	
	/**
	 * 得到当前Activity
	 */
	public Activity currentActivity(){
		if(activityStack == null || activityStack.size() == 0){
			return null;
		}
		Activity activity = activityStack.lastElement();
		return activity;
	}
	
	/**
	 * 压栈
	 */
	public void pushActivity(Activity activity){
		if(activityStack == null){
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	
	/**
	 * 除去指定的Activity外，从栈区删除所有Activity
	 */
	public void popAllActivityExceptOne(Class<Activity> cls){
		while(true){
			Activity activity = currentActivity();
			if(activity == null){
				break;
			}
			if(activity.getClass().equals(cls)){
				break;
			}
			popActivity(activity);
		}
	}
	/**
	 * 除去指定的Activity外，从栈区删除所有Activity
	 */
	public void popAllActivityAboveTheOne(Class<Activity> cls){
		while(true){
			Activity activity = currentActivity();
			if(activity == null){
				break;
			}
			if(activity.getClass().equals(cls)){
				return;
			}
			popActivity(activity);
		}
	}
	
	/**
	 * 判断堆栈是否存在activity
	 */
	public boolean isContainActivity(Class<Activity> cls){
		if(activityStack.contains(cls)){
			return true;
		}
		return false;
	}
	
	/**
	 * 退出应用
	 */
	public void exitApp(){
		while(true){
			Activity activity = currentActivity();
			
			if(activity == null){
				break;
			}
			Log.i("", "activity=" + activity.getClass());
			popActivity(activity);
		}
	}
}