package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_INDEX_AD_URL}")
	private String REST_INDEX_AD_URL;
	@Value("${IMAGE_URL}")
	private String IMAGE_URL;
	
	public String getContentList(){
		String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL);
		try{
			TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);
			List<TbContent> list = (List<TbContent>) taotaoResult.getData();
		
			List<Map> resultList = new ArrayList<>();
			for(TbContent content: list){
				Map map = new HashMap<>();
				map.put("src", IMAGE_URL + content.getPic());
				map.put("height", 240);
				map.put("width", 670);
				map.put("srcB", IMAGE_URL + content.getPic2());
				map.put("heightB", 550);
				map.put("widthB", 240);
				map.put("href", content.getUrl());
				map.put("alt", content.getSubTitle());
				System.out.println(content.getPic() + " " + content.getPic2());
				
				resultList.add(map);
			}
			return JsonUtils.objectToJson(resultList);
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
