package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVo;

@Service
//加载pro配置文件
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{
	
	//定义本地磁盘路径
	@Value("${image.localDirPath}")
	private String localDirPath ;//"D:/you know/jt-images/";
	
	//定义虚拟路径地址
	@Value("${image.urlDirPath}")
	private String urlDirPath; //"http://image.jt.com/";

	/**
	 * 实现思路:
	 * 	1.校验图片的类型:jpg/jpeg/png/gif...
	 * 	2.校验是否为恶意的程序
	 * 	3.分文件存储 (以日期的顺序选择 yyyy/MM/dd)
	 * 	4.防止文件重名 自定义文件名称 uuid.类型
	 */
	@Override
	public ImageVo upload(MultipartFile uploadFile) {
		
		//1.获取图片的名称
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();
		//2.校验 正则表达式
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			return ImageVo.fail();
		}
		//System.out.println("校验成功!!!");
		
		//3.检验恶意程序 图片:高度/宽度/px
		try {
			BufferedImage bImage = ImageIO.read(uploadFile.getInputStream());
			int width = bImage.getWidth();
			int height = bImage.getHeight();
			if(width == 0 || height == 0) {
				return ImageVo.fail();
			}
			//System.out.println("表示是图片!!!");
			
			//4.实现分文件存储  yyyy/MM/dd
			String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
			String dirFilePath = localDirPath + dateDir;
			System.out.println(dirFilePath);
			File dirFile = new File(dirFilePath);
			if(!dirFile.exists()) {
				//如果文件不存在,需要创建目录
				dirFile.mkdirs();
			}
			
			//5.动态生成文件名称 uuid+文件后缀
			String uuid = UUID.randomUUID().toString();
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String realFileName = uuid + fileType;
			String realFilePath = dirFilePath + realFileName;
			uploadFile.transferTo(new File(realFilePath));
			
			//6.实现数据的回显
			String url = urlDirPath + dateDir + realFileName;
			ImageVo imageVo = new ImageVo(0, url, width, height);
			return imageVo;
		} catch (Exception e) {
			e.printStackTrace();
			return ImageVo.fail();
		}
	}
}
