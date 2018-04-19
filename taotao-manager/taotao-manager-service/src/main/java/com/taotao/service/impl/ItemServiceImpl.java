package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
/**
 * 商品管理service
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
	public TbItem getItemById(long itemId) {
		
		TbItemExample example = new TbItemExample();
		
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		
		List<TbItem> list = itemMapper.selectByExample(example);
		
		if(list != null && list.size() > 0){
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}
	
	/**
	 * 商品列表查詢
	 */
	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		
		TbItemExample example = new TbItemExample();
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public TaotaoResult createItem(TbItem item, String desc, String itemParam) {
		Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//1-正常 2-下架 3- 删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		
		itemMapper.insert(item);
		InsertItemDesc(itemId, desc);
		insertItemParamItem(itemId, itemParam);
		return TaotaoResult.ok();
	}
	
	/*
	 * add the description
	 */
	private TaotaoResult InsertItemDesc(Long itemId, String desc){
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
	
	private TaotaoResult insertItemParamItem(long itemId, String itemParam){
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		
		itemParamItemMapper.insert(itemParamItem);
		return TaotaoResult.ok();
	}
}
