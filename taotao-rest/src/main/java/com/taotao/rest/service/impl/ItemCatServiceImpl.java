package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Override
	public CatResult getItemCatList() {

		CatResult catResult = new CatResult();
		catResult.setData(getCatList(0));
		return catResult;
	}
	
	/**
	 * 查询分类列表S
	 * @param parentID
	 * @return
	 */
	public List<?> getCatList(long parentID){
		
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//查询parentid为0的分类信息
		criteria.andParentIdEqualTo(parentID);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
//		for(TbItemCat tbItemCat : list){
//			System.out.println(tbItemCat.getName());
//		}
//		
		List dataList = new ArrayList();
		int counter = 0;
		for (TbItemCat tbItemCat : list) {
			// 判断是否为父节点
			if (tbItemCat.getIsParent()) {
				CatNode catNode = new CatNode();
				//第一层
				if (parentID == 0) {
					catNode.setName("<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
				} else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
				catNode.setItem(getCatList(tbItemCat.getId()));
				dataList.add(catNode);
				counter ++;
				if(parentID == 0 && counter  >= 14){
					break;
				}
			//如果是叶子节点
			} else{
				dataList.add("/products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());
			}
		}
		return dataList;

	}

}
