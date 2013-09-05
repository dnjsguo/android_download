/**
 * Utils.java
 * created at:2011-5-11下午05:55:31
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.utility;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.appdear.client.SoftWareDetailInfoActivity;
import com.appdear.client.commctrls.SharedPreferencesControl;
import com.appdear.client.db.LabellistDB;
import com.appdear.client.model.CatalogListInfo;
import com.appdear.client.service.Constants;
import com.appdear.client.service.api.ApiCatalogListResult;
import com.appdear.client.service.api.ApiManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

/**
 * 工具类
 * 
 * @author zqm
 */
public class Utils  {
	
	/** *//**
	    * 把字节数组转换成16进制字符串
	    * @param bArray
	    * @return
	    */
	public static final String bytesToHexString(byte[] bArray,int length) {
	    StringBuffer sb = new StringBuffer(bArray.length);
	    String sTemp;
	    for (int i = 0; i < length; i++) {
	     sTemp = Integer.toHexString(0xFF & bArray[i]);
	     if (sTemp.length() < 2)
	      sb.append(0);
	     sb.append(sTemp.toUpperCase());
	    }
	    return sb.toString();
	}
	
	public static String byte2HexString(byte[] b,int length) {
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7',
                      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] newChar = new char[length * 2];
        for(int i = 0; i <length; i++) {
            newChar[2 * i] = hex[(b[i] & 0xf0) >> 4];
            newChar[2 * i + 1] = hex[b[i] & 0xf];
        }
        return new String(newChar);
	}
	  
	public static String byte2HexString(byte[] b) {
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7',
                      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] newChar = new char[b.length * 2];
        for(int i = 0; i <b.length; i++) {
            newChar[2 * i] = hex[(b[i] & 0xf0) >> 4];
            newChar[2 * i + 1] = hex[b[i] & 0xf];
        }
        return new String(newChar);
    }
	
	/*
	 * 获取当前时间
	 */
	public static String nowTime() {
	    Calendar c = Calendar.getInstance();
	    c.setTimeInMillis(new Date().getTime());
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return dateFormat.format(c.getTime());
	}
	
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	static{
	   try{
	    messagedigest = MessageDigest.getInstance("MD5");
	   }catch(NoSuchAlgorithmException nsaex){
	    System.err.println(Utils.class.getName()+"初始化失败，MessageDigest不支持MD5Util");
	    nsaex.printStackTrace();
	   }
	}

	/**
	* 适用于上G大的文件
	* @param file
	* @return
	* @throws IOException
	*/
	public static String getFileMD5String(String  filedir) {
		return new File(filedir).length()+filedir;
	}
	
	/**
	 * MD5算法
	 */
	public final static String MD5encode(String s) {
        char hexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};
        try {
          byte[] strTemp = s.getBytes();
          MessageDigest mdTemp = MessageDigest.getInstance("MD5");
          mdTemp.update(strTemp);
          byte[] md = mdTemp.digest();
          int j = md.length;
          char str[] = new char[j * 2];
          int k = 0;
          for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
          }
          return new String(str);
        }
        catch (Exception e) {
          return null;
        }
	}
	
	public final static String MD5encode(byte[] obj){
		String str1="";
	    try {
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	            md5.update(obj);
	        byte[] byArray=md5.digest();
	        
	        StringBuilder sb=new StringBuilder(str1);
	        for (byte element: byArray )
	        {
	         sb.append(String.valueOf(element));
	        }
	        str1=sb.toString();
	       
	    } catch (NoSuchAlgorithmException e){
	        e.printStackTrace();
	    }
		return str1;
	}
	
	/**
     * 生成md5字符串
     * @param value
     * @return
     */
 	public static String generateMD5(String value) {
 		try {
 			MessageDigest md = MessageDigest.getInstance("MD5");
 			byte[] bytes;
 			try {
 				bytes = value.getBytes("UTF-8");
 			} catch (UnsupportedEncodingException e1) {
 				bytes = value.getBytes();
 			}
 			StringBuilder result = new StringBuilder();
 			for (byte b : md.digest(bytes)) {
 				result.append(Integer.toHexString((b & 0xf0) >>> 4));
 				result.append(Integer.toHexString(b & 0x0f));
 			}
 			return result.toString();
 		} catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
 		}
 	}
 	
 	/**
	 * 获取高亮处理过的String
	 * 
	 * @param content  原内容
	 * @param highlightString 需要高亮显示的文字
	 * @param color  指定的颜色
	 * @return  高亮处理后的String
	 */
	public static SpannableString getSpannableString(String content,
			String highlightString, int color) {
		SpannableString ss = new SpannableString(content);
		if ("".equals(highlightString) || highlightString == null) {
			return ss;
		}

		//将关键词按[空格 ， , . 。]分开成多个关键字
		String[] StringParts = highlightString.split("[\\s,，.。]"); 
		for (String part : StringParts) {
			if ("".equals(part)) {
				continue;
			}
			
			int startIndex = 0;
			int retIndex = -1;  //每次循环返回的索引
			List<Integer> startIndexes = new ArrayList<Integer>();

			//content里可能会出现多个需要高亮显示的关键词part，循环获取每个关键词的首字符的retIndex
			while (-1 != (retIndex = content.toLowerCase().indexOf(part.toLowerCase(), startIndex))) {
				startIndexes.add(retIndex); //如果出现关键词，则add其出现的位置retIndex
				startIndex = part.length() + retIndex;
				if (startIndex > content.length()) {
					break; //如果已经检测到最后，则跳出循环
				}
			}

			//将所有关键词设置高亮
			for (Integer index : startIndexes) {
				ForegroundColorSpan fcs = new ForegroundColorSpan(color);
				ss.setSpan(fcs, index, index + part.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
		return ss;
	}
	/**
	 * 网络简单获取图片
	 */
	
	public static Bitmap getImage(String Url) throws Exception {

		try {

			 URL url = new URL(Url);

			String responseCode = url.openConnection().getHeaderField(0);

			if (responseCode.indexOf("200") < 0)

				throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);


			return BitmapFactory.decodeStream(url.openStream());

		} catch (IOException e) {

			// TODO Auto-generated catch block

			throw new Exception(e.getMessage());

		}

	}


	/**
	 * 获取截断的字符串（如：会说话的汤姆猫  ---> 会说话的汤...）
	 * @param content  原始字符串
	 * @param length   截取的长度
	 * @param endTag   替代省略掉的内容的字符（如：...）
	 * @return         截断的字符串
	 */
	public static String getOmitString(String content, int length, String endTag) {
		if (content != null && content.length() > length) {
			return content.substring(0, length - endTag.length()) + endTag;
		}else {
			return content;
		}
	}
	
	/**
	 * 跳至短信编辑界面
	 * @param context
	 * @param smsContent 默认短信内容
	 */
	public static void gotoSmsEditPage(Context context, String smsContent) {
		try{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.putExtra("sms_body", smsContent);
			intent.setType("vnd.android-dir/mms-sms");
			context.startActivity(intent);
		}catch (Exception e) {
			Toast.makeText(context, "不能使用短信分享", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 组合默认短信内容(推荐你玩爱皮应用下载里的"应用名前6个字"+链接地址)
	 * @param softName
	 * @param detailUrl
	 * @return
	 */
	public static String getDefaultSmsContent(String softName, String detailUrl) {
		String SMS_CONTENT_A = "推荐你玩爱皮应用下载里的“";
		
		String omitSoftName = Utils.getOmitString(softName, 15, "..."); //获取软件名的前12个字符+...
		String completeSmsContent = SMS_CONTENT_A + omitSoftName + "”，" + detailUrl;
		
		return completeSmsContent;
	}
   
 	public static void sleep(int nSecond) {
 		try {
			Thread.sleep(nSecond);
 		} catch(Exception e) {
 			e.printStackTrace ();
 		}
 	}
 	
	public static void log(String sMsg) {
		System.err.println(sMsg);
	}
	
	public static void log(int sMsg) {
		System.err.println(sMsg);
	}
	
	/**
	 * 输出调试信息
	 * @param log
	 */
	public static void print(String log){
		if (Constants.DEBUG) {
			System.out.println(log);
		}
	}
	
	/**
	 * 万花筒模块--从数据库或服务器获取分类list数据（带缓存处理机制）
	 * @param contex   你懂的
	 * @param pageno   当前页
	 * @param count    当前页数量
	 * @param catList  获取到的数据将存入此list
	 * @param id       栏目ID
	 * @param nowServerVersion  服务器下发的当前版本
	 * @param MAX_VIEW_COUNT  获取数据的最大条数
	 */
	public static ApiCatalogListResult getCatListFromServerOrDB(Context contex, String pageno, 
			String count, int id, String CAT_KEY, 
			String dbType, int nowServerVersion, int MAX_VIEW_COUNT) {
		
		ApiCatalogListResult catList = new ApiCatalogListResult();
		try {
			catList = new ApiCatalogListResult();
			
			//获取xml文件里保存的上一次标签版本号
			int savedLabelVersion = SharedPreferencesControl.getInstance().getInt("labelversion", com.appdear.client.commctrls.Common.SETTINGS, contex);
			
			// 判断从数据库（缓存）还是服务器取数据
			LabellistDB labelDB = new LabellistDB(contex);
			if (nowServerVersion == savedLabelVersion) {
				// 如果 服务器版本号 = 上次保存版本号，则从找数据库
				print("-----上次保存版本号=服务器版本号-->查询缓存（数据库）是否有数据-----");
				List<CatalogListInfo> list = labelDB.getLabellist(dbType);
				if (list != null && list.size() > 0) {
					// 如果数据库有，则从数据库取
					print("-----数据库有-->取缓存（数据库）数据-----");
					catList.catalogList = list;
				} else {
					// 数据库没有，则从服务器取并保存至数据库
					print("-----数据库没有缓存数据-->从服务器取-----");
					// 取数据
					id = SharedPreferencesControl.getInstance().getInt(CAT_KEY,
							com.appdear.client.commctrls.Common.SECTIONCODEXML, contex);
					catList = ApiManager.labellist(id + "", pageno, count, "1");

					// 存数据
					print("将缓存数据存入数据库");
					if (catList != null && catList.catalogList.size() > 0) {
						int saveCount = catList.catalogList.size() > MAX_VIEW_COUNT ? MAX_VIEW_COUNT
								: catList.catalogList.size();
						for (int i = 0; i < saveCount; i++) {
							CatalogListInfo info = catList.catalogList.get(i);
							labelDB.insertLabellist(info, dbType);
						}
					}

					// 此处不用更新上次保存版本号
				}
			} else {
				// 最后选择，如果 服务器版本号 > 上次保存版本号，则直接从服务器取（先将当前数据库数据清掉，在写入数据库，并更新上一次保存版本）
				print("-----服务器版本号  > 上次保存版本号，直接从服务器取数据-----");
				// 取数据
				id = SharedPreferencesControl.getInstance().getInt(CAT_KEY,
						com.appdear.client.commctrls.Common.SECTIONCODEXML,
						contex);
				catList = ApiManager.labellist(id + "", pageno, count, "1");

				// 存数据
				if (catList != null && catList.catalogList.size() > 0) {
					// 清数据
					labelDB.deleteAll(dbType);

					// 存数据
					print("将缓存数据存入数据库");
					for (CatalogListInfo info : catList.catalogList) {
						labelDB.insertLabellist(info, dbType);
					}
				}

				// 将服务器下发的版本号保存至xml文件的labelversion中
				print("存版本号至xml文件--labelversion = " + nowServerVersion);
				SharedPreferencesControl.getInstance().putInt("labelversion", nowServerVersion,
						com.appdear.client.commctrls.Common.SETTINGS, contex);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catList;
	}
	
	/**
	 * --万花筒模块--
	 * 从二维数组dataArray中取出name并填充至一维数组names中
	 * @param dataArray  二维数组（源数据）
	 * @param names  一位数组（目标数据）
	 * @return -1失败，0成功
	 */
	public static  int getNameArray(String[][] dataArray, String[] names) {
		if (dataArray == null || names == null || names.length > dataArray.length) {
			return -1;
		}
		try {
			for (int i = 0; i < names.length; i++) {
				names[i] = dataArray[i][0]; //取出每一个name填充至一位数组names中
			}
			return 0;
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * 根据标签名查找对应的id
	 * @param catName     根据此key从元数据中查找对应的id
	 * @param tvDataArray 源数据
	 * @return
	 */
	public static String getIdByName(String catName, String[][] tvDataArray) {
		if (catName == null) {
			return "-1";
		}
		for (int i = 0; i < tvDataArray.length; i++) {
			if (catName.equals(tvDataArray[i][0])) {
				return tvDataArray[i][1];  //返回对应的id
			}
		}
		return "-1";
	}
}