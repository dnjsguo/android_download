/**
 * $Id$
 * ServerException.java
 * created at:2011-5-10上午10:09:26
 * 
 * Copyright (c) 2011, 北京爱皮科技有限公司
 *
 * All right reserved
 */ 
package com.appdear.client.exception;
  
/** 
 * 网络异常
 * 
 * @author zqm
 */
public class ServerException extends BaseException {


	/**
	 * @param code
	 */
	public ServerException(int code) {
		super(code);
	}
	
	public ServerException(int code, String msg) {
		super(code, msg);
	}
	
	public ServerException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

	@Override
	protected int getExsistCode(int code) {
		// TODO Auto-generated method stub
		if(ExceptionEnum.ServerExceptionCode.exsist(code)){
			return code;
		}else{
			return ExceptionEnum.ServerExceptionCode.SERVER_OTHER_ERR.getValue();
		}
	}
}

 