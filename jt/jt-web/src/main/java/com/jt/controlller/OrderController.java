package com.jt.controlller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Reference(check = false)
	private DubboCartService cartService;
	
	@Reference(check = false)
	private DubboOrderService orderService;
	
	/**
	 * 	实现订单确认页面的跳转
	 */
	@RequestMapping("/create")
	public String create(Model model) {
		Long userId = ThreadLocalUtil.get().getId();
		List<Cart> carts = cartService.findCartListByUserId(userId);
		model.addAttribute("carts",carts);
		return "order-cart";
	}
	
	/**
	 * 	业务说明:
	 * 	完成订单入库操作,并且返回orderId
	 * 	动态生成一个orderId, 通过uuid的方式
	 * 	同时实现三张表入库操作
	 * 	注意事务控制
	 * @param order
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult savaOrder(Order order) {
		//根据orderId查询数据库,3张表
		Long userId = ThreadLocalUtil.get().getId();
		order.setUserId(userId);
		String orderId = orderService.saveOrder(order);
		return SysResult.success(orderId);
	}
	
	/**
	 * 	根据orderId查询order对象,将
	 * @param id
	 * @return
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
}
