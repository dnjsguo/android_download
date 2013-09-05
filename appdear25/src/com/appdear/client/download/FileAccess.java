/**
 * FileAccess.java
 * created at:2011-5-10下午02:19:14
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.download;
  
import java.io.*;

/** 
 * 文件存储
 * 
 * @author zqm 
 */
public class FileAccess implements Serializable {
	
	private RandomAccessFile oSavedFile;	
	private long nPos;
	
	public FileAccess() throws IOException {
		this("",0);
	}
	
	public FileAccess(String sName, long nPos) throws IOException {
		oSavedFile = new RandomAccessFile(sName, "rw");
		this.nPos = nPos;
		oSavedFile.seek(nPos);
	}
	
	public synchronized int write(byte[] b, int nStart, int nLen) throws IOException {
		int n = -1;
		oSavedFile.write(b, nStart, nLen);
		n = nLen;
		return n;
	}
}


 