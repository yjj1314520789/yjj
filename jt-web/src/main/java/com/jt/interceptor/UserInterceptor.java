package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component	//将对象交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	private JedisCluster jedisCluster;
	
	@SuppressWarnings("unused")
	private static final String JTUSER = "JT-USER";
	/**
	 *	方法执行之前
	 * boolean表示是否方向 true false
	 * 	实现目标:用户不登录,不能访问购物车页面
	 * 	实现思路:
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//System.out.println("方法执行之前执行");
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		String username = CookieUtil.getCookieValue(request, "JT_USERNAME");
		if(!StringUtils.isEmpty(ticket)) {
			String userJSON = jedisCluster.hget(username, "JT_USERJSON");
			if(!StringUtils.isEmpty(userJSON)) {
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//user对象
				//request.setAttribute(JTUSER, user);
				ThreadLocalUtil.set(user);
				System.out.println("用户信息保存到域中");
				return true; //程序方向
			}
		}
		//一般拦截器中的false和重定向连用
		//应该重定向跳转到登录界面
		response.sendRedirect("/user/login.html");
		return false;//标识拦截
	}
	/**
	 * 方法执行之后通知
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("方法执行之后post");
	}
	/**
	 * 方法完成的最后阶段
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//request.removeAttribute(JTUSER);
		ThreadLocalUtil.remove();
		System.out.println("拦截器最后的管理范围");
	}
}
