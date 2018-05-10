package com.taotao.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TaotaoResult;
import com.taotao.portal.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired CartService cartService;
	
	/**
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return 页面
	 */
	@RequestMapping("/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num, HttpServletRequest request, HttpServletResponse response){
		cartService.addCartItem(itemId, num, request, response);
		return "cartSuccess";
	}
}
