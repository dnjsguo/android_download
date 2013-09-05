package com.appdear.client.exception;
/**
 * 异常枚举可扩展
 * @author jdan
 *
 */
public class ExceptionEnum{
	/**
	 * 爱皮市场服务鉴权未通过100000
	 * 请求参数格式错误200000
	 * 服务内部错误300000
	 * 网络超时400000
	 * 用户Token已经失效500000
	 * 未知错误600000
	 * 网络不可用700000
	 */
	public static enum ApiExceptionCode{
		API_NOT_VERIFIED(100000),API_PARAMETERS_ERR(200000),API_SERVER_ERR(300000),API_NETWORK_TIMEOUT(400000),
		API_TOKEN_FAILURE(500000),API_OTHER_ERR(600000),API_NETWORK_NOAVALIBLE(700000);
		private final int value;
		private ApiExceptionCode(int value){
			this.value=value;
		}
		public int getValue(){
			return value;
		}
		public static boolean exsist(int code){
			if(code==API_NOT_VERIFIED.getValue()
				||code==API_PARAMETERS_ERR.getValue()
				||code==API_SERVER_ERR.getValue()
				||code==API_NETWORK_TIMEOUT.getValue()
				||code==API_TOKEN_FAILURE.getValue()
				||code==API_OTHER_ERR.getValue()
				||code==API_NETWORK_NOAVALIBLE.getValue()){
				return true;
			}else{
				return false;
			}	
		}
	}
	/**
	 * 其他-1
	 * 网络连接失败，目标地址不能被打开-2
	 * 无效的响应码 -3
	 */
	public static enum ServerExceptionCode{
		SERVER_OTHER_ERR(-1),SERVER_EXCEPTION_CONNECTIONNOTOPEN(-2),SERVER_INVALID_RESPONSE(-3);
		private final int value;
		private ServerExceptionCode(int value){
			this.value=value;
		}
		public int getValue(){
			return value;
		}
		public static boolean exsist(int code){
			if(code==SERVER_OTHER_ERR.getValue()
				||code==SERVER_EXCEPTION_CONNECTIONNOTOPEN.getValue()
				||code==SERVER_INVALID_RESPONSE.getValue()){
				return true;
			}else{
				return false;
			}	
		}
	}
}