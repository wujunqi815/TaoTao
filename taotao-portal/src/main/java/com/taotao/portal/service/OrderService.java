package com.taotao.portal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.portal.pojo.Order;

public interface OrderService {

	String createOrder(Order order, HttpServletRequest request, HttpServletResponse response);
}
