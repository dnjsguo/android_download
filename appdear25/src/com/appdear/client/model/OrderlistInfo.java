package com.appdear.client.model;

import java.io.Serializable;

/**
 * 消费明细
 * @author zqm
 *
 */
public class OrderlistInfo implements Serializable {
	/**
	 * 订单ID
	 */
	public int orderid;
	
	/**
	 * 流水号
	 */
	public String tranid = "";
	
	/**
	 * 图标地址
	 */
	public String icon = "";
	
	/**
	 * 软件名称
	 */
	public String softname = "";
	
	/**
	 * 软件描述
	 */
	public String desc = "";
	
	/**
	 * 软件价格
	 */
	public int price;
	
	/**
	 * 软件评级
	 */
	public int grade;
	
	/**
	 * 折扣
	 */
	public int discount;
	
	/**
	 * 实际支付
	 */
	public int pay;
	
	/**
	 * 支付类型
	 */
	public int pytype;
}
