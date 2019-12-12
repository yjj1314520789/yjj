package com.jt.controller;

import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(check = false)
	private DubboCartService cartService;
	
	/**
	 *	item="${cartList}"
	 * 	查询用户的全部购物信息
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		//User user = (User)request.getAttribute("JT-USER");
		User user = ThreadLocalUtil.get();
		Long userId = user.getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";	//转发
	}
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		User user = ThreadLocalUtil.get();
		Long userId = user.getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
	}
	/**
	 * userId 和 itemId
	 * @param cart
	 * @return
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		User user = ThreadLocalUtil.get();
		Long userId = user.getId();
		cart.setUserId(userId);
		
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";	//重定向
	}
	@RequestMapping("/add/{itemId}")
	public String saveItem(Cart cart) {
		User user = ThreadLocalUtil.get();
		Long userId = user.getId();
		cart.setUserId(userId);
		cartService.saveItem(cart);
		return "redirect:/cart/show.html";
	}
}
