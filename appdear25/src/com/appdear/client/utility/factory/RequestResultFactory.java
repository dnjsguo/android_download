package com.appdear.client.utility.factory;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象工厂记录对象状态
 * 享元模式、原型模式
 * @author jdan
 *
 * @param <T>
 */
public class RequestResultFactory{
	private static RequestResultFactory factory;
	private RequestResultFactory(){
		
	}
	public static RequestResultFactory getInstance(){
		if(factory==null){
			factory=new RequestResultFactory();
		}
		return factory;
	}
	//记录对象状态
	public Map<String,Object> objs=new HashMap();
	
	/**
	 * 单个对象
	 * @param classname
	 * @return
	 */
	public Object getObjectSingle(Class classname){
		return createObj(classname.getName());
	}
	/**
	 * 多对象
	 * @param classname
	 * @return
	 */
	public Object getObjectPropety(Class classname){
		try {
			return (Object)classname.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private Object createObj(String classname){
		try {
			if(objs.containsKey(classname)){
				return objs.get(classname);
			}else{
				return objs.put(classname,(Object)Class.forName(classname).newInstance());
			}
		 }catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return null;
	}	
	public Map<String,Object> getObjs(){
		return objs;
	}
}
