package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	/**
	 * restFul风格
	 * 	语法:
	 *   1.参数必须使用/分割
	 *   2.参数使用{ }号包裹
	 *   3.使用@PathVariable注解参数
	 *	规则:
	 * 	  如果参数名称与对象的属性一致,可以直接使用对象接收
	 * 	高级应用:
	 * 	例1:userById/100	查询id=100
	 * 	例2:userById/100	删除id=100的数据
	 * 	请求方式:7种,常用的4种:
	 * 		get	查询操作
	 * 		post 入库操作
	 * 		put	更新操作
	 * 		delete 删除操作
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
	//查询操作
//	@RequestMapping(value="/userById/{id}",method = RequestMethod.GET)
//	@GetMapping("/userById/{id}")
//	public String findUserById(@PathVariable Integer id) {
//		return "";
//	}
	//删除操作
//	@DeleteMapping("/userById/{id}")
//	public String deleteUserById(@PathVariable Integer id) {
//		return "";
//	}
	
}
