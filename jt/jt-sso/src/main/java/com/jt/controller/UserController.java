package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController  {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 根据用户信息实现数据的检验
	 * 返回结果:
	 * 		true:用户已经存在    false:用户可以使用该数据
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								 @PathVariable Integer type,
								 String callback) {
		boolean data = userService.checkUser(param,type);
		return new JSONPObject(callback, SysResult.success(data));
	}
	
	/**
	 * http://sso.jt.com/user/query/
	 * 398c89b99017e89fa862af392c028e61?callback=jsonp1571282314877&_=1571282346602
	 * 根据ticket信息查询用户JSON数据之后将数据回传给客户端
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,
										String callback) {
		String userJson = jedisCluster.get(ticket);
		if (StringUtils.isEmpty(userJson)) {
			//用户使用的ticket有问题
			return new JSONPObject(callback,SysResult.fail());
		}
		return new JSONPObject(callback,SysResult.success(userJson));
	}

}
