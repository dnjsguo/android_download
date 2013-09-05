package com.appdear.client.utility;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class PackageParserT {
	
	public  static PackageInfo getPackageInfo(Context context,String apkPath,int flags) {
	  String PATH_PackageParser = "android.content.pm.PackageParser";
	  String PATH_PackageParser_Package = "android.content.pm.PackageParser.Package";
	  String PATH_AssetManager = "android.content.res.AssetManager";
	  try {
	   // apk包的文件路径
	   // 这是一个Package 解释器, 是隐藏的
	   // 构造函数的参数只有一个, apk文件的路径
	   // PackageParser packageParser = new PackageParser(apkPath);
	   Class pkgParserCls = Class.forName(PATH_PackageParser);
	   Class[] typeArgs = new Class[1];
	   typeArgs[0] = String.class;
	   Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
	   Object[] valueArgs = new Object[1];
	   valueArgs[0] = apkPath;
	   Object pkgParser = pkgParserCt.newInstance(valueArgs);
	//   Log.d("ANDROID_LAB", "pkgParser:" + pkgParser.toString());
	   // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
	   DisplayMetrics metrics = new DisplayMetrics();
	   metrics.setToDefaults();
	   // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
	   // File(apkPath), apkPath,
	   // metrics, 0);
	   typeArgs = new Class[4];
	   typeArgs[0] = File.class;
	   typeArgs[1] = String.class;
	   typeArgs[2] = DisplayMetrics.class;
	   typeArgs[3] = Integer.TYPE;
	   Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
	     typeArgs);
	//   Log.d("ANDROID_LAB", "pkgParser_parsePackageMtd:" +apkPath);
	   valueArgs = new Object[4];
	   valueArgs[0] = new File(apkPath);
	   valueArgs[1] = apkPath;
	   valueArgs[2] = metrics;
	   valueArgs[3] = 0;
	   Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
	//   Log.d("ANDROID_LAB", "pkgParserPkg:" +pkgParserPkg.getClass());
	   //jindan---------------------------------
	    typeArgs = new Class[2];
	    typeArgs[0] = pkgParserPkg.getClass();
	    typeArgs[1]=Integer.TYPE;
	   	Method pkgParser_Certificates=pkgParserCls.getDeclaredMethod("collectCertificates",typeArgs);
	//    Log.d("ANDROID_LAB", "pkgParser_Certificates:" +pkgParser_Certificates);
	   	
	   	valueArgs = new Object[2];
		valueArgs[0] = pkgParserPkg;
		valueArgs[1] = 0;
		pkgParser_Certificates.invoke(pkgParser, valueArgs);
	//	 Log.d("ANDROID_LAB", "pkgParser_parsePackageMtd:" +pkgParser_parsePackageMtd);
		
		
		
		Class generatec=Class.forName(PATH_PackageParser); 
		Method get_packageInfo=null;
		int l=0;
		for(Method m:generatec.getDeclaredMethods()){
			if(m.getName().equals("generatePackageInfo")){
				if((l=m.getParameterTypes().length)==3){
					typeArgs = new Class[3];
					typeArgs[0] = pkgParserPkg.getClass();
					typeArgs[1]=new int[]{}.getClass();
					typeArgs[2] = Integer.TYPE;
				}else if(l==5){
					typeArgs = new Class[5];
					typeArgs[0] = pkgParserPkg.getClass();
					typeArgs[1]=new int[]{}.getClass();
					typeArgs[2] = Integer.TYPE;
					typeArgs[3]=Long.TYPE;
					typeArgs[4] = Long.TYPE;
				}
				get_packageInfo=m;
				break;
			}else{
				continue;
			}
		}
	//	 Log.d("ANDROID_LAB", "get_packageInfo:" +get_packageInfo);
		
		if(l==3){
			valueArgs = new Object[3];
		    valueArgs[0] = pkgParserPkg;
		    valueArgs[1] = null;
		    valueArgs[2] = flags;
		}else if(l==5){
			valueArgs = new Object[5];
		    valueArgs[0] = pkgParserPkg;
		    valueArgs[1] = null;
		    valueArgs[2] = flags;
		    valueArgs[3] = 0L;
		    valueArgs[4] = 0L;
		}
		Object packageInfo=get_packageInfo.invoke(generatec, valueArgs);
		return (PackageInfo)packageInfo;
	  } catch (Exception e) {
		  Log.d("ANDROID_LAB", "packageInfo:" +e.getMessage()+"-"+e);
			   e.printStackTrace();
			   return null;
	  }
	 
	 }
	
	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return false签名不相同,true签名相同
	 */
	public static boolean IsSignaturesSame(Signature[] s1, Signature[] s2) {
//		Log.i("info1111", s1+"="+s2);
		if (s1 == null) {
			 return false;
	     }
	     if (s2 == null) {
	        return false;
	     }
	    HashSet<Signature> set1 = new HashSet<Signature>();
	    for (Signature sig : s1) {
		      set1.add(sig);
	    }
		HashSet<Signature> set2 = new HashSet<Signature>();
	    for (Signature sig : s2) {
	    	 set2.add(sig);
	    }
	    
	    // Make sure s2 contains all signatures in s1.
	    if (set1.equals(set2)) {
            return true;
        } else {
        	//签名不相同
        	return false;
        }
     }

}

