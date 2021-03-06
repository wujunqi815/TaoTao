package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;
	
	
	@Override
	public String createOrder(Order order, HttpServletRequest request, HttpServletResponse response) {

		String json = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
		System.out.println(ORDER_BASE_URL + ORDER_CREATE_URL);
		
		
		TaotaoResult result = TaotaoResult.format(json);
		System.out.println(result.getStatus());
		if(result.getStatus() == 200){
			String orderId = result.getData().toString();
			List<CartItem> list = new ArrayList<>();
			CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
			return orderId;
		}
		return "";
	}
	
}
