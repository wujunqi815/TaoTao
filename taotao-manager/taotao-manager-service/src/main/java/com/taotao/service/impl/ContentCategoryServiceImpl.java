package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {


		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EUTreeNode> resultList = new ArrayList<>();
		
		for(TbContentCategory tbContentCategory : list){
			EUTreeNode node = new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed":"open");
			
			resultList.add(node);
			
		}
		return resultList;
	}

	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		contentCategory.setParentId(parentId);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		
		//查看父节点isParent属性值是否为true如果是true改为true
		TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentCategory.getIsParent()){
			parentCategory.setIsParent(true);
			
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult deleteContentCategory(Long parentId, Long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		parentId = contentCategory.getParentId();
		
		contentCategoryMapper.deleteByPrimaryKey(id);
		
		System.out.println("delete succ," + id + parentId);
		//查看父节点isParent属性值是否为true如果是true改为true
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(list.size() == 0){
			TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
			parentCategory.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		
		return TaotaoResult.ok();
		
	}

}
