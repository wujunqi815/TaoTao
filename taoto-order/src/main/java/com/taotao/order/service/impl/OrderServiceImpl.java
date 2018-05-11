package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * 订单管理Service
 * @author jason.wu
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Override
	public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {
		
		String uuid = UUID.randomUUID().toString();
		String orderId = "TT_";
		System.out.print(orderId);
		order.setOrderId(orderId);
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		order.setStatus(1);
		order.setCreateTime(new Date());
		order.setUpdateTime(new Date());
		//买家是否已经评价,0:未评价 1：已评价
		order.setBuyerRate(0);
		orderMapper.insert(order);
		
		//插入订单明细
		Integer i = 1;
		for(TbOrderItem orderItem : itemList){
			String newUuid = UUID.randomUUID().toString();
			i++;
			String orderDetailId = "T_" + i.toString();
			System.out.print(orderDetailId);
			orderItem.setId(orderDetailId + "");
			orderItem.setOrderId(orderId + "");
			orderItemMapper.insert(orderItem);
		}
		
		//插入物流表
		orderShipping.setOrderId(orderId + "");
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		return TaotaoResult.ok(orderId);
	}

}
