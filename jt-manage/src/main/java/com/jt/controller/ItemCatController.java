package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat/")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/queryItemName")
	public String findItemCatById(Long itemCatId) {
		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
	}
	/**
	 * 1.url:/item/cat/list
	 * 2.返回值结果:list<EasyUITree>
	 * 业务思想:
	 * 	只查询一级商品分类信息
	 * parent_id=0
	 * 
	 * SpringMVC 动态接收数据
	 * 	参数名称:id
	 * 	目的:id当做parentId使用
	 * 	要求:初始化时id=0
	 * 
	 * @RequestParam()
	 * 	作用: 当页面传递的参数与接收参数名称不一致时使用
	 * 	参数介绍:
	 * 		1.name/value:接收用户提交数据
	 * 		2.defaultValue:设定默认值
	 * 		3.required:该参数是否必传 true
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findEasyUITreeByParentId(
			@RequestParam(value = "id",defaultValue = "0")Long parentId){
		/**
		 * if(id==null) {id=0L;} Long parentId=id;
		 */
		//调用数据库操作
		return itemCatService.findEasyUITreeByParentId(parentId);
		//return itemCatService.findItemCatCache(parentId);
	}
	
	//java对象		json         redis对象
}
