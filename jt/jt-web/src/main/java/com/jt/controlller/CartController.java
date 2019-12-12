package com.jt.controlller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.pojo.Cart;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(check = false)
	private DubboCartService carService;

	/**
	 * 查询用户的全部购物记录信息 items="${cartList}"
	 * 
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		//User user = (User)request.getAttribute("JT-USER");
		Long userId = ThreadLocalUtil.get().getId();
		List<Cart> cartList = carService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";// 转发
	}

	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		Long userId = ThreadLocalUtil.get().getId();;
		cart.setUserId(userId);
		carService.updateCartNum(cart);
		return SysResult.success();
	}

	/**
	 * userId和itemId 重定向到列表页面
	 * 
	 * @param cart
	 * @return
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = ThreadLocalUtil.get().getId();;
		cart.setUserId(userId);
		carService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}

	/**
	 * 新增购物车,完成之后跳转到购物车展现页面
	 */
	@RequestMapping("/add/{itemId}")
	public String saveItem(Cart cart) {
		Long userId = ThreadLocalUtil.get().getId();;
		cart.setUserId(userId);
		carService.saveItem(cart);
		return "redirect:/cart/show.html";
	}

}
