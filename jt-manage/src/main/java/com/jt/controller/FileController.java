package com.jt.controller;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@RestController
public class FileController {
	/**
	 * 文件上传成功之后,返回json数据
	 * @throws Exception 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws Exception {
		//1.获取图片名称
		String fileName = fileImage.getOriginalFilename();
		//System.out.println(fileName);
		//2.创建文件对象,指定上传的目录
		File dir=new File("D:/JT/JT-image");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		//3.
		String path="D:/JT/JT-image/"+fileName;
		File file = new File(path);
		fileImage.transferTo(file);
		return "上传成功!!!";
		
	}
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping("/pic/upload")
	public ImageVO fileUpload(MultipartFile uploadFile) {
		return fileService.upload(uploadFile);
	}
}
