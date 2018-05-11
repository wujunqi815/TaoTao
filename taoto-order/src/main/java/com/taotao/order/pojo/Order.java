package com.taotao.order.pojo;

import java.util.List;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

public class Order extends TbOrder{

	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShippings;
	
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShippings() {
		return orderShippings;
	}
	public void setOrderShippings(TbOrderShipping orderShippings) {
		this.orderShippings = orderShippings;
	}

}
