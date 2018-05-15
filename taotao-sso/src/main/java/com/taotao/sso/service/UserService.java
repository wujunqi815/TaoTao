package com.taotao.sso.service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	TaotaoResult checkData(String content, Integer type);
	TaotaoResult createUser(TbUser user);
	TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);
	TaotaoResult getUserByToken(String token, HttpServletRequest request, HttpServletResponse response);
	TaotaoResult userLogout(String token, HttpServletRequest request, HttpServletResponse response);
}
