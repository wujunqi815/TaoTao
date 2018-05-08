package com.taotao.portal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.Item;
import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;

/*
 * 商品信息管理
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	@Value("${DESC_URL}")
	private String DESC_URL;
	@Value("${PARAM_URL}")
	private String PARAM_URL;

	@Override
	public ItemInfo getItemById(Long itemId) {

		// 调用rest的服务查询商品信息
		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			// System.out.println(json);
			if (!StringUtils.isBlank(json)) {
				TaotaoResult result = TaotaoResult.formatToPojo(json, ItemInfo.class);
				if (result.getStatus() == 200) {
					ItemInfo itemInfo = (ItemInfo) result.getData();
					return itemInfo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getItemDescById(Long itemId) {

		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL + DESC_URL + itemId);
			// System.out.println("URL:"+ REST_BASE_URL + DESC_URL + itemId);
			// System.out.println("商品描述："+ json);
			if (!StringUtils.isBlank(json)) {
				TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemDesc.class);
				if (result.getStatus() == 200) {
					TbItemDesc itemDesc = (TbItemDesc) result.getData();
					return itemDesc.getItemDesc();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getItemParamById(Long itemId) {
		// 调用rest的服务查询商品信息
		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL + PARAM_URL + itemId);
			System.out.println("URL:" + REST_BASE_URL + PARAM_URL + itemId);
			System.out.println("商品描述：" + json);

			if (!StringUtils.isBlank(json)) {
				TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
				if (result.getStatus() == 200) {
					TbItemParamItem itemParamItem = (TbItemParamItem) result.getData();

					String paramData = itemParamItem.getParamData();

					// 生成HTML
					// 把json数据转换成java对象
					List<Map> paramList = JsonUtils.jsonToList(paramData, Map.class);
					// 将参数信息转换成html
					StringBuffer sb = new StringBuffer();
					// sb.append("<div>");
					sb.append(
							"<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
					sb.append("    <tbody>\n");
					for (Map map : paramList) {
						sb.append("        <tr>\n");
						sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + map.get("group") + "</th>\n");
						sb.append("        </tr>\n");
						List<Map> params = (List<Map>) map.get("params");
						for (Map map2 : params) {
							sb.append("        <tr>\n");
							sb.append("            <td class=\"tdTitle\">" + map2.get("k") + "</td>\n");
							sb.append("            <td>" + map2.get("v") + "</td>\n");
							sb.append("        </tr>\n");
						}
					}
					sb.append("    </tbody>\n");
					sb.append("</table>");
					// sb.append("</div>");
					return sb.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
