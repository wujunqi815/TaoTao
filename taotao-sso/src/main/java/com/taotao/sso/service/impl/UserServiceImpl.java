package com.taotao.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		//username type = 1
		//phone type = 2
		//e-mail tyoe = 3
		switch(type){	
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
		if(list == null || list.size() == 0){
			return TaotaoResult.ok(true);
		}
		 
		return TaotaoResult.ok(false);
	}

}
