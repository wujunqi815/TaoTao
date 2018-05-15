package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;
import com.taotao.portal.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserServiceImpl userService;
	
	@RequestMapping("/order-cart")
	public String showOrderCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CartItem> cartList = cartService.getCartItemList(request, response);
		model.addAttribute("cartList", cartList);
		return "order-cart";
	}

	@RequestMapping("/create")
	public String createOrder(Order order, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			TbUser user = userService.getUserByToken(token);
			order.setUserId(user.getId());
			
			String orderId = orderService.createOrder(order, request, response);
			model.addAttribute("orderId", orderId);
			model.addAttribute("payment", order.getPayment());
			model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy-MM-dd"));
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "生成订单出错");
			return "error/exception";
		}
	}

}
