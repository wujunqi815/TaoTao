package com.taotao.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;


/**
 * 图片上传服务
 * @author jason.wu
 *
 */

@Service
public class PictureServiceImple implements PictureService {

	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	
	@Override
	public Map uploadPicture(MultipartFile uploadFile) {
		// TODO Auto-generated method stub
        Map resultMap = new HashMap<>();

		//原文件名
		String oldName = uploadFile.getOriginalFilename();
		//生成新文件名
		String newName = IDUtils.genImageName();
		newName = newName + oldName.substring(oldName.lastIndexOf("."));
		
		System.out.println(newName);
		
		//图片上传
		//拼接文件绝对路径
//		String localPath = "E:\\github_dev\\TaoTao\\Image\\";
        String filePath = IMAGE_BASE_URL + "/" + newName;
        
        System.out.println(filePath);
        File newFile = new File(filePath);
        try
        {
        	uploadFile.transferTo(newFile);
        	//返回结果
            resultMap.put("error", 0);
            resultMap.put("url", filePath);
    		return resultMap;
        	
        } catch (Exception e)
        {
        	System.out.println("upload failed");
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            resultMap.put("error", 1);
            resultMap.put("message", "exception");
    		return resultMap;
        }
	}
}
