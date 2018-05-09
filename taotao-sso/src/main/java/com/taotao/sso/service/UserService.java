package com.taotao.sso.service;
import com.taotao.pojo.TaotaoResult;

public interface UserService {

	TaotaoResult checkData(String content, Integer type);
}
