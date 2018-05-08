package com.taotao.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.service.ItemService;

/**
 * 商品信息管理
 * @author jason.wu
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired 
	private TbItemMapper itemMapper;
	@Autowired 
	private TbItemDescMapper itemDescMapper;
	@Autowired 
	private TbItemParamItemMapper itemParamItemMapper;
	
	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {

		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		
		return TaotaoResult.ok(item);
	}
	
	@Override
	public TaotaoResult getItemDesc(long itemId) {
		
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		return TaotaoResult.ok(itemDesc);
	}

	@Override
	public TaotaoResult getItemParam(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		
		if(list != null && list.size() > 0){
			TbItemParamItem paramItem = list.get(0);
			return TaotaoResult.ok(paramItem);
		}
		
		return TaotaoResult.build(400, "无此商品信息");
	}

	
}
