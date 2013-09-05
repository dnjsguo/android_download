/**
 * BaseException.java
 * created at:2011-5-9 15:34:25
 * 
 * copyright (c) 2011, 北京爱皮科技有限公司
 * 
 * All right reserved
 * 
 */
package com.appdear.client.exception;

/**
 * 异常基类
 * 
 * @author zqm
 */
public abstract class BaseException extends Exception {

	/**
	 * 异常编号
	 */
	private int code;
	
	/**
	 * 异常
	 * @param code 异常编号
	 */
	public BaseException(int code) {
		super();
		this.code = getExsistCode(code);
	}

	/**
	 * 异常
	 * @param code 异常编号
	 * @param msg 异常信息
	 */
	public BaseException(int code, String msg) {
		super(msg);
		this.code = getExsistCode(code);
	}

	/**
	 * 异常
	 * @param code 异常编号
	 * @param msg 异常信息
	 * @param throwable 异常类型
	 */
	public BaseException(int code, String msg, Throwable throwable) {
		super(msg, throwable);
		this.code =  getExsistCode(code);
	}

	/**
	 * 异常
	 * @param code 异常编号
	 * @param throwable 异常类型
	 */
	public BaseException(int code, Throwable throwable) {
		super(throwable);
		this.code = getExsistCode(code);
	}
	
	/**
	 * 获取异常编号
	 * @return 异常编号
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置异常编号
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * 如果不存在异常编码返回默认异常码
	 * @param code
	 * @return
	 */
	protected  abstract int getExsistCode(int code);
}
