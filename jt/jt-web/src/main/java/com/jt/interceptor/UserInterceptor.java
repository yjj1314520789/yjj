package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component //将对象交给Spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	@SuppressWarnings("unused")
	private static final String JTUSER = "JT-USER";
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * boolean表示是否放行 true false
	 * 
	 * 	实现目标:用户不登录,不能访问购物页面
	 * 	实现思路:
	 * 	 1.获取cookie
	 * 	 2.获取redis
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		//System.out.println("方法执行之前执行");
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		if (!StringUtils.isEmpty(ticket)) {
			String userJSON = jedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJSON)) {
				User user = ObjectMapperUtil.toObject(userJSON,User.class);
				//request.setAttribute(JTUSER, user);
				ThreadLocalUtil.set(user);
				return true;//表示程序放行
			}
		}
		//一般拦截器中的false和重定向联用
		//应该重定向跳转到登录页面
		response.sendRedirect("/user/login.html");
		return false;//标识拦截
	}
	
	/**
	 * 方法执行之后通知
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("方法执行之后:post");
	}
	
	/**
	 * 	方法完成的最后阶段
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//request.removeAttribute(JTUSER);
		ThreadLocalUtil.remove();
		System.out.println("拦截器最后的管理范围");
	}
}
