package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Value("${SSO_BASE_URL}")
	public String SSO_BASE_URL;
	@Value("${SSO_USER_TOKEN}")
	private String SSO_USER_TOKEN;
	@Value("${SSO_PAGE_LOGIN}")
	public String SSO_PAGE_LOGIN;
	
	@Override
	public TbUser getUserByToken(String token) {

		try{
		String json = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN + token);
		System.out.println(SSO_BASE_URL + SSO_USER_TOKEN + token);

		TaotaoResult result = TaotaoResult.formatToPojo(json, TbUser.class);
		
		if(result.getStatus() == 200){
			TbUser user = (TbUser)result.getData();
			return user;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
