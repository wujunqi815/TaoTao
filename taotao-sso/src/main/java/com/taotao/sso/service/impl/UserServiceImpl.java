package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;

import com.mysql.jdbc.StringUtils;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	@Value("${TT_TOKEN_USER}") 
	private String TT_TOKEN_USER;
	
	@Value("${SSO_SESSION_EXPIRE}") 
	private Integer SSO_SESSION_EXPIRE;

	@Override
	public TaotaoResult checkData(String content, Integer type) {

		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		// username type = 1
		// phone type = 2
		// e-mail tyoe = 3
		switch (type) {
		case 1:
			criteria.andUsernameEqualTo(content);
			break;
		case 2:
			criteria.andPhoneEqualTo(content);
			break;
		case 3:
			criteria.andEmailEqualTo(content);
			break;
		default:
			break;
		}

		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);
		}

		return TaotaoResult.ok(false);
	}

	@Override
	public TaotaoResult createUser(TbUser user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// MD5加密，spring框架提供
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		try {
			userMapper.insert(user);
			System.out.println("register ok");
			return TaotaoResult.ok(true);
		} catch (Exception e) {
			System.out.println("register nok");
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	@Override
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {

		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);

		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		//把用户信息写入redis
		//设置session过期时间
		
		//设置session(session不共享，用cookie代替)
		CookieUtils.setCookie(request, response,TT_TOKEN_USER + "_" + token, JsonUtils.objectToJson(user));
		System.out.println("set cookie:" + TT_TOKEN_USER + "_" + token);
		
		//设置cookie,taotao.js需要(首页会调用localhost:8084/user/token/{token})
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult userLogout(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.setCookie(request, response, "TT_TOKEN", "");
		return TaotaoResult.ok();
	}
	
	@Override
	public TaotaoResult getUserByToken(String token, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(TT_TOKEN_USER + "_" + token);
		String  cookieValue = CookieUtils.getCookieValue(request, TT_TOKEN_USER + "_" + token);
		System.out.println(cookieValue.isEmpty());
		System.out.println("get cookie");
		if(!cookieValue.isEmpty()){
			request.setAttribute("COOKIE", cookieValue);
			System.out.println("user in cookie is not null");
		}else{
			System.out.println(cookieValue.isEmpty());
		}
		
		String  cookie = (String) request.getAttribute("COOKIE");
		if(cookie.isEmpty()){
			System.out.println("user in session is null");
			return TaotaoResult.build(400, "session过期");
		}else{
			System.out.println(JsonUtils.jsonToPojo(cookie, TbUser.class).getUsername());
		}
		
		return TaotaoResult.ok(JsonUtils.jsonToPojo(cookie, TbUser.class));
	}

}
