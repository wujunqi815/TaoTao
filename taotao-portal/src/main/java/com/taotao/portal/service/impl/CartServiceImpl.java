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
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

/**
 * 购物车Service
 * 
 * @author jason.wu
 *
 */

@Service
public class CartServiceImpl implements CartService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;

	@Override
	public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {

		CartItem cartItem = null;
		// 取购物车商品列表
		List<CartItem> list = getCartItemList(request);
		for (CartItem cItem : list) {
			if (cItem.getId() == itemId) {
				cItem.setNum(cItem.getNum() + num);
				cartItem = cItem;
				break;
			}
		}

		if (cartItem == null) {
			cartItem = new CartItem();

			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
			if (result.getStatus() == 200) {
				TbItem item = (TbItem) result.getData();
				cartItem.setId(item.getId());
				cartItem.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
				cartItem.setNum(num);
				cartItem.setPrice(item.getPrice());
				cartItem.setTitle(item.getTitle());
			}
			list.add(cartItem);
		}
		
		//把列表写回cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
		return TaotaoResult.ok();
	}

	/**
	 * cookie 中取商品列表
	 * 
	 * @return
	 */
	private List<CartItem> getCartItemList(HttpServletRequest request) {
		String cartJson = CookieUtils.getCookieValue(request, "TT_CART", true);
		if(cartJson == null){
			return new ArrayList<>();
		}
		
		try{
			return JsonUtils.jsonToList(cartJson, CartItem.class);
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
		List<CartItem> itemList = getCartItemList(request);
		return itemList;
	}

}
