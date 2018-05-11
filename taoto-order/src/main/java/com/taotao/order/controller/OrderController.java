package com.taotao.order.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	/**
	 * 创建订单
	 */
	@RequestMapping("/create")
	public TaotaoResult createOrder(@RequestBody Order order){
		try{
			TaotaoResult result = orderService.createOrder(order, order.getOrderItems(), order.getOrderShipping());
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
		}
	}
}
