package com.taotao.sso.service;
import org.springframework.ui.Model;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	TaotaoResult checkData(String content, Integer type);
	TaotaoResult createUser(TbUser user);
	TaotaoResult userLogin(String username, String password);
}