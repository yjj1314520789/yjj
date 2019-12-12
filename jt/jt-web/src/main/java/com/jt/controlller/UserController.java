package com.jt.controlller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboUserService;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Reference(check = false)  //不需要校验
	private DubboUserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@SuppressWarnings("unused")
	private static final String TICKET ="JT_TICKET";
	
	//注册 http://www.jt.com/user/register.html
	//登录http://www.jt.com/user/login.html
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
		
	}
	
	@ResponseBody
	@RequestMapping("/doRegister")
	public  SysResult saveUser(User user) {
		
		userService.saveUser(user);
		return SysResult.success();
		
	}
	/**
	 * setMaxage(>0) 存活的声明周期 单位秒
	 * setMaxage(0) 要求cookie立即删除
	 * setMaxage(-1) 要求cookie当会话关闭时,删除
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletRequest request,HttpServletResponse response) {
		//1.获取用户ip信息
		String ip = IPUtil.getIpAddr(request);
		//2.完成用户信息的校验
		String ticket = userService.doLogin(user,ip);
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		//3.将用户信息写入cookie
		CookieUtil.addCookie(request, response, "JT_TICKET", ticket, 7*24*3600, "jt.com");
		CookieUtil.addCookie(request, response, "JT_USERNAME", user.getUsername(), 7*24*3600, "jt.com");
		return SysResult.success();
	}
//	public SysResult doLogin(User user,HttpServletResponse response) {
//		String ticket = userService.doLogin(user);
//		if (StringUtils.isEmpty(ticket)) {
//			//表示返回值不正确,给用户提示信息
//			return SysResult.fail("用户名或密码错误!!");
//		}
//		//将ticket保存到客户端cookie中
//		Cookie ticketCookie = new Cookie(TICKET,ticket);
//		//7天有效
//		ticketCookie.setMaxAge(7*24*3600);
//		//cookie权限设定(路径)
//		ticketCookie.setPath("/");
//		//cookie实现共享
//		ticketCookie.setDomain("jt.com");
//		response.addCookie(ticketCookie);
//		return SysResult.success();
//	}
	
	/**
	 * 前提:获取cookies
	 * 0.首先应该获取JT_TICKET的Cookie的值 ticket
	 * 1.删除cookie 删除名称为JT_TICKET的cookie
	 * 2.删除redis  根据ticket值删除redis
	 * @return
	 */
	@RequestMapping("/logout")
	public String logOut(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		if(cookies.length>0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JT_TICKET")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		if (!StringUtils.isEmpty(ticket)) {
			jedisCluster.del(ticket);
			Cookie cookie = new Cookie("JT_TICKET", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain("jt.com");
			response.addCookie(cookie);
		}
		return "redirect:/";
	}
	

}
