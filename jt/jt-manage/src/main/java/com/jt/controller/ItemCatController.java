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
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 	根据商品分类id查询名称
	 * 	url地址:
	 * 	/item/cat/queryItemName
	 */
	@RequestMapping("/queryItemName")
	private String findItemCatById(Long itemCatId) {
		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
		
	}
	
	/**
	 * 一:
	 * 1.url:/item/cat/list
	 * 2.返回值结果:List<EasyUITree>
	 * 业务思想:
	 *  只查询一级商品分类信息
	 *  parent_id=0;
	 *  
	 * 二:
	 *  SpringMvc 动态接收数据
	 *  参数名称:id
	 *  目的:id当做parentID使用
	 *  要求:初始化时id=0
	 *  
	 * 三:
	 *  @RequestParam说明:
	 *  作用:当页面传递的参数与接收参数名称不一致时使用
	 *  参数介绍:
	 *  name/value: 接收用户提交数据
	 *  defaultValue:设定默认值
	 *  required:该参数是否必传 默认true(可以省略)
	 */
	@RequestMapping("/list")
	private List<EasyUITree> findItemCatByParentId(
			@RequestParam(value="id",defaultValue = "0",required = true)Long parentId){
		/*
		 * if(id==null) { id = 0L; } Long parentId = id;
		 */
		//调用数据库操作
		return itemCatService.findEasyUITree(parentId);
		//return itemCatService.findItemCacheById(parentId);
	}

 }
