package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;

/**
 * 上传图片处理控制器
 * @author jason.wu
 *
 */

@Controller
public class PictureController {

	@Autowired
	private PictureService pictureService;
	
	@RequestMapping("pic/upload")
	@ResponseBody
	public Map pictureUpload(MultipartFile uploadFile){
		System.out.println("picture upload");
		
		Map result = pictureService.uploadPicture(uploadFile);
		return result;
	}
}
