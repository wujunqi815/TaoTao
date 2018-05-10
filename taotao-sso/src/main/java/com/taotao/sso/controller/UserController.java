package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {
		TaotaoResult result = null;
		if (StringUtils.isBlank(param)) {
			result = TaotaoResult.build(400, "检验内容不能为空");
		}
		if (type == null) {
			result = TaotaoResult.build(400, "检验内容类型不能为空");
		}
		if (type != 1 && type != 2 && type != 3) {
			result = TaotaoResult.build(400, "检验内容类型不正确");
		}

		if (result != null) {
			if (callback != null) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result;
			}
		}
		try {
			result = userService.checkData(param, type);
		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		if (callback != null) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}

	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user) {
		try{
			TaotaoResult result = userService.createUser(user);
			System.out.println("register controller ok");
			System.out.println(result.getStatus() + " " + result.getMsg() + " " + result.getData());
			return result;
		}catch(Exception e){
			return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
		}
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		try{
			TaotaoResult result = userService.userLogin(username, password, request, response);
			System.out.println("login controller ok");
			System.out.println(result.getStatus() + " " + result.getMsg() + " " + result.getData());
			return result;
		}catch(Exception e){
			return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
		}
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	public TaotaoResult userLogout(HttpServletRequest request, HttpServletResponse response) {
		try{
			TaotaoResult result = userService.userLogout(request, response);
			return result;
		}catch(Exception e){
			return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
		}
	}
	
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getToken(@PathVariable String token, String callback, HttpServletRequest request, HttpServletResponse response) {
		TaotaoResult result = null;
		try{
			result = userService.getUserByToken(token, request, response);
		}catch(Exception e){
			result =  TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
		}
		
		if (callback != null) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}

	}
}
